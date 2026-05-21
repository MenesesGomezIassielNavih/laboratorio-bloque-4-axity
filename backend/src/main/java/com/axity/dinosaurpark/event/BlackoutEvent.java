package com.axity.dinosaurpark.event;

import com.axity.dinosaurpark.config.ParkConstants;
import com.axity.dinosaurpark.simulation.ParkState;
import com.axity.dinosaurpark.simulation.PendingEventLog;

/**
 * Apagon masivo: reduce la energia del parque en 50 unidades.
 */
public class BlackoutEvent implements SimulationEvent {

    private static final double ENERGY_DROP = 50.0;

    @Override
    public String name() {
        return ParkConstants.EVENT_BLACKOUT;
    }

    @Override
    public void apply(ParkState state) {
        double newEnergy = Math.max(0.0, state.getEnergy() - ENERGY_DROP);
        state.setEnergy(newEnergy);
        state.addActiveEvent(name());
        state.addEventLog(new PendingEventLog(name(), state.getStep(),
                "Energia tras apagon: " + newEnergy));
    }
}
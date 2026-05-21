package com.axity.dinosaurpark.event;

import com.axity.dinosaurpark.config.ParkConstants;
import com.axity.dinosaurpark.simulation.ParkState;
import com.axity.dinosaurpark.simulation.PendingEventLog;

/**
 * Tormenta torrencial: reduce los turistas activos en 30%.
 */
public class StormEvent implements SimulationEvent {

    private static final double RETENTION_RATE = 0.70;

    @Override
    public String name() {
        return ParkConstants.EVENT_STORM;
    }

    @Override
    public void apply(ParkState state) {
        int reduced = (int) Math.floor(state.getActiveTourists() * RETENTION_RATE);
        state.setActiveTourists(reduced);
        state.addActiveEvent(name());
        state.addEventLog(new PendingEventLog(name(), state.getStep(),
                "Turistas activos tras tormenta: " + reduced));
    }
}
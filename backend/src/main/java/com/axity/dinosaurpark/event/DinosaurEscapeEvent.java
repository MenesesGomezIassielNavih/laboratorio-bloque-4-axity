package com.axity.dinosaurpark.event;

import com.axity.dinosaurpark.config.ParkConstants;
import com.axity.dinosaurpark.model.Dinosaur;
import com.axity.dinosaurpark.simulation.ParkState;
import com.axity.dinosaurpark.simulation.PendingEventLog;
import java.util.List;
import java.util.Random;

/**
 * Marca un dinosaurio aleatorio como escapado.
 * El ObservationEnclosure intentara recapturarlo en steps siguientes.
 */
public class DinosaurEscapeEvent implements SimulationEvent {

    private final Random rnd;

    public DinosaurEscapeEvent() {
        this(new Random());
    }

    public DinosaurEscapeEvent(Random rnd) {
        this.rnd = rnd;
    }

    @Override
    public String name() {
        return ParkConstants.EVENT_ESCAPE;
    }

    @Override
    public void apply(ParkState state) {
        List<Dinosaur> dinos = state.getDinosaurs();
        if (dinos.isEmpty()) {
            return;
        }
        Dinosaur target = dinos.get(rnd.nextInt(dinos.size()));
        target.markEscaped();
        state.addActiveEvent(name());
        state.addEventLog(new PendingEventLog(name(), state.getStep(),
                "Dinosaurio escapado: " + target.getSpecies()));
    }
}
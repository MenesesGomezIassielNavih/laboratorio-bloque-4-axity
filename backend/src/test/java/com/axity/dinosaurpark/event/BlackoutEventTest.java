package com.axity.dinosaurpark.event;

import static org.junit.jupiter.api.Assertions.*;
import com.axity.dinosaurpark.simulation.ParkState;
import org.junit.jupiter.api.Test;

class BlackoutEventTest {

    @Test
    void shouldReduceEnergyBy50WhenApplied() {
        ParkState state = new ParkState();
        state.setEnergy(100.0);
        new BlackoutEvent().apply(state);
        assertEquals(50.0, state.getEnergy(), 0.001);
    }

    @Test
    void shouldClampEnergyAtZeroWhenBelowDrop() {
        ParkState state = new ParkState();
        state.setEnergy(30.0);
        new BlackoutEvent().apply(state);
        assertEquals(0.0, state.getEnergy(), 0.001);
    }
}
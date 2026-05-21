package com.axity.dinosaurpark.event;

import static org.junit.jupiter.api.Assertions.*;
import com.axity.dinosaurpark.simulation.ParkState;
import org.junit.jupiter.api.Test;

class StormEventTest {

    @Test
    void shouldReduceActiveTouristsByThirtyPercent() {
        ParkState state = new ParkState();
        state.setActiveTourists(10);
        new StormEvent().apply(state);
        assertEquals(7, state.getActiveTourists());
    }

    @Test
    void shouldLogEventWhenApplied() {
        ParkState state = new ParkState();
        state.setActiveTourists(20);
        state.setStep(3);
        new StormEvent().apply(state);
        assertFalse(state.getPendingEventLogs().isEmpty());
    }
}
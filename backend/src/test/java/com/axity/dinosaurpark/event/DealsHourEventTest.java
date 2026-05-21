package com.axity.dinosaurpark.event;

import static org.junit.jupiter.api.Assertions.*;
import com.axity.dinosaurpark.simulation.ParkState;
import org.junit.jupiter.api.Test;

class DealsHourEventTest {

    @Test
    void shouldActivateDealsHourFlagWhenApplied() {
        ParkState state = new ParkState();
        new DealsHourEvent().apply(state);
        assertTrue(state.isDealsHourActive());
    }

    @Test
    void shouldSetDiscountFromConfiguration() {
        ParkState state = new ParkState();
        new DealsHourEvent().apply(state);
        assertEquals(0.30, state.getCurrentDiscount(), 0.001);
    }

    @Test
    void shouldRegisterEventLogWhenApplied() {
        ParkState state = new ParkState();
        state.setStep(7);
        new DealsHourEvent().apply(state);
        assertEquals(1, state.getActiveEventNames().size());
        assertEquals(1, state.getPendingEventLogs().size());
    }
}
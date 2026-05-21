package com.axity.dinosaurpark.zone;

import static org.junit.jupiter.api.Assertions.*;
import com.axity.dinosaurpark.simulation.ParkState;
import org.junit.jupiter.api.Test;

class CentralHubTest {

    @Test
    void shouldRegisterSouvenirRevenueWhenTouristsArePresent() {
        ParkState state = new ParkState();
        state.setActiveTourists(10);
        new CentralHub().process(state);
        assertEquals(1, state.getPendingRevenues().size());
    }

    @Test
    void shouldDoNothingWhenNoTourists() {
        ParkState state = new ParkState();
        new CentralHub().process(state);
        assertTrue(state.getPendingRevenues().isEmpty());
    }
}
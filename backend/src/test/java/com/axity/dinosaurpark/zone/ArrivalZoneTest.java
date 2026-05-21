package com.axity.dinosaurpark.zone;

import static org.junit.jupiter.api.Assertions.*;
import com.axity.dinosaurpark.simulation.ParkState;
import org.junit.jupiter.api.Test;

class ArrivalZoneTest {

    @Test
    void shouldRegisterTicketRevenueWhenTouristsArePresent() {
        ParkState state = new ParkState();
        state.setActiveTourists(10);
        new ArrivalZone().process(state);
        assertEquals(1, state.getPendingRevenues().size());
    }

    @Test
    void shouldDoNothingWhenNoActiveTourists() {
        ParkState state = new ParkState();
        new ArrivalZone().process(state);
        assertTrue(state.getPendingRevenues().isEmpty());
    }

    @Test
    void shouldExposeNameAsArrival() {
        assertEquals("ARRIVAL", new ArrivalZone().getName());
    }
}
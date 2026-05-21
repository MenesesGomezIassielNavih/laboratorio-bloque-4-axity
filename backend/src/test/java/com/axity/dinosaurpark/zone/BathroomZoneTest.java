package com.axity.dinosaurpark.zone;

import static org.junit.jupiter.api.Assertions.*;
import com.axity.dinosaurpark.simulation.ParkState;
import org.junit.jupiter.api.Test;

class BathroomZoneTest {

    @Test
    void shouldRegisterServicesRevenueAndMaintenanceExpense() {
        ParkState state = new ParkState();
        state.setActiveTourists(8);
        new BathroomZone().process(state);
        assertEquals(1, state.getPendingRevenues().size());
        assertEquals(1, state.getPendingExpenses().size());
    }

    @Test
    void shouldOnlyChargeMaintenanceWhenNoTourists() {
        ParkState state = new ParkState();
        new BathroomZone().process(state);
        assertTrue(state.getPendingRevenues().isEmpty());
        assertEquals(1, state.getPendingExpenses().size());
    }
}
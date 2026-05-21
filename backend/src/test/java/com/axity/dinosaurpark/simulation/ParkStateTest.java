package com.axity.dinosaurpark.simulation;

import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class ParkStateTest {

    @Test
    void shouldClearStepFlagsAndActiveEvents() {
        ParkState state = new ParkState();
        state.setDealsHourActive(true);
        state.setCurrentDiscount(0.30);
        state.addActiveEvent("X");
        state.clearStepFlags();
        assertFalse(state.isDealsHourActive());
        assertEquals(0.0, state.getCurrentDiscount(), 0.001);
        assertTrue(state.getActiveEventNames().isEmpty());
    }

    @Test
    void shouldClearPendingMovements() {
        ParkState state = new ParkState();
        state.addRevenue(new PendingRevenue("T", BigDecimal.TEN, "X"));
        state.addExpense(new PendingExpense("E", BigDecimal.ONE, "Y"));
        state.addEventLog(new PendingEventLog("EV", 1, "Z"));
        state.clearPending();
        assertTrue(state.getPendingRevenues().isEmpty());
        assertTrue(state.getPendingExpenses().isEmpty());
        assertTrue(state.getPendingEventLogs().isEmpty());
    }

    @Test
    void shouldAccumulateAllPendingTypes() {
        ParkState state = new ParkState();
        state.addRevenue(new PendingRevenue("T", BigDecimal.TEN, "X"));
        state.addExpense(new PendingExpense("E", BigDecimal.ONE, "Y"));
        state.addEventLog(new PendingEventLog("EV", 1, "Z"));
        assertEquals(1, state.getPendingRevenues().size());
        assertEquals(1, state.getPendingExpenses().size());
        assertEquals(1, state.getPendingEventLogs().size());
    }
}
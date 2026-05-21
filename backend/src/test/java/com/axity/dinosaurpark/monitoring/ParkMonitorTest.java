package com.axity.dinosaurpark.monitoring;

import static org.junit.jupiter.api.Assertions.*;
import com.axity.dinosaurpark.simulation.ParkState;
import org.junit.jupiter.api.Test;

class ParkMonitorTest {

    @Test
    void shouldReportWhenStepMatchesInterval() {
        ParkMonitor monitor = new ParkMonitor(5);
        ParkState state = new ParkState();
        state.setStep(5);
        assertDoesNotThrow(() -> monitor.report(state));
    }

    @Test
    void shouldNotReportWhenStepDoesNotMatchInterval() {
        ParkMonitor monitor = new ParkMonitor(10);
        ParkState state = new ParkState();
        state.setStep(7);
        assertDoesNotThrow(() -> monitor.report(state));
    }

    @Test
    void shouldThrowWhenIntervalIsZeroOrNegative() {
        assertThrows(IllegalArgumentException.class, () -> new ParkMonitor(0));
        assertThrows(IllegalArgumentException.class, () -> new ParkMonitor(-1));
    }
}
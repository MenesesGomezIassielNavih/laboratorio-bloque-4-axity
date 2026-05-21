package com.axity.dinosaurpark.event;

import static org.junit.jupiter.api.Assertions.*;
import com.axity.dinosaurpark.model.Vehicle;
import com.axity.dinosaurpark.simulation.ParkState;
import java.util.Random;
import org.junit.jupiter.api.Test;

class VehicleFailureEventTest {

    @Test
    void shouldBreakOneAvailableVehicleWhenApplied() {
        ParkState state = new ParkState();
        state.getVehicles().add(new Vehicle(1));
        state.getVehicles().add(new Vehicle(2));
        new VehicleFailureEvent(new Random(42L)).apply(state);
        long broken = state.getVehicles().stream()
                .filter(v -> v.getStatus() == Vehicle.Status.BROKEN).count();
        assertEquals(1, broken);
    }

    @Test
    void shouldDoNothingWhenAllVehiclesAlreadyBroken() {
        ParkState state = new ParkState();
        Vehicle v = new Vehicle(1);
        v.breakDown(3);
        state.getVehicles().add(v);
        new VehicleFailureEvent(new Random(42L)).apply(state);
        assertTrue(state.getActiveEventNames().isEmpty());
    }
}
package com.axity.dinosaurpark.zone;

import static org.junit.jupiter.api.Assertions.*;
import com.axity.dinosaurpark.model.Vehicle;
import com.axity.dinosaurpark.simulation.ParkState;
import org.junit.jupiter.api.Test;

class PowerPlantTest {

    @Test
    void shouldConsumeEnergyAndRegisterEnergyExpense() {
        ParkState state = new ParkState();
        state.setEnergy(100.0);
        new PowerPlant().process(state);
        assertEquals(95.0, state.getEnergy(), 0.001);
        assertEquals(1, state.getPendingExpenses().size());
    }

    @Test
    void shouldRepairWhenEnergyBelowCriticalAndVehicleAvailable() {
        ParkState state = new ParkState();
        state.setEnergy(15.0);
        state.getVehicles().add(new Vehicle(1));
        new PowerPlant().process(state);
        assertTrue(state.getEnergy() > 20.0);
        assertEquals(2, state.getPendingExpenses().size());
    }

    @Test
    void shouldNotRepairWhenNoVehicleAvailable() {
        ParkState state = new ParkState();
        state.setEnergy(15.0);
        new PowerPlant().process(state);
        assertEquals(1, state.getPendingExpenses().size());
    }
}
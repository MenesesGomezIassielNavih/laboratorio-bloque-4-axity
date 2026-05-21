package com.axity.dinosaurpark.zone;

import com.axity.dinosaurpark.config.ParkConstants;
import com.axity.dinosaurpark.model.Vehicle;
import com.axity.dinosaurpark.simulation.ParkState;
import com.axity.dinosaurpark.simulation.PendingExpense;
import java.math.BigDecimal;

/**
 * Planta de energia: consume 5 unidades de energia por step.
 * Si la energia cae por debajo de 20, se requiere reparacion (costo de 80).
 * La reparacion requiere al menos un vehiculo disponible (representando movilidad del tecnico).
 */
public class PowerPlant implements ParkZone {

    private static final String NAME = "POWER_PLANT";
    private static final double CONSUMPTION_PER_STEP = 5.0;
    private static final double CRITICAL_THRESHOLD   = 20.0;
    private static final double REPAIR_REFILL        = 60.0;
    private static final BigDecimal ENERGY_COST = new BigDecimal("10.00");
    private static final BigDecimal REPAIR_COST = new BigDecimal("80.00");

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void process(ParkState state) {
        double newEnergy = state.getEnergy() - CONSUMPTION_PER_STEP;
        state.setEnergy(Math.max(0.0, newEnergy));
        state.addExpense(new PendingExpense(ParkConstants.EXP_ENERGY, ENERGY_COST, NAME));

        if (state.getEnergy() >= CRITICAL_THRESHOLD) {
            return;
        }
        boolean technicianMobile = state.getVehicles().stream().anyMatch(Vehicle::isAvailable);
        if (!technicianMobile) {
            return;
        }
        state.setEnergy(state.getEnergy() + REPAIR_REFILL);
        state.addExpense(new PendingExpense(ParkConstants.EXP_REPAIR, REPAIR_COST, NAME));
    }
}
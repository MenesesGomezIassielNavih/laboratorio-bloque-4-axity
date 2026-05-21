package com.axity.dinosaurpark.zone;

import com.axity.dinosaurpark.config.ParkConstants;
import com.axity.dinosaurpark.simulation.ParkState;
import com.axity.dinosaurpark.simulation.PendingExpense;
import com.axity.dinosaurpark.simulation.PendingRevenue;
import java.math.BigDecimal;

/**
 * Zona de baños: cobra un servicio simbolico por turista activo
 * e incurre en un costo fijo de mantenimiento por step.
 */
public class BathroomZone implements ParkZone {

    private static final String NAME = "BATHROOM";
    private static final BigDecimal SERVICE_FEE      = new BigDecimal("5.00");
    private static final BigDecimal MAINTENANCE_COST = new BigDecimal("15.00");

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void process(ParkState state) {
        if (state.getActiveTourists() > 0) {
            BigDecimal revenue = SERVICE_FEE.multiply(BigDecimal.valueOf(state.getActiveTourists()));
            state.addRevenue(new PendingRevenue(ParkConstants.REV_SERVICES, revenue, NAME));
        }
        state.addExpense(new PendingExpense(ParkConstants.EXP_MAINTENANCE, MAINTENANCE_COST, NAME));
    }
}
package com.axity.dinosaurpark.zone;

import com.axity.dinosaurpark.config.ParkConfig;
import com.axity.dinosaurpark.config.ParkConstants;
import com.axity.dinosaurpark.simulation.ParkState;
import com.axity.dinosaurpark.simulation.PendingRevenue;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Zona de llegada: cobra boletos a turistas activos.
 * Aplica el descuento de DealsHour cuando esta activo en el step.
 */
public class ArrivalZone implements ParkZone {

    private static final String NAME = "ARRIVAL";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void process(ParkState state) {
        if (state.getActiveTourists() <= 0) {
            return;
        }
        ParkConfig cfg = ParkConfig.getInstance();
        BigDecimal priceAdult = BigDecimal.valueOf(cfg.getDouble("ticket.priceAdult", 350.00));
        BigDecimal discount   = BigDecimal.valueOf(state.isDealsHourActive() ? state.getCurrentDiscount() : 0.0);
        BigDecimal multiplier = BigDecimal.ONE.subtract(discount);
        BigDecimal unitPrice  = priceAdult.multiply(multiplier).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total      = unitPrice.multiply(BigDecimal.valueOf(state.getActiveTourists()));
        state.addRevenue(new PendingRevenue(ParkConstants.REV_TICKETS, total, NAME));
    }
}
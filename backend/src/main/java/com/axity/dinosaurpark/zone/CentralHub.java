package com.axity.dinosaurpark.zone;

import com.axity.dinosaurpark.config.ParkConfig;
import com.axity.dinosaurpark.config.ParkConstants;
import com.axity.dinosaurpark.simulation.ParkState;
import com.axity.dinosaurpark.simulation.PendingRevenue;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Hub central: aproximadamente el 60% de los turistas activos compra un souvenir.
 */
public class CentralHub implements ParkZone {

    private static final String NAME = "CENTRAL_HUB";
    private static final double BUY_RATE = 0.60;

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
        BigDecimal avgPrice = BigDecimal.valueOf(cfg.getDouble("souvenir.avgPrice", 120.00));
        int buyers = (int) Math.round(state.getActiveTourists() * BUY_RATE);
        if (buyers <= 0) {
            return;
        }
        BigDecimal total = avgPrice.multiply(BigDecimal.valueOf(buyers))
                                    .setScale(2, RoundingMode.HALF_UP);
        state.addRevenue(new PendingRevenue(ParkConstants.REV_SOUVENIRS, total, NAME));
    }
}
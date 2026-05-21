package com.axity.dinosaurpark.event;

import com.axity.dinosaurpark.config.ParkConfig;
import com.axity.dinosaurpark.config.ParkConstants;
import com.axity.dinosaurpark.simulation.ParkState;
import com.axity.dinosaurpark.simulation.PendingEventLog;

/**
 * Hora de ofertas: activa un descuento en la zona de llegada por un step.
 * El descuento aplica a los boletos vendidos despues de este evento dentro del mismo step.
 */
public class DealsHourEvent implements SimulationEvent {

    @Override
    public String name() {
        return ParkConstants.EVENT_DEALS_HOUR;
    }

    @Override
    public void apply(ParkState state) {
        double discount = ParkConfig.getInstance().getDouble("dealsHour.discount", 0.30);
        state.setDealsHourActive(true);
        state.setCurrentDiscount(discount);
        state.addActiveEvent(name());
        state.addEventLog(new PendingEventLog(name(), state.getStep(),
                "Descuento activado: " + discount));
    }
}
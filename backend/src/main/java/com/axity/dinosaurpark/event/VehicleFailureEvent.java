package com.axity.dinosaurpark.event;

import com.axity.dinosaurpark.config.ParkConfig;
import com.axity.dinosaurpark.config.ParkConstants;
import com.axity.dinosaurpark.model.Vehicle;
import com.axity.dinosaurpark.simulation.ParkState;
import com.axity.dinosaurpark.simulation.PendingEventLog;
import java.util.List;
import java.util.Random;

/**
 * Falla mecanica: descompone un vehiculo aleatorio no-BROKEN.
 * El contador de reparacion se toma de la configuracion.
 */
public class VehicleFailureEvent implements SimulationEvent {

    private final Random rnd;

    public VehicleFailureEvent() {
        this(new Random());
    }

    public VehicleFailureEvent(Random rnd) {
        this.rnd = rnd;
    }

    @Override
    public String name() {
        return ParkConstants.EVENT_VEHICLE_FAILURE;
    }

    @Override
    public void apply(ParkState state) {
        List<Vehicle> candidates = state.getVehicles().stream()
                .filter(v -> v.getStatus() != Vehicle.Status.BROKEN)
                .toList();
        if (candidates.isEmpty()) {
            return;
        }
        Vehicle target = candidates.get(rnd.nextInt(candidates.size()));
        int repairSteps = ParkConfig.getInstance().getInt("vehicleFailure.repairSteps", 3);
        target.breakDown(repairSteps);
        state.addActiveEvent(name());
        state.addEventLog(new PendingEventLog(name(), state.getStep(),
                "Vehiculo " + target.getId() + " descompuesto por " + repairSteps + " steps"));
    }
}
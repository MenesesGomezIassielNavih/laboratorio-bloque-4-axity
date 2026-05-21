package com.axity.dinosaurpark.model;

public class Technician extends Worker {

    public Technician(int id, String name) {
        super(id, name);
    }

    @Override
    public String role() {
        return "TECHNICIAN";
    }

    /**
     * Repara un vehiculo descompuesto inmediatamente.
     * @return true si el vehiculo paso a AVAILABLE, false si no estaba BROKEN.
     */
    public boolean repair(Vehicle vehicle) {
        if (vehicle == null || vehicle.getStatus() != Vehicle.Status.BROKEN) {
            return false;
        }
        vehicle.forceAvailable();
        return true;
    }
}
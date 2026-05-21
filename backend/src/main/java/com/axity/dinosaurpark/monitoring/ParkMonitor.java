package com.axity.dinosaurpark.monitoring;

import com.axity.dinosaurpark.model.Vehicle;
import com.axity.dinosaurpark.simulation.ParkState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Imprime las cinco metricas obligatorias del parque cada N steps:
 * turistas activos, dinosaurios en recintos, energia disponible,
 * eventos activos y vehiculos en uso.
 */
public class ParkMonitor {

    private static final Logger LOG = LoggerFactory.getLogger(ParkMonitor.class);

    private final int intervalSteps;

    public ParkMonitor(int intervalSteps) {
        if (intervalSteps <= 0) {
            throw new IllegalArgumentException("intervalSteps debe ser > 0");
        }
        this.intervalSteps = intervalSteps;
    }

    public void report(ParkState state) {
        if (state.getStep() % intervalSteps != 0) {
            return;
        }
        long inUse = state.getVehicles().stream()
                .filter(v -> v.getStatus() == Vehicle.Status.IN_USE)
                .count();
        LOG.info("=== MONITOR step={} ===", state.getStep());
        LOG.info("  Turistas activos:        {}", state.getActiveTourists());
        LOG.info("  Dinosaurios en recintos: {}", state.getDinosaursInEnclosures());
        LOG.info("  Energia disponible:      {}", state.getEnergy());
        LOG.info("  Eventos activos:         {}", state.getActiveEventNames());
        LOG.info("  Vehiculos en uso:        {}", inUse);
    }
}
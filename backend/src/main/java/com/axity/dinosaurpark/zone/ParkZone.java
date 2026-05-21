package com.axity.dinosaurpark.zone;

import com.axity.dinosaurpark.simulation.ParkState;

/**
 * Contrato comun para las cinco zonas del parque.
 * Cada zona procesa el estado mutable durante la fase B del step.
 */
public interface ParkZone {
    String getName();
    void process(ParkState state);
}
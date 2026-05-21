package com.axity.dinosaurpark.event;

import com.axity.dinosaurpark.simulation.ParkState;

/**
 * Contrato Strategy para eventos aleatorios del parque.
 * Cada implementacion encapsula un algoritmo de impacto sobre el estado.
 */
public interface SimulationEvent {
    String name();
    void apply(ParkState state);
}
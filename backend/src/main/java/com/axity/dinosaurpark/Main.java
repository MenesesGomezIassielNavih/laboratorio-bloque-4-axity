package com.axity.dinosaurpark;

import com.axity.dinosaurpark.config.ParkConfig;
import com.axity.dinosaurpark.persistence.DatabaseService;
import com.axity.dinosaurpark.simulation.SimulationEngine;

/**
 * Punto de entrada de la simulacion del parque de dinosaurios.
 * Inicializa ParkConfig, ejecuta Liquibase, y corre el motor de simulacion.
 */
public final class Main {

    private Main() {
        // Utility class - no instances.
    }

    public static void main(String[] args) {
        ParkConfig cfg = ParkConfig.getInstance();
        try (DatabaseService db = new DatabaseService(
                cfg.getString("db.url",  "jdbc:h2:file:./data/parkdb;AUTO_SERVER=TRUE"),
                cfg.getString("db.user", "sa"),
                cfg.getString("db.password", ""))) {
            db.migrate();
            new SimulationEngine(db).run();
        }
    }
}
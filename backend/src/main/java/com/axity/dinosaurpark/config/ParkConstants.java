package com.axity.dinosaurpark.config;

/**
 * Constantes del dominio del parque turistico.
 * Clase utilitaria final con constructor privado.
 */
public final class ParkConstants {

    public static final String EVENT_ESCAPE          = "DINOSAUR_ESCAPE";
    public static final String EVENT_BLACKOUT        = "MASSIVE_BLACKOUT";
    public static final String EVENT_STORM           = "TORRENTIAL_STORM";
    public static final String EVENT_DEALS_HOUR      = "DEALS_HOUR";
    public static final String EVENT_VEHICLE_FAILURE = "VEHICLE_FAILURE";

    public static final String REV_TICKETS   = "TICKETS";
    public static final String REV_SOUVENIRS = "SOUVENIRS";
    public static final String REV_SERVICES  = "SERVICES";

    public static final String EXP_MAINTENANCE = "MAINTENANCE";
    public static final String EXP_ENERGY      = "ENERGY";
    public static final String EXP_REPAIR      = "REPAIR";

    private ParkConstants() {
        // Utility class - no instances allowed.
    }
}
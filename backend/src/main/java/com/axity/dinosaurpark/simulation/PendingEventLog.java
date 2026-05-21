package com.axity.dinosaurpark.simulation;

/** Registro de un evento ocurrido en un step, pendiente de persistencia. */
public record PendingEventLog(String eventName, int step, String description) {
}
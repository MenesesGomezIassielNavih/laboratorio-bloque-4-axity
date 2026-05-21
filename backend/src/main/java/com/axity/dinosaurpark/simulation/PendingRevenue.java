package com.axity.dinosaurpark.simulation;

import java.math.BigDecimal;

/** Movimiento de ingreso acumulado durante un step, pendiente de persistencia. */
public record PendingRevenue(String type, BigDecimal amount, String source) {
}
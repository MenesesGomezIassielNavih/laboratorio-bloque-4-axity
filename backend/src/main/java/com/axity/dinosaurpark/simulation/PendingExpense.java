package com.axity.dinosaurpark.simulation;

import java.math.BigDecimal;

/** Movimiento de gasto acumulado durante un step, pendiente de persistencia. */
public record PendingExpense(String type, BigDecimal amount, String description) {
}
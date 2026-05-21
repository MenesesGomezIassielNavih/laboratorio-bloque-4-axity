package com.axity.dinosaurpark.persistence;

import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DatabaseServiceTest {

    private String testUrl;

    @BeforeEach
    void setUp() {
        testUrl = "jdbc:h2:file:./target/test-db/test-" + System.nanoTime()
                + ";AUTO_SERVER=TRUE";
    }

    @Test
    void shouldApplyLiquibaseMigrationsWithoutThrowing() {
        try (DatabaseService db = new DatabaseService(testUrl, "sa", "")) {
            assertDoesNotThrow(() -> db.migrate());
        }
    }

    @Test
    void shouldInsertRevenueAfterMigration() {
        try (DatabaseService db = new DatabaseService(testUrl, "sa", "")) {
            db.migrate();
            assertDoesNotThrow(() -> db.insertRevenue(
                    "TICKETS", new BigDecimal("100.00"), "TEST"));
        }
    }

    @Test
    void shouldInsertExpenseAfterMigration() {
        try (DatabaseService db = new DatabaseService(testUrl, "sa", "")) {
            db.migrate();
            assertDoesNotThrow(() -> db.insertExpense(
                    "ENERGY", new BigDecimal("50.00"), "TEST"));
        }
    }

    @Test
    void shouldInsertEventAfterMigration() {
        try (DatabaseService db = new DatabaseService(testUrl, "sa", "")) {
            db.migrate();
            assertDoesNotThrow(() -> db.insertEvent("BLACKOUT", 5, "TEST"));
        }
    }

    @Test
    void shouldUpsertVehicleAfterMigration() {
        try (DatabaseService db = new DatabaseService(testUrl, "sa", "")) {
            db.migrate();
            assertDoesNotThrow(() -> db.upsertVehicle(1, "AVAILABLE", 0));
            assertDoesNotThrow(() -> db.upsertVehicle(1, "BROKEN", 3));
        }
    }
}
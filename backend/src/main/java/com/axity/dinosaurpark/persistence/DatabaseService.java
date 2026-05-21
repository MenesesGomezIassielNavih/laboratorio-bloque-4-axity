package com.axity.dinosaurpark.persistence;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import liquibase.Scope;
import liquibase.command.CommandScope;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Servicio de persistencia para la simulacion. */
public class DatabaseService implements AutoCloseable {

    private static final Logger LOG = LoggerFactory.getLogger(DatabaseService.class);
    private static final String CHANGELOG = "db/changelog/db.changelog-master.xml";

    private final String url;
    private final String user;
    private final String password;

    private Connection conn;
    private PreparedStatement psInsertRevenue;
    private PreparedStatement psInsertExpense;
    private PreparedStatement psInsertEvent;
    private PreparedStatement psUpsertVehicle;

    public DatabaseService(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public void migrate() {
        try {
            Scope.child(Scope.Attr.resourceAccessor.name(),
                    new ClassLoaderResourceAccessor(),
                    () -> {
                        CommandScope update = new CommandScope("update");
                        update.addArgumentValue("changelogFile", CHANGELOG);
                        update.addArgumentValue("url", url);
                        update.addArgumentValue("username", user);
                        update.addArgumentValue("password", password);
                        update.execute();
                    });
            LOG.info("Liquibase update aplicado a {}", url);
        } catch (Exception ex) {
            throw new IllegalStateException("Fallo al ejecutar Liquibase update", ex);
        }
    }

    private void ensureConnected() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            return;
        }
        conn = DriverManager.getConnection(url, user, password);
        psInsertRevenue = conn.prepareStatement(
                "INSERT INTO revenues(type, amount, source) VALUES(?,?,?)");
        psInsertExpense = conn.prepareStatement(
                "INSERT INTO expenses(type, amount, description) VALUES(?,?,?)");
        psInsertEvent = conn.prepareStatement(
                "INSERT INTO events(event_name, step_number, description) VALUES(?,?,?)");
        psUpsertVehicle = conn.prepareStatement(
                "MERGE INTO vehicles(id, status, repair_countdown, updated_at) " +
                "KEY(id) VALUES(?,?,?,?)");
        LOG.info("Conexion sostenida abierta a {}", url);
    }

    public void insertRevenue(String type, BigDecimal amount, String source) {
        try {
            ensureConnected();
            psInsertRevenue.setString(1, type);
            psInsertRevenue.setBigDecimal(2, amount);
            psInsertRevenue.setString(3, source);
            psInsertRevenue.executeUpdate();
        } catch (SQLException ex) {
            LOG.error("insertRevenue fallo type={} amount={}", type, amount, ex);
        }
    }

    public void insertExpense(String type, BigDecimal amount, String description) {
        try {
            ensureConnected();
            psInsertExpense.setString(1, type);
            psInsertExpense.setBigDecimal(2, amount);
            psInsertExpense.setString(3, description);
            psInsertExpense.executeUpdate();
        } catch (SQLException ex) {
            LOG.error("insertExpense fallo type={} amount={}", type, amount, ex);
        }
    }

    public void insertEvent(String eventName, int step, String description) {
        try {
            ensureConnected();
            psInsertEvent.setString(1, eventName);
            psInsertEvent.setInt(2, step);
            psInsertEvent.setString(3, description);
            psInsertEvent.executeUpdate();
        } catch (SQLException ex) {
            LOG.error("insertEvent fallo eventName={} step={}", eventName, step, ex);
        }
    }

    public void upsertVehicle(int id, String status, int repairCountdown) {
        try {
            ensureConnected();
            psUpsertVehicle.setInt(1, id);
            psUpsertVehicle.setString(2, status);
            psUpsertVehicle.setInt(3, repairCountdown);
            psUpsertVehicle.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            psUpsertVehicle.executeUpdate();
        } catch (SQLException ex) {
            LOG.error("upsertVehicle fallo id={} status={}", id, status, ex);
        }
    }

    @Override
    public void close() {
        closeQuietly(psInsertRevenue, "psInsertRevenue");
        closeQuietly(psInsertExpense, "psInsertExpense");
        closeQuietly(psInsertEvent, "psInsertEvent");
        closeQuietly(psUpsertVehicle, "psUpsertVehicle");
        if (conn != null) {
            try {
                conn.close();
                LOG.info("Conexion sostenida cerrada");
            } catch (SQLException ex) {
                LOG.warn("Fallo al cerrar conexion: {}", ex.getMessage());
            }
        }
    }

    private void closeQuietly(AutoCloseable resource, String name) {
        if (resource == null) {
            return;
        }
        try {
            resource.close();
        } catch (Exception ex) {
            LOG.warn("Fallo al cerrar {}: {}", name, ex.getMessage());
        }
    }
}
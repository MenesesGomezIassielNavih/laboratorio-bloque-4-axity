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

/**
 * Servicio de persistencia para la simulacion.
 * Ejecuta Liquibase programaticamente via CommandScope (API canonica 4.27+)
 * y expone operaciones de insercion con PreparedStatement sobre H2 file mode.
 */
public class DatabaseService implements AutoCloseable {

    private static final Logger LOG = LoggerFactory.getLogger(DatabaseService.class);
    private static final String CHANGELOG = "db/changelog/db.changelog-master.xml";

    private final String url;
    private final String user;
    private final String password;

    public DatabaseService(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    /**
     * Aplica los changelogs via CommandScope.
     * Envuelve la API que declara throws Exception en una excepcion de dominio especifica.
     */
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

    public void insertRevenue(String type, BigDecimal amount, String source) {
        final String sql = "INSERT INTO revenues(type, amount, source) VALUES(?,?,?)";
        try (Connection cn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, type);
            ps.setBigDecimal(2, amount);
            ps.setString(3, source);
            ps.executeUpdate();
        } catch (SQLException ex) {
            LOG.error("insertRevenue fallo type={} amount={}", type, amount, ex);
        }
    }

    public void insertExpense(String type, BigDecimal amount, String description) {
        final String sql = "INSERT INTO expenses(type, amount, description) VALUES(?,?,?)";
        try (Connection cn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, type);
            ps.setBigDecimal(2, amount);
            ps.setString(3, description);
            ps.executeUpdate();
        } catch (SQLException ex) {
            LOG.error("insertExpense fallo type={} amount={}", type, amount, ex);
        }
    }

    public void insertEvent(String eventName, int step, String description) {
        final String sql = "INSERT INTO events(event_name, step_number, description) VALUES(?,?,?)";
        try (Connection cn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, eventName);
            ps.setInt(2, step);
            ps.setString(3, description);
            ps.executeUpdate();
        } catch (SQLException ex) {
            LOG.error("insertEvent fallo eventName={} step={}", eventName, step, ex);
        }
    }

    /**
     * Upsert de un vehiculo. Usa MERGE INTO de H2 (no portable a MySQL/Postgres sin ajustes).
     * Para entrega con H2 default es suficiente.
     */
    public void upsertVehicle(int id, String status, int repairCountdown) {
        final String sql = "MERGE INTO vehicles(id, status, repair_countdown, updated_at) " +
                           "KEY(id) VALUES(?,?,?,?)";
        try (Connection cn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setString(2, status);
            ps.setInt(3, repairCountdown);
            ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            ps.executeUpdate();
        } catch (SQLException ex) {
            LOG.error("upsertVehicle fallo id={} status={}", id, status, ex);
        }
    }

    @Override
    public void close() {
        // H2 en modo file cierra automaticamente al apagar la JVM.
    }
}
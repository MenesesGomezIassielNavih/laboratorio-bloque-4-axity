package com.axity.dinosaurpark.simulation;

import com.axity.dinosaurpark.config.ParkConfig;
import com.axity.dinosaurpark.event.BlackoutEvent;
import com.axity.dinosaurpark.event.DealsHourEvent;
import com.axity.dinosaurpark.event.DinosaurEscapeEvent;
import com.axity.dinosaurpark.event.SimulationEvent;
import com.axity.dinosaurpark.event.StormEvent;
import com.axity.dinosaurpark.event.VehicleFailureEvent;
import com.axity.dinosaurpark.model.Carnivore;
import com.axity.dinosaurpark.model.Herbivore;
import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.model.Vehicle;
import com.axity.dinosaurpark.monitoring.ParkMonitor;
import com.axity.dinosaurpark.persistence.DatabaseService;
import com.axity.dinosaurpark.zone.ArrivalZone;
import com.axity.dinosaurpark.zone.BathroomZone;
import com.axity.dinosaurpark.zone.CentralHub;
import com.axity.dinosaurpark.zone.ObservationEnclosure;
import com.axity.dinosaurpark.zone.ParkZone;
import com.axity.dinosaurpark.zone.PowerPlant;
import java.util.List;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Motor principal de la simulacion. Orquesta seis fases por step:
 *   A. Reset de flags efimeros
 *   B. Procesar las cinco zonas
 *   C. Rolear 0..N eventos aleatorios
 *   D. Tick de vehiculos descompuestos
 *   E. Persistir pendientes en H2
 *   F. Reportar metricas via ParkMonitor
 */
public class SimulationEngine {

    private static final Logger LOG = LoggerFactory.getLogger(SimulationEngine.class);

    private final ParkConfig config = ParkConfig.getInstance();
    private final DatabaseService db;
    private final List<ParkZone> zones;
    private final List<SimulationEvent> eventCatalog;
    private final ParkMonitor monitor;
    private final Random rnd;
    private final ParkState state = new ParkState();

    public SimulationEngine(DatabaseService db) {
        this(db, new Random());
    }

    public SimulationEngine(DatabaseService db, Random rnd) {
        this.db = db;
        this.rnd = rnd;
        this.zones = List.of(
                new ArrivalZone(),
                new CentralHub(),
                new BathroomZone(),
                new PowerPlant(),
                new ObservationEnclosure());
        this.eventCatalog = List.of(
                new DinosaurEscapeEvent(rnd),
                new BlackoutEvent(),
                new StormEvent(),
                new DealsHourEvent(),
                new VehicleFailureEvent(rnd));
        this.monitor = new ParkMonitor(config.getInt("monitoring.intervalSteps", 10));
        seedInitialState();
    }

    private void seedInitialState() {
        int initialTourists   = config.getInt("simulation.initialTourists", 15);
        int initialDinosaurs  = config.getInt("simulation.initialDinosaurs", 8);
        int initialVehicles   = config.getInt("vehicles.initialCount", 3);
        double initialEnergy  = config.getDouble("simulation.initialEnergy", 100.0);

        state.setActiveTourists(initialTourists);
        state.setEnergy(initialEnergy);

        for (int i = 1; i <= initialTourists; i++) {
            state.getTourists().add(new Tourist(i, "Tourist-" + i, Tourist.Type.ADULT));
        }
        for (int i = 1; i <= initialDinosaurs; i++) {
            if (i % 2 == 0) {
                state.getDinosaurs().add(new Carnivore("T-Rex-" + i));
            } else {
                state.getDinosaurs().add(new Herbivore("Triceratops-" + i));
            }
        }
        state.setDinosaursInEnclosures(initialDinosaurs);
        for (int i = 1; i <= initialVehicles; i++) {
            state.getVehicles().add(new Vehicle(i));
        }
        LOG.info("Estado inicial sembrado: {} turistas, {} dinosaurios, {} vehiculos, energia={}",
                initialTourists, initialDinosaurs, initialVehicles, initialEnergy);
    }

    public void run() {
        int steps = config.getInt("simulation.steps", 20);
        LOG.info("Iniciando simulacion: {} steps", steps);
        for (int i = 1; i <= steps; i++) {
            state.setStep(i);
            LOG.info(">> Step {} iniciado", i);
            state.clearStepFlags();
            state.clearPending();
            phaseB_processZones();
            phaseC_rollEvents();
            phaseD_tickVehicles();
            phaseE_persist();
            monitor.report(state);
            LOG.info("<< Step {} completado (revenues={}, expenses={}, events={})", i,
                    state.getPendingRevenues().size(),
                    state.getPendingExpenses().size(),
                    state.getPendingEventLogs().size());
        }
        LOG.info("Simulacion completada exitosamente");
    }

    private void phaseB_processZones() {
        for (ParkZone zone : zones) {
            zone.process(state);
        }
    }

    private void phaseC_rollEvents() {
        int maxEvents = config.getInt("simulation.maxEvents", 2);
        int eventsToFire = rnd.nextInt(maxEvents + 1);
        for (int i = 0; i < eventsToFire; i++) {
            SimulationEvent event = eventCatalog.get(rnd.nextInt(eventCatalog.size()));
            event.apply(state);
        }
    }

    private void phaseD_tickVehicles() {
        for (Vehicle v : state.getVehicles()) {
            v.tickRepair();
        }
    }

    private void phaseE_persist() {
        state.getPendingRevenues().forEach(r ->
                db.insertRevenue(r.type(), r.amount(), r.source()));
        state.getPendingExpenses().forEach(e ->
                db.insertExpense(e.type(), e.amount(), e.description()));
        state.getPendingEventLogs().forEach(e ->
                db.insertEvent(e.eventName(), e.step(), e.description()));
        for (Vehicle v : state.getVehicles()) {
            db.upsertVehicle(v.getId(), v.getStatus().name(), v.getRepairCountdown());
        }
    }

    public ParkState getState() {
        return state;
    }
}
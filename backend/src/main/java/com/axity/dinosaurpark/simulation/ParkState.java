package com.axity.dinosaurpark.simulation;

import com.axity.dinosaurpark.model.Dinosaur;
import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.model.Vehicle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Estado mutable de la simulacion en un step dado.
 * Las zonas y eventos mutan este estado; el motor lo persiste al cierre de cada step.
 */
public class ParkState {

    private int step;
    private int activeTourists;
    private int dinosaursInEnclosures;
    private double energy;

    private final List<Tourist> tourists       = new ArrayList<>();
    private final List<Dinosaur> dinosaurs     = new ArrayList<>();
    private final List<Vehicle> vehicles       = new ArrayList<>();
    private final List<String> activeEventNames = new ArrayList<>();

    private final List<PendingRevenue>  pendingRevenues  = new ArrayList<>();
    private final List<PendingExpense>  pendingExpenses  = new ArrayList<>();
    private final List<PendingEventLog> pendingEventLogs = new ArrayList<>();

    private boolean dealsHourActive;
    private double currentDiscount;

    // --- Mutadores de step ---
    public void clearStepFlags() {
        this.dealsHourActive = false;
        this.currentDiscount = 0.0;
        this.activeEventNames.clear();
    }

    public void clearPending() {
        this.pendingRevenues.clear();
        this.pendingExpenses.clear();
        this.pendingEventLogs.clear();
    }

    public void addRevenue(PendingRevenue r)   { pendingRevenues.add(r); }
    public void addExpense(PendingExpense e)   { pendingExpenses.add(e); }
    public void addEventLog(PendingEventLog e) { pendingEventLogs.add(e); }
    public void addActiveEvent(String name)    { activeEventNames.add(name); }

    // --- Getters ---
    public int getStep()                  { return step; }
    public int getActiveTourists()        { return activeTourists; }
    public int getDinosaursInEnclosures() { return dinosaursInEnclosures; }
    public double getEnergy()             { return energy; }
    public boolean isDealsHourActive()    { return dealsHourActive; }
    public double getCurrentDiscount()    { return currentDiscount; }

    public List<Tourist>   getTourists()   { return tourists; }
    public List<Dinosaur>  getDinosaurs()  { return dinosaurs; }
    public List<Vehicle>   getVehicles()   { return vehicles; }
    public List<String>    getActiveEventNames()    { return Collections.unmodifiableList(activeEventNames); }
    public List<PendingRevenue>  getPendingRevenues()  { return Collections.unmodifiableList(pendingRevenues); }
    public List<PendingExpense>  getPendingExpenses()  { return Collections.unmodifiableList(pendingExpenses); }
    public List<PendingEventLog> getPendingEventLogs() { return Collections.unmodifiableList(pendingEventLogs); }

    // --- Setters ---
    public void setStep(int step)                                  { this.step = step; }
    public void setActiveTourists(int activeTourists)              { this.activeTourists = activeTourists; }
    public void setDinosaursInEnclosures(int n)                    { this.dinosaursInEnclosures = n; }
    public void setEnergy(double energy)                           { this.energy = energy; }
    public void setDealsHourActive(boolean dealsHourActive)        { this.dealsHourActive = dealsHourActive; }
    public void setCurrentDiscount(double currentDiscount)         { this.currentDiscount = currentDiscount; }
}
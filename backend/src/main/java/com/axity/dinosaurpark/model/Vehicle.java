package com.axity.dinosaurpark.model;

public class Vehicle {

    public enum Status { AVAILABLE, IN_USE, BROKEN }

    private final int id;
    private Status status;
    private int repairCountdown;

    public Vehicle(int id) {
        this.id = id;
        this.status = Status.AVAILABLE;
    }

    public boolean isAvailable() { return status == Status.AVAILABLE; }

    public void take() {
        if (status != Status.AVAILABLE) {
            throw new IllegalStateException("Vehicle " + id + " no disponible: " + status);
        }
        this.status = Status.IN_USE;
    }

    public void release() {
        this.status = Status.AVAILABLE;
    }

    public void breakDown(int repairSteps) {
        if (repairSteps <= 0) {
            throw new IllegalArgumentException("repairSteps debe ser > 0");
        }
        this.status = Status.BROKEN;
        this.repairCountdown = repairSteps;
    }

    public void tickRepair() {
        if (status != Status.BROKEN) {
            return;
        }
        repairCountdown--;
        if (repairCountdown <= 0) {
            this.status = Status.AVAILABLE;
            this.repairCountdown = 0;
        }
    }

    void forceAvailable() {
        this.status = Status.AVAILABLE;
        this.repairCountdown = 0;
    }

    public int getId()              { return id; }
    public Status getStatus()       { return status; }
    public int getRepairCountdown() { return repairCountdown; }
}
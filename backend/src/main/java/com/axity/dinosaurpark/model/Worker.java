package com.axity.dinosaurpark.model;

public abstract class Worker {

    protected final int id;
    protected final String name;

    protected Worker(int id, String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Worker name no puede estar vacio");
        }
        this.id = id;
        this.name = name;
    }

    public abstract String role();

    public int getId()      { return id; }
    public String getName() { return name; }
}
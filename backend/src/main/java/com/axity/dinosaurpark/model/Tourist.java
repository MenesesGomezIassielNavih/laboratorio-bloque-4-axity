package com.axity.dinosaurpark.model;

public class Tourist {

    public enum Type { ADULT, CHILD }

    private final int id;
    private final String name;
    private final Type type;
    private boolean active;

    public Tourist(int id, String name, Type type) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Tourist name no puede estar vacio");
        }
        this.id = id;
        this.name = name;
        this.type = type;
        this.active = true;
    }

    public int getId()      { return id; }
    public String getName() { return name; }
    public Type getType()   { return type; }
    public boolean isActive() { return active; }
    public void deactivate()  { this.active = false; }
}
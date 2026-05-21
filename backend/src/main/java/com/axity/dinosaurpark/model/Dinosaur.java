package com.axity.dinosaurpark.model;

public abstract class Dinosaur {

    protected final String species;
    protected boolean escaped;

    protected Dinosaur(String species) {
        if (species == null || species.isBlank()) {
            throw new IllegalArgumentException("species no puede estar vacio");
        }
        this.species = species;
    }

    public abstract String diet();

    public boolean isEscaped()   { return escaped; }
    public void markEscaped()    { this.escaped = true; }
    public void recapture()      { this.escaped = false; }
    public String getSpecies()   { return species; }
}
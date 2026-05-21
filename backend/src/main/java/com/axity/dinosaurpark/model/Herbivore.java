package com.axity.dinosaurpark.model;

public class Herbivore extends Dinosaur {

    public Herbivore(String species) {
        super(species);
    }

    @Override
    public String diet() {
        return "PLANTS";
    }
}
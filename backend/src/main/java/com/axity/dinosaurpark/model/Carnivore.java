package com.axity.dinosaurpark.model;

public class Carnivore extends Dinosaur {

    public Carnivore(String species) {
        super(species);
    }

    @Override
    public String diet() {
        return "MEAT";
    }
}
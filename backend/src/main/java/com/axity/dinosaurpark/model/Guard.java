package com.axity.dinosaurpark.model;

public class Guard extends Worker {

    public Guard(int id, String name) {
        super(id, name);
    }

    @Override
    public String role() {
        return "GUARD";
    }

    /**
     * Intenta recapturar un dinosaurio escapado.
     * @return true si lo recapturo, false si el dinosaurio no estaba escapado.
     */
    public boolean tryRecapture(Dinosaur dino) {
        if (dino == null || !dino.isEscaped()) {
            return false;
        }
        dino.recapture();
        return true;
    }
}
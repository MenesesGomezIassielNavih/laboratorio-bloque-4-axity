package com.axity.dinosaurpark.zone;

import com.axity.dinosaurpark.model.Dinosaur;
import com.axity.dinosaurpark.simulation.ParkState;

/**
 * Recinto de observacion: cuenta dinosaurios que no han escapado
 * y patrulla intentando recapturar los que esten marcados como escapados.
 */
public class ObservationEnclosure implements ParkZone {

    private static final String NAME = "OBSERVATION_ENCLOSURE";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void process(ParkState state) {
        long inEnclosure = state.getDinosaurs().stream()
                                .filter(d -> !d.isEscaped())
                                .count();
        state.setDinosaursInEnclosures((int) inEnclosure);

        for (Dinosaur d : state.getDinosaurs()) {
            if (d.isEscaped()) {
                d.recapture();
            }
        }
    }
}
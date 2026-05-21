package com.axity.dinosaurpark.zone;

import static org.junit.jupiter.api.Assertions.*;
import com.axity.dinosaurpark.model.Carnivore;
import com.axity.dinosaurpark.model.Herbivore;
import com.axity.dinosaurpark.simulation.ParkState;
import org.junit.jupiter.api.Test;

class ObservationEnclosureTest {

    @Test
    void shouldCountNonEscapedDinosaurs() {
        ParkState state = new ParkState();
        state.getDinosaurs().add(new Carnivore("T-Rex"));
        state.getDinosaurs().add(new Herbivore("Triceratops"));
        new ObservationEnclosure().process(state);
        assertEquals(2, state.getDinosaursInEnclosures());
    }

    @Test
    void shouldRecaptureEscapedDinosaurs() {
        ParkState state = new ParkState();
        Carnivore esc = new Carnivore("Velociraptor");
        esc.markEscaped();
        state.getDinosaurs().add(esc);
        new ObservationEnclosure().process(state);
        assertFalse(esc.isEscaped());
    }
}
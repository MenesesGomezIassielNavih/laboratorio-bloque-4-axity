package com.axity.dinosaurpark.event;

import static org.junit.jupiter.api.Assertions.*;
import com.axity.dinosaurpark.model.Carnivore;
import com.axity.dinosaurpark.simulation.ParkState;
import java.util.Random;
import org.junit.jupiter.api.Test;

class DinosaurEscapeEventTest {

    @Test
    void shouldMarkOneDinosaurAsEscapedWhenApplied() {
        ParkState state = new ParkState();
        state.getDinosaurs().add(new Carnivore("T-Rex"));
        new DinosaurEscapeEvent(new Random(42L)).apply(state);
        assertTrue(state.getDinosaurs().get(0).isEscaped());
    }

    @Test
    void shouldDoNothingWhenDinosaurListIsEmpty() {
        ParkState state = new ParkState();
        new DinosaurEscapeEvent(new Random(42L)).apply(state);
        assertTrue(state.getActiveEventNames().isEmpty());
    }
}
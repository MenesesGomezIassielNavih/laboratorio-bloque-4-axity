package com.axity.dinosaurpark.simulation;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import com.axity.dinosaurpark.persistence.DatabaseService;
import java.math.BigDecimal;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SimulationEngineTest {

    @Mock
    DatabaseService db;

    @Test
    void shouldExecuteFullSimulationAndPersistAllMovements() {
        SimulationEngine engine = new SimulationEngine(db, new Random(42L));
        engine.run();
        verify(db, atLeastOnce())
                .insertRevenue(anyString(), any(BigDecimal.class), anyString());
        verify(db, atLeastOnce())
                .insertExpense(anyString(), any(BigDecimal.class), anyString());
        verify(db, atLeastOnce())
                .upsertVehicle(anyInt(), anyString(), anyInt());
    }

    @Test
    void shouldExposeInternalParkState() {
        SimulationEngine engine = new SimulationEngine(db);
        assertNotNull(engine.getState());
    }
}
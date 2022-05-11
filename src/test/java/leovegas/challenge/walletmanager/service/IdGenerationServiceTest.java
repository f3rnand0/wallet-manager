package leovegas.challenge.walletmanager.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class IdGenerationServiceTest {

    private static IdGenerationService idGenerationService;

    @BeforeAll
    public static void init() {
        idGenerationService = new IdGenerationService();
    }

    @Test
    public void whenIdRequested_thenIdGenerated() {
        String id = idGenerationService.generateId();
        assertNotNull(id);
        assertNotEquals(id, "");
        assertEquals(id.length(), 36);
    }
}
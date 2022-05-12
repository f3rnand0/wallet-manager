package leovegas.challenge.walletmanager.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class IdGenerationServiceTest {

    private static IdGenerationService idGenerationService;

    @BeforeAll
    public static void init() {
        idGenerationService = new IdGenerationService();
    }

    @Test
    void whenIdRequested_thenIdGenerated() {
        String id = idGenerationService.generateId();
        assertNotNull(id);
        assertEquals(36, id.length());
    }
}
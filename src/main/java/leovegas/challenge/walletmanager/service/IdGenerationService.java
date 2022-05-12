package leovegas.challenge.walletmanager.service;

import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class IdGenerationService {

    public String generateId() {
        return UUID.randomUUID().toString();
    }

}

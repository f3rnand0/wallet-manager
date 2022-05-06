package leovegas.challenge.walletmanager.service;

import java.util.UUID;
import leovegas.challenge.walletmanager.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IdGenerationService {

    public String generateId() {
        return UUID.randomUUID().toString();
    }

}

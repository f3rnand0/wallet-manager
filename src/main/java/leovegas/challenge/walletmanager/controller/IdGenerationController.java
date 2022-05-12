package leovegas.challenge.walletmanager.controller;

import leovegas.challenge.walletmanager.model.response.IdResponse;
import leovegas.challenge.walletmanager.service.IdGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IdGenerationController {

    @Autowired
    private IdGenerationService idGenerationService;

    @GetMapping("/api/generateId")
    public IdResponse generateId() {
        return new IdResponse(idGenerationService.generateId());
    }

}

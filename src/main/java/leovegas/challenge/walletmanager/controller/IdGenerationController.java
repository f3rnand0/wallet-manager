package leovegas.challenge.walletmanager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import leovegas.challenge.walletmanager.model.response.IdResponse;
import leovegas.challenge.walletmanager.service.IdGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IdGenerationController {

    @Autowired
    private IdGenerationService idGenerationService;

    @Operation(summary = "Generates a type 4 UUID string")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @GetMapping("/api/generateId")
    public IdResponse generateId() {
        return new IdResponse(idGenerationService.generateId());
    }

}

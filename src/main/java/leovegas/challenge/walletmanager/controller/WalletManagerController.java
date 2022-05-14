package leovegas.challenge.walletmanager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import leovegas.challenge.walletmanager.config.Constants;
import leovegas.challenge.walletmanager.model.request.TransactionRequest;
import leovegas.challenge.walletmanager.model.response.BalanceResponse;
import leovegas.challenge.walletmanager.model.response.TransactionHistoryResponse;
import leovegas.challenge.walletmanager.model.response.TransactionResponse;
import leovegas.challenge.walletmanager.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WalletManagerController {

    @Autowired
    private TransactionService transactionService;

    @Operation(summary = "Returns the current balance of a user's account")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successful operation"),
        @ApiResponse(responseCode = "404",
            description = "The username does not exist, or the user doesn't have an account")})
    @GetMapping("/api/getBalance/{username}")
    public BalanceResponse getBalance(@PathVariable String username) {
        return transactionService.getBalance(username);
    }

    @Operation(summary = "Debits a specified amount to a user's account")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successful operation"),
        @ApiResponse(responseCode = "404",
            description = "The username does not exist, or the user doesn't have an account "),
        @ApiResponse(responseCode = "400",
            description = "The transaction id was used on a previous transaction, or the user "
                + "doesn't have enough funds")})
    @PostMapping("/api/debit")
    public TransactionResponse debitFromAccount(
        @RequestBody TransactionRequest transactionRequest) {
        return transactionService.debitFromAccount(transactionRequest);
    }

    @Operation(summary = "Credits a specified amount to a user's account")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successful operation"),
        @ApiResponse(responseCode = "404",
            description = "The username does not exist, or the user doesn't have an account "),
        @ApiResponse(responseCode = "400",
            description = "The transaction id was used on a previous transaction")})
    @PostMapping("/api/credit")
    public TransactionResponse creditFromAccount(
        @RequestBody TransactionRequest transactionRequest) {
        return transactionService.creditFromAccount(transactionRequest);
    }

    @Operation(summary = "Provides a list of transactions of a user's account")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successful operation"),
        @ApiResponse(responseCode = "404",
            description = "The username does not exist, or the user doesn't have an account ")})
    @GetMapping("/api/getTransactionHistory/{username}")
    public TransactionHistoryResponse getTransactionHistory(@PathVariable String username,
        @RequestParam(value = "pageNumber", defaultValue = Constants.DEFAULT_PAGE_NUMBER,
            required = false) int pageNumber,
        @RequestParam(value = "pageSize", defaultValue = Constants.DEFAULT_PAGE_SIZE,
            required = false) int pageSize) {
        return transactionService.getTransactionHistory(username, pageNumber, pageSize);
    }

}

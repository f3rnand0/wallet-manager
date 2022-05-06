package leovegas.challenge.walletmanager.controller;

import leovegas.challenge.walletmanager.model.response.BalanceResponse;
import leovegas.challenge.walletmanager.model.request.TransactionRequest;
import leovegas.challenge.walletmanager.model.response.TransactionHistoryResponse;
import leovegas.challenge.walletmanager.model.response.TransactionResponse;
import leovegas.challenge.walletmanager.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WalletManagerController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/api/getBalance/{username}")
    public BalanceResponse getBalance(@PathVariable String username) {
        return transactionService.getBalance(username);
    }

    @PostMapping("/api/debit")
    public TransactionResponse debitFromAccount(@RequestBody TransactionRequest transactionRequest) {
        return transactionService.debitFromAccount(transactionRequest);
    }

    @PostMapping("/api/credit")
    public TransactionResponse creditFromAccount(@RequestBody TransactionRequest transactionRequest) {
        return transactionService.creditFromAccount(transactionRequest);
    }

    @GetMapping("/api/getTransactionHistory/{username}")
    public TransactionHistoryResponse getTransactionHistory(@PathVariable String username) {
        return transactionService.getTransactionHistory(username);
    }

}

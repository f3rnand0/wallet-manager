package leovegas.challenge.walletmanager.service;

import leovegas.challenge.walletmanager.model.request.TransactionRequest;
import leovegas.challenge.walletmanager.model.response.BalanceResponse;
import leovegas.challenge.walletmanager.model.response.TransactionHistoryResponse;
import leovegas.challenge.walletmanager.model.response.TransactionResponse;
import leovegas.challenge.walletmanager.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public BalanceResponse getBalance(String username) {
        return null;
    }

    public TransactionResponse debitFromAccount(TransactionRequest transactionRequest) {
        return null;
    }

    public TransactionResponse creditFromAccount(TransactionRequest transactionRequest) {
        return null;
    }

    public TransactionHistoryResponse getTransactionHistory(String username) {
        return null;
    }
}

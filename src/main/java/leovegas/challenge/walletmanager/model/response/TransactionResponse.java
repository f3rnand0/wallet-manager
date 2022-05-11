package leovegas.challenge.walletmanager.model.response;

import leovegas.challenge.walletmanager.entity.Transaction;
import leovegas.challenge.walletmanager.entity.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionResponse {

    private String username;

    private Integer accountNumber;

    private String transactionId;

    private TransactionType type;

    private Double amount;

    private Double balance;

    public TransactionResponse(String transactionId, TransactionType type, Double amount,
        Double balance) {
        this.transactionId = transactionId;
        this.type = type;
        this.amount = amount;
        this.balance = balance;
    }
}

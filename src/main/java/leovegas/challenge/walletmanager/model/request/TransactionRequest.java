package leovegas.challenge.walletmanager.model.request;

import leovegas.challenge.walletmanager.entity.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionRequest {

    private String username;

    private Integer accountNumber;

    private String transactionId;

    private Double amount;

    private TransactionType type;

}

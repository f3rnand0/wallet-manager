package leovegas.challenge.walletmanager.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.sql.Timestamp;
import leovegas.challenge.walletmanager.entity.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class TransactionResponse {

    private String username;

    private Integer accountNumber;

    private String transactionId;

    private TransactionType type;

    private Timestamp creationDate;

    private Double amount;

    private Double balance;

    public TransactionResponse(String transactionId, TransactionType type, Timestamp creationDate,
        Double amount, Double balance) {
        this.transactionId = transactionId;
        this.type = type;
        this.creationDate = creationDate;
        this.amount = amount;
        this.balance = balance;
    }
}

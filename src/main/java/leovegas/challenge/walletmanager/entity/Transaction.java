package leovegas.challenge.walletmanager.entity;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Data
@AllArgsConstructor
public class Transaction {

    @Id
    @Column(name = "transaction_id")
    private String id;

    private Timestamp creationDate;

    private TransactionType type;

    private Double amount;

    private Double balance;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
}

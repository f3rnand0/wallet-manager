package leovegas.challenge.walletmanager.entity;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "transaction")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    @Id
    @Column(name = "transaction_id")
    private String id;

    @Column(name = "creation_date")
    private Timestamp creationDate;

    private TransactionType type;

    private Double amount;

    private Double balance;

    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;
}

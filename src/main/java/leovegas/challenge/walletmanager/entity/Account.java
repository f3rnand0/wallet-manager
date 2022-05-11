package leovegas.challenge.walletmanager.entity;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Entity
@Table(name = "account")
@Data
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    private Integer accountNumber;

    @OneToOne(fetch=FetchType.LAZY)
    @MapsId
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "account")
    private List<Transaction> transactions;
}

package leovegas.challenge.walletmanager.repository;

import java.util.Optional;
import leovegas.challenge.walletmanager.entity.Account;
import leovegas.challenge.walletmanager.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {

    Optional<Transaction> findFirst1ByAccountOrderByCreationDateDesc(Account account);

    Page<Transaction> findAllByAccount(Account account, Pageable pageable);

}

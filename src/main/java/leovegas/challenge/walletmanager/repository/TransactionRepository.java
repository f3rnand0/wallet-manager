package leovegas.challenge.walletmanager.repository;

import leovegas.challenge.walletmanager.entity.Account;
import leovegas.challenge.walletmanager.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}

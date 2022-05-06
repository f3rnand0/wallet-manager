package leovegas.challenge.walletmanager.repository;

import leovegas.challenge.walletmanager.entity.Account;
import leovegas.challenge.walletmanager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

}

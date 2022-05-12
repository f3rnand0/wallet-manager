package leovegas.challenge.walletmanager.repository;

import java.util.Optional;
import leovegas.challenge.walletmanager.entity.Account;
import leovegas.challenge.walletmanager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByUser(User user);

    Optional<Account> findByAccountNumberAndUser(Integer accountNumber, User user);

}

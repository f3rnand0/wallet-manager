package leovegas.challenge.walletmanager.repository;

import java.util.Optional;
import leovegas.challenge.walletmanager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
}

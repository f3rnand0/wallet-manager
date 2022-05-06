package leovegas.challenge.walletmanager.repository;

import leovegas.challenge.walletmanager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}

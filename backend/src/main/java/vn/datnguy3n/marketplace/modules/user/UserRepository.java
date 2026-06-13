package vn.datnguy3n.marketplace.modules.user;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.datnguy3n.marketplace.modules.user.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}

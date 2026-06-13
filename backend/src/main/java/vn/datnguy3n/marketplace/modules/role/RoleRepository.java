package vn.datnguy3n.marketplace.modules.role;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.datnguy3n.marketplace.modules.role.entity.Role;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {

    Optional<Role> findByName(String name);

    boolean existsByName(String name);
}

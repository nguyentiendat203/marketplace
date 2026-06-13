package vn.datnguy3n.marketplace.modules.permission;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.datnguy3n.marketplace.modules.permission.entity.Permission;

import java.util.UUID;

public interface PermissionRepository extends JpaRepository<Permission, UUID> {

    boolean existsByName(String name);
}

package vn.datnguy3n.marketplace.modules.role;

import java.util.Optional;
import java.util.UUID;

import vn.datnguy3n.marketplace.core.crud.BaseCRUDService;
import vn.datnguy3n.marketplace.modules.role.dto.RolePermissionRequest;
import vn.datnguy3n.marketplace.modules.role.entity.Role;

public interface RoleService extends BaseCRUDService<Role> {

    Optional<Role> findByName(String name);

    Role assignPermissions(UUID roleId, RolePermissionRequest request);
}

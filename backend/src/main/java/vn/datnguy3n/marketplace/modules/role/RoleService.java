package vn.datnguy3n.marketplace.modules.role;

import java.util.Optional;
import java.util.UUID;

import vn.datnguy3n.marketplace.core.crud.BaseCRUDService;
import vn.datnguy3n.marketplace.modules.role.dto.RolePermissionRequest;
import vn.datnguy3n.marketplace.modules.role.dto.RoleResponse;
import vn.datnguy3n.marketplace.modules.role.entity.Role;

public interface RoleService extends BaseCRUDService<Role, RoleResponse> {

    Optional<Role> findByName(String name);

    RoleResponse assignPermissions(UUID roleId, RolePermissionRequest request);
}

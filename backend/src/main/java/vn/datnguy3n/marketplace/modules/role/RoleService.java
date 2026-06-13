package vn.datnguy3n.marketplace.modules.role;

import vn.datnguy3n.marketplace.modules.role.dto.RoleRequest;
import vn.datnguy3n.marketplace.modules.role.dto.RoleResponse;

import java.util.List;
import java.util.UUID;

public interface RoleService {

    RoleResponse create(RoleRequest request);

    RoleResponse getById(UUID id);

    List<RoleResponse> getAll();

    RoleResponse update(UUID id, RoleRequest request);

    void deleteById(UUID id);
}

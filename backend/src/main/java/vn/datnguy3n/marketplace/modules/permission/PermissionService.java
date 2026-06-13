package vn.datnguy3n.marketplace.modules.permission;

import vn.datnguy3n.marketplace.modules.permission.dto.PermissionRequest;
import vn.datnguy3n.marketplace.modules.permission.dto.PermissionResponse;

import java.util.List;
import java.util.UUID;

public interface PermissionService {

    PermissionResponse create(PermissionRequest request);

    PermissionResponse getById(UUID id);

    List<PermissionResponse> getAll();

    PermissionResponse update(UUID id, PermissionRequest request);

    void deleteById(UUID id);
}

package vn.datnguy3n.marketplace.modules.permission;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.datnguy3n.marketplace.modules.permission.dto.PermissionRequest;
import vn.datnguy3n.marketplace.modules.permission.dto.PermissionResponse;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;

    @Override
    public PermissionResponse create(PermissionRequest request) {
        // TODO: check name uniqueness, persist, return DTO
        return null;
    }

    @Override
    public PermissionResponse getById(UUID id) {
        // TODO: throw NotFoundException if absent, map to DTO
        return null;
    }

    @Override
    public List<PermissionResponse> getAll() {
        // TODO: map entities to DTOs
        return List.of();
    }

    @Override
    public PermissionResponse update(UUID id, PermissionRequest request) {
        // TODO: fetch, patch, persist, return DTO
        return null;
    }

    @Override
    public void deleteById(UUID id) {
        // TODO: soft-delete
    }
}

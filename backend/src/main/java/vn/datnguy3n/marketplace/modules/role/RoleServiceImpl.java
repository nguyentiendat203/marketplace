package vn.datnguy3n.marketplace.modules.role;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.datnguy3n.marketplace.modules.role.dto.RoleRequest;
import vn.datnguy3n.marketplace.modules.role.dto.RoleResponse;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public RoleResponse create(RoleRequest request) {
        // TODO: check name uniqueness, resolve permissions by IDs, persist, return DTO
        return null;
    }

    @Override
    public RoleResponse getById(UUID id) {
        // TODO: throw NotFoundException if absent, map to DTO
        return null;
    }

    @Override
    public List<RoleResponse> getAll() {
        // TODO: map entities to DTOs
        return List.of();
    }

    @Override
    public RoleResponse update(UUID id, RoleRequest request) {
        // TODO: fetch, patch permissions set, persist, return DTO
        return null;
    }

    @Override
    public void deleteById(UUID id) {
        // TODO: soft-delete
    }
}

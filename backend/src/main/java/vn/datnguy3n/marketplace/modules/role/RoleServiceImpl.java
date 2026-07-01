package vn.datnguy3n.marketplace.modules.role;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.datnguy3n.marketplace.core.crud.BaseMapper;
import vn.datnguy3n.marketplace.core.crud.BaseCRUDServiceImpl;
import vn.datnguy3n.marketplace.core.exception.BusinessException;
import vn.datnguy3n.marketplace.modules.permission.PermissionRepository;
import vn.datnguy3n.marketplace.modules.permission.entity.Permission;
import vn.datnguy3n.marketplace.modules.role.dto.RolePermissionRequest;
import vn.datnguy3n.marketplace.modules.role.dto.RoleResponse;
import vn.datnguy3n.marketplace.modules.role.entity.Role;

@Service
public class RoleServiceImpl extends BaseCRUDServiceImpl<Role, RoleResponse> implements RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RoleMapper roleMapper;

    public RoleServiceImpl(RoleRepository roleRepository, PermissionRepository permissionRepository,
            RoleMapper roleMapper) {
        super(roleRepository);
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.roleMapper = roleMapper;
    }

    @Override
    protected BaseMapper<Role, RoleResponse> getMapper() {
        return roleMapper;
    }

    @Override
    protected void beforeCreate(Role entity) {
        if (roleRepository.existsByName(entity.getName())) {
            throw new BusinessException("Role đã tồn tại: " + entity.getName(), HttpStatus.CONFLICT);
        }
    }

    @Override
    @Transactional(readOnly = true)
    protected Role fetchEntityById(UUID id) {
        return roleRepository.findByIdWithPermissions(id)
                .orElseThrow(() -> new BusinessException(
                        "Role không tồn tại với id: " + id, HttpStatus.NOT_FOUND));
    }

    @Override
    @Transactional
    public RoleResponse assignPermissions(UUID roleId, RolePermissionRequest request) {
        Role role = roleRepository.findByIdWithPermissions(roleId)
                .orElseThrow(() -> new BusinessException(
                        "Role không tồn tại với id: " + roleId, HttpStatus.NOT_FOUND));

        List<Permission> newPermissions = resolvePermissions(request.getPermissionIds());

        role.getPermissions().clear();
        role.getPermissions().addAll(newPermissions);

        return toDto(roleRepository.save(role));
    }

    @Override
    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name);
    }

    private List<Permission> resolvePermissions(Set<UUID> ids) {
        List<Permission> found = permissionRepository.findAllById(ids);
        if (found.size() != ids.size()) {
            throw new BusinessException(
                    "Một hoặc nhiều Permission không tồn tại", HttpStatus.BAD_REQUEST);
        }
        return found;
    }
}

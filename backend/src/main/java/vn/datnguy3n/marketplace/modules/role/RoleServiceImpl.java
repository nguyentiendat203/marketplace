package vn.datnguy3n.marketplace.modules.role;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.datnguy3n.marketplace.common.ResultPaginationResponse;
import vn.datnguy3n.marketplace.core.crud.BaseCRUDServiceImpl;
import vn.datnguy3n.marketplace.core.exception.BusinessException;
import vn.datnguy3n.marketplace.modules.permission.PermissionRepository;
import vn.datnguy3n.marketplace.modules.permission.dto.PermissionResponse;
import vn.datnguy3n.marketplace.modules.permission.entity.Permission;
import vn.datnguy3n.marketplace.modules.role.dto.RolePermissionRequest;
import vn.datnguy3n.marketplace.modules.role.entity.Role;

@Service
public class RoleServiceImpl extends BaseCRUDServiceImpl<Role> implements RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public RoleServiceImpl(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        super(roleRepository);
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    // ─── BaseCRUDServiceImpl hooks ────────────────────────────────────────────

    @Override
    protected void beforeCreate(Role entity) {
        if (roleRepository.existsByName(entity.getName())) {
            throw new BusinessException("Role đã tồn tại: " + entity.getName(), HttpStatus.CONFLICT);
        }
    }

    @Override
    protected void mergeEntity(Role source, Role target) {
        if (source.getName() != null) target.setName(source.getName());
        if (source.getDescription() != null) target.setDescription(source.getDescription());
    }

    // ─── Overrides to eager-load permissions ─────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public Role getById(UUID id) {
        return roleRepository.findByIdWithPermissions(id)
                .orElseThrow(() -> new BusinessException(
                        "Role không tồn tại với id: " + id, HttpStatus.NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public ResultPaginationResponse getAll(Specification<Role> spec, Pageable pageable) {
        Page<Role> page = roleRepository.findAll(spec, pageable);

        List<UUID> ids = page.getContent().stream().map(Role::getId).toList();
        Map<UUID, Role> withPermissions = ids.isEmpty()
                ? Map.of()
                : roleRepository.findAllWithPermissionsByIdIn(ids).stream()
                        .collect(Collectors.toMap(Role::getId, r -> r));

        ResultPaginationResponse.Meta meta = new ResultPaginationResponse.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(page.getTotalPages());
        meta.setTotal(page.getTotalElements());

        ResultPaginationResponse result = new ResultPaginationResponse();
        result.setMeta(meta);
        result.setResult(page.getContent().stream()
                .map(r -> withPermissions.getOrDefault(r.getId(), r))
                .toList());
        return result;
    }

    // ─── Permission management ────────────────────────────────────────────────
    @Override
    @Transactional
    public Role assignPermissions(UUID roleId, RolePermissionRequest request) {
        // 1. Lấy Role từ DB (Ném lỗi nếu không thấy)
        Role role = roleRepository.findByIdWithPermissions(roleId)
                .orElseThrow(() -> new BusinessException(
                        "Role không tồn tại với id: " + roleId, HttpStatus.NOT_FOUND));

        // 2. Lấy danh sách Permission mới dựa trên list ID từ Request 
        // và chặn bắt kỳ id nào không tồn tại trong DB
        List<Permission> newPermissions = resolvePermissions(request.getPermissionIds());

        // 3. XÓA SẠCH quyền cũ và NẠP TOÀN BỘ quyền mới
        role.getPermissions().clear();
        role.getPermissions().addAll(newPermissions);

        // 4. Lưu lại 
        return roleRepository.save(role);
    }

    @Override
    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name);
    }

    // ─── Helpers ─────────────────────────────────────────────────────────────

    private List<Permission> resolvePermissions(Set<UUID> ids) {
        List<Permission> found = permissionRepository.findAllById(ids);
        if (found.size() != ids.size()) {
            throw new BusinessException(
                    "Một hoặc nhiều Permission không tồn tại", HttpStatus.BAD_REQUEST);
        }
        return found;
    }
}

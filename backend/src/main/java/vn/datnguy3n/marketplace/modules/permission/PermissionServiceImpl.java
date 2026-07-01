package vn.datnguy3n.marketplace.modules.permission;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import vn.datnguy3n.marketplace.core.crud.BaseMapper;
import vn.datnguy3n.marketplace.core.crud.BaseCRUDServiceImpl;
import vn.datnguy3n.marketplace.core.exception.BusinessException;
import vn.datnguy3n.marketplace.modules.permission.dto.PermissionResponse;
import vn.datnguy3n.marketplace.modules.permission.entity.Permission;

@Service
public class PermissionServiceImpl extends BaseCRUDServiceImpl<Permission, PermissionResponse> implements PermissionService {

    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    public PermissionServiceImpl(PermissionRepository permissionRepository, PermissionMapper permissionMapper) {
        super(permissionRepository);
        this.permissionRepository = permissionRepository;
        this.permissionMapper = permissionMapper;
    }

    @Override
    protected BaseMapper<Permission, PermissionResponse> getMapper() {
        return permissionMapper;
    }

    @Override
    protected void beforeCreate(Permission entity) {
        permissionRepository.findByPmsApiPathAndPmsApiMethod(
                entity.getPmsApiPath(), entity.getPmsApiMethod())
                .ifPresent(existing -> {
                    throw new BusinessException(
                            "Permission đã tồn tại với cùng API path và method", HttpStatus.CONFLICT);
                });
    }
}

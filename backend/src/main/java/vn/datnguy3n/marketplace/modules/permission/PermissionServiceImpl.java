package vn.datnguy3n.marketplace.modules.permission;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import vn.datnguy3n.marketplace.core.crud.BaseCRUDServiceImpl;
import vn.datnguy3n.marketplace.core.exception.BusinessException;
import vn.datnguy3n.marketplace.modules.permission.entity.Permission;

@Service
public class PermissionServiceImpl extends BaseCRUDServiceImpl<Permission> implements PermissionService {

    private final PermissionRepository permissionRepository;

    public PermissionServiceImpl(PermissionRepository permissionRepository) {
        super(permissionRepository);
        this.permissionRepository = permissionRepository;
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

    @Override
    protected void mergeEntity(Permission source, Permission target) {
        if (source.getPmsName() != null) target.setPmsName(source.getPmsName());
        if (source.getPmsApiPath() != null) target.setPmsApiPath(source.getPmsApiPath());
        if (source.getPmsApiMethod() != null) target.setPmsApiMethod(source.getPmsApiMethod());
        if (source.getPmsApiModule() != null) target.setPmsApiModule(source.getPmsApiModule());
    }
}

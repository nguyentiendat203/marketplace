package vn.datnguy3n.marketplace.modules.permission;

import org.springframework.stereotype.Service;
import vn.datnguy3n.marketplace.core.crud.BaseCRUDServiceImpl;
import vn.datnguy3n.marketplace.modules.permission.entity.Permission;

@Service
public class PermissionServiceImpl extends BaseCRUDServiceImpl<Permission> implements PermissionService {

    public PermissionServiceImpl(PermissionRepository repository) {
        super(repository);
    }
}

package vn.datnguy3n.marketplace.modules.permission;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.datnguy3n.marketplace.core.crud.BaseCRUDController;
import vn.datnguy3n.marketplace.modules.permission.entity.Permission;

@RestController
@RequestMapping("/api/v1/permissions")
public class PermissionController extends BaseCRUDController<Permission> {

    public PermissionController(PermissionService permissionService) {
        super(permissionService);
    }
}

package vn.datnguy3n.marketplace.modules.role;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.datnguy3n.marketplace.core.crud.BaseCRUDController;
import vn.datnguy3n.marketplace.modules.role.entity.Role;

@RestController
@RequestMapping("/api/v1/roles")
public class RoleController extends BaseCRUDController<Role> {

    public RoleController(RoleService roleService) {
        super(roleService);
    }

}

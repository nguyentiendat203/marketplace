package vn.datnguy3n.marketplace.modules.role;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.datnguy3n.marketplace.core.crud.BaseCRUDController;
import vn.datnguy3n.marketplace.modules.role.dto.RolePermissionRequest;
import vn.datnguy3n.marketplace.modules.role.dto.RoleResponse;
import vn.datnguy3n.marketplace.modules.role.entity.Role;

@RestController
@RequestMapping("/api/v1/roles")
public class RoleController extends BaseCRUDController<Role, RoleResponse> {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        super(roleService);
        this.roleService = roleService;
    }

    @PostMapping("/{id}/permissions")
    public ResponseEntity<RoleResponse> assignPermissions(@PathVariable("id") UUID id,
            @Valid @RequestBody RolePermissionRequest request) {
        return ResponseEntity.ok(roleService.assignPermissions(id, request));
    }
}

package vn.datnguy3n.marketplace.modules.role.dto;

import lombok.Getter;
import lombok.Setter;
import vn.datnguy3n.marketplace.modules.permission.dto.PermissionResponse;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class RoleResponse {

    private UUID id;
    private String name;
    private String description;
    private Set<PermissionResponse> permissions;
}

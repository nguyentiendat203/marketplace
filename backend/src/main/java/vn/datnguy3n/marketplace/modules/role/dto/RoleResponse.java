package vn.datnguy3n.marketplace.modules.role.dto;

import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import vn.datnguy3n.marketplace.modules.permission.dto.PermissionResponse;

@Getter
@Setter
public class RoleResponse {

    private UUID id;
    private String name;
    private String description;
    private List<PermissionResponse> permissions;
}

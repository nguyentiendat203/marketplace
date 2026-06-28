package vn.datnguy3n.marketplace.modules.role.dto;

import java.util.Set;
import java.util.UUID;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RolePermissionRequest {

    @NotEmpty(message = "Danh sách permissionIds không được để trống")
    private Set<UUID> permissionIds;
}

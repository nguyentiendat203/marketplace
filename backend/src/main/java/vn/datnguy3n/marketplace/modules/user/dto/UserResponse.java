package vn.datnguy3n.marketplace.modules.user.dto;

import java.time.Instant;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import vn.datnguy3n.marketplace.modules.role.dto.RoleResponse;

@Getter
@Setter
public class UserResponse {

    private UUID id;
    private String email;
    private String fullName;
    private String avatarUrl;
    private String phone;
    private RoleResponse role;
    private boolean active;
    private boolean kycVerified;
    private Instant createdAt;
}

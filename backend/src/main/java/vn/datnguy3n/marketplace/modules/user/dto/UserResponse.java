package vn.datnguy3n.marketplace.modules.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class UserResponse {

    private UUID id;
    private String email;
    private String fullName;
    private String avatarUrl;
    private String phone;
    private UUID roleId;
    private boolean active;
    private boolean kycVerified;
    private LocalDateTime createdAt;
}

package vn.datnguy3n.marketplace.modules.auth.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AuthResponse {

    private String accessToken;
    private String tokenType = "Bearer";
    private UserInfo user;

    @Getter
    @Setter
    public static class UserInfo {
        private UUID id;
        private String email;
        private String fullName;
        private String role;
    }
}

package vn.datnguy3n.marketplace.modules.auth.dto;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponse {

    private String accessToken;
    private String refreshToken;
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

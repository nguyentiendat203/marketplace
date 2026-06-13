package vn.datnguy3n.marketplace.modules.auth;

import vn.datnguy3n.marketplace.modules.auth.dto.AuthResponse;
import vn.datnguy3n.marketplace.modules.auth.dto.LoginRequest;
import vn.datnguy3n.marketplace.modules.auth.dto.RegisterRequest;

public interface AuthService {

    AuthResponse login(LoginRequest request);

    AuthResponse register(RegisterRequest request);

    void forgotPassword(String email);

    void changePassword(String userId, String oldPassword, String newPassword);
}

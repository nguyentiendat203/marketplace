package vn.datnguy3n.marketplace.modules.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.datnguy3n.marketplace.modules.auth.dto.AuthResponse;
import vn.datnguy3n.marketplace.modules.auth.dto.LoginRequest;
import vn.datnguy3n.marketplace.modules.auth.dto.RegisterRequest;
import vn.datnguy3n.marketplace.modules.user.UserService;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;

    @Override
    public AuthResponse login(LoginRequest request) {
        // TODO: validate credentials, generate JWT
        return null;
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        // TODO: check email uniqueness, hash password, persist user, return JWT
        return null;
    }

    @Override
    public void forgotPassword(String email) {
        // TODO: generate OTP and send via email (@Async)
    }

    @Override
    public void changePassword(String userId, String oldPassword, String newPassword) {
        // TODO: verify old password, hash new password, persist
    }
}

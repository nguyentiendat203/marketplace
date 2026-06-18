package vn.datnguy3n.marketplace.modules.auth;

import jakarta.servlet.http.HttpServletResponse;
import vn.datnguy3n.marketplace.modules.auth.dto.AuthResponse;
import vn.datnguy3n.marketplace.modules.auth.dto.ForgotPasswordRequest;
import vn.datnguy3n.marketplace.modules.auth.dto.LoginRequest;
import vn.datnguy3n.marketplace.modules.auth.dto.RegisterRequest;
import vn.datnguy3n.marketplace.modules.auth.dto.ResetPasswordRequest;
import vn.datnguy3n.marketplace.modules.user.dto.UserResponse;

public interface AuthService {

    AuthResponse login(LoginRequest request, HttpServletResponse response);

    String register(RegisterRequest request);

    AuthResponse refresh(String refreshToken, HttpServletResponse response);

    UserResponse activate(String key);

    String forgotPassword(ForgotPasswordRequest request);

    String resetPassword(ResetPasswordRequest request);
}

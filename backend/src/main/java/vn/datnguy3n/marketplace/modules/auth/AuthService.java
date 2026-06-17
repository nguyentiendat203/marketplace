package vn.datnguy3n.marketplace.modules.auth;

import jakarta.servlet.http.HttpServletResponse;
import vn.datnguy3n.marketplace.modules.auth.dto.AuthResponse;
import vn.datnguy3n.marketplace.modules.auth.dto.LoginRequest;
import vn.datnguy3n.marketplace.modules.auth.dto.RegisterRequest;

public interface AuthService {

    AuthResponse login(LoginRequest request, HttpServletResponse response);

    AuthResponse register(RegisterRequest request, HttpServletResponse response);

    AuthResponse refresh(String refreshToken, HttpServletResponse response);
}

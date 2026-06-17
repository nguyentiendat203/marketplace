package vn.datnguy3n.marketplace.modules.auth;

import java.time.Instant;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import vn.datnguy3n.marketplace.config.security.JwtProperties;
import vn.datnguy3n.marketplace.config.security.TokenProvider;
import vn.datnguy3n.marketplace.core.exception.BusinessException;
import vn.datnguy3n.marketplace.modules.auth.dto.AuthResponse;
import vn.datnguy3n.marketplace.modules.auth.dto.LoginRequest;
import vn.datnguy3n.marketplace.modules.auth.dto.RegisterRequest;
import vn.datnguy3n.marketplace.modules.role.RoleService;
import vn.datnguy3n.marketplace.modules.role.entity.Role;
import vn.datnguy3n.marketplace.modules.user.UserService;
import vn.datnguy3n.marketplace.modules.user.entity.User;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final String DEFAULT_ROLE = "BUYER";

    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final UserService userService;
    private final RoleService roleService;
    private final JwtProperties jwtProperties;

    @Override
    @Transactional
    public AuthResponse login(LoginRequest request, HttpServletResponse httpResponse) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String accessToken = tokenProvider.createToken(authentication, jwtProperties.getAccessTokenValidityInSeconds());
            String refreshToken = tokenProvider.createToken(authentication, jwtProperties.getRefreshTokenValidityInSeconds());

            User user = userService.findEntityByEmail(request.getEmail());
            userService.updateRefreshToken(user.getId(), refreshToken);
            setRefreshTokenCookie(httpResponse, refreshToken);

            return buildResponse(accessToken, refreshToken, user);
        } catch (BadCredentialsException e) {
            throw new BusinessException("Email hoặc mật khẩu không đúng", HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request, HttpServletResponse httpResponse) {
        if (userService.existsByEmail(request.getEmail())) {
            throw new BusinessException("Email đã được sử dụng", HttpStatus.CONFLICT);
        }

        Role defaultRole = roleService.findByName(DEFAULT_ROLE)
                .orElseThrow(() -> new BusinessException(
                        "Vai trò mặc định không tồn tại trong hệ thống", HttpStatus.INTERNAL_SERVER_ERROR));

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setFullName(request.getFullName());
        user.setRole(defaultRole);
        User savedUser = userService.create(user);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                savedUser.getEmail(), null,
                List.of(new SimpleGrantedAuthority("ROLE_" + defaultRole.getName().toUpperCase()))
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = tokenProvider.createToken(authentication, jwtProperties.getAccessTokenValidityInSeconds());
        String refreshToken = tokenProvider.createToken(authentication, jwtProperties.getRefreshTokenValidityInSeconds());

        userService.updateRefreshToken(savedUser.getId(), refreshToken);
        setRefreshTokenCookie(httpResponse, refreshToken);

        return buildResponse(accessToken, refreshToken, savedUser);
    }

    @Override
    @Transactional
    public AuthResponse refresh(String refreshToken, HttpServletResponse httpResponse) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new BusinessException("Refresh token không hợp lệ", HttpStatus.UNAUTHORIZED);
        }

        User user = userService.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new BusinessException("Refresh token không hợp lệ", HttpStatus.UNAUTHORIZED));

        Instant expiryTime = user.getUpdatedAt().plusSeconds(jwtProperties.getRefreshTokenValidityInSeconds());
        if (Instant.now().isAfter(expiryTime)) {
            throw new BusinessException("Refresh token đã hết hạn, vui lòng đăng nhập lại", HttpStatus.UNAUTHORIZED);
        }

        String roleName = user.getRole() != null ? "ROLE_" + user.getRole().getName().toUpperCase() : "ROLE_USER";
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getEmail(), null,
                List.of(new SimpleGrantedAuthority(roleName))
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String newAccessToken = tokenProvider.createToken(authentication, jwtProperties.getAccessTokenValidityInSeconds());
        String newRefreshToken = tokenProvider.createToken(authentication, jwtProperties.getRefreshTokenValidityInSeconds());

        userService.updateRefreshToken(user.getId(), newRefreshToken);
        setRefreshTokenCookie(httpResponse, newRefreshToken);

        return buildResponse(newAccessToken, newRefreshToken, user);
    }

    private void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(jwtProperties.getRefreshTokenValidityInSeconds())
                .sameSite("Strict")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    private AuthResponse buildResponse(String accessToken, String refreshToken, User user) {
        AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo();
        userInfo.setId(user.getId());
        userInfo.setEmail(user.getEmail());
        userInfo.setFullName(user.getFullName());
        userInfo.setRole(user.getRole() != null ? user.getRole().getName() : null);

        AuthResponse response = new AuthResponse();
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        response.setUser(userInfo);
        return response;
    }
}

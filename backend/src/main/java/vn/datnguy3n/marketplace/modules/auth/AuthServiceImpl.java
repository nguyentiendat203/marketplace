package vn.datnguy3n.marketplace.modules.auth;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import vn.datnguy3n.marketplace.config.security.JwtProperties;
import vn.datnguy3n.marketplace.config.security.TokenProvider;
import vn.datnguy3n.marketplace.core.exception.BusinessException;
import vn.datnguy3n.marketplace.modules.auth.dto.AuthResponse;
import vn.datnguy3n.marketplace.modules.auth.dto.ForgotPasswordRequest;
import vn.datnguy3n.marketplace.modules.auth.dto.LoginRequest;
import vn.datnguy3n.marketplace.modules.auth.dto.RegisterRequest;
import vn.datnguy3n.marketplace.modules.auth.dto.ResetPasswordRequest;
import vn.datnguy3n.marketplace.modules.auth.entity.PasswordResetToken;
import vn.datnguy3n.marketplace.modules.mail.MailService;
import vn.datnguy3n.marketplace.modules.role.RoleService;
import vn.datnguy3n.marketplace.modules.role.entity.Role;
import vn.datnguy3n.marketplace.modules.user.UserService;
import vn.datnguy3n.marketplace.modules.user.dto.UserResponse;
import vn.datnguy3n.marketplace.modules.user.entity.User;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final String DEFAULT_ROLE = "ROLE_USER";
    private static final long RESET_TOKEN_TTL_MINUTES = 5;

    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final UserService userService;
    private final RoleService roleService;
    private final JwtProperties jwtProperties;
    private final MailService mailService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${application.mail.activation-ttl-minutes}")
    private long ttlMinutes;

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

            return buildAuthResponse(accessToken, refreshToken, user);
        } catch (BadCredentialsException e) {
            throw new BusinessException("Email hoặc mật khẩu không đúng", HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    @Transactional
    public String register(RegisterRequest request) {
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
        user.setActivated(false);

        User savedUser = userService.create(user);

        mailService.sendActivationEmail(savedUser);

        return "Đăng ký thành công! Vui lòng kiểm tra email của bạn để thực hiện xác thực kích hoạt tài khoản.";
    }

    @Override
    @Transactional(noRollbackFor = BusinessException.class) // ✨ Thêm noRollbackFor vào đây
    public UserResponse activate(String key) {
        User user = userService.findByActivationKey(key)
                .orElseThrow(() -> new BusinessException(
                        "Mã kích hoạt không hợp lệ hoặc đã được sử dụng!", HttpStatus.BAD_REQUEST));

        long minutesElapsed = Duration.between(user.getCreatedAt(), Instant.now()).toMinutes();
        if (minutesElapsed > ttlMinutes) {
            userService.delete(user.getId());
            throw new BusinessException(
                    "Liên kết xác thực đã hết hạn! Vui lòng thực hiện đăng ký lại tài khoản.", HttpStatus.BAD_REQUEST);
        }

        user.setActivated(true);
        user.setActivationKey(null);
        User activatedUser = userService.saveUser(user);

        return toUserResponse(activatedUser);
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

        return buildAuthResponse(newAccessToken, newRefreshToken, user);
    }

    @Override
    @Transactional
    public String forgotPassword(ForgotPasswordRequest request) {
        User user = userService.findEntityByEmail(request.getEmail());
        if (user == null) {
            return "Nếu email tồn tại trong hệ thống, mã khôi phục mật khẩu sẽ được gửi đến hộp thư của bạn.";
        }

        passwordResetTokenRepository.deleteAllByUserId(user.getId());

        String rawToken = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(rawToken);
        resetToken.setExpiryDate(Instant.now().plusSeconds(RESET_TOKEN_TTL_MINUTES * 60));
        resetToken.setUser(user);
        passwordResetTokenRepository.save(resetToken);

        mailService.sendPasswordResetEmail(user, rawToken);

        return "Mã khôi phục mật khẩu đã được gửi đến hộp thư của bạn. Vui lòng kiểm tra email để lấy mã.";
    }

    @Override
    @Transactional
    public String resetPassword(ResetPasswordRequest request) {
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new BusinessException("Mật khẩu mới và xác nhận mật khẩu không trùng khớp", HttpStatus.BAD_REQUEST);
        }

        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new BusinessException("Mã xác nhận không hợp lệ hoặc đã được sử dụng", HttpStatus.BAD_REQUEST));

        if (Instant.now().isAfter(resetToken.getExpiryDate())) {
            passwordResetTokenRepository.deleteByIdPhysically(resetToken.getId());
            throw new BusinessException("Mã xác nhận đã hết hạn. Vui lòng yêu cầu gửi lại mã mới.", HttpStatus.BAD_REQUEST);
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userService.saveUser(user);

        passwordResetTokenRepository.deleteByIdPhysically(resetToken.getId());

        return "Đặt lại mật khẩu thành công. Vui lòng đăng nhập với mật khẩu mới.";
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

    private AuthResponse buildAuthResponse(String accessToken, String refreshToken, User user) {
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

    private UserResponse toUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setFullName(user.getFullName());
        response.setAvatarUrl(user.getAvatarUrl());
        response.setPhone(user.getPhone());
        response.setRoleId(user.getRole() != null ? user.getRole().getId() : null);
        response.setActive(user.isActivated());
        response.setKycVerified(user.isKycVerified());
        response.setCreatedAt(user.getCreatedAt() != null
                ? LocalDateTime.ofInstant(user.getCreatedAt(), ZoneId.systemDefault())
                : null);
        return response;
    }
}

package vn.datnguy3n.marketplace.config.security;

public final class SecurityConstants {

    public static final String[] PUBLIC_PATHS = {
            "/",
            "/api/v1/auth/authenticate",
            "/api/v1/auth/register",
            "/api/v1/auth/refresh",
            "/api/v1/auth/activate",
            "/api/v1/auth/forgot-password",
            "/api/v1/auth/reset-password"
    };

    private SecurityConstants() {}
}

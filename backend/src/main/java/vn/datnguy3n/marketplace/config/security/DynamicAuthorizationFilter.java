package vn.datnguy3n.marketplace.config.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import vn.datnguy3n.marketplace.modules.role.RolePermissionService;

@RequiredArgsConstructor
public class DynamicAuthorizationFilter extends OncePerRequestFilter {

    private static final String SUPER_ADMIN_AUTHORITY = "ROLE_ADMIN";
    private static final AntPathMatcher antPathMatcher = new AntPathMatcher();

    private final RolePermissionService rolePermissionService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        return Arrays.stream(SecurityConstants.PUBLIC_PATHS)
                .anyMatch(pattern -> antPathMatcher.match(pattern, requestUri));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            filterChain.doFilter(request, response);
            return;
        }

        boolean isSuperAdmin = authentication.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .anyMatch(SUPER_ADMIN_AUTHORITY::equals);

        if (isSuperAdmin) {
            filterChain.doFilter(request, response);
            return;
        }

        String apiPath = request.getRequestURI();
        String httpMethod = request.getMethod();

        List<String> authorityNames = authentication.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .toList();

        boolean hasPermission = rolePermissionService.hasPermissionForRequest(authorityNames, apiPath, httpMethod);

        if (!hasPermission) {
            throw new AccessDeniedException("Bạn không có quyền truy cập tài nguyên này");
        }

        filterChain.doFilter(request, response);
    }
}

package vn.datnguy3n.marketplace.config.security;

import java.time.Instant;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
@Service
public class TokenProvider {

    private static final String AUTHORITIES_KEY = "auth";

    private final JwtEncoder jwtEncoder;
    private final JwtProperties jwtProperties;
    public TokenProvider(JwtEncoder jwtEncoder, JwtProperties jwtProperties) {
        this.jwtEncoder = jwtEncoder;
        this.jwtProperties = jwtProperties;
    }

    public String createToken(Authentication authentication, long validityInSeconds) {
        System.out.println(">>> THỜI HẠN TOKEN ĐANG ĐƯỢC DÙNG: " + validityInSeconds);
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Instant now = Instant.now();
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS512).build();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(authentication.getName())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(validityInSeconds))
                .claim(AUTHORITIES_KEY, authorities)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }

    public long getAccessTokenValidityInSeconds() {
        return jwtProperties.getAccessTokenValidityInSeconds();
    }

    public long getRefreshTokenValidityInSeconds() {
        return jwtProperties.getRefreshTokenValidityInSeconds();
    }

}

package com.dju.woojiman.global.security;

import com.dju.woojiman.domain.user.UserRepository;
import com.dju.woojiman.domain.user.Users;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {

    private final UserRepository userRepository;

    public JwtTokenProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Value("${jwt.secret-key}")
    private String secretKey;
    @Value("${jwt.access-token-validity-in-ms}")
    private int accessTokenValidityInMs;
    @Value("${jwt.refresh-token-validity-in-ms}")
    private int refreshTokenValidityInMs;
    private Key key;

    @PostConstruct
    protected void init() {
        byte[] keyBytes = Base64.getUrlDecoder().decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(String userId) {
        try {
            if (userId == null) {
                throw new IllegalArgumentException("userId is null");
            }

            Map<String, Object> claims = new HashMap<>();

            Users user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("user not found"));

            claims.put("role", user.getRole());
            Date now = new Date();
            Date validity = new Date(now.getTime() + accessTokenValidityInMs);

            return Jwts.builder().setSubject(userId).setClaims(claims).setIssuedAt(now)
                    .setExpiration(validity).signWith(key, SignatureAlgorithm.HS256).compact();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String createRefreshToken(String userId) {
        try {
            Date now = new Date();
            Date validity = new Date(now.getTime() + refreshTokenValidityInMs);

            return Jwts.builder().setSubject(userId).setIssuedAt(now)
                    .setExpiration(validity).signWith(key, SignatureAlgorithm.HS256).compact();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public long getRefreshTokenRemainingTime(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            Date expiration = claims.getExpiration();
            long remainingTime = expiration.getTime() - System.currentTimeMillis();
            return remainingTime / 1000;
        } catch (Exception e) {
            return 0L;
        }
    }

    public boolean validateToken(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);

            return true;
        } catch (SignatureException | MalformedJwtException | IllegalArgumentException |
                 ExpiredJwtException e) {
            return false;
        }
    }
}

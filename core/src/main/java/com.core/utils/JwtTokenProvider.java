package com.core.utils;

import com.core.constants.FConstants;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;

@NoArgsConstructor
@Slf4j
@Component
public class JwtTokenProvider extends OncePerRequestFilter {

    private String secretKey = "FaceClone is my project test Microservice and Kafka and Redis";
    private long JWT_EXPIRATION = 604800000L;

    public String generateToken(String userId) {
        log.info("Generating JWT token for user: " + userId);
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);

        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public String getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey())
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey()).parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
            return false;
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
            return false;
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
            return false;
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
            return false;
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.info("Starting filter chain");
        String requestURI = request.getRequestURI();

        if (requestURI.startsWith("/user-service")) {
//		if (requestURI.startsWith("/login") || requestURI.startsWith("/register") || requestURI.startsWith("/home")) {
            log.info("No JWT validation needed for: {}", requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        String token = getTokenFromRequest(request);
        log.info("Get token: {}", token);
        if (token != null && validateToken(token)) {
            String userId = getUserIdFromJWT(token);
            if (userId != null) {
                request.setAttribute(FConstants.USER_ID, userId);
            } else {
                log.error("Failed to load user details for token: {}", token);
            }
        }
        filterChain.doFilter(request, response);
        log.info("Ending filter chain");
    }

    public String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        log.info("Bearer token: {}", bearerToken);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public SecretKey secretKey() {
        byte[] keyBytes = Arrays.copyOf(this.secretKey.getBytes(StandardCharsets.UTF_8), 512);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
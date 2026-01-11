package com.smart.procurement1.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    // ✅ 256-bit secure key (MUST be ≥ 32 chars)
    private static final SecretKey SECRET_KEY =
            Keys.hmacShaKeyFor(
                    "smart-procurement-secure-jwt-key-256-bit-long"
                            .getBytes()
            );

    private static final long EXPIRATION_TIME =
            24 * 60 * 60 * 1000; // 24 hours

    /* ================= GENERATE TOKEN ================= */
    public String generateToken(String username, String role) {

        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(System.currentTimeMillis() + EXPIRATION_TIME)
                )
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    /* ================= VALIDATE TOKEN ================= */
    public boolean isTokenValid(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /* ================= EXTRACT USERNAME ================= */
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    /* ================= EXTRACT ROLE ================= */
    public String extractRole(String token) {
        return extractClaims(token).get("role", String.class);
    }

    /* ================= EXTRACT CLAIMS ================= */
    private Claims extractClaims(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}

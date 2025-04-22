package edu.tcu.cs.tcusportsbroadcasting.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {

    private static final String SECRET_KEY = "U29tZVN1cGVyU2VjdXJlS2V5Rm9ySlc1VGVzdDEyMzQ1Ng==";

    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour in ms

    public static String generateToken(Long userId, String role, String email) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("role", role);
        claims.put("email", email);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(String.valueOf(userId))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }
}

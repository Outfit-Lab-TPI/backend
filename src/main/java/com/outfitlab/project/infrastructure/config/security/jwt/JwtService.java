package com.outfitlab.project.infrastructure.config.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtService {

    @Value("${JWT_SECRET}")
    private String jwtSecret;
    @Value("${TOKEN_EXPIRATION}")
    private Long tokenExpiration;
    @Value("${REFRESH_EXPIRATION}")
    private Long refreshExpiration;

    public String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7); // Elimina el prefijo "Bearer "
        }
        return null;
    }

    private Key getSigninKey(){
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Claims getAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSigninKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    public String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, Long expiration){
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigninKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails){
        return buildToken(extraClaims, userDetails, tokenExpiration);
    }

    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateRefreshToken(UserDetails userDetails){
        return buildToken(new HashMap<>(), userDetails, refreshExpiration);
    }

    public Boolean isTokenValid(String token, UserDetails userDetails){
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername());
    }

    public Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

}

package com.example.securityjwttemplate.util;

import com.example.securityjwttemplate.exception.UnauthorizedException;
import com.example.securityjwttemplate.model.enums.TokenType;
import com.example.securityjwttemplate.service.MyUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    private final Logger logger;
    private final MyUserDetailsService myUserDetailsService;
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    @Value("${application.security.jwt.access-token-expiration}")
    private Long accessTokenExpiration;
    @Value("${application.security.jwt.refresh-token-expiration}")
    private Long refreshTokenExpiration;

    public JwtService(MyUserDetailsService myUserDetailsService) {
        this.logger = LoggerFactory.getLogger(JwtService.class);
        this.myUserDetailsService = myUserDetailsService;
    }

    public SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(UserDetails user) {
        return generateToken(user, accessTokenExpiration, TokenType.fromString("access"));
    }

    public String generateRefreshToken(UserDetails user) {
        return generateToken(user, refreshTokenExpiration, TokenType.fromString("refresh"));
    }

    private String generateToken(UserDetails user, long expiration, TokenType type) {
        return Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .claim("roles", user.getAuthorities())
                .claim("token_type", type.name())
                .signWith(getKey())
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        return userDetails.getUsername().equals(extractUsername(token)) && !isTokenExpired(token);
    }

    public UserDetails extractUserDetails(String token) {
        String username = extractUsername(token);
        return myUserDetailsService.loadUserByUsername(username);
    }

    private String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }


    public TokenType extractTokenType(String token) {
        String typeString = extractClaim(token, e -> e.get("token_type", String.class));
        if (typeString != null) return TokenType.fromString(typeString);
        return null;
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date(System.currentTimeMillis()));
    }


    private <T> T extractClaim(String token, Function<Claims, T> claimsTFunction) {
        Claims claims = extractAllClaims(token);
        return claimsTFunction.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException e) {
            logger.warn("Failed to parse jwt: {}", e.getMessage());
            throw new UnauthorizedException("Unauthorized: valid refresh token is required.");
        }
    }

}

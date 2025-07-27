package com.example.securityjwttemplate.filter;

import com.example.securityjwttemplate.exception.UnauthorizedException;
import com.example.securityjwttemplate.model.enums.TokenType;
import com.example.securityjwttemplate.service.MyUserDetailsService;
import com.example.securityjwttemplate.util.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtService jwtService;
    private final MyUserDetailsService myUserDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, MyUserDetailsService myUserDetailsService) {
        this.jwtService = jwtService;
        this.myUserDetailsService = myUserDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        TokenType tokenType = null;
        UserDetails userDetails = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                userDetails = jwtService.extractUserDetails(token);
                tokenType = jwtService.extractTokenType(token);
            } catch (UnauthorizedException e) {
                logger.warn("Failed to parse jwt: {}", e.getMessage());
            }
        }

        if (SecurityContextHolder.getContext().getAuthentication() == null && tokenType != null && tokenType.equals(TokenType.ACCESS)) {


            if (jwtService.isTokenValid(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}

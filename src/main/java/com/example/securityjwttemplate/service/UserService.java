package com.example.securityjwttemplate.service;

import com.example.securityjwttemplate.dto.request.LoginRequest;
import com.example.securityjwttemplate.dto.request.RegisterRequest;
import com.example.securityjwttemplate.dto.response.LoginResponse;
import com.example.securityjwttemplate.dto.response.RefreshResponse;
import com.example.securityjwttemplate.dto.response.RegisterResponse;
import com.example.securityjwttemplate.exception.UnauthorizedException;
import com.example.securityjwttemplate.model.Role;
import com.example.securityjwttemplate.model.User;
import com.example.securityjwttemplate.model.UserPrincipal;
import com.example.securityjwttemplate.model.enums.TokenType;
import com.example.securityjwttemplate.repository.UserRepository;
import com.example.securityjwttemplate.util.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final JwtService jwtService;
    private final RoleService roleService;
    private final UserRepository repository;
    private final AuthenticationManager authManager;
    private final PasswordEncoder encoder;

    private final String ERROR_MESSAGE = "Unauthorized: login or password is invalid.";

    public UserService(JwtService jwtService, RoleService roleService, UserRepository repository, AuthenticationManager authManager, PasswordEncoder encoder) {
        this.jwtService = jwtService;
        this.roleService = roleService;
        this.repository = repository;
        this.authManager = authManager;
        this.encoder = encoder;
    }

    public RegisterResponse register(RegisterRequest request) {
        User user = new User();
        user.setUsername(request.login());
        user.setPassword(encoder.encode(request.password()));
        Role userRole = roleService.getRoleByName("ROLE_USER");
        user.setRole(userRole);

        User createdUser = repository.save(user);

        RegisterResponse response = new RegisterResponse(createdUser.getId(), createdUser.getUsername());
        return response;
    }

    public LoginResponse login(LoginRequest request) {
        Optional<User> user = repository.findByUsername(request.login());
        if (!user.isPresent())
            throw new UnauthorizedException(ERROR_MESSAGE);

        try {
            Authentication authentication =
                    authManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    user.get().getUsername(),
                                    request.password()
                            )
                    );
            UserPrincipal userPrincipal = new UserPrincipal(user.get());
            String accessToken = jwtService.generateAccessToken(userPrincipal);
            String refreshToken = jwtService.generateRefreshToken(userPrincipal);
            LoginResponse response = new LoginResponse(accessToken, refreshToken);
            return response;
        } catch (BadCredentialsException e) {
            throw new UnauthorizedException(ERROR_MESSAGE);
        }

    }

    public RefreshResponse refresh(HttpServletRequest httpRequest) {
        String authHeader = httpRequest.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            TokenType tokenType = jwtService.extractTokenType(token);
            if (tokenType != null && tokenType.equals(TokenType.REFRESH)) {
                UserDetails userDetails = jwtService.extractUserDetails(token);
                if (jwtService.isTokenValid(token, userDetails)) {
                    String accessToken = jwtService.generateAccessToken(userDetails);
                    RefreshResponse response = new RefreshResponse(accessToken);
                    return response;
                }
            }
        }
        throw new UnauthorizedException("Unauthorized: valid refresh token is required.");
    }
}
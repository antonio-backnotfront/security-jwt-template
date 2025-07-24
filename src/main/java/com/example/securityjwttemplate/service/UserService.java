package com.example.securityjwttemplate.service;

import com.example.securityjwttemplate.dto.request.LoginRequest;
import com.example.securityjwttemplate.dto.request.RegisterRequest;
import com.example.securityjwttemplate.dto.response.LoginResponse;
import com.example.securityjwttemplate.dto.response.RegisterResponse;
import com.example.securityjwttemplate.model.Role;
import com.example.securityjwttemplate.model.User;
import com.example.securityjwttemplate.model.UserPrincipal;
import com.example.securityjwttemplate.repository.UserRepository;
import com.example.securityjwttemplate.util.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserRepository repository;

    @Autowired
    private AuthenticationManager authManager;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public RegisterResponse register(RegisterRequest request){
        User user = new User();
        user.setUsername(request.login());
        user.setPassword(encoder.encode(request.password()));
        Role userRole =  roleService.getRoleByName("ROLE_USER");
        user.setRole(userRole);

        User createdUser = repository.save(user);

        RegisterResponse response = new RegisterResponse(createdUser.getId(), createdUser.getUsername(), request.password());
        return response;
    }

    public LoginResponse login(LoginRequest request){
        User user = new User();
        user.setUsername(request.login());
        user.setPassword(encoder.encode(request.password()));
        user.setRole(roleService.getRoleByName("ROLE_USER"));

        try {
            Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), request.password()));
            UserPrincipal userPrincipal = new UserPrincipal(user);
            String jwt = jwtService.generateToken(userPrincipal);
            LoginResponse response = new LoginResponse(jwt);
            return response;
        } catch (BadCredentialsException e){
            throw e;
        }


    }

}

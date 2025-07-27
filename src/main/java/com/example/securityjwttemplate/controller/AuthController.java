package com.example.securityjwttemplate.controller;

import com.example.securityjwttemplate.dto.request.LoginRequest;
import com.example.securityjwttemplate.dto.request.RegisterRequest;
import com.example.securityjwttemplate.dto.response.LoginResponse;
import com.example.securityjwttemplate.dto.response.RefreshResponse;
import com.example.securityjwttemplate.dto.response.RegisterResponse;
import com.example.securityjwttemplate.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/auth")
public class AuthController {

    private final UserService service;

    public AuthController(UserService service) {
        this.service = service;
    }

    @PostMapping(path = "register")
    public ResponseEntity<RegisterResponse> register(@RequestBody @Valid RegisterRequest request) {

        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping(path = "login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(service.login(request));
    }

    @PostMapping(path = "refresh")
    public ResponseEntity<RefreshResponse> refresh(HttpServletRequest httpRequest) {
        return ResponseEntity.ok(service.refresh(httpRequest));
    }

}

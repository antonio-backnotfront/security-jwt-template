package com.example.securityjwttemplate.controller;

import com.example.securityjwttemplate.dto.request.LoginRequest;
import com.example.securityjwttemplate.dto.request.RegisterRequest;
import com.example.securityjwttemplate.dto.response.LoginResponse;
import com.example.securityjwttemplate.dto.response.RegisterResponse;
import com.example.securityjwttemplate.model.User;
import com.example.securityjwttemplate.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/auth")
public class AuthController {

    @Autowired
    private UserService service;

    @PostMapping(path = "register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request){

        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping(path = "login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request){
        return ResponseEntity.ok(service.login(request));
    }

}

package com.example.securityjwttemplate.service;

import com.example.securityjwttemplate.exception.NotFoundException;
import com.example.securityjwttemplate.model.Role;
import com.example.securityjwttemplate.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository repository;

    public Role getRoleByName(String name) {
        Optional<Role> role = repository.findByNameIgnoreCase(name);
        if (!role.isPresent()) {
            throw new NotFoundException(String.format("Role with name '%s' not found.", name));
        }
        return role.get();
    }

}

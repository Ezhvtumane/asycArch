package com.georgyorlov.auth.controller;

import com.georgyorlov.auth.dto.UserCreateDTO;
import com.georgyorlov.auth.dto.UserUpdateDTO;
import com.georgyorlov.auth.entity.UserEntity;
import com.georgyorlov.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@EnableMethodSecurity
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public UserEntity findById(@PathVariable("id") Long id) {//DTO return
        return userService.findById(id);
    }

    @PostMapping
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> createUser(
        @RequestBody UserCreateDTO userCreateDTO
    ) {
        UserEntity andSaveExampleEntity = userService.createUser(userCreateDTO);
        return ResponseEntity.ok(andSaveExampleEntity.getPublicId().toString());
    }

    @PutMapping("/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateUser(
        @PathVariable("id") Long id,
        @RequestBody UserUpdateDTO dto
    ) {
        UserEntity andSaveExampleEntity = userService.updateUser(id, dto);
        return ResponseEntity.ok(andSaveExampleEntity.getPublicId().toString());
    }
}

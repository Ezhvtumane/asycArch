package com.georgyorlov.audit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/audit")
@RequiredArgsConstructor
//@EnableMethodSecurity
public class AuditController {

    @GetMapping
    public ResponseEntity get() {
        return ResponseEntity.ok().build();
    }
}

package com.georgyorlov.accounting.controller;

import com.georgyorlov.accounting.entity.BillingCycle;
import com.georgyorlov.accounting.service.BillingCycleService;
import jakarta.websocket.server.PathParam;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/billing")
@RequiredArgsConstructor
@EnableMethodSecurity
public class BillingController {

    private final BillingCycleService billingCycleService;

    @PostMapping("/open")
    //@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public BillingCycle createAndOpenBillingCycle() {
        return billingCycleService.createAndOpenBillingCycle();
    }

    @PostMapping("/close")
    //@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public BillingCycle closeBillingCycle() {
        return billingCycleService.closeOpenedBillingCycle();
    }

    @GetMapping("/{publicId}")
    //@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public BillingCycle getBillingCycleByPublicId(@PathParam("publicId") UUID publicId) {
        return billingCycleService.findByPublicId(publicId);
    }

    @GetMapping
    //@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public List<BillingCycle> getAllBillingCycles() {
        return billingCycleService.findAll();
    }

}

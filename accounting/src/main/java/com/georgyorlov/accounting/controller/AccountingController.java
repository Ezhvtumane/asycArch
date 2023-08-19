package com.georgyorlov.accounting.controller;

import com.georgyorlov.accounting.dto.UserAccountResponse;
import com.georgyorlov.accounting.entity.Account;
import com.georgyorlov.accounting.entity.TransactionEntity;
import com.georgyorlov.accounting.service.AccountService;
import com.georgyorlov.accounting.service.BillingCycleService;
import com.georgyorlov.accounting.service.TransactionService;
import jakarta.websocket.server.PathParam;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounting")
@RequiredArgsConstructor
@EnableMethodSecurity
public class AccountingController {

    private final AccountService accountService;
    private final TransactionService transactionService;
    private final BillingCycleService billingCycleService;

    @GetMapping("/{userPublicId}")
    //@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WORKER')")
    //add check for workers onlyu their account
    public UserAccountResponse getUserAccountAndUserTransactions(@PathParam("userPublicId") UUID userPublicId) {
        Account accountByUserPublicId = accountService.findAccountByUserPublicId(userPublicId);
        List<TransactionEntity> allTransactionsForUser = transactionService.findAllTransactionsForUser(userPublicId);
        return UserAccountResponse.builder()
            .account(accountByUserPublicId)
            .transactionEntityList(allTransactionsForUser)
            .build();
    }

    @GetMapping("/management/{billingPublicId}")
    public Long getManagementIncomeForBillingCycle(@PathParam("billing_public_id") UUID billingPublicId) {
        return transactionService.getManagementIncomeForBillingCycle(billingPublicId);
    }
}

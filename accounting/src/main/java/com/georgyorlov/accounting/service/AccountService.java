package com.georgyorlov.accounting.service;

import com.georgyorlov.accounting.entity.Account;
import com.georgyorlov.accounting.repository.AccountRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    @Transactional
    public void createAccountForUSer(UUID userPublicId) {
        Account account = new Account();
        account.setPublicId(UUID.randomUUID());
        account.setUserPublicId(userPublicId);
        account.setCurrentBalance(0L);
        save(account);
    }

    @Transactional
    public void updateUserAccount(UUID userPublicId, Long debit, Long credit) {
        Account accountByUserPublicId = findAccountByUserPublicId(userPublicId);
        Long currentBalance = accountByUserPublicId.getCurrentBalance();
        Long sum = currentBalance - credit + debit;
        accountByUserPublicId.setCurrentBalance(sum);
        save(accountByUserPublicId);
    }

    public Account findAccountByUserPublicId(UUID userPublicId) {
        return accountRepository.findByUserPublicId(userPublicId)
            .orElseThrow(() -> new RuntimeException("fix me"));
    }


    private void save(Account account) {
        accountRepository.save(account);
    }
}

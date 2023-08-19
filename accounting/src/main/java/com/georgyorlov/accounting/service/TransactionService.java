package com.georgyorlov.accounting.service;

import com.georgyorlov.accounting.entity.BillingCycle;
import com.georgyorlov.accounting.entity.TransactionEntity;
import com.georgyorlov.accounting.entity.TransactionType;
import com.georgyorlov.accounting.repository.TransactionRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountService accountService;
    private final BillingCycleService billingCycleService;

    public List<TransactionEntity> findAllTransactionsByBillingCycle(UUID billingPublicId) {
        return transactionRepository.findAllByBillingPublicId(billingPublicId);
    }

    @Transactional
    public void createTransactionEntity(UUID taskPublicId, UUID userPublicId, Long debit, Long credit) {
        BillingCycle openedBillingCycle = billingCycleService.getOpenedBillingCycle();

        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setPublicId(UUID.randomUUID());
        transactionEntity.setTaskPublicId(taskPublicId);
        transactionEntity.setUserPublicId(userPublicId);
        transactionEntity.setBillingPublicId(openedBillingCycle.getPublicId());
        transactionEntity.setCredit(credit);
        transactionEntity.setDebit(debit);
        transactionEntity.setTransactionType(getTransactionType(transactionEntity));
        save(transactionEntity);

        accountService.updateUserAccount(userPublicId, debit, credit);
    }

    @Transactional
    public void createPayment(UUID userPublicId, Long credit) {
        BillingCycle openedBillingCycle = billingCycleService.getOpenedBillingCycle();

        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setPublicId(UUID.randomUUID());
        transactionEntity.setUserPublicId(userPublicId);
        transactionEntity.setBillingPublicId(openedBillingCycle.getPublicId());
        transactionEntity.setCredit(credit);
        transactionEntity.setTransactionType(TransactionType.PAYMENT);
        save(transactionEntity);

        accountService.updateUserAccount(userPublicId, 0L, credit);
    }


    private TransactionType getTransactionType(TransactionEntity transactionEntity) {
        return
            transactionEntity.getDebit() - transactionEntity.getCredit() > 0
                ? TransactionType.DEBIT
                : TransactionType.CREDIT;
    }

    private void save(TransactionEntity transactionEntity) {
        transactionRepository.save(transactionEntity);
    }

    public List<TransactionEntity> findAllTransactionsForUser(UUID userPublicId) {
        return transactionRepository.findAllByUserPublicId(userPublicId);
    }

    public Long getManagementIncomeForBillingCycle(UUID billingPublicId) {
        return transactionRepository.findAllByBillingPublicId(billingPublicId).stream()
            .map(tr -> tr.getDebit() - tr.getCredit())
            .reduce(Long::sum)
            .orElse(0L);
    }
}

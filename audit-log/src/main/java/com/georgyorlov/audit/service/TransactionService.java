package com.georgyorlov.audit.service;

import com.georgyorlov.audit.entity.TransactionEntity;
import com.georgyorlov.audit.entity.TransactionType;
import com.georgyorlov.audit.repository.TransactionRepository;
import com.georgyorlov.avro.transaction.v1.TransactionCreated;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public List<TransactionEntity> findAllTransactionsByBillingCycle(UUID billingPublicId) {
        return transactionRepository.findAllByBillingPublicId(billingPublicId);
    }

    @Transactional
    public void createTransactionEntity(UUID transactionPublicId,
                                        UUID taskPublicId,
                                        UUID userPublicId,
                                        UUID billingPublicId,
                                        Long debit,
                                        Long credit) {
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setPublicId(transactionPublicId);
        transactionEntity.setTaskPublicId(taskPublicId);
        transactionEntity.setUserPublicId(userPublicId);
        transactionEntity.setBillingPublicId(billingPublicId);
        transactionEntity.setCredit(credit);
        transactionEntity.setDebit(debit);
        transactionEntity.setTransactionType(getTransactionType(transactionEntity));
        save(transactionEntity);
    }

    @Transactional
    public void createPayment(UUID userPublicId, UUID billingPublicId, Long credit) {
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setPublicId(UUID.randomUUID());
        transactionEntity.setUserPublicId(userPublicId);
        transactionEntity.setBillingPublicId(billingPublicId);
        transactionEntity.setCredit(credit);
        transactionEntity.setTransactionType(TransactionType.PAYMENT);
        save(transactionEntity);

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
        // убирать тип payment
        // сделать разную логику расчета для open и close периодов
        //
        return transactionRepository.findAllByBillingPublicId(billingPublicId).stream()
            .map(tr -> tr.getDebit() - tr.getCredit())
            .reduce(Long::sum)
            .orElse(0L);
    }

    public void createOrUpdateFromTransactionStreaming(TransactionCreated value) {
        createTransactionEntity(UUID.fromString(value.getPublicId().toString()),
                                UUID.fromString(value.getTaskPublicId().toString()),
                                UUID.fromString(value.getUserPublicId().toString()),
                                UUID.fromString(value.getBillingPublicId().toString()),
                                value.getDebit(),
                                value.getCredit());
    }
}

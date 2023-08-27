package com.georgyorlov.accounting.service;

import com.georgyorlov.accounting.entity.TransactionEntity;
import com.georgyorlov.accounting.entity.TransactionType;
import com.georgyorlov.accounting.repository.TransactionRepository;
import com.georgyorlov.accounting.service.kafka.KafkaSenderService;
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
    private final AccountService accountService;
    private final KafkaSenderService kafkaSenderService;

    public List<TransactionEntity> findAllTransactionsByBillingCycle(UUID billingPublicId) {
        return transactionRepository.findAllByBillingPublicId(billingPublicId);
    }

    @Transactional
    public void createTransactionEntity(UUID taskPublicId, UUID userPublicId, UUID billingPublicId, Long debit, Long credit) {
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setPublicId(UUID.randomUUID());
        transactionEntity.setTaskPublicId(taskPublicId);
        transactionEntity.setUserPublicId(userPublicId);
        transactionEntity.setBillingPublicId(billingPublicId);
        transactionEntity.setCredit(credit);
        transactionEntity.setDebit(debit);
        transactionEntity.setTransactionType(getTransactionType(transactionEntity));
        save(transactionEntity);

        accountService.updateUserAccount(userPublicId, debit, credit);

        TransactionCreated transactionStreaming = TransactionCreated
            .newBuilder()
            .setPublicId(transactionEntity.getPublicId().toString())
            .setBillingPublicId(transactionEntity.getBillingPublicId().toString())
            .setUserPublicId(transactionEntity.getUserPublicId().toString())
            .setTaskPublicId(transactionEntity.getTaskPublicId().toString())
            .setDebit(transactionEntity.getDebit())
            .setCredit(transactionEntity.getCredit())
            .setTransactionType(transactionEntity.getTransactionType().name())
            .build();

        kafkaSenderService.sendTransactionCreatedEvent(transactionStreaming);
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

        accountService.updateUserAccount(userPublicId, 0L, credit);

        TransactionCreated transactionCreated = TransactionCreated
            .newBuilder()
            .setPublicId(transactionEntity.getPublicId().toString())
            .setBillingPublicId(transactionEntity.getBillingPublicId().toString())
            .setUserPublicId(transactionEntity.getUserPublicId().toString())
            .setTaskPublicId(transactionEntity.getTaskPublicId().toString())
            .setDebit(transactionEntity.getDebit())
            .setCredit(transactionEntity.getCredit())
            .setTransactionType(transactionEntity.getTransactionType().name())
            .build();

        kafkaSenderService.sendTransactionCreatedEvent(transactionCreated);
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
}

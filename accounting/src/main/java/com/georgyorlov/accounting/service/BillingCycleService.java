package com.georgyorlov.accounting.service;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingLong;

import com.georgyorlov.accounting.entity.BillingCycle;
import com.georgyorlov.accounting.entity.BillingStatus;
import com.georgyorlov.accounting.entity.TransactionEntity;
import com.georgyorlov.accounting.repository.BillingCycleRepository;
import jakarta.persistence.LockModeType;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BillingCycleService {

    private final BillingCycleRepository billingCycleRepository;
    private final TransactionService transactionService;

    @Transactional
    public BillingCycle createAndOpenBillingCycle() {
        // findLast для переходного периода
        // проверять что больше нет открытых периодов
        // и переносить отрицательные остати с предыдущего
        BillingCycle billingCycle = new BillingCycle();
        billingCycle.setPublicId(UUID.randomUUID());
        billingCycle.setBillingStatus(BillingStatus.OPEN);
        billingCycle.setCreatedAt(Instant.now());
        return save(billingCycle);
    }

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE) //block everything on period closing...
    public BillingCycle closeOpenedBillingCycle() {
        BillingCycle openedBillingCycle = getOpenedBillingCycle();
        List<TransactionEntity> allTransactionsByBillingCycle =
            transactionService.findAllTransactionsByBillingCycle(openedBillingCycle.getPublicId());

        allTransactionsByBillingCycle
            .stream()
            .collect(groupingBy(
                         TransactionEntity::getUserPublicId,
                         summingLong(tra -> tra.getDebit() - tra.getCredit())
                     )
            )
            .forEach((uuid, billingCycleBalance) -> {
                if (billingCycleBalance > 0) {
                    transactionService.createPayment(uuid, openedBillingCycle.getPublicId(), billingCycleBalance); //след период с нуля
                    // send payment mail
                } else {
                    // send payment mail
                    // nothing change at account and transactions.
                    // как перенести - в слудующий билинг период?
                }
            });

        //отправка BE события что (БЦ зарылся) нет - скорее про транзакции и БЕ по транзакциям.
        openedBillingCycle.setBillingStatus(BillingStatus.CLOSE);
        openedBillingCycle.setClosedAt(Instant.now());
        return save(openedBillingCycle);

    }

    public BillingCycle getOpenedBillingCycle() {
        //check for only one opened in one moment of time
        return billingCycleRepository.findByBillingStatus(BillingStatus.OPEN)
            .orElseThrow(() -> new RuntimeException("fix me"));
    }

    private BillingCycle save(BillingCycle billingCycle) {
        return billingCycleRepository.save(billingCycle);
    }

    public BillingCycle findByPublicId(UUID publicId) {
        return billingCycleRepository.findByPublicId(publicId)
            .orElseThrow(() -> new RuntimeException("fix me"));
    }

    public List<BillingCycle> findAll() {
        return billingCycleRepository.findAll();
    }
}

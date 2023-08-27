package com.georgyorlov.audit.repository;

import com.georgyorlov.audit.entity.TransactionEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    Optional<TransactionEntity> findByPublicId(UUID publicId);

    List<TransactionEntity> findAllByBillingPublicId(UUID billingPublicId);

    List<TransactionEntity> findAllByUserPublicId(UUID userPublicId);

}
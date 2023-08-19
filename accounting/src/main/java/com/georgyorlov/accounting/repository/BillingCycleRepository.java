package com.georgyorlov.accounting.repository;

import com.georgyorlov.accounting.entity.BillingCycle;
import com.georgyorlov.accounting.entity.BillingStatus;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillingCycleRepository extends JpaRepository<BillingCycle, Long> {

    Optional<BillingCycle> findByPublicId(UUID publicId);

    Optional<BillingCycle> findByBillingStatus(BillingStatus billingStatus);

}

package com.georgyorlov.accounting.repository;

import com.georgyorlov.accounting.entity.Account;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByPublicId(UUID publicId);

    Optional<Account> findByUserPublicId(UUID userPublicId);
}

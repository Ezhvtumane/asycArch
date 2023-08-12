package com.georgyorlov.task.repository;

import com.georgyorlov.task.entity.Role;
import com.georgyorlov.task.entity.UserEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByPublicId(UUID publicID);

    List<UserEntity> findByRole(Role role);

    @Query(value = " SELECT * FROM users "
        + " WHERE role = 'WORKER' "
        + " ORDER BY random() LIMIT 1 ", nativeQuery = true)
    UserEntity getRandomWorkerEntity();

}

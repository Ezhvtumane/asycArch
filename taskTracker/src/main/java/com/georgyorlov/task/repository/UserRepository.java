package com.georgyorlov.task.repository;

import com.georgyorlov.task.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

}

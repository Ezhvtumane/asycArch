package com.georgyorlov.audit.service;

import com.georgyorlov.audit.entity.Role;
import com.georgyorlov.audit.entity.UserEntity;
import com.georgyorlov.audit.repository.UserRepository;
import com.georgyorlov.avro.user.v1.UserStreaming;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void createOrUpdateFromUserStreaming(UserStreaming user) {
        userRepository
            .findByPublicId(UUID.fromString(user.getPublicId().toString()))
            .ifPresentOrElse(
                userEntity -> {
                    updateUser(userEntity, user);
                },
                () -> {
                    createAndSaveUserEntity(user);
                }
            );
    }

    public UUID getRandomWorkerPublicId() {
        return userRepository.getRandomWorkerEntity().getPublicId();
    }

    private void updateUser(UserEntity userEntity, UserStreaming user) {
        userEntity.setRole(Role.valueOf(user.getRole().toString()));
        save(userEntity);
    }

    private void createAndSaveUserEntity(UserStreaming user) {
        UserEntity userEntity = new UserEntity();
        userEntity.setLogin(user.getLogin().toString());
        userEntity.setRole(Role.valueOf(user.getRole().toString()));
        userEntity.setPublicId(UUID.fromString(user.getPublicId().toString()));
        UserEntity saved = save(userEntity);
    }

    private UserEntity save(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }
}

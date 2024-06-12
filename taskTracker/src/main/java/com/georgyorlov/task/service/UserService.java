package com.georgyorlov.task.service;

import com.georgyorlov.task.dto.kafka.UserEventDTO;
import com.georgyorlov.task.entity.Role;
import com.georgyorlov.task.entity.UserEntity;
import com.georgyorlov.task.repository.UserRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void createOrUpdateFromUserStreaming(UserEventDTO userEventDTO) {
        userRepository
            .findByPublicId(userEventDTO.getPublicId())
            .ifPresentOrElse(
                user -> {
                    updateUser(user, userEventDTO);
                },
                () -> {
                    createAndSaveUserEntity(userEventDTO);
                }
            );
    }

    public UUID getRandomWorkerPublicId() {
        return userRepository.getRandomWorkerEntity().getPublicId();
    }

    private void updateUser(UserEntity user, UserEventDTO userEventDTO) {
        user.setRole(Role.valueOf(userEventDTO.getRole()));
        save(user);
    }

    private void createAndSaveUserEntity(UserEventDTO dto) {
        UserEntity user = new UserEntity();
        user.setLogin(dto.getLogin());
        user.setRole(Role.valueOf(dto.getRole()));
        user.setPublicId(dto.getPublicId());
        save(user);
    }

    private UserEntity save(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }
}

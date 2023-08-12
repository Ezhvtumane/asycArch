package com.georgyorlov.task.service;

import com.georgyorlov.task.dto.kafka.UserEventDTO;
import com.georgyorlov.task.entity.Role;
import com.georgyorlov.task.entity.UserEntity;
import com.georgyorlov.task.repository.UserRepository;
import java.util.List;
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
                task -> {
                    task.setRole(Role.valueOf(userEventDTO.getRole()));
                    save(task);
                },
                () -> {
                    createAndSaveUserEntity(userEventDTO);
                }
            );
    }

    private void createAndSaveUserEntity(UserEventDTO dto) {
        UserEntity user = new UserEntity();
        user.setLogin(dto.getLogin());
        user.setRole(Role.valueOf(dto.getRole()));
        user.setPublicId(dto.getPublicId());
        save(user);
    }

    public UserEntity save(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    public UserEntity findByPublicId(UUID publicId) {
        return userRepository.findByPublicId(publicId)
            .orElseThrow(() -> new RuntimeException("No entity found by id: " + publicId));
    }

    public List<UserEntity> findAllWorkers() {
        return userRepository.findByRole(Role.WORKER);
    }

    public UUID getRandomWorkerPublicId() {
        return userRepository.getRandomWorkerEntity().getPublicId();
    }
}

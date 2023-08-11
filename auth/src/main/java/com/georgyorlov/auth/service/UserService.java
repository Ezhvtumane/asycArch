package com.georgyorlov.auth.service;

import com.georgyorlov.auth.dto.UserCreateDTO;
import com.georgyorlov.auth.dto.UserUpdateDTO;
import com.georgyorlov.auth.dto.kafka.UserEventDTO;
import com.georgyorlov.auth.entity.Role;
import com.georgyorlov.auth.entity.UserEntity;
import com.georgyorlov.auth.repository.UserRepository;
import com.georgyorlov.auth.service.kafka.KafkaSenderService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final KafkaSenderService kafkaSenderService;

    @Transactional
    public UserEntity createUser(UserCreateDTO dto) {
        UserEntity savedUser = createAndSaveUserEntity(dto);
        sendUserCreatedEvent(savedUser);
        return savedUser;
    }

    private UserEntity createAndSaveUserEntity(UserCreateDTO dto) {
        UserEntity user = new UserEntity();
        user.setLogin(dto.getLogin());
        user.setRole(Role.valueOf(dto.getRole()));
        user.setPublicId(UUID.randomUUID());
        return save(user);
    }

    public UserEntity save(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    public UserEntity findById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("No entity found by id: " + id));
    }

    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

    @Transactional
    public UserEntity updateUser(Long id, UserUpdateDTO dto) {
        UserEntity userEntity = findById(id);
        userEntity.setRole(Role.valueOf(dto.getRole()));
        UserEntity updatedUser = save(userEntity);
        sendUserUpdatedEvent(updatedUser);
        return updatedUser;
    }

    @Async
    public void sendUserCreatedEvent(UserEntity user) {
        UserEventDTO userEventDTO = new UserEventDTO();
        userEventDTO.setLogin(user.getLogin());
        userEventDTO.setPublicId(user.getPublicId().toString());
        userEventDTO.setRole(user.getRole().name());
        kafkaSenderService.sendUserStreamingEvent(userEventDTO, "user-streaming");
    }

    @Async
    public void sendUserUpdatedEvent(UserEntity user) {
        UserEventDTO userEventDTO = new UserEventDTO();
        userEventDTO.setLogin(user.getLogin());
        userEventDTO.setPublicId(user.getPublicId().toString());
        userEventDTO.setRole(user.getRole().name());
        kafkaSenderService.sendUserStreamingEvent(userEventDTO, "user-streaming");
    }
}
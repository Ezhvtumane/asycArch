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
        sendUserStreamingEvent(savedUser);
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

    public UserEntity findByPublicId(UUID publicId) {
        return userRepository.findByPublicId(publicId)
            .orElseThrow(() -> new RuntimeException("No entity found by id: " + publicId));
    }

    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

    @Transactional
    public UserEntity updateUser(UUID publicId, UserUpdateDTO dto) {
        UserEntity userEntity = findByPublicId(publicId);
        userEntity.setRole(Role.valueOf(dto.getRole()));
        UserEntity updatedUser = save(userEntity);
        sendUserStreamingEvent(updatedUser);
        return updatedUser;
    }

    @Async
    public void sendUserStreamingEvent(UserEntity user) {
        UserEventDTO userEventDTO = new UserEventDTO();
        userEventDTO.setLogin(user.getLogin());
        userEventDTO.setPublicId(user.getPublicId());
        userEventDTO.setRole(user.getRole().name());
        kafkaSenderService.sendUserStreamingEvent(userEventDTO, "user-streaming");
    }

}

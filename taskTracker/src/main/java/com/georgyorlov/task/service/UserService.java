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
    public UserEntity createOrUpdateFromUserStreaming(UserEventDTO userEventDTO) {
        UserEntity byPublicId = userRepository.findByPublicId(UUID.fromString(userEventDTO.getPublicId()))
            .orElseGet(() -> createAndSaveUserEntity(userEventDTO));
        return byPublicId;
    }

    private UserEntity createAndSaveUserEntity(UserEventDTO dto) {
        UserEntity user = new UserEntity();
        user.setLogin(dto.getLogin());
        user.setRole(Role.valueOf(dto.getRole()));
        user.setPublicId(UUID.randomUUID());
        return save(user);
    }

    public UserEntity save(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }


    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }


}

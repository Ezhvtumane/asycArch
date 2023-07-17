package com.georgyorlov.service;

import com.georgyorlov.entity.ExampleEntity;
import com.georgyorlov.repository.ExampleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExampleService {

    private final ExampleRepository exampleRepository;


    public ExampleEntity createAndSaveExampleEntity(String text) {
        ExampleEntity exampleEntity = new ExampleEntity();
        exampleEntity.setText(text);
        return save(exampleEntity);
    }

    @Transactional
    public ExampleEntity save(ExampleEntity exampleEntity) {
        return exampleRepository.save(exampleEntity);
    }

    public ExampleEntity findById(Long id) {
        return exampleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("No entity found by id: " + id));
    }

}

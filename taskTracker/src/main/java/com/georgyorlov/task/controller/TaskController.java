package com.georgyorlov.task.controller;

import com.georgyorlov.task.entity.TaskEntity;
import com.georgyorlov.task.service.TaskService;
import com.georgyorlov.task.service.kafka.KafkaSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/example")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final KafkaSenderService kafkaSenderService;

    @PostMapping
    public TaskEntity createTaskEntity(@RequestBody String descritpion) {
        return taskService.createTaskEntity(descritpion);
    }

    @GetMapping("/{id}")
    public TaskEntity findById(@PathVariable("id") Long id) {
        return taskService.findById(id);
    }

}

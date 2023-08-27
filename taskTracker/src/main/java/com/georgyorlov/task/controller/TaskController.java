package com.georgyorlov.task.controller;

import com.georgyorlov.avro.task.v2.TaskStreaming;
import com.georgyorlov.task.dto.TaskCreateDTO;
import com.georgyorlov.task.entity.TaskEntity;
import com.georgyorlov.task.service.TaskService;
import com.georgyorlov.task.service.kafka.KafkaSenderService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/task")
@RequiredArgsConstructor
@EnableMethodSecurity
public class TaskController {

    private final TaskService taskService;
    private final KafkaSenderService kafkaSenderService;

    @PostMapping
    //@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WORKER')")
    public TaskEntity createTaskEntity(@RequestBody TaskCreateDTO taskCreateDTO) {
        return taskService.createTaskEntity(taskCreateDTO);
    }

    @PostMapping("/shuffle")
    //@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public void shuffleTasks() {
        taskService.shuffleTasks();
    }

    @PostMapping("/{public_id}/done")
    //@PreAuthorize("hasRole('WORKER')") //???
    public TaskEntity doneTask(@PathVariable("public_id") UUID publicId) {
        return taskService.doneTask(publicId);
    }


    @GetMapping
    public void sendBOthVersions() {
        TaskStreaming v2 = TaskStreaming.newBuilder()
            .setPublicId(UUID.randomUUID().toString())
            .setTitle("title")
            .setJiraId("jira")
            .setCostAssigning(0)
            .setCostCompleting(0)
            .build();

        com.georgyorlov.avro.task.v1.TaskStreaming v1 = new com.georgyorlov.avro.task.v1.TaskStreaming().newBuilder()
            .setPublicId(UUID.randomUUID().toString())
            .setDescription("descr")
            .setCostAssigning(0)
            .setCostCompleting(0)
            .build();

        kafkaSenderService.sendTaskStreamingEventV1(v1);

        kafkaSenderService.sendTaskStreamingEvent(v2);

    }
}

package com.georgyorlov.task.controller;

import com.georgyorlov.task.dto.TaskCreateDTO;
import com.georgyorlov.task.entity.TaskEntity;
import com.georgyorlov.task.service.TaskService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
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
}

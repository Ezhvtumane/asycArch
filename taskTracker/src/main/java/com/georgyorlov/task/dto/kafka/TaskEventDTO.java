package com.georgyorlov.task.dto.kafka;

import java.util.UUID;
import lombok.Data;

@Data
public class TaskEventDTO {

    private String description;
    private UUID publicId;
    private UUID userPublicId;
    private Integer costAssaigning;
    private Integer costCompleting;

}

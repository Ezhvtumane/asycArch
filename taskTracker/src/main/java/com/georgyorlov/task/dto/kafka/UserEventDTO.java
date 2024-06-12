package com.georgyorlov.task.dto.kafka;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEventDTO {

    private String login;
    private UUID publicId;
    private String role;
}

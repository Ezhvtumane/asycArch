package com.georgyorlov.task.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEventDTO {

    private String login;
    private String publicId;
    private String role;

}

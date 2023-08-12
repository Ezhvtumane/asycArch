package com.georgyorlov.auth.dto.kafka;

import java.util.UUID;
import lombok.Data;

@Data
public class UserEventDTO {

    private String login;
    private UUID publicId;
    private String role;

}

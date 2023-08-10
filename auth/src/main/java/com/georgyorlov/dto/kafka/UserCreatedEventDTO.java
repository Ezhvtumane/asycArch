package com.georgyorlov.dto.kafka;

import lombok.Data;

@Data
public class UserCreatedEventDTO {

    private String login;
    private String publicId;
    private String role;

}

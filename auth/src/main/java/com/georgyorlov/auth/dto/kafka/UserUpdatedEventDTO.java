package com.georgyorlov.auth.dto.kafka;

import lombok.Data;

@Data
public class UserUpdatedEventDTO {

    private String login;
    private String publicId;
    private String role;

}

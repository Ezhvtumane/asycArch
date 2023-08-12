package com.georgyorlov.auth.dto;

import lombok.Data;

@Data
public class UserCreateDTO {

    private String login;
    private String role;
}

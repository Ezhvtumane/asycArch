package com.georgyorlov.auth.controller;

import com.georgyorlov.auth.dto.LoginRequestDTO;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
public class LoginController {

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/login")
    public String login(
        @RequestBody LoginRequestDTO loginRequestDTO
    ) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", "aa-client");
        map.add("grant_type", "password");
        map.add("scope", "openid");
        map.add("username", loginRequestDTO.getLogin());
        map.add("password", loginRequestDTO.getPassword());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, httpHeaders);

        ParameterizedTypeReference<Map<String, String>> responseType =
            new ParameterizedTypeReference<>() {
            };

        Map<String, String> body = restTemplate.exchange(
            "http://localhost:8484/auth/realms/aa-realm/protocol/openid-connect/token",
            HttpMethod.POST,
            entity,
            responseType
        ).getBody();

        String accessToken = body.get("access_token");
        String tokenType = body.get("token_type");

        return tokenType
            + " "
            + accessToken;
    }
}

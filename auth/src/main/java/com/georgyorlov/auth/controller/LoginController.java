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
    private final static String keycloakUrl = "http://localhost:8484/auth/realms/aa-realm/protocol/openid-connect/token";

    @PostMapping("/login")
    public String login(
        @RequestBody LoginRequestDTO loginRequestDTO
    ) {
        ParameterizedTypeReference<Map<String, String>> responseType =
            new ParameterizedTypeReference<>() {
            };

        final HttpEntity<MultiValueMap<String, String>> request = createRequest(loginRequestDTO);

        final Map<String, String> body = restTemplate.exchange(
            keycloakUrl,
            HttpMethod.POST,
            request,
            responseType
        ).getBody();

        String accessToken = body.get("access_token");
        String tokenType = body.get("token_type");

        return tokenType
            + " "
            + accessToken;
    }

    private HttpEntity<MultiValueMap<String, String>> createRequest(LoginRequestDTO loginRequestDTO) {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        final MultiValueMap<String, String> map = prepareParams(loginRequestDTO.getLogin(), loginRequestDTO.getPassword());

        return new HttpEntity<>(map, httpHeaders);
    }

    private MultiValueMap<String, String> prepareParams(String login, String password) {
        final MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", "aa-client");
        map.add("grant_type", "password");
        map.add("scope", "openid");
        map.add("username", login);
        map.add("password", password);
        return map;
    }
}

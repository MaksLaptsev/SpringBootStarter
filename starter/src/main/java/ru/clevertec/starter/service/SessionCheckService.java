package ru.clevertec.starter.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import ru.clevertec.starter.exception.ConnectionException;
import ru.clevertec.starter.model.Session;

public class SessionCheckService {
    private final String url;
    private final RestTemplate restTemplate;

    public SessionCheckService(String url, RestTemplate restTemplate) {
        this.url = url;
        this.restTemplate = restTemplate;
    }

    public Session findOrCreateAndGetByLogin(String login) {
        try {
            ResponseEntity<Session> response = restTemplate.exchange(url, HttpMethod.POST, request(login), Session.class);
            return response.getBody();
        } catch (ResourceAccessException e) {
            throw new ConnectionException("Connection denied: Session service is unavailable/check the URL in the application properties or try again later");
        }
    }

    private HttpEntity<Session> request(String login) {
        return new HttpEntity<>(Session.builder().login(login).build());
    }
}

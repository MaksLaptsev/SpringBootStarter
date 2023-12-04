package ru.clevertec.starter.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;
import ru.clevertec.starter.exception.ConnectionException;
import ru.clevertec.starter.model.Session;

public class SessionCheckService {
    private final WebClient webClient;
    private final String url;
    private final ObjectMapper objectMapper;

    public SessionCheckService(WebClient webClient, String url,ObjectMapper objectMapper) {
        this.webClient = webClient;
        this.url = url;
        this.objectMapper = objectMapper;
    }

    @SneakyThrows
    public Session findOrCreateAndGetByLogin(String login){
        try {
            String jsonResponse = webClient.post()
                    .uri(url)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(Mono.just("{\"login\":\"%s\"}".formatted(login)),String.class)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            return objectMapper.readValue(jsonResponse,Session.class);
          }catch (WebClientRequestException e){
            throw new ConnectionException("Connection denied: Session service is unavailable/check the URL in the application properties or try again later");
        }
    }
}

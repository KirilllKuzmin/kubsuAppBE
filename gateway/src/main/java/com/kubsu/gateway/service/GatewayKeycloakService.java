package com.kubsu.gateway.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Service
public class GatewayKeycloakService {

    private final WebClient webClient;
    private final String tokenUri;
    private final String clientId;
    private final String clientSecret;

    public GatewayKeycloakService(WebClient.Builder webClientBuilder,
                                  @Value("${keycloak.token-uri}") String tokenUri,
                                  @Value("${keycloak.client-id}") String clientId,
                                  @Value("${keycloak.client-secret}") String clientSecret) {
        this.webClient = webClientBuilder.build();
        this.tokenUri = tokenUri;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public Mono<String> getToken(String username, String password) {
        return webClient.post()
                .uri(tokenUri)
                .body(BodyInserters.fromFormData("grant_type", "password")
                        .with("client_id", clientId)
                        .with("client_secret", clientSecret)
                        .with("username", username)
                        .with("password", password))
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new ResponseStatusException(response.statusCode(), errorBody))))
                .bodyToMono(String.class);
    }
}

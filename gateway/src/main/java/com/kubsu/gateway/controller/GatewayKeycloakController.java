package com.kubsu.gateway.controller;

import com.kubsu.gateway.service.GatewayKeycloakService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class GatewayKeycloakController {

    private final GatewayKeycloakService gatewayKeycloakService;

    @Autowired
    public GatewayKeycloakController(GatewayKeycloakService gatewayKeycloakService) {
        this.gatewayKeycloakService = gatewayKeycloakService;
    }

     //Форма для авторизации через Keycloak напрямую
     @GetMapping(value = "/authentication")
     public Mono<OAuth2AccessToken> getTokenWithForm(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient) {
         return Mono.just(authorizedClient.getAccessToken());
     }
//
//    @PostMapping(value = "/authentication", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<Mono<String>> getToken(@RequestBody Map<String, String> body) {
//        String username = body.get("username");
//        String password = body.get("password");
//        return ResponseEntity.ok(gatewayKeycloakService.getToken(username, password));
//    }
}

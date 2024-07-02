package com.kubsu.users.controller;

import com.kubsu.users.dto.GroupResponseDTO;
import com.kubsu.users.dto.StudentResponseDTO;
import com.kubsu.users.dto.UserResponseDTO;
import com.kubsu.users.service.UserService;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/ping")
    public String ping() {
        return "Ping";
    }

    @GetMapping("")
    public UserResponseDTO profile(@AuthenticationPrincipal Jwt jwt) {
        UserResponseDTO user = new UserResponseDTO(jwt);
        return user;
    }
//
//    @GetMapping("all")
//    public CompletableFuture<List<UserResponseDTO>> allProfiles() {
//        CompletableFuture<List<User>> usersFuture = userService.getAllUsers();
//
//        return usersFuture.thenApplyAsync(users -> users.stream()
//                .map(UserResponseDTO::new)
//                .collect(Collectors.toList()));
//    }
//
    @GetMapping("groups")
    public List<GroupResponseDTO> allGroups(@RequestParam(required = false) List<Long> groupId) {
        return userService.getAllGroups(groupId)
                .stream()
                .map(GroupResponseDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("students")
    public List<StudentResponseDTO> allStudents(@RequestParam List<Long> Id) {
        return userService.getAllStudents(Id)
                .stream()
                .map(StudentResponseDTO::new)
                .collect(Collectors.toList());
    }
}

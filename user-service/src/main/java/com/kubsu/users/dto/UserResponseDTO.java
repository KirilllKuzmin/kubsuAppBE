package com.kubsu.users.dto;

import com.kubsu.users.model.Group;
import com.kubsu.users.model.Role;
import com.kubsu.users.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
public class UserResponseDTO {

    private String id;

    private Long kubsuUserId;

    private String username;

    private String fullName;

    private String email;

    private String group;

    private String startEducationPeriod;

    private Object authorities;

//    public UserResponseDTO(User user) {
//        id = user.getId();
//        kubsuUserId = user.getKubsuUserId();
//        username = user.getUsername();
//        fullName = user.getFullName();
//        email = user.getEmail();
//        group = user.getGroup();
//        startEducationDate = user.getStartEducationDate();
//        endEducationDate = user.getEndEducationDate();
//        creationDate = user.getCreationDate();
//        authorities = user.getRoles();
//    }

    public UserResponseDTO(Jwt jwtUserInformation) {
        id = jwtUserInformation.getSubject();
        kubsuUserId = jwtUserInformation.getClaim("kubsuUserId");
        username = jwtUserInformation.getClaim("preferred_username");
        fullName = jwtUserInformation.getClaim("name") + " " + jwtUserInformation.getClaim("patronymic");
        email = jwtUserInformation.getClaim("email");
        group = jwtUserInformation.getClaim("studyGroupId");
        startEducationPeriod = jwtUserInformation.getClaim("startEducationPeriod");
        authorities = jwtUserInformation.getClaimAsMap("realm_access").get("roles");
    }
}

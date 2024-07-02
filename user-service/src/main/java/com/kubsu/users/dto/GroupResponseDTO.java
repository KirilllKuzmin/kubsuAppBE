package com.kubsu.users.dto;

import com.kubsu.users.model.Group;
import com.kubsu.users.model.Specialty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupResponseDTO {

    private Long id;

    private String name;

    private Specialty specialty;

    public GroupResponseDTO(Group group) {
        id = group.getId();
        name = group.getName();
        specialty = group.getSpecialty();
    }
}

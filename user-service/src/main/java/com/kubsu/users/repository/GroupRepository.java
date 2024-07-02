package com.kubsu.users.repository;

import com.kubsu.users.model.Group;
import com.kubsu.users.model.Specialty;
import com.kubsu.users.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {

    Boolean existsByNameAndSpecialty(String name, Specialty specialty);

    Optional<Group> findByNameAndSpecialty(String name, Specialty specialty);
}

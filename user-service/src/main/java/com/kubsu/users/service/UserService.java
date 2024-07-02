package com.kubsu.users.service;

import com.kubsu.users.model.Group;
import com.kubsu.users.model.User;
import com.kubsu.users.repository.GroupRepository;
import com.kubsu.users.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class UserService {

    private final UserRepository userRepository;

    private final GroupRepository groupRepository;

    public UserService(UserRepository userRepository, GroupRepository groupRepository) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
    }

    public List<Group> getAllGroups(List<Long> groupIds) {
        if (groupIds == null)
            return groupRepository.findAll()
                    .stream()
                    .sorted(Comparator.comparing(Group::getName))
                    .collect(Collectors.toList());

        return groupRepository.findAllById(groupIds)
                .stream()
                .sorted(Comparator.comparing(Group::getName))
                .collect(Collectors.toList());
    }

    public List<User> getAllStudents(List<Long> userIds) {
        return userRepository.findAllById(userIds)
                .stream()
                .sorted(Comparator.comparing(User::getFullName))
                .collect(Collectors.toList());
    }

}

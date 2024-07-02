package com.kubsu.accounting.rest;

import com.kubsu.accounting.dto.GroupResponseDTO;
import com.kubsu.accounting.dto.StudentResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "user-service", url = "http://localhost:8090")
public interface UserServiceClient {

    @RequestMapping(value = "/api/v1/users/groups", method = RequestMethod.GET)
    List<GroupResponseDTO> getGroups(@RequestParam("groupId") List<Long> groupId, @RequestHeader("Authorization") String token);

    @RequestMapping(value = "/api/v1/users/students", method = RequestMethod.GET)
    List<StudentResponseDTO> getStudents(@RequestParam("Id") List<Long> studentId, @RequestHeader("Authorization") String token);
}

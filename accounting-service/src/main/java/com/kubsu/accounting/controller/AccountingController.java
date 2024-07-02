package com.kubsu.accounting.controller;

import com.kubsu.accounting.dto.*;
import com.kubsu.accounting.model.*;
import com.kubsu.accounting.rest.UserServiceClient;
import com.kubsu.accounting.service.AccountingService;
import net.minidev.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;



@RestController
@RequestMapping("/api/v1/accounting")
public class AccountingController {

    private final AccountingService accountingService;

    private final UserServiceClient userServiceClient;

    public AccountingController(AccountingService accountingService, UserServiceClient userServiceClient) {
        this.accountingService = accountingService;
        this.userServiceClient = userServiceClient;
    }

    @GetMapping("/access")
    public String allAccess() {
        return "Public Content.";
    }


    @GetMapping("/lecturers/access")
    @PreAuthorize("hasRole('LECTURER')")
    public String lecturerAccess() {
        return "Lecturer access.";
    }

    @GetMapping("/lecturers/courses")
    @PreAuthorize("hasRole('LECTURER')")
    public List<Course> lecturerCourses(@AuthenticationPrincipal Jwt jwt) {

        return accountingService.lecturerCourses(jwt.getClaim("oldUserId"));
    }

    @GetMapping("/lecturers/courses/{courseId}/groups")
    @PreAuthorize("hasRole('LECTURER') or hasRole('MODERATOR')")
    public List<GroupResponseDTO> courseGroups(@AuthenticationPrincipal Jwt jwt, @PathVariable Long courseId) {
        String token = "Bearer " + jwt.getTokenValue();

        return userServiceClient.getGroups(new ArrayList<>(accountingService.courseGroups(courseId, jwt.getClaim("oldUserId"))), token);
    }

    @GetMapping("/groups/{groupId}/students")
    @PreAuthorize("hasRole('LECTURER') or hasRole('MODERATOR')")
    public List<StudentResponseDTO> groupStudents(@AuthenticationPrincipal Jwt jwt, @PathVariable Long groupId) {
        String token = "Bearer " + jwt.getTokenValue();
        return userServiceClient.getStudents(accountingService
                .getStudentsByGroup(groupId)
                .stream()
                .map(Student::getUserId)
                .collect(Collectors.toList()), token);
    }

    @GetMapping("/lecturers/courses/{courseId}/groups/{groupId}/dates")
    @PreAuthorize("hasRole('LECTURER') or hasRole('MODERATOR') or hasRole('STUDENT') or hasRole('ADMIN')")
    public List<OffsetDateTime> courseDates(@AuthenticationPrincipal Jwt jwt, @PathVariable Long courseId, @PathVariable Long groupId) {

        return accountingService.getDatesOfCourse(courseId, groupId, jwt.getClaim("oldUserId"));
    }

    @PostMapping("/lecturers/absences")
    @PreAuthorize("hasRole('LECTURER') or hasRole('MODERATOR')")
    public ResponseEntity<?> setAbsenceStudent(@AuthenticationPrincipal Jwt jwt, @RequestBody SetAbsenceRequestDTO setAbsenceRequestDTO) {

        return ResponseEntity.ok(accountingService.setAbsenceStudents(setAbsenceRequestDTO.getStudentId(),
                jwt.getClaim("oldUserId"),
                setAbsenceRequestDTO.getCourseId(),
                setAbsenceRequestDTO.getAbsenceDate(),
                setAbsenceRequestDTO.getAbsenceTypeId()));
    }

    @GetMapping("/lecturers/absences/courses/{courseId}/groups/{groupId}")
    @PreAuthorize("hasRole('LECTURER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<GetAbsenceResponseDTO> getAbsences(@AuthenticationPrincipal Jwt jwt, @PathVariable Long courseId, @PathVariable Long groupId) {
        List<Absence> absences = accountingService.getAbsenceStudents(groupId, (Long) jwt.getClaim("oldUserId"), courseId);

        return absences
                .stream()
                .map(GetAbsenceResponseDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/lecturers/courses/{courseId}/groups/{groupId}/works")
    @PreAuthorize("hasRole('LECTURER') or hasRole('MODERATOR') or hasRole('STUDENT') or hasRole('ADMIN')")
    public List<WorkDateResponseDTO> workDates(@AuthenticationPrincipal Jwt jwt, @PathVariable Long courseId, @PathVariable Long groupId) {

        return accountingService.getWorkDates(courseId, groupId, jwt.getClaim("oldUserId"))
                .stream()
                .map(WorkDateResponseDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/workTypes")
    @PreAuthorize("hasRole('LECTURER') or hasRole('MODERATOR') or hasRole('STUDENT') or hasRole('ADMIN')")
    public List<TypeOfWorkResponseDTO> workTypes() {

        return accountingService.getWorkTypes()
                .stream()
                .map(TypeOfWorkResponseDTO::new)
                .collect(Collectors.toList());
    }

    @PostMapping("/lecturers/courses/{courseId}/groups/{groupId}/dates/{workDate}/works")
    @PreAuthorize("hasRole('LECTURER') or hasRole('MODERATOR')")
    public List<WorkDateResponseDTO> setWorkTypes(@AuthenticationPrincipal Jwt jwt,
                                                  @PathVariable Long courseId,
                                                  @PathVariable Long groupId,
                                                  @PathVariable OffsetDateTime workDate,
                                                  @RequestBody List<SetWorkTypesRequestDTO> workTypes) {

        List<WorkDate> workDateResponse = accountingService.setWorks(courseId, groupId, jwt.getClaim("oldUserId"), workTypes, workDate);

        return workDateResponse
                .stream()
                .map(WorkDateResponseDTO::new)
                .collect(Collectors.toList());
    }

    @PostMapping("/lecturers/evaluations/systems")
    @PreAuthorize("hasRole('LECTURER') or hasRole('MODERATOR')")
    public SetEvaluationSystemResponseDTO setEvaluationSystem(@RequestBody SetEvaluationSystemRequestDTO setEvaluationSystemRequestDTO) {

        return new SetEvaluationSystemResponseDTO(accountingService.setEvaluationSystem(
                setEvaluationSystemRequestDTO.getMinGrade(),
                setEvaluationSystemRequestDTO.getMaxGrade(),
                setEvaluationSystemRequestDTO.getPassingGrade()));
    }

    @PostMapping("/lecturers/evaluations")
    @PreAuthorize("hasRole('LECTURER') or hasRole('MODERATOR')")
    public ResponseEntity<?> setEvaluationStudents(@AuthenticationPrincipal Jwt jwt, @RequestBody List<SetEvaluationRequestDTO> setEvaluationRequestDTOs) {

        return ResponseEntity.ok(accountingService.setEvaluationStudents(jwt.getClaim("oldUserId"),
                setEvaluationRequestDTOs));
    }

    @GetMapping("/lecturers/evaluations/courses/{courseId}/groups/{groupId}")
    @PreAuthorize("hasRole('LECTURER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<GetEvaluationResponseDTO> getEvaluations(@AuthenticationPrincipal Jwt jwt, @PathVariable Long courseId, @PathVariable Long groupId) {

        List<Evaluation> evaluations = accountingService.getEvaluationStudents(groupId, jwt.getClaim("oldUserId"), courseId);

        return evaluations
                .stream()
                .map(GetEvaluationResponseDTO::new)
                .collect(Collectors.toList());
    }
}

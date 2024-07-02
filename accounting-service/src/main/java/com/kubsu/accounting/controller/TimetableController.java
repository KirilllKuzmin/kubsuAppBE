package com.kubsu.accounting.controller;

import com.kubsu.accounting.dto.NumTimeClassHeldResponseDTO;
import com.kubsu.accounting.model.Timetable;
import com.kubsu.accounting.service.TimetableService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/timetables")
public class TimetableController {

    private final TimetableService timetableService;

    public TimetableController(TimetableService timetableService) {
        this.timetableService = timetableService;
    }

    @GetMapping("/number-time-classes-held")
    @PreAuthorize("hasRole('LECTURER') or hasRole('MODERATOR') or hasRole('STUDENT') or hasRole('ADMIN')")
    public List<NumTimeClassHeldResponseDTO> getAllNumTimeClassHeld() {
        return timetableService.getAllNumTimeClassHeld()
                .stream()
                .map(NumTimeClassHeldResponseDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("")
    @PreAuthorize("hasRole('LECTURER') or hasRole('MODERATOR') or hasRole('STUDENT') or hasRole('ADMIN')")
    public List<Timetable> getAllTimetable(@AuthenticationPrincipal Jwt jwt,
                                           @RequestParam("start_date") OffsetDateTime startDate,
                                           @RequestParam("end_date") OffsetDateTime endDate) {

        return timetableService.getAllTimetable(startDate, endDate, jwt.getClaim("oldUserId"));
    }
}

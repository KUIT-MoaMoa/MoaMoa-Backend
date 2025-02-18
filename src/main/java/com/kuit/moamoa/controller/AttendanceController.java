package com.kuit.moamoa.controller;

import com.kuit.moamoa.domain.Attendance;
import com.kuit.moamoa.dto.response.AttendanceResponse;
import com.kuit.moamoa.global.response.ApiResponse;
import com.kuit.moamoa.jwt.Jwt;
import com.kuit.moamoa.service.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/attendances")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @Operation(summary = "출석 체크")
    @GetMapping()
    public ApiResponse<List<AttendanceResponse>> getAttendance(@Jwt Long userId) {
        return new ApiResponse<>(attendanceService.getAllAttendances(userId));
    }
}

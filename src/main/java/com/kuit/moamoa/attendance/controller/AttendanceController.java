package com.kuit.moamoa.attendance.controller;

import com.kuit.moamoa.attendance.dto.AttendanceResponse;
import com.kuit.moamoa.attendance.service.AttendanceService;
import com.kuit.moamoa.configuration.response.ApiResponse;
import com.kuit.moamoa.global.jwt.Jwt;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
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

package com.kuit.moamoa.attendance.dto;

import com.kuit.moamoa.user.domain.Attendance;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class AttendanceResponse {
    Long attendanceId;
    Long userId;
    LocalDateTime attendanceTime;

    public static AttendanceResponse from(Attendance attendance) {
        return AttendanceResponse.builder()
                .attendanceId(attendance.getId())
                .userId(attendance.getUser().getId())
                .attendanceTime(attendance.getCreatedAt())
                .build();
    }
}

package com.kuit.moamoa.dto.response;

import com.kuit.moamoa.domain.Attendance;
import com.kuit.moamoa.domain.Status;
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


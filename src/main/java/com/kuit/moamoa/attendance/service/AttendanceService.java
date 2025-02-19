package com.kuit.moamoa.attendance.service;

import com.kuit.moamoa.attendance.dto.AttendanceResponse;
import com.kuit.moamoa.attendance.repository.AttendanceRepository;
import com.kuit.moamoa.global.Status;
import com.kuit.moamoa.user.domain.Attendance;
import com.kuit.moamoa.user.domain.User;
import com.kuit.moamoa.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<AttendanceResponse> getAllAttendances(Long userId) {
        return attendanceRepository.findAllByUserId(userId)
                .stream()
                .map(AttendanceResponse::from)  // 빌더 패턴을 사용한 변환
                .collect(Collectors.toList());
    }

    @Transactional
    public void recordAttendance(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

//        Attendance attendance = new Attendance();
//        attendance.setUser(user);
//        attendance.setCreatedAt(LocalDateTime.now());
//        attendanceRepository.save(attendance);

        Attendance recordAttendance = Attendance.builder()
                .user(user)
                .status(Status.ACTIVE)
                .build();
        attendanceRepository.save(recordAttendance);
    }

    @Transactional(readOnly = true)
    public boolean hasAttendedRecently(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        LocalDateTime nowMinusTwoWeeks = LocalDateTime.now().minusWeeks(2);
        boolean hasNotAttended = attendanceRepository.hasAttendedRecently(user, nowMinusTwoWeeks);

        return hasNotAttended;
    }

}

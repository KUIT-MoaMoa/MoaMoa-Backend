package com.kuit.moamoa.service;

import com.kuit.moamoa.domain.Attendance;
import com.kuit.moamoa.domain.Status;
import com.kuit.moamoa.domain.User;
import com.kuit.moamoa.dto.response.AttendanceResponse;
import com.kuit.moamoa.repository.AttendanceRepository;
import com.kuit.moamoa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
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
                .createdAt(LocalDateTime.now())
                .build();
        attendanceRepository.save(recordAttendance);
    }


}

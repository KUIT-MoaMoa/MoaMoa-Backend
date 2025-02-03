package com.kuit.moamoa.controller;

import com.kuit.moamoa.domain.User;
import com.kuit.moamoa.dto.request.JoinRequest;
import com.kuit.moamoa.dto.request.LoginRequest;
import com.kuit.moamoa.dto.response.JoinResponse;
import com.kuit.moamoa.global.response.ApiResponse;
import com.kuit.moamoa.global.response.ErrorResponse;
import com.kuit.moamoa.global.response.ExceptionResponseStatus;
import com.kuit.moamoa.service.JoinService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "사용자 로그인", description = "사용자 회원가입 및 로그인")
@RestController
@Slf4j
//@RequestMapping("/login")
@RequiredArgsConstructor
public class JoinController {

    private final JoinService joinService;

//    @Operation(summary = "유저 일반 로그인", description = "서비스 내 간편 로그인 경로입니다.")
//    @PostMapping("/login")
//    public ApiResponse<String> login(@ModelAttribute LoginRequest request) {
//        joinService.loginProcess(request);
////        log.info("DTO:{}",joinDTO);
//        return new ApiResponse<>("로그인 완료");
//    }

    @Operation(summary = "유저 일반 회원가입", description = "서비스 내 간편 회원가입 경로입니다. 닉네임 입력까지 완료한 다음 유저 정보를 저장합니다.")
    @PostMapping("/join")
    public String join(@ModelAttribute JoinRequest request){
        joinService.joinProcess(request);

        return "ok";
    }

//    @Operation(summary = "닉네임 입력", description = "회원가입 후 닉네임을 처음 설정하는 경로입니다.")
//    @PostMapping("/nickname")
//    public ResponseEntity<ApiResponse<Void>> nickname(LoginRequest request){
////        joinService.joinProcess(request);
//        ResponseEntity.ok(chatService.updateChat(chatId, request.getContent()));
//        return ResponseEntity.ok(joinService.loginProcess(request));
//    }

    @Operation(summary = "비밀번호 변경", description = "비밀번호 재설정 경로입니다.")
    @PostMapping("/resetPassword")
    public ApiResponse<String> resetPassword(@RequestBody LoginRequest request){
        joinService.resetPassword(request);
        return new ApiResponse<>("비밀번호가 성공적으로 변경되었습니다.");
    }

}

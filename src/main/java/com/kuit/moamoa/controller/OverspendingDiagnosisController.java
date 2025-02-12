package com.kuit.moamoa.controller;

import com.kuit.moamoa.dto.request.OverspendingDiagnosisRequest;
import com.kuit.moamoa.global.response.ApiResponse;
import com.kuit.moamoa.service.OverspendingDiagnosisService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/overspending-diagnosis")
public class OverspendingDiagnosisController {

    private final OverspendingDiagnosisService overspendingDiagnosisService;

    @Operation(summary = "과소비 진단", description = "과소비 진단 프로세스 경로입니다. 진단 결과가 저장되지 않습니다.")
    @GetMapping("")
    public ApiResponse<String> diagnoseOverspending(@RequestBody OverspendingDiagnosisRequest request){

        return new ApiResponse<>(overspendingDiagnosisService.diagnoseOverspending(request));
    }

}

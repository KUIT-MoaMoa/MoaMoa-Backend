package com.kuit.moamoa.controller;

import com.kuit.moamoa.dto.request.OverspendingDiagnosisRequest;
import com.kuit.moamoa.global.response.ApiResponse;
import com.kuit.moamoa.service.OverspendingDiagnosisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/overspending-diagnosis")
@Tag(name = "과소비 진단 프로세스", description = "")
public class OverspendingDiagnosisController {

    private final OverspendingDiagnosisService overspendingDiagnosisService;

    @Operation(summary = "과소비 진단", description = "ageGroup변수 0:10~20대, 1:30대, 2:40대, 3:50대 이상으로 매핑돼있습니다.")
    @GetMapping("")
    public ApiResponse<String> diagnoseOverspending(@RequestBody OverspendingDiagnosisRequest request){

        return new ApiResponse<>(overspendingDiagnosisService.diagnoseOverspending(request));
    }

}

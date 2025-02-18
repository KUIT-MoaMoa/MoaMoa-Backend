package com.kuit.moamoa.controller;

import com.kuit.moamoa.domain.BuyItemRequest;
import com.kuit.moamoa.domain.ConsumptionChallengeSortType;
import com.kuit.moamoa.dto.AdornProfileResponse;
import com.kuit.moamoa.dto.BuyItemResponse;
import com.kuit.moamoa.dto.ChangeNicknameRequest;
import com.kuit.moamoa.dto.ChangeNicknameResponse;
import com.kuit.moamoa.dto.CoinRecordResponse;
import com.kuit.moamoa.dto.ConsumptionChallengeSummaryResponse;
import com.kuit.moamoa.dto.InvitationUrlResponse;
import com.kuit.moamoa.dto.MyChallengeSummaryResponse;
import com.kuit.moamoa.dto.MyConsumptionSummaryResponse;
import com.kuit.moamoa.dto.UserPageResponse;
import com.kuit.moamoa.global.response.ApiResponse;
import com.kuit.moamoa.jwt.Jwt;
import com.kuit.moamoa.service.CoinService;
import com.kuit.moamoa.service.ConsumptionChallengeService;
import com.kuit.moamoa.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final ConsumptionChallengeService consumptionChallengeService;
    private final CoinService coinService;

    @GetMapping("/adorn-profile")
    public ApiResponse<AdornProfileResponse> adornProfile(@Jwt Long userId) throws Exception {
        return new ApiResponse<>(userService.lookUpItems(userId));
    }

    @PostMapping("/item")
    public ApiResponse<BuyItemResponse> butItem(@Jwt Long userId, @RequestBody BuyItemRequest buyItemRequest)
            throws Exception {
        log.info(buyItemRequest.toString());
        return new ApiResponse<>(userService.buyItem(userId, buyItemRequest.getItemId()));
    }

    @GetMapping("")
    public ApiResponse<UserPageResponse> getUserInfo(@Jwt Long userId) throws Exception {
        return new ApiResponse<>(userService.getUserInfo(userId));
    }

    @GetMapping("/my-challenge")
    public ApiResponse<MyChallengeSummaryResponse> getUserChallengeSummary(@Jwt Long userId) throws Exception {
        return new ApiResponse<>(userService.getUserChallengeSummary(userId));
    }

    @GetMapping("/my-consumption")
    public ApiResponse<MyConsumptionSummaryResponse> getUserConsumptionSummary(@Jwt Long userId, @RequestParam int duration) throws Exception {
        return new ApiResponse<>(userService.getUserConsumptionSummary(userId, duration));
    }

    @GetMapping("/my-consumption-record")
    public ApiResponse<List<ConsumptionChallengeSummaryResponse>> getUserConsumptionRecord(@Jwt Long userId,
                                                                                           // TODO: SORT TYPE
                                                                                           @RequestParam ConsumptionChallengeSortType sortType)
            throws Exception {
        return new ApiResponse<>(consumptionChallengeService.lookUpConsumptionChallengeSummary(userId, sortType));
    }

    @GetMapping("/invite")
    public ApiResponse<InvitationUrlResponse> makeInvitation(@Jwt Long userId) throws Exception {
        return new ApiResponse<>(userService.makeInvitationUrl(userId));
    }

    @PostMapping("/nickname")
    public ApiResponse<ChangeNicknameResponse> changeNickname(@Jwt Long userId,
                                                              @RequestBody ChangeNicknameRequest changeNicknameRequest)
            throws Exception {
        return new ApiResponse<>(userService.changeNickname(userId, changeNicknameRequest.getNewNickname()));
    }

    @GetMapping("/coin")
    public ApiResponse<CoinRecordResponse> getUserCoinRecord(@Jwt Long userId) throws Exception{
        return new ApiResponse<>(coinService.getUserCoinRecord(userId));
    }

    @PostMapping("delete")
    public ApiResponse<Object> leaveService(@Jwt Long userId) throws Exception {
        return new ApiResponse<>(userService.leaveService(userId));
    }
}

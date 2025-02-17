package com.kuit.moamoa.service;

import com.kuit.moamoa.domain.ConsumptionChallenge;
import com.kuit.moamoa.dto.ChangeNicknameResponse;
import com.kuit.moamoa.dto.InvitationUrlResponse;
import com.kuit.moamoa.dto.MyConsumptionSummaryResponse;
import com.kuit.moamoa.dto.MyConsumptionSummaryResponse.Stat;
import com.kuit.moamoa.dto.MyConsumptionSummaryResponse.TotalSpent;
import com.kuit.moamoa.dto.UserPageResponse;
import com.kuit.moamoa.domain.ChallengeRecord;
import com.kuit.moamoa.domain.Item;
import com.kuit.moamoa.domain.PurchaseRecord;
import com.kuit.moamoa.domain.User;
import com.kuit.moamoa.dto.AdornProfileResponse;
import com.kuit.moamoa.dto.BuyItemResponse;
import com.kuit.moamoa.dto.MyChallengeSummaryResponse;
import com.kuit.moamoa.global.exception.ErrorCode;
import com.kuit.moamoa.global.exception.GlobalException;
import com.kuit.moamoa.repository.ConsumptionChallengeRepository;
import com.kuit.moamoa.repository.ItemRepository;
import com.kuit.moamoa.repository.PurchaseRecordRepository;
import com.kuit.moamoa.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final PurchaseRecordRepository purchaseRecordRepository;
    private final ConsumptionChallengeRepository consumptionChallengeRepository;

    public AdornProfileResponse lookUpItems(Long userId) throws Exception {
        List<PurchaseRecord> purchaseRecords = userRepository.findById(userId)
                .orElseThrow(Exception::new) // TODO: custom Exception needed
                .getPurchaseRecords();
        List<Item> items = itemRepository.findAll();

        return new AdornProfileResponse(items, purchaseRecords);
    }

    public BuyItemResponse buyItem(Long userId, Long itemId) throws Exception {
        Item item = itemRepository.findById(itemId).orElseThrow(Exception::new);
        User user = userRepository.findById(userId).orElseThrow(Exception::new);
        user.deductBattleCoins(item.getPrice().intValue());
        userRepository.save(user);
        purchaseRecordRepository.save(
                PurchaseRecord.builder()
                        .user(user)
                        .transaction(-item.getPrice())
                        .itemId(itemId)
                        .build()
        ).setUser(user);
        return new BuyItemResponse(itemId);
    }

    public UserPageResponse getUserInfo(Long userId) throws Exception {
        User user = userRepository.findById(userId).orElseThrow(Exception::new);
        return new UserPageResponse(user);
    }

    public MyChallengeSummaryResponse getUserChallengeSummary (Long userId) throws Exception {  // TODO:아직 다 안 끝남
        User user = userRepository.findById(userId)
                .orElseThrow(Exception::new);
        List<ChallengeRecord> challengeRecords = user.getChallengeRecords();
        long successRate = calculateSuccessRate(challengeRecords);
        return new MyChallengeSummaryResponse(
                calculateTotalEarned(challengeRecords).orElseThrow(Exception::new),
                successRate,
                calculateTop(successRate),
                challengeRecords.size(),
                calculateTotalSucceed(challengeRecords),
                challengeRecords

        );
    }

    private Optional<Integer> calculateTotalEarned(List<ChallengeRecord> challengeRecords) {
        return challengeRecords.stream()
                .map(challengeRecord -> challengeRecord.getTransaction().intValue())
                .reduce(Integer::sum);
    }

    private int calculateTotalSucceed(List<ChallengeRecord> challengeRecords) {
        return (int) challengeRecords.stream()
                .filter(challengeRecord -> challengeRecord.getTransaction() > 0)
                .count();
    }
    private long calculateSuccessRate(List<ChallengeRecord> challengeRecords) {
        return calculateTotalSucceed(challengeRecords) / challengeRecords.size();
    }

    private int calculateTop(long successRate) {   // TODO: THIS IS A MOCK
        return (int) (successRate * 0.9);
    }

    public InvitationUrlResponse makeInvitationUrl(Long userId) throws Exception {
        String nickname = userRepository.findById(userId).orElseThrow(Exception::new).getNickname();
        return new InvitationUrlResponse("moamoa.store/invitation?nickname=" + nickname);
    }

    public ChangeNicknameResponse changeNickname(Long userId, String newNickname) throws Exception {
        boolean duplicated = userRepository.existsByNickname(newNickname);

        if(!duplicated) {
            User user = userRepository.findById(userId).orElseThrow(Exception::new);
            user.setNickname(newNickname);
            userRepository.save(user);
        }
        return new ChangeNicknameResponse(duplicated, newNickname);
    }

    public MyConsumptionSummaryResponse getUserConsumptionSummary(Long userId) throws Exception {   // TODO: THIS IS A MOCK
        User user = userRepository.findById(userId).orElseThrow(Exception::new);
        List<ConsumptionChallenge> consumptionChallenges = consumptionChallengeRepository.findAllByUser(user);
        return new MyConsumptionSummaryResponse(consumptionChallenges);
    }

    // 챌린지에 성공했을 경우 배틀 코인 추가
    public void deductBattleCoins(Long userId, int amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND, "Can't find user."));

        if (user.getBattleCoins() < amount) {
            throw new GlobalException(ErrorCode.INSUFFICIENT_BATTLE_COINS, "Not enough battle coins.");
        }

        user.deductBattleCoins(amount);
        userRepository.save(user);
    }

    //챌린지에 실패했을 경우 배틀 코인 감소
    public void addBattleCoins(Long userId, int amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND, "Can't find user."));

        user.addBattleCoins(amount);
        userRepository.save(user);
    }
}

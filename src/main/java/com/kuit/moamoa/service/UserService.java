package com.kuit.moamoa.service;

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

    public AdornProfileResponse lookUpItems(Long userId) throws Exception {
        List<PurchaseRecord> purchaseRecords = userRepository.findById(userId)
                .orElseThrow(Exception::new) // TODO: custom Exception needed
                .getPurchaseRecords();
        List<Item> items = itemRepository.findAll();

        return new AdornProfileResponse(items, purchaseRecords);
    }

    public BuyItemResponse buyItem(Long userId, Long itemId, String itemName, int price) throws Exception {
        User user = userRepository.findById(userId).orElseThrow(Exception::new);
        purchaseRecordRepository.save(
                PurchaseRecord.builder()
                        .user(user)
                        .name(itemName)
                        .transaction((long) -price)
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

    private int calculateTotalSucceed(List<ChallengeRecord> challengeRecords) { // TODO: SQL만으로도 가능
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
        }
        return new ChangeNicknameResponse(duplicated, newNickname);
    }

    public MyConsumptionSummaryResponse getUserConsumptionSummary() {   // TODO: THIS IS A MOCK
        return new MyConsumptionSummaryResponse(
                12,
                12,
                12,
                10,
                List.of(new Stat("11-1", 10000, 8000),
                        new Stat("11-2", 10000, 12000),
                        new Stat("11-3", 10000, 9000),
                        new Stat("11-4", 10000, 7000))
                , new TotalSpent(1000, 2000, 30000, 4000, 5000, 42000)
        );
    }

}

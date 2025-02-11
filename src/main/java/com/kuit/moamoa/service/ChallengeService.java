package com.kuit.moamoa.service;

import com.kuit.moamoa.domain.Challenge;
import com.kuit.moamoa.dto.request.challenge.ChallengeCreateRequest;
import com.kuit.moamoa.dto.response.challenge.ChallengeCreateResponse;
import com.kuit.moamoa.repository.ChallengeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChallengeService {
    private final ChallengeRepository challengeRepository;

    @Transactional
    public ChallengeCreateResponse createChallenge(ChallengeCreateRequest request) {
        Challenge challenge = new Challenge(
                request.getTitle(),
                request.getContent(),
                request.getHeadCount(),
                request.getDuration(),
                request.getPublicChallenge(),
                request.getGoalAmount(),
                request.getBattleCoin(),
                request.getChallengeCategory()
        );

        Challenge savedChallenge = challengeRepository.save(challenge);
        return new ChallengeCreateResponse(savedChallenge.getId(), "챌린지 생성 성공");
    }
}
package com.kuit.moamoa.configuration.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // 그룹 & 사용자 관련
    USER_GROUP_NOT_FOUND("존재하지 않는 그룹입니다."),
    USER_NOT_FOUND("존재하지 않는 사용자입니다."),
    CHAT_NOT_FOUND("존재하지 않는 메시지입니다."),
    USER_ALREADY_IN_GROUP("이미 그룹에 있는 사용자입니다."),
    INVALID_STATUS("유효하지 않은 상태입니다."),
    NOT_PARTICIPATING("참여중이지 않은 사용자입니다."),

    //Challenge ErrorCode
    INSUFFICIENT_BATTLE_COINS("코인이 충분하지 않습니다."),
    CHALLENGE_NOT_FOUND("존재하지 않는 챌린지 입니다"),
    REWARD_ALREADY_CLAIMED("이미 보상을 지급받았습니다."),
    CHALLENGE_FULL("챌린지 참가 인원이 초과되었습니다."),
    ALREADY_PARTICIPATING("이미 해당 챌린지에 참여중입니다."),
    INSUFFICIENT_COINS("배틀 코인이 부족합니다."),
    INVALID_DATE("유효하지 않은 날짜입니다."),
    INVALID_AMOUNT("유효하지 않은 금액입니다."),
    INVALID_INPUT("유효하지 않은 입력값입니다."),
    INSUFFICIENT_PARTICIPANTS("챌린지 시작을 위한 참가자 수가 부족합니다."),
    RECRUITMENT_CLOSED("챌린지 모집 기간이 종료되었습니다."),
    GOAL_NOT_ACHIEVED("챌린지 목표를 달성하지 못했습니다."),

    // 친구 관련
    INVALID_REQUEST("자기 자신에게 친구 요청을 보낼 수 없습니다."),
    ALREADY_FRIENDS("이미 친구 관계입니다."),
    DUPLICATE_REQUEST("이미 친구 요청을 보냈습니다."),

    // 인증 관련
    UNVERIFIED_USER("인증되지 않은 사용자 이메일입니다."),
    NOTIFICATION_NOT_FOUND("알림을 찾을 수 없습니다."),
    TOKEN_IS_EXPIRED("토큰이 만료되었습니다.");

    private final String message;

    public int getStatus() {
        return ordinal();
    }
}
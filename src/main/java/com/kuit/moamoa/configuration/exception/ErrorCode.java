package com.kuit.moamoa.configuration.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode { //Todo: ErrorCode도 수정이 필요할듯
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
    CHALLENGE_FULL("Challenge has reached maximum participants"),
    ALREADY_PARTICIPATING("User is already participating in this challenge"),
    INSUFFICIENT_COINS("User does not have enough battle coins"),
    INVALID_DATE("Invalid date provided"),
    INVALID_AMOUNT("Invalid amount provided"),
    INVALID_INPUT("Invalid input provided"),
    INSUFFICIENT_PARTICIPANTS("Not enough participants to start challenge"),
    RECRUITMENT_CLOSED("Challenge recruitment period has ended"),
    GOAL_NOT_ACHIEVED("Challenge goal was not achieved"),
    INVALID_REQUEST("Cannot send friend request to yourself"),
    ALREADY_FRIENDS("Users are already friends"),
    DUPLICATE_REQUEST("Friend request already sent"),
    //Auth ErrorCode
    UNVERIFIED_USER("인증되지 않은 유저 이메일입니다."),
    NOTIFICATION_NOT_FOUND("Notification not found"),
    TOKEN_IS_EXPIRED("token is expired");


    private final String message;

    public int getStatus() {
        return ordinal();
    }
}
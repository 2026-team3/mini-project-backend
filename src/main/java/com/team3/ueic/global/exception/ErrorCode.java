package com.team3.ueic.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C001", "잘못된 입력값입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C002", "서버 내부 오류입니다."),

    EMAIL_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "U001", "이미 사용 중인 이메일입니다."),
    PHONE_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "U002", "이미 사용 중인 전화번호입니다."),
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "U003", "비밀번호와 비밀번호 확인이 일치하지 않습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U004", "사용자를 찾을 수 없습니다."),
    INVALID_LOGIN(HttpStatus.UNAUTHORIZED, "U005", "이메일 또는 비밀번호가 올바르지 않습니다."),

    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "A001", "유효하지 않은 토큰입니다."),
    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "A002", "리프레시 토큰이 만료되었습니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "A003", "저장된 리프레시 토큰이 없습니다."),
    REFRESH_TOKEN_MISMATCH(HttpStatus.UNAUTHORIZED, "A004", "리프레시 토큰이 일치하지 않습니다."),

    STUDY_NOT_FOUND(HttpStatus.NOT_FOUND, "S001", "스터디를 찾을 수 없습니다."),
    STUDY_APPLICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "S002", "스터디 신청을 찾을 수 없습니다."),
    STUDY_ALREADY_APPLIED(HttpStatus.BAD_REQUEST, "S003", "이미 신청했거나 멤버입니다."),
    STUDY_LEADER_CANNOT_APPLY(HttpStatus.BAD_REQUEST, "S004", "방장은 스터디에 신청할 수 없습니다."),
    STUDY_ACCESS_DENIED(HttpStatus.FORBIDDEN, "S005", "스터디에 대한 권한이 없습니다."),
    STUDY_APPLICATION_ALREADY_PROCESSED(HttpStatus.BAD_REQUEST, "S006", "이미 처리된 신청입니다."),
    STUDY_FULL(HttpStatus.BAD_REQUEST, "S007", "스터디 정원이 초과되었습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}

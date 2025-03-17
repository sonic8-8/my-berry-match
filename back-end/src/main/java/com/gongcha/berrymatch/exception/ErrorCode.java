package com.gongcha.berrymatch.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    UUID_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 uuid입니다."),

    MEMBER_NOT_UPDATED(HttpStatus.BAD_REQUEST, "유저 정보가 업데이트되지 않았습니다."),

    LIKES_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 좋아요 목록을 찾을 수 없습니다."),

    ALREADY_REGISTERED(HttpStatus.BAD_REQUEST, "이미 회원가입이 완료된 사용자입니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원 정보를 찾을 수 없습니다."),
    DUPLICATED_NICKNAME(HttpStatus.BAD_REQUEST, "이미 존재하는 닉네임입니다."),
    NOT_AUTHENTICATED_USER(HttpStatus.BAD_REQUEST, "인증 가능한 사용자가 아닙니다."),

    INVALID_EXPIRED_JWT(HttpStatus.BAD_REQUEST, "이미 만료된 JWT 입니다."),
    INVALID_MALFORMED_JWT(HttpStatus.BAD_REQUEST, "JWT의 구조가 유효하지 않습니다."),
    INVALID_CLAIM_JWT(HttpStatus.BAD_REQUEST, "JWT의 Claim이 유효하지 않습니다."),
    UNSUPPORTED_JWT(HttpStatus.BAD_REQUEST, "지원하지 않는 JWT 형식입니다."),
    INVALID_JWT(HttpStatus.BAD_REQUEST, "JWT가 유효하지 않습니다."),
    INVALID_PROGRESS(HttpStatus.BAD_REQUEST, "존재하지 않는 정보입니다."),


    JWT_NOT_FOUND_IN_DB(HttpStatus.NOT_FOUND, "DB에 JWT 정보가 존재하지 않습니다."),
    JWT_NOT_FOUND_IN_HEADER(HttpStatus.NOT_FOUND, "Header에 JWT 정보가 존재하지 않습니다."),
    JWT_NOT_FOUND_IN_COOKIE(HttpStatus.NOT_FOUND, "Cookie에 JWT 정보가 존재하지 않습니다."),
    REFRESH_TOKEN_NOT_MATCH(HttpStatus.BAD_REQUEST, "DB에 저장되어 있는 Refresh token과 일치하지 않습니다."),
    REFRESH_TOKEN_NOT_FOUNT(HttpStatus.NOT_FOUND, "Refresh token이 존재하지 않습니다."),
    DUPLICATED_REFRESH_TOKEN(HttpStatus.BAD_REQUEST,"Refresh token이 중복해서 존재합니다."),

    MULTIPART_FILE_NOT_EXIST(HttpStatus.BAD_REQUEST, "MultipartFile이 전달되지 않았습니다."),
    FILE_NOT_EXIST(HttpStatus.BAD_REQUEST, "해당 파일(이미지)이 존재하지 않습니다."),
    INVALID_FILE_NAME(HttpStatus.BAD_REQUEST, "전달받은 파일(이미지)의 이름이 null이거나 빈 문자열입니다."),
    NOT_SUPPORTED_EXTENSION(HttpStatus.BAD_REQUEST, "지원하지 않는 확장자입니다."),
    NOT_SUPPORTED_IMAGE_TYPE(HttpStatus.BAD_REQUEST, "지원하지 않는 이미지 타입입니다."),
    FILE_NOT_DELETED(HttpStatus.BAD_REQUEST, "파일(이미지)이 정상적으로 삭제되지 않았습니다."),
    FILE_NOT_SAVED(HttpStatus.BAD_REQUEST, "파일(이미지)가 정상적으로 저장되지 않았습니다."),
    FILE_NOT_COPIED(HttpStatus.BAD_REQUEST, "파일(이미지)가 정상적으로 복사되지 않았습니다."),
    IMAGE_NOT_ENCODED(HttpStatus.BAD_REQUEST, "이미지를 인코딩하는 과정에서 오류가 발생했습니다."),
    FILE_MAX_SIZE_EXCEED(HttpStatus.BAD_REQUEST, "파일(이미지)의 크기가 최대 용량을 초과했습니다."),

    ALREADY_PENDING_USER(HttpStatus.BAD_REQUEST,"이미 매칭 중인 유저입니다.");

    private final HttpStatus status;
    private final String message;
}

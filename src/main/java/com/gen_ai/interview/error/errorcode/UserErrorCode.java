package com.gen_ai.interview.error.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {
    DUPLICATE_USER(HttpStatus.CONFLICT, "User with same email already exists"),
    NOT_EXIST_USER(HttpStatus.NOT_FOUND, "User not found"),
    BAD_CREDENTIALS(HttpStatus.UNAUTHORIZED, "Bad Credentials"),
    GOOGLE_ACCOUNT_PARSE_ERROR(HttpStatus.BAD_REQUEST, "Google Account Parse Error"),
    GOOGLE_ACCOUNT_RETRIEVE_ERROR(HttpStatus.NOT_FOUND, "Google Account Retrieve Error"),
    INTERVIEW_COUNT_ERROR(HttpStatus.BAD_REQUEST, "No Remaining Interviews");


    private final HttpStatus httpStatus;
    private final String message;

}

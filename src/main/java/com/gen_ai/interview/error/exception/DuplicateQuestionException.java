package com.gen_ai.interview.error.exception;

import com.gen_ai.interview.error.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DuplicateQuestionException extends RuntimeException{
    private final ErrorCode errorCode;

}

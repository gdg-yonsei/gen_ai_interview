package com.gen_ai.interview.error.handler;

import com.gen_ai.interview.error.ErrorResponse;
import com.gen_ai.interview.error.errorcode.ErrorCode;
import com.gen_ai.interview.error.exception.BadCredentialsException;
import com.gen_ai.interview.error.exception.CompanyNotFoundException;
import com.gen_ai.interview.error.exception.DuplicateEmailException;
import com.gen_ai.interview.error.exception.DuplicateIdException;
import com.gen_ai.interview.error.exception.DuplicateQuestionException;
import com.gen_ai.interview.error.exception.GoogleAccountRetrieveException;
import com.gen_ai.interview.error.exception.InterviewCountException;
import com.gen_ai.interview.error.exception.JsonParseException;
import com.gen_ai.interview.error.exception.NotExistIdException;
import com.gen_ai.interview.error.exception.NotExistResumeException;
import com.gen_ai.interview.error.exception.NotResumeOwnerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(DuplicateIdException.class)
    public ResponseEntity<Object> handleDuplicateUserRequest(final DuplicateIdException e) {
        final ErrorCode errorCode = e.getErrorCode();
        return handleExceptionInternal(errorCode);
    }

    @ExceptionHandler(NotExistIdException.class)
    public ResponseEntity<Object> handleNotExistId(final NotExistIdException e) {
        final ErrorCode errorCode = e.getErrorCode();
        return handleExceptionInternal(errorCode);
    }


    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentials(final BadCredentialsException e) {
        final ErrorCode errorCode = e.getErrorCode();
        return handleExceptionInternal(errorCode);
    }

    @ExceptionHandler(NotExistResumeException.class)
    public ResponseEntity<Object> handleNotExistResume(final NotExistResumeException e) {
        final ErrorCode errorCode = e.getErrorCode();
        return handleExceptionInternal(errorCode);
    }

    @ExceptionHandler(JsonParseException.class)
    public ResponseEntity<Object> handleJsonParseError(final JsonParseException e) {
        final ErrorCode errorCode = e.getErrorCode();
        return handleExceptionInternal(errorCode);
    }

    @ExceptionHandler(NotResumeOwnerException.class)
    public ResponseEntity<Object> handleJsonParseError(final NotResumeOwnerException e) {
        final ErrorCode errorCode = e.getErrorCode();
        return handleExceptionInternal(errorCode);
    }

    @ExceptionHandler(DuplicateQuestionException.class)
    public ResponseEntity<Object> handleDuplicatePersonalQuestion(final DuplicateQuestionException e) {
        final ErrorCode errorCode = e.getErrorCode();
        return handleExceptionInternal(errorCode);
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<Object> handleDuplicateEmail(final DuplicateEmailException e) {
        final ErrorCode errorCode = e.getErrorCode();
        return handleExceptionInternal(errorCode);
    }

    @ExceptionHandler(GoogleAccountRetrieveException.class)
    public ResponseEntity<Object> handleGoogleAccountRetrieval(final GoogleAccountRetrieveException e) {
        final ErrorCode errorCode = e.getErrorCode();
        return handleExceptionInternal(errorCode);
    }

    @ExceptionHandler(InterviewCountException.class)
    public ResponseEntity<Object> handleInterviewCount(final InterviewCountException e) {
        final ErrorCode errorCode = e.getErrorCode();
        return handleExceptionInternal(errorCode);
    }

    @ExceptionHandler(CompanyNotFoundException.class)
    public ResponseEntity<Object> handleCompanyNotFound(final CompanyNotFoundException e) {
        final ErrorCode errorCode = e.getErrorCode();
        return handleExceptionInternal(errorCode);
    }


    private ResponseEntity<Object> handleExceptionInternal(final ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeErrorResponse(errorCode));
    }


    private ErrorResponse makeErrorResponse(final ErrorCode errorCode) {
        return ErrorResponse.builder()
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .build();
    }
}

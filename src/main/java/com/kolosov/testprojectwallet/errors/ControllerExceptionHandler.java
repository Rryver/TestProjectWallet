package com.kolosov.testprojectwallet.errors;

import org.springframework.http.ProblemDetail;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@RestControllerAdvice
public class ControllerExceptionHandler extends BasicExceptionHandler {

    public ControllerExceptionHandler(ErrorMessageHandler errorMessageHandler) {
        super(errorMessageHandler);
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail globalValidationExceptionHandler(Exception exception, WebRequest request) {
        ErrorType errorType = findErrorTypeByExceptionClass(exception.getClass());
        return createProblemDetail(exception, errorType.title, errorType.httpStatus, exception.getMessage());
    }

    @ExceptionHandler(BindException.class)
    public ProblemDetail globalValidationExceptionHandler(BindException exception, WebRequest request) {
        Map<String, String> errorMap = errorMessageHandler.getErrorMap(exception.getBindingResult());

        ErrorType errorType = findErrorTypeByExceptionClass(exception.getClass());
        ProblemDetail problemDetail = createProblemDetail(exception, errorType.title, errorType.httpStatus, "Invalid request");

        problemDetail.setProperty("Invalid_params", errorMap);

        return problemDetail;
    }
}

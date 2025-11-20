package com.kolosov.testprojectwallet.errors;

import com.kolosov.testprojectwallet.errors.exceptions.AppException;
import com.kolosov.testprojectwallet.errors.exceptions.BadRequestException;
import com.kolosov.testprojectwallet.errors.exceptions.ResourceNotFoundException;
import com.kolosov.testprojectwallet.errors.exceptions.WalletOperationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.NonNull;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
public class BasicExceptionHandler {

    static final Map<Class<? extends Throwable>, ErrorType> HTTP_STATUS_MAP = new LinkedHashMap<>() {
        {
            put(ChangeSetPersister.NotFoundException.class, ErrorType.NOT_FOUND);
            put(NoHandlerFoundException.class, ErrorType.NOT_FOUND);
            put(BadRequestException.class, ErrorType.BAD_REQUEST);
            put(HttpMessageNotReadableException.class, ErrorType.BAD_REQUEST);
            put(MethodArgumentTypeMismatchException.class, ErrorType.BAD_REQUEST);
            put(MethodArgumentNotValidException.class, ErrorType.BAD_REQUEST);
            put(ResourceNotFoundException.class, ErrorType.RESOURCE_NOT_FOUND);
            put(WalletOperationException.class, ErrorType.WALLET_OPERATION_EXCEPTION);
            put(AppException.class, ErrorType.APP_ERROR);
        }
    };

    protected final ErrorMessageHandler errorMessageHandler;

    protected ErrorType findErrorTypeByExceptionClass(@NonNull Class<? extends Throwable> exceptionClazz) {
        Optional<ErrorType> errorType = HTTP_STATUS_MAP.entrySet().stream()
                .filter(entry -> entry.getKey().equals(exceptionClazz))
                .findAny()
                .map(Map.Entry::getValue);

        if (errorType.isPresent()) {
            return errorType.get();
        }

        return HTTP_STATUS_MAP.entrySet().stream()
                .filter(entry -> entry.getKey().isAssignableFrom(exceptionClazz))
                .findAny()
                .map(Map.Entry::getValue)
                .orElse(ErrorType.APP_ERROR);
    }

    protected ProblemDetail createProblemDetail(Exception exception, String title, HttpStatus httpStatus, String defaultDetail) {
        ErrorResponse.Builder builder = ErrorResponse.builder(exception, httpStatus, defaultDetail);
        return builder
                .title(title)
                .type(URI.create("Request"))
                .build()
                .updateAndGetBody(errorMessageHandler.getMessageSource(), LocaleContextHolder.getLocale());
    }

}

package com.mastercard.developer.exception;

import lombok.extern.slf4j.Slf4j;
import org.openapitools.client.model.ErrorItem;
import org.openapitools.client.model.Errors;
import org.openapitools.client.model.ErrorsList;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class APIExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<ErrorItem> errorItemsList = getValidationErrorItemList(Arrays.asList(ex.getMessage()));
        ErrorsList errorsList = getErrorsList(errorItemsList);
        return new ResponseEntity<>(errorsList, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> validationErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
        List<ErrorItem> errorItemsList = getValidationErrorItemList(validationErrors);
        ErrorsList errorsList = getErrorsList(errorItemsList);
        return new ResponseEntity<>(errorsList, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({InvalidRequest.class})
    protected ResponseEntity<Object> handleInvalidRequest(InvalidRequest ex) {
        List<ErrorItem> errorItemsList = getErrorItemList(ErrorCodes.getErrorSource(ex.getCode()).name(),
                ex.getCode(),
                ex.getLocalizedMessage(),
                false);
        ErrorsList errorsList = getErrorsList(errorItemsList);
        log.error(errorItemsList.get(0).getReasonCode() + errorItemsList.get(0).getDetails() + HttpStatus.BAD_REQUEST, ex);
        return new ResponseEntity<>(errorsList, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({EntityNotFoundException.class})
    protected ResponseEntity<Object> entityNotFound(EntityNotFoundException ex) {
        List<ErrorItem> errorItemsList = getErrorItemList(ErrorCodes.getErrorSource(ex.getCode()).name(),
                ex.getCode(),
                ex.getLocalizedMessage(),
                false);
        ErrorsList errorsList = getErrorsList(errorItemsList);
        log.error(errorItemsList.get(0).getReasonCode() + errorItemsList.get(0).getDetails() + HttpStatus.NOT_FOUND, ex);
        return new ResponseEntity<>(errorsList, HttpStatus.NOT_FOUND);
    }

    public List<ErrorItem> getErrorItemList(String source, String reasonCode, String description, boolean recoverable) {
        List<ErrorItem> errors = new ArrayList<>();
        ErrorItem error = new ErrorItem();
        error.setSource(source);
        error.setReasonCode(reasonCode);
        error.setDescription(description);
        error.setRecoverable(recoverable);
        error.setDetails(null);
        errors.add(error);
        return errors;
    }

    public List<ErrorItem> getValidationErrorItemList(List<String> details) {
        List<ErrorItem> errors = new ArrayList<>();
        for (String detail : details) {
            ErrorItem error = new ErrorItem();
            error.setSource(ErrorCodes.VALIDATION_ERROR.code);
            error.setDescription(ErrorCodes.INVALID_INPUT.code);
            error.setReasonCode(ErrorCodes.INVALID_INPUT.name());
            error.setRecoverable(false);
            error.setDetails(null);
            errors.add(error);
        }
        return errors;
    }

    public ErrorsList getErrorsList(List<ErrorItem> errorItemList) {
        ErrorsList errorsList = new ErrorsList();
        Errors errors = new Errors();
        errors.setError(errorItemList);
        errorsList.setErrors(errors);
        return errorsList;
    }
}
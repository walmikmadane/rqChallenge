package com.example.rqchallenge.employees.exception;

import com.example.rqchallenge.employees.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.Objects;


@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private ResponseEntity<ErrorResponse> generateErrorResponse(
            List<String> params, HttpStatus httpStatus) {
        ErrorResponse responseBody = new ErrorResponse();
        responseBody.setErrorMessages(params);
        responseBody.setErrorCode(httpStatus.value());
        responseBody.setStatus("failed");
        return new ResponseEntity<>(responseBody, httpStatus);
    }

    @ExceptionHandler(EmployeeRuntimeException.class)
    public ResponseEntity<ErrorResponse> handleServiceRuntimeException(EmployeeRuntimeException employeeRuntimeException)
    {
        List<String> errorMessages=employeeRuntimeException.getErrorMessages();
        if(Objects.isNull(errorMessages))
            errorMessages=List.of(employeeRuntimeException.getExceptionMessage());
        return generateErrorResponse(errorMessages, employeeRuntimeException.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleServiceException(Exception exception)
    {
        return generateErrorResponse(List.of(exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}

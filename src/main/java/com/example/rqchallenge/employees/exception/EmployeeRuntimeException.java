package com.example.rqchallenge.employees.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
public class EmployeeRuntimeException extends RuntimeException{
    public String exceptionMessage;
    public  HttpStatus httpStatus;
    public Throwable exception;
    public List<String> errorMessages;

    EmployeeRuntimeException(String message,HttpStatus httpStatus )
    {
        super(message);
        this.httpStatus=httpStatus;
        this.exceptionMessage=message;
    }

    public EmployeeRuntimeException(String message, HttpStatus httpStatus, Throwable throwable)
    {
        super(message);
        this.httpStatus=httpStatus;
        this.exceptionMessage=message;
        this.exception= throwable;
    }

    public EmployeeRuntimeException(String message, HttpStatus httpStatus, Throwable throwable , List<String> errorMessages)
    {
        super(message);
        this.httpStatus=httpStatus;
        this.exceptionMessage=message;
        this.exception= throwable;
        this.errorMessages =errorMessages;
    }
}

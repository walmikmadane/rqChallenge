package com.example.rqchallenge.employees.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorResponse {
    private int errorCode;
    private String status="failed";
    private List<String> errorMessages;
}

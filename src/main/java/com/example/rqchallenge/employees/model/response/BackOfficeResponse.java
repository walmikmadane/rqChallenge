package com.example.rqchallenge.employees.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BackOfficeResponse {
    private String status;
    private String message;
}

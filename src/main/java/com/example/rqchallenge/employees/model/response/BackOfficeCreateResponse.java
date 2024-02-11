package com.example.rqchallenge.employees.model.response;

import lombok.Data;

@Data
public class BackOfficeCreateResponse extends BackOfficeResponse {
    private CreateEmployeeResponse data;
}

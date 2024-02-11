package com.example.rqchallenge.employees.model.response;

import com.example.rqchallenge.employees.model.Employee;
import lombok.Data;

@Data
public class BackOfficeEmployeeApiResponse extends BackOfficeResponse {
    private Employee data;
}

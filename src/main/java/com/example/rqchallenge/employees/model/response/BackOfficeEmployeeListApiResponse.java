package com.example.rqchallenge.employees.model.response;

import com.example.rqchallenge.employees.model.Employee;
import lombok.Data;

import java.util.List;

@Data
public class BackOfficeEmployeeListApiResponse extends BackOfficeResponse {
    private List<Employee> data;
}

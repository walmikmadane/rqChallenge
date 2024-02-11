package com.example.rqchallenge.employees.model.response;

import lombok.Data;

@Data
public class CreateEmployeeResponse {
    private String id;
    private String name;
    private int age;
    private int salary;
}

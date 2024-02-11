package com.example.rqchallenge.employees.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Employee {
    @JsonProperty("id")
    private String id;

    @JsonProperty("employee_name")
    private String name;

    @JsonProperty("employee_salary")
    private Integer salary;

    @JsonProperty("employee_age")
    private int age;

    @JsonProperty("profile_image")
    private String profileImage;
}

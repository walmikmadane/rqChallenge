package com.example.rqchallenge.employees.utils;

import com.example.rqchallenge.employees.model.response.BackOfficeCreateResponse;
import com.example.rqchallenge.employees.model.response.BackOfficeEmployeeApiResponse;
import com.example.rqchallenge.employees.model.response.BackOfficeEmployeeListApiResponse;
import com.example.rqchallenge.employees.model.response.BackOfficeResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;

public class TestData {
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static final String EMPLOYEES_DATA= "employeeData.json";
    private static final String SINGLE_EMPLOYEE_DATA= "singleEmployeeData.json";
    private static final String CREATE_EMPLOYEE_RESPONSE= "createResponse.json";
    private static final String DELETE_EMPLOYEE_RESPONSE= "deleteResponse.json";

    public static BackOfficeEmployeeListApiResponse getAllEmployees() throws IOException {
        URL resource = TestData.class.getClassLoader().getResource(EMPLOYEES_DATA);
        return objectMapper.readValue(resource, BackOfficeEmployeeListApiResponse.class);
    }

    public static BackOfficeEmployeeApiResponse getEmployee() throws IOException {
        URL resource = TestData.class.getClassLoader().getResource(SINGLE_EMPLOYEE_DATA);
        return objectMapper.readValue(resource, BackOfficeEmployeeApiResponse.class);
    }

    public static BackOfficeCreateResponse getCreateResponse() throws IOException {
        URL resource = TestData.class.getClassLoader().getResource(CREATE_EMPLOYEE_RESPONSE);
        return objectMapper.readValue(resource, BackOfficeCreateResponse.class);
    }

    public static BackOfficeResponse getDeleteResponse() throws IOException {
        URL resource = TestData.class.getClassLoader().getResource(DELETE_EMPLOYEE_RESPONSE);
        return objectMapper.readValue(resource, BackOfficeResponse.class);
    }
}

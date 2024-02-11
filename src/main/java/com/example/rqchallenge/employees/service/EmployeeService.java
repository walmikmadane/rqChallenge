package com.example.rqchallenge.employees.service;

import com.example.rqchallenge.employees.model.Employee;

import java.util.List;

public interface EmployeeService {
    List<Employee> getAllEmployees();
    List<Employee> getEmployeesByNameSearch(String name);
    Employee getEmployeeById(String id);
    Integer getHighestSalaryOfEmployees();
    List<String> getTop10HighestEarningEmployeeNames();
    Employee createEmployee(String name, String salary, String age);
    Employee deleteEmployee(String id);
}

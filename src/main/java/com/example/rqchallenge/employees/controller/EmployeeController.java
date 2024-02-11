package com.example.rqchallenge.employees.controller;

import com.example.rqchallenge.employees.model.Employee;
import com.example.rqchallenge.employees.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController implements IEmployeeController{

    @Autowired
    private EmployeeService employeeService;

    @Override
    public ResponseEntity<List<Employee>> getAllEmployees() throws IOException {
        List<Employee> employeeList = employeeService.getAllEmployees();
        return ResponseEntity.ok(employeeList);
    }

    @Override
    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(String searchString) {
        List<Employee> result= employeeService.getEmployeesByNameSearch(searchString);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Employee> getEmployeeById(String id) {
        Employee employee= employeeService.getEmployeeById(id);
        return ResponseEntity.ok(employee);
    }

    @Override
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        Integer highestSalary= employeeService.getHighestSalaryOfEmployees();
        return ResponseEntity.ok(highestSalary);
    }

    @Override
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        List<String> result= employeeService.getTop10HighestEarningEmployeeNames();
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Employee> createEmployee(Map<String, Object> employeeInput) {
        String name= employeeInput.containsKey("name")?employeeInput.get("name").toString():null;
        String age= employeeInput.containsKey("age")?employeeInput.get("age").toString():null;
        String salary= employeeInput.containsKey("salary")?employeeInput.get("salary").toString():null;
        Employee employee= employeeService.createEmployee(name, age, salary);
        return new ResponseEntity(employee,HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<String> deleteEmployeeById(String id) {
       Employee employee= employeeService.deleteEmployee(id);
       return ResponseEntity.ok(employee.getName());
    }
}

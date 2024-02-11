package com.example.rqchallenge.employees.service.impl;

import com.example.rqchallenge.employees.config.BackOfficeApiConfig;
import com.example.rqchallenge.employees.exception.EmployeeRuntimeException;
import com.example.rqchallenge.employees.model.response.BackOfficeCreateResponse;
import com.example.rqchallenge.employees.model.response.BackOfficeEmployeeApiResponse;
import com.example.rqchallenge.employees.model.response.BackOfficeEmployeeListApiResponse;
import com.example.rqchallenge.employees.model.Employee;
import com.example.rqchallenge.employees.model.response.BackOfficeResponse;
import com.example.rqchallenge.employees.repository.RestClientAdapter;
import com.example.rqchallenge.employees.service.EmployeeService;
import com.example.rqchallenge.employees.util.NullCheckUtil;
import com.example.rqchallenge.employees.util.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.*;
import java.util.stream.Collectors;

import static com.example.rqchallenge.employees.constant.AppConstants.*;

@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private BackOfficeApiConfig backOfficeApiConfig;

    @Autowired
    private RestClientAdapter restClientAdapter;

    @Override
    public List<Employee> getAllEmployees() {
        log.info("Fetching all employee data.");
        try{
            ResponseEntity<BackOfficeEmployeeListApiResponse> responseEntity =
                    restClientAdapter.executeGetRequest(null, BackOfficeEmployeeListApiResponse.class, null, backOfficeApiConfig.getUrl()+"/"+EMPLOYEES);
            log.info("Employees Data fetched successfully.");
            List<Employee> employees= NullCheckUtil.getOrNull(()-> responseEntity.getBody().getData());
            if(Objects.nonNull(employees))
            {
                return employees;
            }
            return Collections.emptyList();
        }catch (HttpStatusCodeException httpStatusCodeException)
        {
            log.error("Error while fetching employee list data , exception :{}", httpStatusCodeException.getMessage());
            throw new EmployeeRuntimeException("List Employee API Error", httpStatusCodeException.getStatusCode(), httpStatusCodeException);
        }
        catch (Exception exception)
        {
            log.error("Error while fetching employee list data , exception :{}", exception.getMessage());
            throw new EmployeeRuntimeException("List Employee API Error", HttpStatus.INTERNAL_SERVER_ERROR, exception);
        }
    }

    @Override
    public List<Employee> getEmployeesByNameSearch(String name) {
        log.info("Request for employee name search, name: {}", name);
        List<Employee> employees= getAllEmployees();
        List<Employee> result= Collections.emptyList();;
        if(!employees.isEmpty())
        {
            result= employees.stream()
                    .filter(employee -> employee.getName().contains(name))
                    .collect(Collectors.toList());
        }
        log.info("Result for employee name search, name: {}, count:{}", name, result.size());
        return result;
    }

    @Override
    public Employee getEmployeeById(String id) {
        log.info("Fetching employee data for id:{}", id);
        try{
            ResponseEntity<BackOfficeEmployeeApiResponse> responseEntity =
                    restClientAdapter.executeGetRequest(null, BackOfficeEmployeeApiResponse.class, null, backOfficeApiConfig.getUrl()+"/"+EMPLOYEE+"/"+id);
            log.info("Single Employee Data fetched successfully.");
            Employee employee= NullCheckUtil.getOrNull(()-> responseEntity.getBody().getData());
            if(Objects.isNull(employee))
                throw new EmployeeRuntimeException("Employee Not Found", HttpStatus.NOT_FOUND, null);
            return employee;
        }catch (HttpStatusCodeException httpStatusCodeException)
        {
            log.error("Error while fetching employee data for ID:{} , exception :{}", id, httpStatusCodeException.getMessage());
            throw new EmployeeRuntimeException("GET Employee API Error", httpStatusCodeException.getStatusCode(), httpStatusCodeException);
        }catch (EmployeeRuntimeException employeeRuntimeException)
        {
            log.error("Error while fetching employee data for ID:{} , exception :{}", id, employeeRuntimeException.getMessage());
            throw employeeRuntimeException;
        }
        catch (Exception exception)
        {
            log.error("Error while fetching employee  data for ID:{} , exception :{}", id, exception.getMessage());
            throw new EmployeeRuntimeException("GET Employee API Error", HttpStatus.INTERNAL_SERVER_ERROR, exception);
        }
    }

    @Override
    public Integer getHighestSalaryOfEmployees() {
        log.info("Request for highest salary received.");
        List<Employee> employees= getAllEmployees();
        Optional<Integer> maxSalary = employees.stream().map(employee -> employee.getSalary()).max(Comparator.comparing(Integer::intValue));
        log.info("Request for highest salary completed.");
        if(maxSalary.isPresent())
            return maxSalary.get();
        return -1;
    }

    @Override
    public List<String> getTop10HighestEarningEmployeeNames() {
        log.info("Request for Top 10 highest salary employees received.");
        List<Employee> employees= getAllEmployees();
        List<String> result = employees.stream()
                .sorted(Comparator.comparing(Employee::getSalary).reversed())
                .limit(10)
                .map(employee -> employee.getName())
                .collect(Collectors.toList());
        log.info("Request for Top 10 highest salary employees completed.");
        return result;
    }

    @Override
    public Employee createEmployee(String name, String salary, String age) {
        log.info("Create Request for Employee received.");

        List<String> failedParams= Validator.validateCreateEmployeeData(name, age, salary);

        if(failedParams.size()>0)
            throw new EmployeeRuntimeException("Bad Request", HttpStatus.BAD_REQUEST, null, failedParams);

        HashMap<String, String> params= new HashMap<>();
        params.put("name",name);
        params.put("salary", salary);
        params.put("age", age);
        try{
            ResponseEntity<BackOfficeCreateResponse> responseEntity =
                    restClientAdapter.executePostRequest(null, BackOfficeCreateResponse.class, null, backOfficeApiConfig.getUrl()+"/"+CREATE, params);
            if(responseEntity.getStatusCode().value()==200)
            {
                log.info("Employee Created successfully");
                Employee employee = new Employee();
                employee.setId(responseEntity.getBody().getData().getId());
                employee.setName(name);
                employee.setAge(Integer.parseInt(age));
                employee.setSalary(Integer.parseInt(salary));
                return employee;
            }else
                throw new EmployeeRuntimeException("Employee Create Error", HttpStatus.INTERNAL_SERVER_ERROR, null);
        }catch (HttpStatusCodeException httpStatusCodeException)
        {
            log.error("Error while creating employee data, exception :{}", httpStatusCodeException.getMessage());
            throw new EmployeeRuntimeException("Create Employee API Error", httpStatusCodeException.getStatusCode(), httpStatusCodeException);
        }
        catch (EmployeeRuntimeException employeeRuntimeException)
        {
            log.error("Error while creating employee data, exception :{}", employeeRuntimeException.getMessage());
            throw employeeRuntimeException;
        }
        catch(Exception exception)
        {
            log.error("Error while creating employee data, exception :{}", exception.getMessage());
            throw new EmployeeRuntimeException("Create Employee API Error", HttpStatus.INTERNAL_SERVER_ERROR, exception);
        }

    }

    @Override
    public Employee deleteEmployee(String id) {
        log.info("Delete request for employee with ID:{} received", id);
        try{
            Employee employee= getEmployeeById(id);
            ResponseEntity<BackOfficeResponse> responseEntity =
                    restClientAdapter.executeDeleteRequest(null, BackOfficeResponse.class, null, backOfficeApiConfig.getUrl()+"/"+DELETE+"/"+id);
            if(responseEntity.getStatusCode().equals(HttpStatus.OK))
                return employee;
            else
                throw new EmployeeRuntimeException("Delete Api error", HttpStatus.INTERNAL_SERVER_ERROR, null);
        }catch (HttpStatusCodeException httpStatusCodeException)
        {
            log.error("Error while deleting employee data, exception :{}", httpStatusCodeException.getMessage());
            throw new EmployeeRuntimeException("Delete Employee API Error", httpStatusCodeException.getStatusCode(), httpStatusCodeException);
        }
        catch (EmployeeRuntimeException employeeRuntimeException)
        {
            log.error("Error while deleting employee data, exception :{}", employeeRuntimeException.getMessage());
            throw employeeRuntimeException;
        }
        catch(Exception exception)
        {
            log.error("Error while deleting employee data, exception :{}", exception.getMessage());
            throw new EmployeeRuntimeException("Delete Employee API Error", HttpStatus.INTERNAL_SERVER_ERROR, exception);
        }

    }
}

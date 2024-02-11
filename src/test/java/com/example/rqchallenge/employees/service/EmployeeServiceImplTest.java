package com.example.rqchallenge.employees.service;

import com.example.rqchallenge.employees.config.BackOfficeApiConfig;
import com.example.rqchallenge.employees.exception.EmployeeRuntimeException;
import com.example.rqchallenge.employees.model.Employee;
import com.example.rqchallenge.employees.model.response.BackOfficeCreateResponse;
import com.example.rqchallenge.employees.model.response.BackOfficeEmployeeApiResponse;
import com.example.rqchallenge.employees.model.response.BackOfficeEmployeeListApiResponse;
import com.example.rqchallenge.employees.model.response.BackOfficeResponse;
import com.example.rqchallenge.employees.repository.RestClientAdapter;
import com.example.rqchallenge.employees.service.impl.EmployeeServiceImpl;
import com.example.rqchallenge.employees.utils.TestData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;

import java.io.IOException;
import java.util.List;

import static com.example.rqchallenge.employees.constant.AppConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceImplTest {
    @Mock
    private BackOfficeApiConfig backOfficeApiConfig;

    @Mock
    private RestClientAdapter restClientAdapter;

    @InjectMocks
    EmployeeServiceImpl employeeService;

    @Test
    public void getAllEmployees_Success() throws IOException {
        Mockito.when(backOfficeApiConfig.getUrl()).thenReturn(HOST);
        Mockito.when(restClientAdapter.executeGetRequest(null,BackOfficeEmployeeListApiResponse.class,null, HOST +"/"+EMPLOYEES))
                .thenReturn(ResponseEntity.ok(TestData.getAllEmployees()));
        List<Employee> employees= employeeService.getAllEmployees();
        Assertions.assertNotNull(employees);
        Assertions.assertFalse(employees.isEmpty());
    }

    @Test
    public void getAllEmployees_Exception() throws IOException {
        Mockito.when(backOfficeApiConfig.getUrl()).thenReturn(HOST);
        Mockito.doThrow(new HttpServerErrorException(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED,"Too many requests"))
                        .when(restClientAdapter).executeGetRequest(null,BackOfficeEmployeeListApiResponse.class,null, HOST +"/"+EMPLOYEES);
        assertThrows(
                EmployeeRuntimeException.class,
                ()-> employeeService.getAllEmployees()
        );
    }

    @Test
    public void getEmployeesByNameSearch_Success() throws IOException
    {
        Mockito.when(backOfficeApiConfig.getUrl()).thenReturn(HOST);
        Mockito.when(restClientAdapter.executeGetRequest(null,BackOfficeEmployeeListApiResponse.class,null, HOST +"/"+EMPLOYEES))
                .thenReturn(ResponseEntity.ok(TestData.getAllEmployees()));
        List<Employee> employees= employeeService.getEmployeesByNameSearch("Tiger");
        Assertions.assertNotNull(employees);
        Assertions.assertFalse(employees.isEmpty());
        for(Employee employee: employees)
        {
            Assertions.assertTrue(employee.getName().contains("Tiger"));
        }
    }

    @Test
    public void getHighestSalaryOfEmployees_Success() throws IOException
    {
        Mockito.when(backOfficeApiConfig.getUrl()).thenReturn(HOST);
        Mockito.when(restClientAdapter.executeGetRequest(null,BackOfficeEmployeeListApiResponse.class,null, HOST +"/"+EMPLOYEES))
                .thenReturn(ResponseEntity.ok(TestData.getAllEmployees()));
        Integer highestSalaryOfEmployees= employeeService.getHighestSalaryOfEmployees();
        Assertions.assertNotNull(highestSalaryOfEmployees);
        Assertions.assertEquals(725000, highestSalaryOfEmployees);
    }

    @Test
    public void getTop10HighestEarningEmployeeNames_Success() throws IOException
    {
        Mockito.when(backOfficeApiConfig.getUrl()).thenReturn(HOST);
        Mockito.when(restClientAdapter.executeGetRequest(null,BackOfficeEmployeeListApiResponse.class,null, HOST +"/"+EMPLOYEES))
                .thenReturn(ResponseEntity.ok(TestData.getAllEmployees()));
        List<String> names= employeeService.getTop10HighestEarningEmployeeNames();
        Assertions.assertNotNull(names);
        Assertions.assertEquals(10, names.size());
    }

    @Test
    public void getEmployeeById_Success() throws IOException
    {
        Mockito.when(backOfficeApiConfig.getUrl()).thenReturn(HOST);
        Mockito.when(restClientAdapter.executeGetRequest(null, BackOfficeEmployeeApiResponse.class,null, HOST +"/"+EMPLOYEE+"/1"))
                .thenReturn(ResponseEntity.ok(TestData.getEmployee()));
        Employee result= employeeService.getEmployeeById("1");
        Assertions.assertNotNull(result);
        Assertions.assertEquals("1", result.getId());
        Assertions.assertEquals("Tiger Nixon", result.getName());
    }

    @Test
    public void getEmployeeById_Exception() throws IOException
    {
        Mockito.when(backOfficeApiConfig.getUrl()).thenReturn(HOST);
        Mockito.doThrow(new HttpServerErrorException(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED,"Too many requests"))
                .when(restClientAdapter).executeGetRequest(null,BackOfficeEmployeeApiResponse.class,null, HOST +"/"+EMPLOYEE+"/1");
        assertThrows(
                EmployeeRuntimeException.class,
                ()-> employeeService.getEmployeeById("1")
        );
    }

    @Test
    public void createEmployee_Success() throws IOException
    {
        Mockito.when(backOfficeApiConfig.getUrl()).thenReturn(HOST);

        Mockito.when(restClientAdapter.executePostRequest(any(), eq(BackOfficeCreateResponse.class), any(), anyString(), anyMap()))
                .thenReturn(ResponseEntity.ok(TestData.getCreateResponse()));
        Employee employee= employeeService.createEmployee("test234","22","10000");
        Assertions.assertNotNull(employee);
        Assertions.assertEquals("1000", employee.getId());
    }

    @Test
    public void createEmployee_BadRequest() throws IOException
    {
        try{
            employeeService.createEmployee("","22","10000");
        }catch (EmployeeRuntimeException employeeRuntimeException)
        {
            Assertions.assertEquals(HttpStatus.BAD_REQUEST, employeeRuntimeException.getHttpStatus());
            Assertions.assertTrue(employeeRuntimeException.getErrorMessages().contains("Invalid name value provided"));
        }
    }

    @Test
    public void createEmployee_Exception() throws IOException
    {
        Mockito.when(backOfficeApiConfig.getUrl()).thenReturn(HOST);

        Mockito.doThrow(new HttpServerErrorException(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED,"Too many requests"))
                .when(restClientAdapter).executePostRequest(any(), eq(BackOfficeCreateResponse.class), any(), anyString(), anyMap());
        assertThrows(
                EmployeeRuntimeException.class,
                ()-> employeeService.createEmployee("test234","22","10000")
        );
    }

    @Test
    public void deleteEmployee_Success() throws IOException
    {
        Mockito.when(backOfficeApiConfig.getUrl()).thenReturn(HOST);
        Mockito.when(restClientAdapter.executeGetRequest(null, BackOfficeEmployeeApiResponse.class,null, HOST +"/"+EMPLOYEE+"/1"))
                .thenReturn(ResponseEntity.ok(TestData.getEmployee()));
        Mockito.when(restClientAdapter.executeDeleteRequest(null, BackOfficeResponse.class, null,HOST+"/"+DELETE+"/1"))
                .thenReturn(ResponseEntity.ok(TestData.getDeleteResponse()));

        Employee employee= employeeService.deleteEmployee("1");
        Assertions.assertNotNull(employee);
        Assertions.assertEquals("1", employee.getId());
    }

    @Test
    public void deleteEmployee_NotFoundException() throws IOException
    {
        Mockito.when(backOfficeApiConfig.getUrl()).thenReturn(HOST);
        Mockito.when(restClientAdapter.executeGetRequest(null, BackOfficeEmployeeApiResponse.class,null, HOST +"/"+EMPLOYEE+"/1"))
                .thenReturn(ResponseEntity.ok(null));
        try{
            employeeService.deleteEmployee("1");
        }catch(EmployeeRuntimeException e)
        {
            Assertions.assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
        };

    }

    @Test
    public void deleteEmployee_Exception() throws IOException
    {
        Mockito.when(backOfficeApiConfig.getUrl()).thenReturn(HOST);
        Mockito.when(restClientAdapter.executeGetRequest(null, BackOfficeEmployeeApiResponse.class,null, HOST +"/"+EMPLOYEE+"/1"))
                .thenReturn(ResponseEntity.ok(TestData.getEmployee()));

        Mockito.doThrow(new HttpServerErrorException(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED,"Too many requests"))
                .when(restClientAdapter).executeDeleteRequest(null, BackOfficeResponse.class, null, HOST+"/"+DELETE+"/1");

        assertThrows(
                EmployeeRuntimeException.class,
                ()-> employeeService.deleteEmployee("1")
        );
    }
}

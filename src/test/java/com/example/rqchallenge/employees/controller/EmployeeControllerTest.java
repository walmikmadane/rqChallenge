package com.example.rqchallenge.employees.controller;

import com.example.rqchallenge.employees.exception.EmployeeRuntimeException;
import com.example.rqchallenge.employees.exception.GlobalExceptionHandler;
import com.example.rqchallenge.employees.model.Employee;
import com.example.rqchallenge.employees.model.ErrorResponse;
import com.example.rqchallenge.employees.service.EmployeeService;
import com.example.rqchallenge.employees.utils.TestData;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.rqchallenge.employees.constant.AppConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@ExtendWith(MockitoExtension.class)
public class EmployeeControllerTest {
    @Mock
    private EmployeeService employeeService;
    @InjectMocks
    EmployeeController employeeController;
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup()
    {
        this.mockMvc= standaloneSetup(employeeController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    public void getAllEmployees_ValidRequest() throws Exception {
        Mockito.when(employeeService.getAllEmployees()).thenReturn(TestData.getAllEmployees().getData());
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get(API_ROOT_PATH+"/"+EMPLOYEES)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        List<Employee> result= objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                new TypeReference<List<Employee>>(){});
        assertNotNull(result);
        assertTrue(result.size()>0);
    }

    @Test
    public void getAllEmployees_ErrorResponse() throws Exception {
        Mockito.doThrow(new EmployeeRuntimeException("Too many request",HttpStatus.INTERNAL_SERVER_ERROR))
                .when(employeeService).getAllEmployees();

        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.get(API_ROOT_PATH+"/"+EMPLOYEES)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andReturn();
        ErrorResponse errorResponse= objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                ErrorResponse.class);
        assertNotNull(errorResponse);
        assertEquals("failed",errorResponse.getStatus());
        assertEquals(500,errorResponse.getErrorCode());
    }

    @Test
    public void getEmployeesByNameSearch_ValidRequest() throws Exception {
        Mockito.when(employeeService.getEmployeesByNameSearch("test")).thenReturn(Collections.singletonList(TestData.getEmployee().getData()));
        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.get(API_ROOT_PATH+"/"+EMPLOYEES+"/"+SEARCH+"/test")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        List<Employee> result= objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                new TypeReference<List<Employee>>(){});
        assertNotNull(result);
        assertTrue(result.size()>0);
    }

    @Test
    public void getEmployeesByNameSearch_ErrorResponse() throws Exception {
        Mockito.doThrow(new EmployeeRuntimeException("Too many request",HttpStatus.INTERNAL_SERVER_ERROR))
                .when(employeeService).getEmployeesByNameSearch("test");

        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.get(API_ROOT_PATH+"/"+EMPLOYEES+"/"+SEARCH+"/test")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andReturn();
        ErrorResponse errorResponse= objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                ErrorResponse.class);
        assertNotNull(errorResponse);
        assertEquals("failed",errorResponse.getStatus());
        assertEquals(500,errorResponse.getErrorCode());
    }

    @Test
    public void getEmployeeById_ValidRequest() throws Exception {
        Mockito.when(employeeService.getEmployeeById("test")).thenReturn(TestData.getEmployee().getData());
        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.get(API_ROOT_PATH+"/"+EMPLOYEES+"/test")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        Employee result= objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                Employee.class);
        assertNotNull(result);
    }

    @Test
    public void getEmployeeById_ErrorResponse() throws Exception {
        Mockito.doThrow(new EmployeeRuntimeException("Too many request",HttpStatus.INTERNAL_SERVER_ERROR))
                .when(employeeService).getEmployeeById("test");

        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.get(API_ROOT_PATH+"/"+EMPLOYEES+"/test")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andReturn();
        ErrorResponse errorResponse= objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                ErrorResponse.class);
        assertNotNull(errorResponse);
        assertEquals("failed",errorResponse.getStatus());
        assertEquals(500,errorResponse.getErrorCode());
    }

    @Test
    public void getHighestSalaryOfEmployees_ValidRequest() throws Exception {
        Mockito.when(employeeService.getHighestSalaryOfEmployees()).thenReturn(100);
        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.get(API_ROOT_PATH+"/"+EMPLOYEES+"/"+HIGHEST_SALARY_PATH)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        Integer result= objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                Integer.class);
        assertNotNull(result);
        assertEquals(100, result);
    }

    @Test
    public void getHighestSalaryOfEmployees_ErrorResponse() throws Exception {
        Mockito.doThrow(new EmployeeRuntimeException("Too many request",HttpStatus.INTERNAL_SERVER_ERROR))
                .when(employeeService).getHighestSalaryOfEmployees();

        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.get(API_ROOT_PATH+"/"+EMPLOYEES+"/"+HIGHEST_SALARY_PATH)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andReturn();
        ErrorResponse errorResponse= objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                ErrorResponse.class);
        assertNotNull(errorResponse);
        assertEquals("failed",errorResponse.getStatus());
        assertEquals(500,errorResponse.getErrorCode());
    }

    @Test
    public void getTopTenHighestEarningEmployeeNames_ValidRequest() throws Exception {
        List<String> names= new ArrayList<>();
        names.add("test1");
        names.add("test2");
        Mockito.when(employeeService.getTop10HighestEarningEmployeeNames()).thenReturn(names);
        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.get(API_ROOT_PATH+"/"+EMPLOYEES+"/"+TOP10_HIGHEST_EARNING_PATH)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        List<String> result= objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                new TypeReference<List<String>>() {});
        assertNotNull(result);
        assertTrue(result.contains("test1"));
    }

    @Test
    public void getTopTenHighestEarningEmployeeNames_ErrorResponse() throws Exception {
        Mockito.doThrow(new EmployeeRuntimeException("Too many request",HttpStatus.INTERNAL_SERVER_ERROR))
                .when(employeeService).getTop10HighestEarningEmployeeNames();

        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.get(API_ROOT_PATH+"/"+EMPLOYEES+"/"+TOP10_HIGHEST_EARNING_PATH)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andReturn();
        ErrorResponse errorResponse= objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                ErrorResponse.class);
        assertNotNull(errorResponse);
        assertEquals("failed",errorResponse.getStatus());
        assertEquals(500,errorResponse.getErrorCode());
    }

    @Test
    public void createEmployee_ValidRequest() throws Exception {
        Mockito.when(employeeService.createEmployee(anyString(),anyString(),anyString())).thenReturn(TestData.getEmployee().getData());
        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.post(API_ROOT_PATH+"/"+EMPLOYEES)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\n    \"name\":\"test\",\n    \"salary\": \"1000\",\n    \"age\": \"21\"\n}"))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();
        Employee result= objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                Employee.class);
        assertNotNull(result);
    }

    @Test
    public void createEmployee_ErrorResponse() throws Exception {
        Mockito.doThrow(new EmployeeRuntimeException("Too many request",HttpStatus.INTERNAL_SERVER_ERROR))
                .when(employeeService).createEmployee(anyString(),anyString(),anyString());

        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.post(API_ROOT_PATH+"/"+EMPLOYEES)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\n    \"name\":\"test\",\n    \"salary\": \"1000\",\n    \"age\": \"21\"\n}"))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andReturn();
        ErrorResponse errorResponse= objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                ErrorResponse.class);
        assertNotNull(errorResponse);
        assertEquals("failed",errorResponse.getStatus());
        assertEquals(500,errorResponse.getErrorCode());
    }

    @Test
    public void deleteEmployeeById_ValidRequest() throws Exception {
        Mockito.when(employeeService.deleteEmployee("1")).thenReturn(TestData.getEmployee().getData());
        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.delete(API_ROOT_PATH+"/"+EMPLOYEES+"/1")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String result= mvcResult.getResponse().getContentAsString();
        assertNotNull(result);
        assertTrue(result.equals("Tiger Nixon"));
    }

    @Test
    public void deleteEmployeeById_ErrorResponse() throws Exception {
        Mockito.doThrow(new EmployeeRuntimeException("Too many request",HttpStatus.INTERNAL_SERVER_ERROR))
                .when(employeeService).deleteEmployee("1");

        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.delete(API_ROOT_PATH+"/"+EMPLOYEES+"/1")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andReturn();
        ErrorResponse errorResponse= objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                ErrorResponse.class);
        assertNotNull(errorResponse);
        assertEquals("failed",errorResponse.getStatus());
        assertEquals(500,errorResponse.getErrorCode());
    }
}

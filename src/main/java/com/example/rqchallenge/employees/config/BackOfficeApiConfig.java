package com.example.rqchallenge.employees.config;

import lombok.Data;
import org.springframework.stereotype.Component;

import static com.example.rqchallenge.employees.constant.AppConstants.HOST;

@Component
@Data
public class BackOfficeApiConfig {

    //TODO
    // We can read this config from central config management tool also...
    public String url = HOST;
}

package com.example.rqchallenge.employees.util;


import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Validator {

    public static List<String> validateCreateEmployeeData(String name, String age, String salary)
    {
        List<String> failedParams= new ArrayList<>();
        if(StringUtils.isEmpty(name))
            failedParams.add("Invalid name value provided");
        if(StringUtils.isEmpty(age) || !StringUtils.isNumeric(age) )
            failedParams.add("Invalid age value provided");
        if(StringUtils.isEmpty(salary) || !StringUtils.isNumeric(salary))
            failedParams.add("Invalid salary value provided");
        return failedParams;
    }
}

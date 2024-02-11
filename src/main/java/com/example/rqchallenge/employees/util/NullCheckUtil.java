package com.example.rqchallenge.employees.util;


import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.function.Supplier;

@Slf4j
public class NullCheckUtil {

    private NullCheckUtil(){

    }
    public static <T> T getOrNull(Supplier<T> supplier) {
        T evaluated = null;
        try {
            evaluated = supplier.get();
        } catch (NullPointerException ex) {
            log.info(ex.getMessage());
        }
        return Optional.ofNullable(evaluated).orElse(null);
    }
}

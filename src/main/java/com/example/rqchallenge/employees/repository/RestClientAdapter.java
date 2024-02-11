package com.example.rqchallenge.employees.repository;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Repository
@Slf4j
public class RestClientAdapter {
    @Autowired
    private RestTemplate restTemplate;

    public <I,O>ResponseEntity<O> executeGetRequest(I requestPayload, Class<O> className, HttpHeaders httpHeaders, String url)
    {
        HttpEntity<I> requestEntity = new HttpEntity<>(requestPayload, httpHeaders);
        log.debug("GET API request with headers : {},url : {}", httpHeaders, url);
        return this.restTemplate.exchange(url, HttpMethod.GET, requestEntity,className);
    }

    public <I,O>ResponseEntity<O> executePostRequest(I requestPayload, Class<O> className, HttpHeaders httpHeaders, String url, Map<String, String> params)
    {
        HttpEntity<I> requestEntity = new HttpEntity<>(requestPayload, httpHeaders);
        log.debug("POST API request with headers : {},url : {}", httpHeaders, url);
        return this.restTemplate.exchange(url, HttpMethod.POST, requestEntity,className, params);
    }

    public <I,O>ResponseEntity<O> executeDeleteRequest(I requestPayload, Class<O> className, HttpHeaders httpHeaders, String url)
    {
        HttpEntity<I> requestEntity = new HttpEntity<>(requestPayload, httpHeaders);
        log.debug("DELETE API request with headers : {},url : {}", httpHeaders, url);
        return this.restTemplate.exchange(url, HttpMethod.DELETE, requestEntity,className);
    }
}

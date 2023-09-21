package com.example.cafe.service;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface UserSevice {
    ResponseEntity<String> signUp(Map<String,String> requestMap);


}

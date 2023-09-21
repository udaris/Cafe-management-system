package com.example.cafe.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class CafeUtils {
    private CafeUtils(){
    }

    public static ResponseEntity<String> getResponseentity(String responsrMessage, HttpStatus httpStatus){
        return new ResponseEntity<String>("{\"message\":\""+responsrMessage+"\"}", httpStatus);
    }
}

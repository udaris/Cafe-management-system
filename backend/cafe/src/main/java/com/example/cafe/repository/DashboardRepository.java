package com.example.cafe.repository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RequestMapping(path = "/dashboard")
public interface DashboardRepository {
    @GetMapping(path = "/details")
    ResponseEntity <Map<String, Object>> getCount();
}

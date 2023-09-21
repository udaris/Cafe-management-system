package com.example.cafe.service;

import com.example.cafe.constents.CafeConsents;
import com.example.cafe.entity.Category;
import com.example.cafe.utils.CafeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface CategoryService {
    ResponseEntity<String >addNewCategory(Map<String, String> requestMap);
    ResponseEntity<List<Category>> getAllCategory(String filterValue);
    ResponseEntity<String> updateCategory(Map<String, String>requestMap);

}

package com.example.cafe.repositoryImpl;

import com.example.cafe.constents.CafeConsents;
import com.example.cafe.repository.UserRepository;
import com.example.cafe.service.UserSevice;
import com.example.cafe.utils.CafeUtils;
import com.example.cafe.wrapper.UserWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Slf4j
@RestController
@RequestMapping("/user")
public class UserRepositoryImpl implements UserRepository {
    @Autowired
    UserSevice userSevice;

    @Override
    @PostMapping(path = "signup")
    public ResponseEntity<String> signup(Map<String, String> requestMap) {
        try{
            return userSevice.signUp(requestMap);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseentity(CafeConsents.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    @PostMapping(path = "/login")
    public ResponseEntity<String> login(Map<String, String> requestMap) {
       log.info("Inside login");
        try{
            return userSevice.login(requestMap);
       }catch (Exception ex){
            ex.printStackTrace();
       }
        return CafeUtils.getResponseentity(CafeConsents.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    @GetMapping(path = "/get")
    public ResponseEntity<List<UserWrapper>> getAllUser() {
        log.info("INSIDE GetAllUser");
        try{
            log.info("Print all users.");
            return userSevice.getAllUser();
        }catch (Exception ex){
            log.info("all users error");
            ex.printStackTrace();
        }
        return new ResponseEntity<List<UserWrapper>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    @PostMapping(path = "/update")
    public ResponseEntity<String> update(Map<String, String> requestMap) {
        try {
            return userSevice.update(requestMap);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseentity(CafeConsents.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> checkToken() {
        try{
            return userSevice.checkToken();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseentity(CafeConsents.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        try{
            return userSevice.changePassword(requestMap);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseentity(CafeConsents.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
        try{
            return userSevice.forgotPassword(requestMap);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseentity(CafeConsents.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}

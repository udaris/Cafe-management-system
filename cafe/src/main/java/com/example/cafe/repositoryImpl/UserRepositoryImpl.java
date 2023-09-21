package com.example.cafe.repositoryImpl;

import com.example.cafe.constents.CafeConsents;
import com.example.cafe.repository.UserRepository;
import com.example.cafe.service.UserSevice;
import com.example.cafe.utils.CafeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    UserSevice userSevice;

    @Override
    public ResponseEntity<String> signup(Map<String, String> requestMap) {
        try{
            return userSevice.signUp(requestMap);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseentity(CafeConsents.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

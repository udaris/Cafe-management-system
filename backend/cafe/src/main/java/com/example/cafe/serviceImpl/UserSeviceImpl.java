package com.example.cafe.serviceImpl;

import com.example.cafe.JWT.CustomerUsersDetailsService;
import com.example.cafe.JWT.JwtFilter;
import com.example.cafe.JWT.JwtUtil;
import com.example.cafe.constents.CafeConsents;
import com.example.cafe.dao.UserDao;
import com.example.cafe.entity.User;
import com.example.cafe.service.UserSevice;
import com.example.cafe.utils.CafeUtils;
import com.example.cafe.utils.EmailUtils;
import com.example.cafe.wrapper.UserWrapper;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class UserSeviceImpl implements UserSevice {
    @Autowired
    UserDao userDao;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    CustomerUsersDetailsService customerUsersDetailsService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    EmailUtils emailUtils;

    @Override
    public ResponseEntity<String> signUp(Map <String, String> requestMap ){
        log.info("Inside signup {}",requestMap);
        try{
            if(validationSignUpMap(requestMap)){
                User user=userDao.findByEmailId(requestMap.get("email"));
                if(Objects.isNull(user)){
                    userDao.save(getUserFromMap(requestMap));
                    return CafeUtils.getResponseentity("Successfully registered!.", HttpStatus.OK);
                }else{
                    return CafeUtils.getResponseentity("Email already exits.", HttpStatus.BAD_REQUEST);
                }
            }else {
                return CafeUtils.getResponseentity(CafeConsents.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }}catch (Exception e){
            e.printStackTrace();
        }return CafeUtils.getResponseentity(CafeConsents.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validationSignUpMap(Map<String, String> requestMap){
        if(requestMap.containsKey("name")&& requestMap.containsKey("contactNumber") && requestMap.containsKey("email") && requestMap.containsKey("password")){
            return true;
        }else{
            return false;
        }
    }

    private User getUserFromMap(Map<String, String> requestMap){
        User user=new User();
        user.setName(requestMap.get("name"));
        user.setContactNumber(requestMap.get("contactNumber"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(requestMap.get("password"));
        user.setStatus("false");
        user.setRole("user");
        return user;

    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        log.info("Inside login");
        try {
            Authentication auth=authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestMap.get("email"), requestMap.get("password"))
            );
            if(auth.isAuthenticated()){
                if(customerUsersDetailsService.getUserDetails().getStatus().equalsIgnoreCase("true")){
                    return new ResponseEntity<String>("{\"token\":\""+
                        jwtUtil.generateToken(customerUsersDetailsService.getUserDetails().getEmail(),
                                customerUsersDetailsService.getUserDetails().getRole())+"\"}",
                          HttpStatus.OK  );
                }
                else {
                    return new ResponseEntity<String>("\\message\":\""+"Wait foe admin approval."+"\"}",
                            HttpStatus.BAD_REQUEST);
                }
            }
        }catch (Exception ex){
            log.error("{}",ex);
        }
        return new ResponseEntity<String>("\\message\":\""+"Bad Credentials."+"\"}",
                HttpStatus.BAD_REQUEST);

    }

    @Override
    public ResponseEntity<List<UserWrapper>> getAllUser() {
        log.info("Inside getALLUserService");
        try{
            if(jwtFilter.isAdmin()){
                log.info("inside is admin true");
                return new ResponseEntity<>(userDao.getAllUser(), HttpStatus.OK);
            }else {
                log.info("inside is admin false");
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception ex){
            log.info("isAdmin catch ex");
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> update(Map<String, String> requestMap) {
        try {
            if(jwtFilter.isAdmin()){
               Optional<User> optional= userDao.findById(Integer.parseInt(requestMap.get("id")));
               if(!optional.isEmpty()){
                    userDao.updateStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));
                    sendMailToAllAdmin(requestMap.get("status"), optional.get().getEmail(), userDao.getAllAdmin());
                    return CafeUtils.getResponseentity("User status updated successfully.", HttpStatus.OK);
               }else {
                   return CafeUtils.getResponseentity("User id does not exist.", HttpStatus.OK);
               }
            }else {
                return CafeUtils.getResponseentity(CafeConsents.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseentity(CafeConsents.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private void sendMailToAllAdmin(String status, String user, List<String> allAdmin) {
        allAdmin.remove(jwtFilter.getCurrentUser());
        if(status!=null && status.equalsIgnoreCase("true")){
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(), "Account Approved","USER:-"+ user +"\n is approved by \n Admin : "+jwtFilter.getCurrentUser(), allAdmin);
        }else {
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(), "Account Disabled","USER:-"+ user +"\n is disabled by \n Admin : "+jwtFilter.getCurrentUser(), allAdmin);
        }
    }
    @Override
    public ResponseEntity<String> checkToken() {
        return CafeUtils.getResponseentity("true", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        try {
            User userObj=userDao.findByEmail(jwtFilter.getCurrentUser());
            if(!userObj.equals(null)){
                if(userObj.getPassword().equals(requestMap.get("oldPassword"))){
                    userObj.setPassword(requestMap.get("newPassword"));
                    userDao.save(userObj);
                    return CafeUtils.getResponseentity("Password updated successfully", HttpStatus.OK);
                }
                return CafeUtils.getResponseentity("Incorrect Old Password", HttpStatus.BAD_REQUEST);
            }
            return CafeUtils.getResponseentity(CafeConsents.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseentity(CafeConsents.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
        try {
            User user=userDao.findByEmail(requestMap.get("email"));
            if(!Objects.isNull(user)&&!Strings.isNullOrEmpty(user.getEmail())){
                emailUtils.forgotMail(user.getEmail(), "Credentials by Cafe management system", user.getPassword());
            }
            return CafeUtils.getResponseentity("Check your main for credentials", HttpStatus.OK);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseentity(CafeConsents.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}

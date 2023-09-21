package com.example.cafe.JWT;

import com.example.cafe.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;
@Service
public class CustomerUsersDetailsService implements UserDetailsService {
    @Autowired
    UserDao userDao;

    private com.example.cafe.entity.User userDetails;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        userDetails=userDao.findByEmailId(username);
        if(!Objects.isNull(userDetails)){
            return new User(userDetails.getEmail(), userDetails.getPassword(), new ArrayList<>());
        }else {
            throw new UsernameNotFoundException("User not found.");
        }
    }

    public com.example.cafe.entity.User getUserDetails(){
        return userDetails;
    }
}

package com.calvin.cafe.serviceImpl;

import com.calvin.cafe.JWT.CustomerUserDetailsService;
import com.calvin.cafe.JWT.JwtService;
import com.calvin.cafe.POJO.User;
import com.calvin.cafe.constents.CafeConstants;
import com.calvin.cafe.dao.UserDao;
import com.calvin.cafe.service.UserService;
import com.calvin.cafe.utils.CafeUtils;
import jakarta.validation.constraints.Null;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtService jwtService;

    @Autowired
    CustomerUserDetailsService customerUserDetailsService;

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        log.info("Inside signup {}",requestMap);
        try{
            if(validateSignUpMap(requestMap)){
                User user = userDao.findByEmailId(requestMap.get("email"));
                if(Objects.isNull(user))
                {
                    userDao.save(getUserFromMap(requestMap));
                    return CafeUtils.getResponseEntity(CafeConstants.SUCCESS_REGISTERED,HttpStatus.OK);
                }
                else
                {
                    return CafeUtils.getResponseEntity(CafeConstants.EMAIL_ALREADY_EXIST,HttpStatus.BAD_REQUEST);
                }
            }
            else
            {
                return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }

        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);

    }


    private boolean validateSignUpMap(Map<String,String>requestMap){
        if(requestMap.containsKey("name") && requestMap.containsKey("contactNumber") && requestMap.containsKey("email") && requestMap.containsKey("password"))
        {
            return true;
        }
        return false;
    }

    private User getUserFromMap(Map<String,String> requestMap){
        User user = new User();
        user.setName(requestMap.get("name"));
        user.setRole("user");
        user.setEmail(requestMap.get("email"));
        user.setPassword(requestMap.get("password"));
        user.setStatus("false");
        user.setContactNumber(requestMap.get("contactNumber"));
        return user;
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        log.info("Inside login");

        try {
            System.out.println(requestMap.get("email"));
            User user = new User();
            user = userDao.findByEmailId(requestMap.get("email"));

            if (user == null) {
                System.out.println("MASUK");
                return new ResponseEntity<String>("{\"message\":\"" + "Account doesn't exist." + "\"}",HttpStatus.NOT_FOUND);

            }else if(user.getStatus().equalsIgnoreCase("true"))
            {
                return new ResponseEntity<String>("{\"token\":\"" + jwtService.generateToken2(user.getEmail(),user.getRole()) + "\"}",
                        HttpStatus.OK);
            }

        }catch (Exception ex)
        {
            ex.printStackTrace();
            return new ResponseEntity<String>("{\"message\":\"" + "Account doesn't exist." + "\"}",HttpStatus.NOT_FOUND);
        }



        return new ResponseEntity<String>("{\"message\":\"" + "Bad Credentials." + "\"}",HttpStatus.BAD_REQUEST);
    }

    //        if(user.getEmail() == null)
//        {
//            return new ResponseEntity<String>("{\"message\":\"" + "Account doesn't exist." + "\"}",HttpStatus.NOT_FOUND);
//        }

//        try {
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(requestMap.get("email"),requestMap.get("password"))
//            );
//            if (authentication.isAuthenticated()) {
//
//
//            if (customerUserDetailsService.getUserDetail().getStatus().equalsIgnoreCase("true")){
//                return new ResponseEntity<String>("{\"token\":\"" + jwtService.generateToken2(customerUserDetailsService
//                        .getUserDetail().getEmail(),customerUserDetailsService.getUserDetail().getRole()) + "\"}",
//                        HttpStatus.OK);
//            }else
//            {
//                return new ResponseEntity<String>("{\"message\":\"" + "Wait for admin approval." + "\"}",HttpStatus.BAD_REQUEST);
//            }
//            }
//
//        }catch (Exception ex){
//            log.error("{}",ex);
//        }
}

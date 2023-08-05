package com.calvin.cafe.serviceImpl;

//import com.calvin.cafe.JWT.CustomerUserDetailsService;
import com.calvin.cafe.JWT.JwtService;
import com.calvin.cafe.POJO.AuthAuthorityMember;
import com.calvin.cafe.POJO.User;
import com.calvin.cafe.constents.CafeConstants;
import com.calvin.cafe.dao.AuthAuthorityMemberDao;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Autowired
    AuthAuthorityMemberDao authAuthorityMemberDao;

    @Autowired
    AuthenticationManager authenticationManager;

//    @Autowired
//    CustomerUserDetailsService customerUserDetailsService;

    @Autowired
    UserDetailsManager userDetailsManager;

    @Autowired
    JwtService jwtService;

    @Autowired
    PasswordEncoder passwordEncoder;

//    @Autowired
//    CustomerUserDetailsService customerUserDetailsService;

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        log.info("Inside signup {}",requestMap);
        try{
            if(validateSignUpMap(requestMap)){
                User user = userDao.findByEmailId(requestMap.get("email"));
                if(Objects.isNull(user))
                {
                    Map<String,String> authAuthorityMemberMap =  new HashMap<>();

                    authAuthorityMemberMap.put("user_id",requestMap.get("email"));
                    authAuthorityMemberMap.put("auth_id","user");

                    System.out.println("inside auth authority member map : " + authAuthorityMemberMap);

                    String passwordEncode = passwordEncoder.encode(requestMap.get("password"));
                    requestMap.put("password",passwordEncode);

                    authAuthorityMemberDao.save(getAuthAuthorityMemberFromMap(authAuthorityMemberMap));
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

    private AuthAuthorityMember getAuthAuthorityMemberFromMap(Map<String,String> requestMap)
    {
        AuthAuthorityMember authAuthorityMember = new AuthAuthorityMember();
        authAuthorityMember.setUser_id(requestMap.get("user_id"));
        authAuthorityMember.setAuth_id(requestMap.get("auth_id"));
        return authAuthorityMember;
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        log.info("Inside login");

        User user = userDao.findByEmailId(requestMap.get("email"));

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(),requestMap.get("password"))
            );
            if (authentication.isAuthenticated()) {

                UserDetails userDetails = userDetailsManager.loadUserByUsername(user.getEmail());

                return new ResponseEntity<String>("{\"token\":\"" + jwtService.generateToken(
                        userDetails) + "\"}",
                        HttpStatus.OK);

            }

        }catch (Exception ex){
            log.error("{}",ex);
        }


        return new ResponseEntity<String>("{\"message\":\"" + "Bad Credentials." + "\"}",HttpStatus.BAD_REQUEST);
    }



}

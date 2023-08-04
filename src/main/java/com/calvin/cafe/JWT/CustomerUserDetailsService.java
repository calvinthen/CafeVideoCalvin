//package com.calvin.cafe.JWT;
//
//import com.calvin.cafe.dao.UserDao;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.Objects;
//
//@Slf4j
//@Service
//public class CustomerUserDetailsService implements UserDetailsService {
//
//    @Autowired
//    UserDao userDao;
//
//    @Autowired
//    PasswordEncoder passwordEncoder;
//
//
//    private com.calvin.cafe.POJO.User userDetail;
//
////    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
////    }
//
//    public com.calvin.cafe.POJO.User getUserDetail(){
//
//        return userDetail;
//    }
//
//
//
//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//
//        log.info("Inside loaduserbyEmail {}" , email);
//        userDetail = userDao.findByEmailId(email);
//        if(!Objects.isNull(userDetail))
//        {
//
//            return new User(userDetail.getEmail(), passwordEncoder.encode(userDetail.getPassword()),new ArrayList<>());
//        }
//        else
//        {
//            throw new UsernameNotFoundException("User not found.");
//        }
//    }
//}

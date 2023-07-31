package com.calvin.cafe.dao;

import com.calvin.cafe.POJO.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Map;

public interface UserDao extends JpaRepository<User,Integer> {
    @Query("SELECT u FROM User u WHERE u.email = :email")
    User findByEmailId(@Param("email") String email);

}

package com.niu.study.repository;

import com.niu.study.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface UserRepository  {

    //根据用户名查询用户
    User findUsername(String username);







}

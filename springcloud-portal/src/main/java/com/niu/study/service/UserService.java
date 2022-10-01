package com.niu.study.service;

import com.niu.study.application.dto.UserDto;

public interface UserService {


    void createUser(UserDto dto);

    UserDto queryUser(String accessToken);
}

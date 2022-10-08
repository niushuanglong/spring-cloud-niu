package com.niu.study.service;

import com.niu.study.application.dto.UserDto;
import com.niu.study.utils.enums.JsonResult;

public interface UserService {


    void createUser(UserDto dto);

    JsonResult queryUser(String accessToken);
}

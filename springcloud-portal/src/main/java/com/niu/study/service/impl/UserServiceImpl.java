package com.niu.study.service.impl;

import com.niu.study.ExceptionDealWth.CustomizeException;
import com.niu.study.application.UserAssembler;
import com.niu.study.domain.User;
import com.niu.study.repository.UserRepository;
import com.niu.study.service.UserService;
import com.niu.study.service.dto.UserDto;
import com.niu.study.utils.JWTTokenUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Map;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void createUser(UserDto dto) {
        User user=new UserAssembler().fromUser(dto);
        userRepository.createUserByUsernameAndPwd(user);
    }

    @Override
    public UserDto queryUser(String accessToken) {
        if (StringUtils.isNotBlank(accessToken)) throw new CustomizeException("令牌为空!");
        Map<String, String> map = JWTTokenUtil.verifyTokenAndGetClaims(accessToken);
        User user = userRepository.findUsername(map.get(0));
        return new UserAssembler().toUser(user);
    }






}

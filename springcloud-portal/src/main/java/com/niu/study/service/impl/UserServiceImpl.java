package com.niu.study.service.impl;

import com.niu.study.ExceptionDealWth.CustomizeException;
import com.niu.study.application.assembler.UserAssembler;
import com.niu.study.domain.AccessToken;
import com.niu.study.domain.User;
import com.niu.study.domain.base.IBeansFactoryService;
import com.niu.study.repository.AccessTokenRepository;
import com.niu.study.repository.UserRepository;
import com.niu.study.service.UserService;
import com.niu.study.application.dto.UserDto;
import com.niu.study.utils.JWTTokenUtil;
import com.niu.study.utils.enums.JsonResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Map;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Resource
    private AccessTokenRepository tokenRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private IBeansFactoryService beansFactoryService;

    @Override
    public void createUser(UserDto dto) {
        User user=new UserAssembler(beansFactoryService).formUser(dto);
        userRepository.createUserByUsernameAndPwd(user);
    }

    @Override
    public JsonResult queryUser(String accessToken) {
        if (StringUtils.isBlank(accessToken)) throw new CustomizeException("令牌为空!");
        AccessToken token=tokenRepository.findByTokenId(accessToken);
        Map<String, Object> map = JWTTokenUtil.verifyTokenAndGetClaims(token.getAccessToken());
        User user = userRepository.findUsername(map.get("username").toString());
        UserDto userDto = new UserAssembler(beansFactoryService).toUser(user);
        return new JsonResult(userDto);
    }






}

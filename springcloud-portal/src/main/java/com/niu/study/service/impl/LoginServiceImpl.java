package com.niu.study.service.impl;

import com.niu.study.domain.AccessToken;
import com.niu.study.domain.User;
import com.niu.study.repository.AccessTokenRepository;
import com.niu.study.repository.UserRepository;
import com.niu.study.service.LoginService;
import com.niu.study.utils.JWTTokenUtil;
import com.niu.study.utils.JsonResult;
import com.niu.study.utils.Sm4Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private UserRepository userRepository;
    @Resource
    private AccessTokenRepository tokenRepository;

    @Resource
    private RedisTemplate<String ,Object> redisTemplate;


    @Override
    public JsonResult chunkLoginInfo(String username, String password, HttpServletRequest request) {
        User user= (User) userRepository.findUsername(username);
        if (user==null||!user.getEnabled()){
            //没有启用
            throw new RuntimeException("用户不存在或未启用!");
        }
        if (user.getPassword()!= Sm4Utils.encryptSm4(password)){
            throw new RuntimeException("密码错误!");
        }
        Map<String,String > map=new HashMap<>();
        map.put("username",username);
        String token = JWTTokenUtil.createToken(map, 24 * 60 * 60 );
        HttpSession session = request.getSession();
        AccessToken accessToken = new AccessToken(token, new Date(24 * 60 * 60));
        tokenRepository.save(accessToken);
        session.setAttribute("accessToken",accessToken);
        session.setMaxInactiveInterval(30 * 60);//30分钟没活动，自动移除
        redisTemplate.opsForValue().setIfAbsent("accessToken",accessToken,24, TimeUnit.HOURS);
        return new JsonResult(200);
    }
}

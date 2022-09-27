package com.niu.study.service.impl;

import com.niu.study.ExceptionDealWth.CustomizeException;
import com.niu.study.domain.AccessToken;
import com.niu.study.domain.User;
import com.niu.study.repository.AccessTokenRepository;
import com.niu.study.repository.JpaHibernateRepository;
import com.niu.study.repository.UserRepository;
import com.niu.study.service.LoginService;
import com.niu.study.utils.CalendarUtils;
import com.niu.study.utils.IPUtils;
import com.niu.study.utils.JWTTokenUtil;
import com.niu.study.utils.Sm4Utils;
import com.niu.study.utils.enums.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class LoginServiceImpl extends JpaHibernateRepository implements LoginService {
    @Autowired
    private UserRepository userRepository;
    @Resource
    private AccessTokenRepository tokenRepository;

    @Resource
    private RedisTemplate<String ,Object> redisTemplate;


    @Override
    public JsonResult chunkLoginInfo(String username, String password, HttpServletResponse response) {
        User user= userRepository.findUsername(username);
        if (user==null||!user.isEnabled()){
            //没有启用
            throw new CustomizeException("用户不存在或未启用!");
        }
        if (!user.getPassword().equals(Sm4Utils.encryptSm4(password))){
            throw new CustomizeException("密码错误!");
        }
        Map<String,String > map=new HashMap<>();
        map.put("username",username);
        String token = JWTTokenUtil.createToken(map, 24 * 60 * 60 * 2 );
        //登陆的时候创建token 和超时时间
        AccessToken accessToken = new AccessToken(token, CalendarUtils.addOneDay(new Date(),24), IPUtils.getIpAddr());
        tokenRepository.saveToken(accessToken);
        redisTemplate.opsForValue().setIfAbsent("token",accessToken,24, TimeUnit.HOURS);
        return new JsonResult(accessToken);
    }
    //暂时未加重复用户判断..
    @Override
    public AccessToken queryAccessToken(String token) {
        return tokenRepository.findByToken(token);
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findUsername(username);
    }

    @Override
    public void refreshToken(AccessToken token) {
        tokenRepository.updateToken(token);
    }
}

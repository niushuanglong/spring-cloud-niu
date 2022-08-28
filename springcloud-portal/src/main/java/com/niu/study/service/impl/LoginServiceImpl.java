package com.niu.study.service.impl;

import com.niu.study.ExceptionDealWth.CustomizeException;
import com.niu.study.domain.AccessToken;
import com.niu.study.domain.User;
import com.niu.study.repository.AccessTokenRepository;
import com.niu.study.repository.JpaHibernateRepository;
import com.niu.study.repository.UserRepository;
import com.niu.study.service.LoginService;
import com.niu.study.utils.CalendarUtils;
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
import javax.transaction.Transactional;
import java.time.LocalDate;
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
    public JsonResult chunkLoginInfo(String username, String password, HttpServletRequest request) {
        User user= userRepository.findUsername(username);
        if (user==null||!user.getEnabled()){
            //没有启用
            throw new CustomizeException("用户不存在或未启用!");
        }
        if (!user.getPassword().equals(Sm4Utils.encryptSm4(password))){
            throw new CustomizeException("密码错误!");
        }
        Map<String,String > map=new HashMap<>();
        map.put("username",username);
        String token = JWTTokenUtil.createToken(map, 24 * 60 * 60 * 2 );
        HttpSession session = request.getSession();
        //登陆的时候创建token 和超时时间
        AccessToken accessToken = new AccessToken(token, CalendarUtils.addOneDay(new Date(),24));
        tokenRepository.saveToken(accessToken);
        session.setAttribute("accessToken",accessToken);
        session.setMaxInactiveInterval(30 * 60);//30分钟没活动，自动移除
        redisTemplate.opsForValue().setIfAbsent("accessToken",accessToken,24, TimeUnit.HOURS);
        return new JsonResult(200);
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

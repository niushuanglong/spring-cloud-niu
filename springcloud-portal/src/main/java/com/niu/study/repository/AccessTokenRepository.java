package com.niu.study.repository;

import com.niu.study.domain.AccessToken;
import com.niu.study.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 用户令牌
 */
public interface AccessTokenRepository{
    /**
     * 根据令牌查询
     * @param token
     * @return
     */
    AccessToken findByToken(String token);

    /**
     * 创建令牌
     * @param accessToken
     */
    void saveToken(AccessToken accessToken);

    /**
     * 更新令牌
     * @param token
     */
    void updateToken(AccessToken token);






}

package com.niu.study.repository;

import com.niu.study.domain.AccessToken;
import com.niu.study.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 用户令牌
 */
public interface AccessTokenRepository extends JpaRepository<AccessToken, Long>, JpaSpecificationExecutor<AccessToken> {








}

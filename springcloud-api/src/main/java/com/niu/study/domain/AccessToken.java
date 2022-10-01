package com.niu.study.domain;


import com.niu.study.domain.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name="sys_access_token")
@Entity
public class AccessToken extends BaseEntity implements Serializable {
    //身份令牌
    @Column(name = "access_token")
    private String accessToken;
    @Column(name = "expire_time", updatable = false)
    private Date expireTime;

    public String getAccessToken() {
        return accessToken;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public AccessToken() {
    }

    public AccessToken(String accessToken, Date expireTime,String ip) {
        this.accessToken = accessToken;
        this.expireTime = expireTime;
    }


    /*************************************更新领域对象******************************************/
    public void updateExpireTime(Date expireTime) {
        this.expireTime=expireTime;
    }



}

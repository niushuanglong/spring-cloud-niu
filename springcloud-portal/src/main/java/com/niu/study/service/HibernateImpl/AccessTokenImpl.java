package com.niu.study.service.HibernateImpl;

import com.niu.study.domain.AccessToken;
import com.niu.study.domain.User;
import com.niu.study.repository.AccessTokenRepository;
import com.niu.study.repository.JpaHibernateRepository;
import com.niu.study.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
@Repository
public class AccessTokenImpl extends JpaHibernateRepository implements AccessTokenRepository {


    @Override
    public AccessToken findByToken(String token) {
        Map<String ,Object> map=new HashMap<>();
        StringBuffer hql=new StringBuffer();
        hql.append("select a from "+ AccessToken.class.getName()+" a where a.accessToken=:accessToken");
        map.put("accessToken",token);
        return this.createHQLQueryByMapParams(AccessToken.class,hql.toString(),map).uniqueResult();
    }

    @Override
    public void saveToken(AccessToken accessToken) {
        this.getSession().save(accessToken);
    }

    @Override
    public void updateToken(AccessToken token) {
        this.getSession().update(token);
    }

    @Override
    public AccessToken findByTokenId(String id) {
        return this.getSession().get(AccessToken.class,id);
    }

}

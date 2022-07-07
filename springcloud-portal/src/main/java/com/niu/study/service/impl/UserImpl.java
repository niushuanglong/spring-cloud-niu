package com.niu.study.service.impl;

import com.niu.study.domain.User;
import com.niu.study.repository.JpaHibernateRepository;
import com.niu.study.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@SuppressWarnings("unchecked")
@Repository
public class UserImpl extends JpaHibernateRepository implements UserRepository {
    @Override
    public User findUsername(String username) {
        Map<String ,Object> map=new HashMap<>();
        StringBuffer hql=new StringBuffer();
        hql.append("select u from "+User.class.getName()+" u where u.username=:username");
        map.put("username",username);
        return this.createHQLQueryByMapParams(User.class,hql.toString(),map).uniqueResult();
    }
}

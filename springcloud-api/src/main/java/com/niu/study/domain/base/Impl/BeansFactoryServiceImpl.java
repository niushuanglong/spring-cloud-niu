package com.niu.study.domain.base.Impl;


import com.niu.study.domain.base.IBeansFactoryService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class BeansFactoryServiceImpl implements IBeansFactoryService, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) {
        Map<String, T> beans=applicationContext.getBeansOfType(type);
        if(beans==null){
            beans=new HashMap<String, T>();
        }
        return beans;
    }

    @Override
    public <T> T getBeanOfType(Class<T> type) {
        return applicationContext.getBean(type);
    }

    @Override
    public Object getBeanOfBeanName(String beanName) {
        return applicationContext.getBean(beanName);
    }

    @Override
    public <T> T getBeanOfBeanNameAndType(String beanName, Class<T> type) {
        return applicationContext.getBean(beanName,type);
    }
}

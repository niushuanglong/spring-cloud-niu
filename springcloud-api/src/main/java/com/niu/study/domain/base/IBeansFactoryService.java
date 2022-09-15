package com.niu.study.domain.base;

import java.util.Map;

public interface IBeansFactoryService {
	
    /**
     * 根据类型获取实所有现类
     * @param type
     * @return
     */
    <T> Map<String, T> getBeansOfType(Class<T> type);
    /**
     * 根据类型唯一实现类
     * @param type
     * @return
     */
    <T> T getBeanOfType(Class<T> type);
    /**
     * 根据实现类 spring管理的对象名获取对象
     * @param beanName
     * @return
     */
    Object getBeanOfBeanName(String beanName);
    /**
     * 根据实现类 spring管理的对象名和指定的类型获取对象
     * @param beanName
     * @param type
     * @return
     */
    <T> T getBeanOfBeanNameAndType(String beanName, Class<T> type);

}

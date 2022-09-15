/*
 * Copyright 2002-2007 tayoo company.
 */
package com.niu.study.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 利用反射调用方法
 * @author wyx since 2008-10-15
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ReflectionUtils {

    /**
     * 反射注入属性
     * @param o
     * @param fieldName
     * @param val
     * @throws Exception
     */
	public static void writeField(Object o, String fieldName, Object val) throws Exception {
		Class clazz = o.getClass();
		Field f = clazz.getDeclaredField(fieldName);
		f.setAccessible(true);
		f.set(o, val);
	}
	
	/**
	 * 反射读取属性
	 * @param o
	 * @param fieldName
	 * @return
	 * @throws Exception
	 */
	public static Object readField(Object o, String fieldName) throws Exception {
		Class clazz = o.getClass();
		Field f = clazz.getDeclaredField(fieldName);
		f.setAccessible(true);
		return f.get(o);
	}

	/**
     * 反射读取属性
     * @param o
     * @param fieldName
     * @return
     * @throws Exception
     */
	public static Object getProperty(Object o, String... props) {
		if (props != null && o != null) {
			for (String p : props) {
				try {
					String method="get"+(p.charAt(0)+"").toUpperCase()+p.substring(1);
					Object v = invokeMethod(o, method);
					return v;

				} catch (Exception e) {
					// e.printStackTrace();
					// 无需对此异常作特殊处理,因为可能需猜测props
				}
			}
			return null;
		} else
			return null;
	}

	/**
     * 反射注入属性
     * @param o
     * @param fieldName
     * @param val
     * @throws Exception
     */
	public static void setProperty(Object o, String prop, Object value) {
		try {
			String method="set"+(prop.charAt(0)+"").toUpperCase()+prop.substring(1);
			invokeMethod(o, method, value);
		} catch (Exception e) {
			// e.printStackTrace();
			// 无需对此异常作特殊处理,因为可能需猜测props
		}
	}
	
	/**
	 * 反射调用方法
	 * @param obj
	 * @param method
	 * @param args
	 * @return
	 */
	public static Object invokeMethod(Object obj, String method, Object... args) {
		if (obj == null)
			return null;
		Method[] ms = obj.getClass().getMethods();
		for (Method m : ms) {
			if (method.equals(m.getName())){
				Class[] paramTypes = m.getParameterTypes();
				Object[] invokeParams = new Object[0];
				List invokeParamList = new ArrayList();
				if (paramTypes != null) {
					for (int i = 0; i < paramTypes.length; i++) {
						invokeParamList.add(args[i]);
					}
					invokeParams = invokeParamList.toArray();
				}
				try {
					return m.invoke(obj, invokeParams);
				} catch (IllegalArgumentException e) {
					throw new RuntimeException(e);
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				}catch(InvocationTargetException re){
					Throwable c = re.getCause();
					if(c instanceof RuntimeException )
						throw (RuntimeException)c;
					else
						throw new RuntimeException(re);
				}
			}
		}
		return null;
	}
}

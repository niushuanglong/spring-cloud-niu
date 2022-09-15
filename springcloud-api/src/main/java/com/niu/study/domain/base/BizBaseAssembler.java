package com.niu.study.domain.base;

import com.niu.study.utils.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public abstract class BizBaseAssembler {
    protected IBeansFactoryService beansFactoryService=null;
    private Map<String,Object> beans=new HashMap<String,Object>();//本次Assembler 用到的 spring 管理对象  缓存
    public BizBaseAssembler(IBeansFactoryService beansFactoryService) {
        super();
        this.beansFactoryService = beansFactoryService;
    }
    
    //根据类型 获取spring管理对象获取
    protected <T> T getBeanByType(Class<T> type) {
        String beanName=type.getName();
        T bean=(T)beans.get(beanName);
        if(bean==null){
            bean=beansFactoryService.getBeanOfType(type);
            beans.put(beanName, bean);
        }
        return bean;
    }
    //根据类型 获取spring管理对象获取
    protected <T> T getBeanOfBeanNameAndType(String beanName, Class<T> type) {
        T bean=(T)beans.get(beanName);
        if(bean==null){
            bean=beansFactoryService.getBeanOfBeanNameAndType(beanName, type);
            beans.put(beanName, bean);
        }
        return bean;
    }
    
    private Map<String,Boolean> needLoadKeys=null;//需要加载的 keys
	//增加 需要加载的 keys
	public void addNeedLoadKeys(String keys) {
		this.addNeedLoadKeys(Utils.splitString(keys,","));
	}
	public void addNeedLoadKeys(List<String> keys) {
		if(needLoadKeys==null) needLoadKeys=new HashMap<>();
		for (String key : keys) {
			needLoadKeys.put(key, true);
		}
	}
	private Map<String,Boolean> ignoreLoadKeys=null;//忽略加载的 keys
	//增加 忽略加载的 keys
	public void addIgnoreLoadKeys(String keys) {
		this.addNeedLoadKeys(Utils.splitString(keys,","));
	}
	public void addIgnoreLoadKeys(List<String> keys) {
		if(ignoreLoadKeys==null) ignoreLoadKeys=new HashMap<>();
		for (String key : keys) {
			ignoreLoadKeys.put(key, true);
		}
	}
	//判断是否加载指定key数据
	protected boolean isLoadAppointKeyDatas(String key) {
    	boolean load=true;
    	if(needLoadKeys!=null&&needLoadKeys.get(key)==null) {//无需加载
        	load=false;
        }
        if(ignoreLoadKeys!=null&&ignoreLoadKeys.get(key)!=null) {//忽略加载
        	load=false;
        }
        return load;
    }
    
}

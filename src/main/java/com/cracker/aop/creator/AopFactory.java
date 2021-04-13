package com.cracker.aop.creator;


import com.cracker.aop.advisor.Advisor;
import com.cracker.aop.creator.impl.GenericAopFactory;
import com.cracker.aop.proxy.AopProxy;
import com.cracker.core.factory.BeanFactory;

import java.util.List;

public interface AopFactory {

    AopProxy createAopProxyInstance(Object target, List<Advisor> advisor, BeanFactory beanFactory, String beanName);

    /**
     * 用于判断使用哪种代理方式来完成增强功能
     * 简单判断：类实现了接口就用JDK代理 没实现接口就用cglib代理
     * @param target
     * @return
     */
    default boolean judgeUseWhichProxyMode(Object target){
        Class<?>[] interfaces = target.getClass().getInterfaces();
        return interfaces.length>0;
    }

    //不通过创建对象 直接调用
    static AopFactory createProxyInstance(){
        return new GenericAopFactory();
    }
}

package com.cracker.aop.creator.impl;


import com.cracker.aop.advisor.Advisor;
import com.cracker.aop.creator.AopFactory;
import com.cracker.aop.proxy.AopProxy;
import com.cracker.aop.proxy.impl.CglibDynamicProxy;
import com.cracker.aop.proxy.impl.JDKDynamicProxy;
import com.cracker.core.factory.BeanFactory;

import java.util.List;

public class GenericAopFactory implements AopFactory {

    @Override
    public AopProxy createAopProxyInstance(Object target, List<Advisor> advisor, BeanFactory beanFactory, String beanName) {
        boolean res = judgeUseWhichProxyMode(target);
        if(res){
            //JDK
            return new JDKDynamicProxy(target, advisor, beanFactory);
        }else {
            //cglib
            return new CglibDynamicProxy(target, advisor, beanFactory, beanName);
        }
    }

}

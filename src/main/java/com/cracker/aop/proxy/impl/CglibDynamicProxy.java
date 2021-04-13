package com.cracker.aop.proxy.impl;


import com.cracker.aop.AopUtils;
import com.cracker.aop.advisor.Advisor;
import com.cracker.aop.proxy.AopProxy;
import com.cracker.core.beandefinition.BeanDefinition;
import com.cracker.core.factory.BeanFactory;
import com.cracker.core.factory.DefaultBeanFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class CglibDynamicProxy implements MethodInterceptor,AopProxy {

    private static final Log logger = LogFactory.getLog(JDKDynamicProxy.class);
    //加强器 用于生成代理对象
    private Enhancer enhancer = new Enhancer();
    private Object target;
    private List<Advisor> advisors;
    private BeanFactory beanFactory;
    private String beanName;

    public CglibDynamicProxy(Object target, List<Advisor> advisors, BeanFactory beanFactory, String beanName) {
        this.target = target;
        this.advisors = advisors;
        this.beanFactory = beanFactory;
        this.beanName = beanName;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Exception {
        return AopUtils.applyAdvice(target, o, advisors, objects, method, beanFactory);
    }

    @Override
    public Object getProxy() {
        return getProxy(target.getClass().getClassLoader());
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        if (logger.isDebugEnabled()) {
            logger.debug("为" + target + "创建cglib代理");
        }
        enhancer.setSuperclass(target.getClass());
        enhancer.setClassLoader(classLoader);
        enhancer.setInterfaces(target.getClass().getInterfaces());
        enhancer.setCallback(this);
        Constructor<?>[] constructors = target.getClass().getConstructors();
        Object res = null;
        if(constructors.length > 0){
            BeanDefinition  bd = ((DefaultBeanFactory) beanFactory).getBeanDefinition(beanName);
            if(bd.getConstructor() != null){
                return enhancer.create(bd.getConstructor().getParameterTypes(), bd.getConstructorArgument().toArray());
            }else {
                return enhancer.create();
            }
        }else {
            return enhancer.create();
        }
    }
}

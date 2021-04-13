package com.cracker.core.factory;

import com.cracker.core.postprocessor.AopPostProcessor;

import java.util.Map;

/**
 * @Author Cracker
 */
public interface BeanFactory {

    Object getBean(String beanName) throws Exception;

    void registerBeanPostProcessor(AopPostProcessor processor);

}

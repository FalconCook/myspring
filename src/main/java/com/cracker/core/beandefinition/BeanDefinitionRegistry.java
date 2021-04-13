package com.cracker.core.beandefinition;

/**
 * @Author Cracker
 */
public interface BeanDefinitionRegistry {
    /**
     * 注册Bean
     * @param beanName
     * @param beanDefinition
     * @throws Exception
     */
    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws Exception;

    /**
     * 获取Bean
     * @param beanName
     * @return
     */
    BeanDefinition getBeanDefinition(String beanName);

    /**
     * 判断Bean是否已经被注册
     * @param beanName
     * @return
     */
    Boolean containsBeanDefinition(String beanName);
}

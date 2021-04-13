package com.cracker.core.beandefinition;

import com.cracker.core.PropertyValue;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @Author Cracker
 */
public interface BeanDefinition {
    String SCOPE_SINGLETON = "singleton";
    String SCOPE_PROTOTYPE = "prototype";

    /**
     * 获取bean的字节码对象
     * @return
     */
    Class<?> getBeanClass();

    /**
     * 获取bean的类型
     * @return
     */
    String getScope();

    boolean isSingleton();

    boolean isPrototype();

    /**
     * 获取创建bean的工厂名
     * @return
     */
    String getFactoryBeanName();

    //获取工厂方法名
    String getFactoryMethodName();

    String getBeanInitMethodName();

    String getBeanDestroyMethodName();

    /**
     * 构造方法传入的参数
     * @return
     */
    List<?> getConstructorArgument();

    //缓存相关方法
    Constructor<?> getConstructor();
    void setConstructor(Constructor<?> constructor);
    Method getFactoryMethod();
    void setFactoryMethod(Method method);

    List<PropertyValue> getPropertyValues();

    /**
     * 校验传入的bean定义是否正确
     * tips:java8开始就可以直接写接口默认方法了
     * @return
     */
    default boolean validate() {
        //Class<?>没指定,FactoryBean或工厂FactoryBeanMethod不指定皆为不合法情况
        if (this.getBeanClass() == null) {
            if (StringUtils.isBlank(getFactoryBeanName()) || StringUtils.isBlank(getFactoryMethodName())) {
                return false;
            }
        }

        //Class<?>和FactoryBean同时存在
        if (this.getBeanClass() != null && StringUtils.isNotBlank(getFactoryBeanName())) {
            return false;
        }
        return true;
    }
}
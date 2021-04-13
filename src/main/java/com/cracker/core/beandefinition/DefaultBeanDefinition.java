package com.cracker.core.beandefinition;

import com.cracker.core.PropertyValue;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @Author Cracker
 */
public class DefaultBeanDefinition implements BeanDefinition{
    private Class<?> beanClass;
    private String scope = BeanDefinition.SCOPE_SINGLETON;
    private String factoryBeanName;
    private String factoryMethodName;
    private String beanInitMethodName;
    private String beanDestroyMethodName;
    private List<?> constructorArgument;
    private Constructor<?> constructor;
    private Method factoryMethod;
    private List<PropertyValue> propertyValues;
    private boolean singleton;

    @Override
    public Class<?> getBeanClass() {
        return this.beanClass;
    }

    public void setBeanClass(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    public String getScope() {
        return this.scope;
    }

    public void setScope(String scope) {
        if (StringUtils.isNotBlank(scope)) {
            this.scope = scope;
        }
    }


    @Override
    public boolean isPrototype() {
        return BeanDefinition.SCOPE_PROTOTYPE.equals(this.scope);
    }

    @Override
    public String getFactoryBeanName() {
        return factoryBeanName;
    }

    public void setFactoryBeanName(String factoryBeanName) {
        this.factoryBeanName = factoryBeanName;
    }

    @Override
    public String getFactoryMethodName() {
        return factoryMethodName;
    }

    public void setFactoryMethodName(String factoryMethodName) {
        this.factoryMethodName = factoryMethodName;
    }

    @Override
    public String getBeanInitMethodName() {
        return beanInitMethodName;
    }

    public void setBeanInitMethodName(String beanInitMethodName) {
        this.beanInitMethodName = beanInitMethodName;
    }

    @Override
    public String getBeanDestroyMethodName() {
        return beanDestroyMethodName;
    }

    public void setBeanDestroyMethodName(String beanDestroyMethodName) {
        this.beanDestroyMethodName = beanDestroyMethodName;
    }

    @Override
    public List<?> getConstructorArgument() {
        return constructorArgument;
    }

    public void setConstructorArgument(List<?> constructorArgument) {
        this.constructorArgument = constructorArgument;
    }

    public Constructor<?> getConstructor() {
        return constructor;
    }

    public void setConstructor(Constructor<?> constructor) {
        this.constructor = constructor;
    }

    public Method getFactoryMethod() {
        return factoryMethod;
    }

    public void setFactoryMethod(Method factoryMethod) {
        this.factoryMethod = factoryMethod;
    }

    public List<PropertyValue> getPropertyValues() {
        return propertyValues;
    }

    public void setPropertyValues(List<PropertyValue> propertyValues) {
        this.propertyValues = propertyValues;
    }

    @Override
    public boolean isSingleton() {
        return singleton;
    }

    public void setSingleton(boolean singleton) {
        this.singleton = singleton;
    }
}

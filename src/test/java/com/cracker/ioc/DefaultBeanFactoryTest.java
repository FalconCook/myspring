package com.cracker.ioc;

import com.cracker.core.beandefinition.BeanDefinition;
import com.cracker.core.beandefinition.DefaultBeanDefinition;
import com.cracker.core.factory.DefaultBeanFactory;
import org.junit.AfterClass;
import org.junit.Test;

/**
 * @Author Cracker
 */
public class DefaultBeanFactoryTest {
    static DefaultBeanFactory defaultBeanFactory = new DefaultBeanFactory();

    @Test
    public void testRegist() throws Exception{
        DefaultBeanDefinition defaultBeanDefinition = new DefaultBeanDefinition();
        defaultBeanDefinition.setBeanClass(Bean1.class);
        defaultBeanDefinition.setScope(BeanDefinition.SCOPE_PROTOTYPE);
        defaultBeanDefinition.setBeanInitMethodName("init");
        defaultBeanDefinition.setBeanDestroyMethodName("destroy");

        defaultBeanFactory.registerBeanDefinition("bean1", defaultBeanDefinition);
    }

    @Test
    public void testRegistStaticFactoryMethod() throws Exception{
        DefaultBeanDefinition defaultBeanDefinition = new DefaultBeanDefinition();
        defaultBeanDefinition.setBeanClass(Bean1Factory.class);
        defaultBeanDefinition.setFactoryMethodName("getBean1");
        defaultBeanFactory.registerBeanDefinition("staticBean1",defaultBeanDefinition);
    }

    @Test
    public void testRegistFactoryMethod() throws Exception{
        DefaultBeanDefinition defaultBeanDefinition = new DefaultBeanDefinition();
        defaultBeanDefinition.setBeanClass(Bean1Factory.class);
        String factoryBeanName = "factory";
        defaultBeanFactory.registerBeanDefinition(factoryBeanName,defaultBeanDefinition);

        defaultBeanDefinition = new DefaultBeanDefinition();
        defaultBeanDefinition.setFactoryBeanName(factoryBeanName);
        defaultBeanDefinition.setFactoryMethodName("getOtherBean1");
        defaultBeanDefinition.setScope(BeanDefinition.SCOPE_PROTOTYPE);
        defaultBeanFactory.registerBeanDefinition("factoryBean",defaultBeanDefinition);
    }

    @AfterClass
    public static void testGetBean() throws Exception{
        System.out.println("构造方法方式···");
        for (int i = 0;i<3;i++){
            Bean1 bean1 = (Bean1) defaultBeanFactory.getBean("bean1");
            bean1.doSomething();
        }

        System.out.println("静态工厂方法方式···");
        for (int i = 0;i<3;i++){
            Bean1 bean1 = (Bean1) defaultBeanFactory.getBean("staticBean1");
            bean1.doSomething();
        }

        System.out.println("工厂方法方式···");
        for (int i = 0;i<3;i++){
            Bean1 bean1 = (Bean1) defaultBeanFactory.getBean("factoryBean");
            bean1.doSomething();
        }

        defaultBeanFactory.close();
    }
}

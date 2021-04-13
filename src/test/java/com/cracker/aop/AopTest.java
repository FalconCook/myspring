package com.cracker.aop;

import com.cracker.aop.advice.MyAroundAdvice;
import com.cracker.aop.advisor.impl.RegexMatchAdvisor;
import com.cracker.aop.creator.impl.AopProxyCreator;
import com.cracker.aop.pointcut.impl.RegexExpressionPointCutResolver;
import com.cracker.core.beandefinition.DefaultBeanDefinition;
import com.cracker.core.factory.DefaultBeanFactory;
import org.junit.Test;

public class AopTest {
    static DefaultBeanFactory factory = new DefaultBeanFactory();

    @Test
    public void test() throws Exception {
        DefaultBeanDefinition bd = new DefaultBeanDefinition();
        bd.setBeanClass(User.class);
        bd.setSingleton(true);
        factory.registerBeanDefinition( "user", bd);

        bd = new DefaultBeanDefinition();
        bd.setBeanClass(MyAroundAdvice.class);
        factory.registerBeanDefinition( "MyAroundAdvice", bd);

        AopProxyCreator aapc = new AopProxyCreator();
        aapc.setBeanFactory(factory);
        factory.registerBeanPostProcessor(aapc);
        // 向AdvisorAutoProxyCreator注册Advisor
        aapc.register(new RegexMatchAdvisor("MyAroundAdvice", "execution(* com.cracker.aop.User.*())", new RegexExpressionPointCutResolver()));

        User user = (User) factory.doGetBean("user");
        user.sayHello();
    }
}

package com.cracker.aop;

import com.cracker.aop.advice.Advice;
import com.cracker.aop.advisor.Advisor;
import com.cracker.aop.advisor.PointCutAdvisor;
import com.cracker.aop.chain.AopAdviceChain;
import com.cracker.core.factory.BeanFactory;
import org.apache.commons.collections4.CollectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class AopUtils {

    public static Object applyAdvice(Object target, Object proxy, List<Advisor> advisors, Object[] args, Method method, BeanFactory beanFactory) throws Exception {
        List<Advice> advices = getMatchMethodAdvice(method,target.getClass(), advisors, beanFactory);
        if(CollectionUtils.isEmpty(advices)){
            //如果没有匹配的增强器 就直接执行方法
            return method.invoke(target,args);
        }else {
            //存在着增强该方法的增强器
            return new AopAdviceChain(method, target, args, proxy, advices).invoke();
        }
    }

    /**
     * 获取与方法匹配的advice
     * @param method
     * @param aClass
     * @param advisors
     * @param beanFactory
     * @return 通知列表
     */
    public static List<Advice> getMatchMethodAdvice(Method method, Class<?> aClass, List<Advisor> advisors, BeanFactory beanFactory) throws Exception {
        if(CollectionUtils.isEmpty(advisors)){
            return null;
        }
        List<Advice> advices = new ArrayList<>();
        for(Advisor advisor:advisors){
            if(advisor instanceof PointCutAdvisor){
                PointCutAdvisor pointCutAdvisor = (PointCutAdvisor) advisor;
                boolean res = pointCutAdvisor.getPointCutResolver().matchMethod(aClass, method, advisor.getExpression());
                if(res){
                       advices.add((Advice) beanFactory.getBean(advisor.getAdviceBeanName()));
                }
            }
        }
        return advices;
    }
}

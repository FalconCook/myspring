package com.cracker.aop.proxy;

import com.cracker.aop.advisor.Advisor;
import com.cracker.aop.advisor.PointCutAdvisor;

import java.util.ArrayList;
import java.util.List;

/**
 * 代理提供接口
 */
public interface AopProxy {

    Object getProxy();

    Object getProxy(ClassLoader classLoader);

    /**
     * 获取所有匹配的通知者(advisor)
     * @param clazz
     * @return
     */
    default List<Advisor> getMatchAdvisors(Class clazz, List<Advisor> advisors) throws Exception {
        List<Advisor> match = new ArrayList<>();
        for(Advisor advisor:advisors){
            if(advisor instanceof PointCutAdvisor){
                PointCutAdvisor pointCutAdvisor = (PointCutAdvisor) advisor;
                if(pointCutAdvisor.getPointCutResolver().matchClass(clazz, pointCutAdvisor.getExpression())){
                    //匹配到了
                    match.add(advisor);
                }
            }
        }
        return match;
    }

}

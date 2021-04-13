package com.cracker.aop.advisor;


import com.cracker.aop.pointcut.PointCut;

public interface PointCutAdvisor extends Advisor{
    PointCut getPointCutResolver();
}

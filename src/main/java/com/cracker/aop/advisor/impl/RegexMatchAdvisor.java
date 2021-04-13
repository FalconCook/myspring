package com.cracker.aop.advisor.impl;

import com.cracker.aop.advisor.PointCutAdvisor;
import com.cracker.aop.pointcut.PointCut;

public class RegexMatchAdvisor implements PointCutAdvisor {

    private String adviceName;
    private String expression;
    private PointCut pointCut;

    public RegexMatchAdvisor(String adviceName, String expression, PointCut pointCut) {
        this.adviceName = adviceName;
        this.expression = expression;
        this.pointCut = pointCut;
    }

    @Override
    public PointCut getPointCutResolver() {
        return this.pointCut;
    }

    @Override
    public String getAdviceBeanName() {
        return this.adviceName;
    }

    @Override
    public String getExpression() {
        return this.expression;
    }
}

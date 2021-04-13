package com.cracker.aop.advisor;

import com.cracker.aop.advisor.Advisor;

import java.util.List;

public interface AdvisorRegistry {
    void register(Advisor advisor);
    List<Advisor> getAdvisor();
}

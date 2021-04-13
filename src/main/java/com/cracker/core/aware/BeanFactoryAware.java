package com.cracker.core.aware;

import com.cracker.core.factory.BeanFactory;

public interface BeanFactoryAware extends Aware{

    void setBeanFactory(BeanFactory beanFactory);
}

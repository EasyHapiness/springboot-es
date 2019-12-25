package com.song.es.es.strategy.manager.impl;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Desc
 * @Author
 * @Date 2019/12/25
 */
@Component
public class ApplicationContextHelper implements ApplicationContextAware {

    ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public <T> T getBean(Class<T> tClass){
        return applicationContext.getBean(tClass);
    }

    public <T> Map<String,T> getBeansOfType(Class<T> clazz){
        return applicationContext.getBeansOfType(clazz);
    }
}

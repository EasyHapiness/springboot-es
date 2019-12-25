package com.song.es.es.strategy.manager.impl;

import com.song.es.es.strategy.manager.StrategyHandler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Desc
 * @Author
 * @Date 2019/12/25
 */
@Component
public class StatementClient<T> implements InitializingBean {

    @Autowired
    private ApplicationContextHelper applicationContextHelper;

    private Map<String,StrategyHandler> handlerMap = new ConcurrentHashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {

        Map<String, StrategyHandler> beansOfType = applicationContextHelper.getBeansOfType(StrategyHandler.class);
        for (Map.Entry<String,StrategyHandler> strategyHandler : beansOfType.entrySet()){
            handlerMap.put(strategyHandler.getValue().getType().getType(),strategyHandler.getValue());
        }
    }

    public void doHandler(String tag,T t){
        handlerMap.get(tag).process(t);
    }
}

package com.song.es.es.strategy.manager.impl;

import com.song.es.es.strategy.manager.StrategyHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Desc 可以通过下面方式获取通过 @Component注入spring的bean
 * @Author
 * @Date 2019/12/25
 */
@Component
public class SecTestClient<T> {

    private final Map<String,StrategyHandler> handlerMap = new ConcurrentHashMap<>();

    @Autowired
    public  SecTestClient(Map<String,StrategyHandler> map){
        map.forEach((k,v)->{

            handlerMap.put(k,v);
        });
    }

}

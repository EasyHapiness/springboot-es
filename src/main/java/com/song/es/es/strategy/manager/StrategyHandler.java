package com.song.es.es.strategy.manager;

import com.song.es.es.strategy.enums.TypeEnums;

/**
 * 策略类接口
 * @param <T>
 */
public interface StrategyHandler<T> {

    void process(T t);

    TypeEnums getType();

}

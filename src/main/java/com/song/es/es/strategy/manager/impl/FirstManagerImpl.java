package com.song.es.es.strategy.manager.impl;

import com.song.es.es.strategy.entity.Student;
import com.song.es.es.strategy.enums.TypeEnums;
import com.song.es.es.strategy.manager.StrategyHandler;
import org.springframework.stereotype.Component;

/**
 * @Desc
 * @Author
 * @Date 2019/12/25
 */
@Component
public class FirstManagerImpl implements StrategyHandler<Student> {

    @Override
    public void process(Student student) {

        System.out.println("这是一类First学生");
    }

    @Override
    public TypeEnums getType() {
        return TypeEnums.FIRST;
    }
}

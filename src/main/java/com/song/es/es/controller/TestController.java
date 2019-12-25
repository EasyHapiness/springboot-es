package com.song.es.es.controller;

import com.song.es.es.entity.ColorModeBean;
import com.song.es.es.strategy.entity.Student;
import com.song.es.es.strategy.enums.TypeEnums;
import com.song.es.es.strategy.manager.impl.StatementClient;
import com.song.es.es.strategy.manager.impl.TestClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Desc
 * @Author
 * @Date 2019/12/25
 */
@RestController
public class TestController {

    @RequestMapping("/http/test/{name}")
    public String httpTest(@PathVariable("name") String name){

        return "this is httpTest" + name;
    }


    @RequestMapping("/http/test/ob/")
    public String httpTest(ColorModeBean bean){

        return "this is httpTest  " + bean.getColorName();
    }


    @PostMapping(value = "/http/test/json")
    public String httpTestJson(@RequestBody ColorModeBean bean){

        return "this is httpTest  " + bean.getColorName();
    }

    /**
     * 两种注入bean的管理方式
     */
    @Resource
    StatementClient statementClient;

    @Resource
    TestClient testClient;


    @GetMapping("/sTest")
    public void getStrategy(){

        Student first = Student.builder().name("first").type("1").build();
        Student second = Student.builder().name("second").type("2").build();

        statementClient.doHandler(TypeEnums.FIRST.getType(),first);
        statementClient.doHandler(TypeEnums.SECOND.getType(),second);

        testClient.doHandler(TypeEnums.FIRST.getType(),first);
        testClient.doHandler(TypeEnums.SECOND.getType(),second);

    }
}

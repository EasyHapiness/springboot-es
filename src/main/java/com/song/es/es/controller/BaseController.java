package com.song.es.es.controller;

import com.alibaba.fastjson.JSONObject;

/**
 * @Desc
 * @Author
 * @Date 2019/8/16
 */
public class BaseController {

    private static String code = "200";

    private static String msg ="操作成功";

    private static String failCode = "201";

    public JSONObject getSuccessResult(String msg,Object object){

        JSONObject json = new JSONObject();
        json.put("code",code);
        json.put("msg",msg);
        json.put("data",object);
        return json;
    }

    public JSONObject getSuccessResult(Object object){

        JSONObject json = new JSONObject();
        json.put("code",this.code);
        json.put("msg",this.msg);
        json.put("data",object);
        return json;
    }


    public JSONObject getSuccessResult(){

        JSONObject json = new JSONObject();
        json.put("code",code);
        json.put("msg",msg);
        return json;
    }

    public JSONObject getFailResult(){

        JSONObject json = new JSONObject();
        json.put("code",failCode);
        json.put("msg","请求失败");
        return json;
    }
}

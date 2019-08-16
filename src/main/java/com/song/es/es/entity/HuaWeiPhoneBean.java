package com.song.es.es.entity;

import lombok.Data;

import java.util.List;

/**
 * @Desc
 * @Author
 * @Date 2019/8/16
 */
@Data
public class HuaWeiPhoneBean extends PhoneModel{

    public HuaWeiPhoneBean() {
        super();
    }

    private String productName;

    private List<ColorModeBean> colorsItemMode;

    private List<String> sellingPoints;

}

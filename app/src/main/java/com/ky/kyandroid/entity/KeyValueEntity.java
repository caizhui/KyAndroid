package com.ky.kyandroid.entity;

/**
 * Created by Caizhui on 2017/6/10.
 * 获取控件键值对
 */

public class KeyValueEntity {

    private String code;
    private String value;

    public KeyValueEntity(String code, String value) {
        this.code = code;
        this.value = value;
    }

    @Override
    public String toString() {
        // 为什么要重写toString()呢？因为适配器在显示数据的时候，如果传入适配器的对象不是字符串的情况下，直接就使用对象.toString()  
        return value;
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}

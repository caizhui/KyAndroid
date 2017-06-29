package com.ky.kyandroid.entity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Caizhui on 2017/6/10.
 * 获取控件键值对
 */
@Table(name = "t_desc")
public class DescEntity {

    @Column(name = "id",isId = true,autoGen = false)
    private  String id;
    @Column(name = "code")
    private String code;
    @Column(name = "value")
    private String value;
    @Column(name = "type")
    private String type;
    @Column(name = "parentId")
    private String parentId;

    public DescEntity(){};

    public DescEntity(String code, String value) {
        this.code = code;
        this.value = value;
    }

    @Override
    public String toString() {
        // 为什么要重写toString()呢？因为适配器在显示数据的时候，如果传入适配器的对象不是字符串的情况下，直接就使用对象.toString()  
        return value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}

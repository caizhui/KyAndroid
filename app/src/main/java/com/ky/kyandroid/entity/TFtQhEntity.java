package com.ky.kyandroid.entity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Caizhui on 2017/6/10.
 * 获取控件键值对
 */
@Table(name = "t_ft_qh")
public class TFtQhEntity {

    @Column(name = "uuid", isId = true, autoGen = true)
    private int uuid;
    @Column(name = "id")
    private String id;
    @Column(name = "jddm")
    private String jddm;
    @Column(name = "jdmc")
    private String jdmc;
    @Column(name = "jwh")
    private String jwh;
    @Column(name = "sqgzz")
    private String sqgzz;
    @Column(name = "xq")
    private String xq;

    public TFtQhEntity() {
    }


    @Override
    public String toString() {
        // 为什么要重写toString()呢？因为适配器在显示数据的时候，如果传入适配器的对象不是字符串的情况下，直接就使用对象.toString()  
        return "";
    }

    public int getUuid() {
        return uuid;
    }

    public void setUuid(int uuid) {
        this.uuid = uuid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJddm() {
        return jddm;
    }

    public void setJddm(String jddm) {
        this.jddm = jddm;
    }

    public String getJdmc() {
        return jdmc;
    }

    public void setJdmc(String jdmc) {
        this.jdmc = jdmc;
    }

    public String getJwh() {
        return jwh;
    }

    public void setJwh(String jwh) {
        this.jwh = jwh;
    }

    public String getSqgzz() {
        return sqgzz;
    }

    public void setSqgzz(String sqgzz) {
        this.sqgzz = sqgzz;
    }

    public String getXq() {
        return xq;
    }

    public void setXq(String xq) {
        this.xq = xq;
    }
}

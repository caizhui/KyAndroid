package com.ky.kyandroid.entity;

import java.util.List;

/**
 * Created by Caizhui on 2017/6/18.
 */

public class TFtSjDetailEntity {

    /**
     * 获取事件附件 - 子表信息
     */
    List<TFtSjFjEntity> sjfjList ;
    /**
     * 人员具体信息
     */
    List<TFtSjRyEntity> sjryList;

    /**
     * 事件日志信息
     */
    List<TFtSjLogEntity> sjlogList;

    /**
     * 事件关联信息
     */
    List<TFtSjGlsjEntity> glsjList;

    public List<TFtSjFjEntity> getSjfjList() {
        return sjfjList;
    }

    public void setSjfjList(List<TFtSjFjEntity> sjfjList) {
        this.sjfjList = sjfjList;
    }

    public List<TFtSjRyEntity> getSjryList() {
        return sjryList;
    }

    public void setSjryList(List<TFtSjRyEntity> sjryList) {
        this.sjryList = sjryList;
    }

    public List<TFtSjLogEntity> getSjlogList() {
        return sjlogList;
    }

    public void setSjlogList(List<TFtSjLogEntity> sjlogList) {
        this.sjlogList = sjlogList;
    }

    public List<TFtSjGlsjEntity> getGlsjList() {
        return glsjList;
    }

    public void setGlsjList(List<TFtSjGlsjEntity> glsjList) {
        this.glsjList = glsjList;
    }
}

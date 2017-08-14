package com.ky.kyandroid.entity;

import java.util.List;
import java.util.Map;

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

    /**
     * 事件关联Map信息
     */
    Map<String,TFtSjEntity> glsjListMap;

    /**
     * 事件跟踪
     */
    List<String>  progressList;
    /**
     * 事件实体
     */
    TFtSjEntity ftSj;

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

    public Map<String, TFtSjEntity> getGlsjListMap() {
        return glsjListMap;
    }

    public void setGlsjListMap(Map<String, TFtSjEntity> glsjListMap) {
        this.glsjListMap = glsjListMap;
    }

    public TFtSjEntity getFtSj() {
        return ftSj;
    }

    public void setFtSj(TFtSjEntity ftSj) {
        this.ftSj = ftSj;
    }

    public List<String> getProgressList() {
        return progressList;
    }

    public void setProgressList(List<String> progressList) {
        this.progressList = progressList;
    }
}

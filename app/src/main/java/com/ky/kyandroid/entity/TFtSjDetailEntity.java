package com.ky.kyandroid.entity;

import java.util.List;

/**
 * Created by Caizhui on 2017/6/18.
 */

public class TFtSjDetailEntity {

    // 获取事件附件 - 子表信息
    List<TFtSjFjEntity> sjfjList ;
    // 人员具体信息
    List<TFtSjRyEntity> sjryList;

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
}

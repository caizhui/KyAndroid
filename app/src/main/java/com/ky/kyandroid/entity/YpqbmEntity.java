package com.ky.kyandroid.entity;

import java.io.Serializable;

/**
 * Created by Caizhui on 2017/6/30.
 * 已派遣部门实体
 */

public class YpqbmEntity implements Serializable{


    /**
     * sjId : 5948d1ad3cd19d121c1cce99
     * bmlx : 1
     * bmmc : 华强北职能部门1
     * clzt : 8
     * bm_id : 12
     * rwnr : 787878878787
     * pqr : hqb02
     * clsx : 2017-06-30 00:17:00
     * hashValue : -2147483648
     * id : 59552826f834682fe0f581a5
     * lastUpdateBy : hqb02
     * lastUpdateTime : 2017-06-30 00:37:42
     */

    private String sjId;
    private String bmlx;
    private String bmmc;
    private String clzt;
    private String bm_id;
    private String rwnr;
    private String pqr;
    private String clsx;
    private int hashValue;
    private String id;
    private String lastUpdateBy;
    private String lastUpdateTime;

    /**
     * 判断是否能删除
     */
    private String isDelete;

    public String getSjId() {
        return sjId;
    }

    public void setSjId(String sjId) {
        this.sjId = sjId;
    }

    public String getBmlx() {
        return bmlx;
    }

    public void setBmlx(String bmlx) {
        this.bmlx = bmlx;
    }

    public String getBmmc() {
        return bmmc;
    }

    public void setBmmc(String bmmc) {
        this.bmmc = bmmc;
    }

    public String getClzt() {
        return clzt;
    }

    public void setClzt(String clzt) {
        this.clzt = clzt;
    }

    public String getBm_id() {
        return bm_id;
    }

    public void setBm_id(String bm_id) {
        this.bm_id = bm_id;
    }

    public String getRwnr() {
        return rwnr;
    }

    public void setRwnr(String rwnr) {
        this.rwnr = rwnr;
    }

    public String getPqr() {
        return pqr;
    }

    public void setPqr(String pqr) {
        this.pqr = pqr;
    }

    public String getClsx() {
        return clsx;
    }

    public void setClsx(String clsx) {
        this.clsx = clsx;
    }

    public int getHashValue() {
        return hashValue;
    }

    public void setHashValue(int hashValue) {
        this.hashValue = hashValue;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }
}

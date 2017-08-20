package com.ky.kyandroid.entity;

import java.io.Serializable;

/**
 * Created by Caizhui on 2017/8/20.
 */

public class TFtAppReportEntity implements Serializable{

    /** long */
    private static final long serialVersionUID = 1L;
    /**
     * 创建人ID
     */
    private String repBy;
    /**
     * 上报给用户
     */
    private String repToUser;
    /**
     * 上报给部门
     */
    private String repToOrg;

    /**
     * 上报时间
     */
    private String repTime;
    /**
     * 状态
     */
    private String status;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getRepBy() {
        return repBy;
    }

    public void setRepBy(String repBy) {
        this.repBy = repBy;
    }

    public String getRepToUser() {
        return repToUser;
    }

    public void setRepToUser(String repToUser) {
        this.repToUser = repToUser;
    }

    public String getRepToOrg() {
        return repToOrg;
    }

    public void setRepToOrg(String repToOrg) {
        this.repToOrg = repToOrg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRepTime() {
        return repTime;
    }

    public void setRepTime(String repTime) {
        this.repTime = repTime;
    }
}

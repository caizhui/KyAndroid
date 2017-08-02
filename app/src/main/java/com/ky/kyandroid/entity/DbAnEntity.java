package com.ky.kyandroid.entity;

import java.io.Serializable;

/**
 * Created by Caizhui on 2017/8/2.
 * 督办按钮
 */

public class DbAnEntity implements Serializable {
    private static final long serialVersionUID = -7620435178023928252L;


    /**
     * action : SL
     * actionName : 受理
     * showName : 受理
     * nowStatus : 0
     * nextStatus : 2
     */

    private String action;
    private String actionname;
    private String showname;
    private String nowstatus;
    private String nextstatus;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getActionname() {
        return actionname;
    }

    public void setActionname(String actionname) {
        this.actionname = actionname;
    }

    public String getShowname() {
        return showname;
    }

    public void setShowname(String showname) {
        this.showname = showname;
    }

    public String getNowstatus() {
        return nowstatus;
    }

    public void setNowstatus(String nowstatus) {
        this.nowstatus = nowstatus;
    }

    public String getNextstatus() {
        return nextstatus;
    }

    public void setNextstatus(String nextstatus) {
        this.nextstatus = nextstatus;
    }
}

package com.ky.kyandroid.entity;

import com.ky.kyandroid.view.SlideView;

import java.io.Serializable;

/**
 * Created by msi on 2017/6/26.
 */
public class MsgNoticeEntity implements Serializable {
    /** long */
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 发送部门
     */
    private String fsbm;

    /**
     * 发送部门名称
     */
    private String fsbmmc;

    /**
     * 发送人
     */
    private String fsr;

    /**
     * 发送时间
     */
    private String fssj;

    /**
     * 接收人
     */
    private String jsr;

    /**
     * 内容
     */
    private String nr;

    /**
     * 打开时间
     */
    private String dksj;

    /**
     * 阅读时间
     */
    private String ydsj;

    /**
     * 接收人所属部门
     */
    private String jsbm;

    /**
     * 事件ID
     */
    private String sjid;

    /**
     * 事件名称/处理名称/受理名称
     */
    private String sjmc;

    /**
     * 消息类型：1，事件、2，督办
     */
    private String lx;
    /**
     * 消息细分类型
     */
    private String xflx;
    /**
     * 是否从列表中移除 1、移除
     */
    private String clbyc;

    /**
     * 后续加上受理结果的一系列操作
     * */

    /** SlideView */
    public transient SlideView slideView;

    public String getFsbm() {
        return fsbm;
    }

    public void setFsbm(String fsbm) {
        this.fsbm = fsbm;
    }

    public String getFsr() {
        return fsr;
    }

    public void setFsr(String fsr) {
        this.fsr = fsr;
    }

    public String getFssj() {
        return fssj;
    }

    public void setFssj(String fssj) {
        this.fssj = fssj;
    }

    public String getJsr() {
        return jsr;
    }

    public void setJsr(String jsr) {
        this.jsr = jsr;
    }

    public String getNr() {
        return nr;
    }

    public void setNr(String nr) {
        this.nr = nr;
    }

    public String getDksj() {
        return dksj;
    }

    public void setDksj(String dksj) {
        this.dksj = dksj;
    }

    public String getYdsj() {
        return ydsj;
    }

    public void setYdsj(String ydsj) {
        this.ydsj = ydsj;
    }

    public String getJsbm() {
        return jsbm;
    }

    public void setJsbm(String jsbm) {
        this.jsbm = jsbm;
    }

    public String getSjid() {
        return sjid;
    }

    public void setSjid(String sjid) {
        this.sjid = sjid;
    }

    public String getLx() {
        return lx;
    }

    public void setLx(String lx) {
        this.lx = lx;
    }

    public String getXflx() {
        return xflx;
    }

    public void setXflx(String xflx) {
        this.xflx = xflx;
    }

    public String getClbyc() {
        return clbyc;
    }

    public void setClbyc(String clbyc) {
        this.clbyc = clbyc;
    }

    public SlideView getSlideView() {
        return slideView;
    }

    public void setSlideView(SlideView slideView) {
        this.slideView = slideView;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFsbmmc() {
        return fsbmmc;
    }

    public void setFsbmmc(String fsbmmc) {
        this.fsbmmc = fsbmmc;
    }

    public String getSjmc() {
        return sjmc;
    }

    public void setSjmc(String sjmc) {
        this.sjmc = sjmc;
    }
}

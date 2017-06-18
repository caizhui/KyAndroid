package com.ky.kyandroid.entity;

import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by Caizhui on 2017/6/16.
 * 事件实体
 */

@Table(name =  "t_event")
public class EventEntity  implements Serializable {
    /**
     * SSSQ : 99
     * SFSQQT : CMOS
     * ZYSQ : 社区1
     * FSSJ : 2017-06-23T10:22:00
     * SSJWH : null
     * JWD : 114.092504,22.553015
     * isYj : false
     * BXXS : 5
     * SFSJ : 0
     * SFSW : 0
     * ID : 58ff66d64839fe38bcc20f97
     * SCLSX : null
     * LDPS : 对对对
     * SSJD : 华强北街道
     * CLSX : null
     * LRBM : 华强北街道办
     * ZT : 13
     * DCBM : 100
     * SFGACZ : 0
     * SJGYQK : 据CMOS诉，2017-06-23 10:22分在广东省深圳市福田区女人世界美食街76-a号港澳城，发生A事件事件。据悉该事件有1-5人参与，涉及：违章等领域，表现形式有：纠纷等，已有测试父节点等部门到场。
     * LRR : hqb01
     * SFSYQ : 0
     * XCTS : null
     * LRSJ : 2017-04-25T23:10:13
     * SJMC : A事件
     * isDb : true
     * GM : 1
     * FSDD : 广东省深圳市福田区女人世界美食街76-a号港澳城
     * SJLY : 7
     */
    private static final long serialVersionUID = -7620435178023928252L;


    private String id;

    private String  uuid;
    /**
     * 事件名称
     */
    private  String sjmc;

    /**
     * 发生时间
     */
    private  String fssj;

    /**
     * 发生地点
     */
    private  String fsdd;

    /**
     * 上访诉求群体
     */
    private  String sfsqqt;

    /**
     * 到场部门
     */
    private  String dcbm;

    /**
     * 涉及领域
     */
    private  String sjly;

    /**
     * 主要诉求
     */
    private  String zysq;

    /**
     * 事件概要情况
     */
    private  String sjgyqk;

    /**
     * 规模
     */
    private  String gm;

    /**
     * 表现形式
     */
    private  String bxxs;

    /**
     * 是否涉外
     */
    private  String sfsw;

    /**
     * 是否涉疆
     */
    private  String sfsj;

    /**
     * 是否涉舆情
     */
    private  String sfsyq;

    /**
     * 是否公安处置
     */
    private  String sfgacz;

    /**
     * 所属街道及社区
     */
    private String ssjd;

    /**
     * 领导批示
     */
    private String ldps;

    /**
     * 发生地经伟度
     */
    private String jwd;

    /**
     * 处理时限
     */
    private String clsx;

    /**
     * 录入人
     */
    private String lrr;

    /**
     * 录入部门
     */
    private String lrbm;

    /**
     * 录入时间
     */
    private String lrsj;

    /**
     * 事件状态
     */
    private String zt;
    /**
     *
     */
    private String sssq;//所属社区
    private String ssjwh;//所属居委会

    private String znbm;

    private String bxxsfy;

    private String clsjfy;

    private String xcts;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSjmc() {
        return sjmc;
    }

    public void setSjmc(String sjmc) {
        this.sjmc = sjmc;
    }

    public  String getFssj() {
        return fssj;
    }

    public void setFssj( String fssj) {
        this.fssj = fssj;
    }

    public String getFsdd() {
        return fsdd;
    }

    public void setFsdd(String fsdd) {
        this.fsdd = fsdd;
    }

    public String getSfsqqt() {
        return sfsqqt;
    }

    public void setSfsqqt(String sfsqqt) {
        this.sfsqqt = sfsqqt;
    }

    public String getDcbm() {
        return dcbm;
    }

    public void setDcbm(String dcbm) {
        this.dcbm = dcbm;
    }

    public String getSjly() {
        return sjly;
    }

    public void setSjly(String sjly) {
        this.sjly = sjly;
    }

    public String getZysq() {
        return zysq;
    }

    public void setZysq(String zysq) {
        this.zysq = zysq;
    }

    public String getSjgyqk() {
        return sjgyqk;
    }

    public void setSjgyqk(String sjgyqk) {
        this.sjgyqk = sjgyqk;
    }

    public String getGm() {
        return gm;
    }

    public void setGm(String gm) {
        this.gm = gm;
    }

    public String getBxxs() {
        return bxxs;
    }

    public void setBxxs(String bxxs) {
        this.bxxs = bxxs;
    }

    public String getSfsw() {
        return sfsw;
    }

    public void setSfsw(String sfsw) {
        this.sfsw = sfsw;
    }

    public String getSfsj() {
        return sfsj;
    }

    public void setSfsj(String sfsj) {
        this.sfsj = sfsj;
    }

    public String getSfsyq() {
        return sfsyq;
    }

    public void setSfsyq(String sfsyq) {
        this.sfsyq = sfsyq;
    }

    public String getSfgacz() {
        return sfgacz;
    }

    public void setSfgacz(String sfgacz) {
        this.sfgacz = sfgacz;
    }

    public String getSsjd() {
        return ssjd;
    }

    public void setSsjd(String ssjd) {
        this.ssjd = ssjd;
    }

    public String getLdps() {
        return ldps;
    }

    public void setLdps(String ldps) {
        this.ldps = ldps;
    }

    public String getJwd() {
        return jwd;
    }

    public void setJwd(String jwd) {
        this.jwd = jwd;
    }

    public String getClsx() {
        return clsx;
    }

    public void setClsx(String clsx) {
        this.clsx = clsx;
    }

    public String getLrr() {
        return lrr;
    }

    public void setLrr(String lrr) {
        this.lrr = lrr;
    }

    public String getLrbm() {
        return lrbm;
    }

    public void setLrbm(String lrbm) {
        this.lrbm = lrbm;
    }

    public String getLrsj() {
        return lrsj;
    }

    public void setLrsj(String lrsj) {
        this.lrsj = lrsj;
    }

    public String getZt() {
        return zt;
    }

    public void setZt(String zt) {
        this.zt = zt;
    }

    public String getSssq() {
        return sssq;
    }

    public void setSssq(String sssq) {
        this.sssq = sssq;
    }

    public String getSsjwh() {
        return ssjwh;
    }

    public void setSsjwh(String ssjwh) {
        this.ssjwh = ssjwh;
    }

    public String getZnbm() {
        return znbm;
    }

    public void setZnbm(String znbm) {
        this.znbm = znbm;
    }

    public String getBxxsfy() {
        return bxxsfy;
    }

    public void setBxxsfy(String bxxsfy) {
        this.bxxsfy = bxxsfy;
    }

    public String getClsjfy() {
        return clsjfy;
    }

    public void setClsjfy(String clsjfy) {
        this.clsjfy = clsjfy;
    }

    public String getXcts() {
        return xcts;
    }

    public void setXcts(String xcts) {
        this.xcts = xcts;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}



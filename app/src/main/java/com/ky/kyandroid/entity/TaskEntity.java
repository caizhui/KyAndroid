package com.ky.kyandroid.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Caizhui on 2017/6/21.
 */

public class TaskEntity implements Serializable{


    /**
     * rwnr : test
     * anlist : [{"action":"sjjdpq","actionname":"街道派遣","prevzt":"5,8,8.1,8.2,13","nextzt":"8","actionrole":"街道办事件处理人员","enabled":"1","name":"街道派遣","hashvalue":-2147483648,"id":"9"}]
     * sssq : 99
     * sfsqqt : cmos
     * zysq : 社区1
     * fssj : 2017-06-23 10:22:00
     * jwd : 114.092504,22.553015
     * isyj : true
     * bxxs : 5
     * sfsj : 0
     * sfsw : 0
     * id : 58ff66d64839fe38bcc20f97
     * sclsx : 2017-06-16 18:54:00
     * ldps : 对对对
     * ssjd : 华强北街道
     * lrbm : 华强北街道办
     * zt : 8.1
     * ztname : 职能部门接收
     * clid : 5942678afcbaf208101206b0
     * dcbm : 100
     * sfgacz : 0
     * cqcd : 3
     * sjgyqk : 据cmos诉，2017-06-23 10:22分在广东省深圳市福田区女人世界美食街76-a号港澳城，发生a事件事件。据悉该事件有1-5人参与，涉及：违章等领域，表现形式有：纠纷等，已有测试父节点等部门到场。
     * lrr : hqb01
     * sfsyq : 0
     * lrsj : 2017-04-25 23:10:13
     * sjmc : a事件
     * isdb : true
     * gm : 1
     * fsdd : 广东省深圳市福田区女人世界美食街76-a号港澳城
     * sjly : 7
     */

    private String rwnr;
    private String sssq;
    private String sfsqqt;
    private String zysq;
    private String fssj;
    private String jwd;
    private boolean isyj;
    private String bxxs;
    private String sfsj;
    private String sfsw;
    private String id;
    private String sclsx;
    private String ldps;
    private String ssjd;
    private String lrbm;
    private String zt;
    private String ztname;
    private String clid;
    private String dcbm;
    private String sfgacz;
    private String cqcd;
    private String sjgyqk;
    private String lrr;
    private String sfsyq;
    private String lrsj;
    private String sjmc;
    private boolean isdb;
    private String gm;
    private String fsdd;
    private String sjly;
    private String jsr;
    private  String jssj;
    private List<TFtZtlzEntity> anlist;

    public String getRwnr() {
        return rwnr;
    }

    public void setRwnr(String rwnr) {
        this.rwnr = rwnr;
    }

    public String getSssq() {
        return sssq;
    }

    public void setSssq(String sssq) {
        this.sssq = sssq;
    }

    public String getSfsqqt() {
        return sfsqqt;
    }

    public void setSfsqqt(String sfsqqt) {
        this.sfsqqt = sfsqqt;
    }

    public String getZysq() {
        return zysq;
    }

    public void setZysq(String zysq) {
        this.zysq = zysq;
    }

    public String getFssj() {
        return fssj;
    }

    public void setFssj(String fssj) {
        this.fssj = fssj;
    }

    public String getJwd() {
        return jwd;
    }

    public void setJwd(String jwd) {
        this.jwd = jwd;
    }

    public boolean isIsyj() {
        return isyj;
    }

    public void setIsyj(boolean isyj) {
        this.isyj = isyj;
    }

    public String getBxxs() {
        return bxxs;
    }

    public void setBxxs(String bxxs) {
        this.bxxs = bxxs;
    }

    public String getSfsj() {
        return sfsj;
    }

    public void setSfsj(String sfsj) {
        this.sfsj = sfsj;
    }

    public String getSfsw() {
        return sfsw;
    }

    public void setSfsw(String sfsw) {
        this.sfsw = sfsw;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSclsx() {
        return sclsx;
    }

    public void setSclsx(String sclsx) {
        this.sclsx = sclsx;
    }

    public String getLdps() {
        return ldps;
    }

    public void setLdps(String ldps) {
        this.ldps = ldps;
    }

    public String getSsjd() {
        return ssjd;
    }

    public void setSsjd(String ssjd) {
        this.ssjd = ssjd;
    }

    public String getLrbm() {
        return lrbm;
    }

    public void setLrbm(String lrbm) {
        this.lrbm = lrbm;
    }

    public String getZt() {
        return zt;
    }

    public void setZt(String zt) {
        this.zt = zt;
    }

    public String getZtname() {
        return ztname;
    }

    public void setZtname(String ztname) {
        this.ztname = ztname;
    }

    public String getClid() {
        return clid;
    }

    public void setClid(String clid) {
        this.clid = clid;
    }

    public String getDcbm() {
        return dcbm;
    }

    public void setDcbm(String dcbm) {
        this.dcbm = dcbm;
    }

    public String getSfgacz() {
        return sfgacz;
    }

    public void setSfgacz(String sfgacz) {
        this.sfgacz = sfgacz;
    }

    public String getCqcd() {
        return cqcd;
    }

    public void setCqcd(String cqcd) {
        this.cqcd = cqcd;
    }

    public String getSjgyqk() {
        return sjgyqk;
    }

    public void setSjgyqk(String sjgyqk) {
        this.sjgyqk = sjgyqk;
    }

    public String getLrr() {
        return lrr;
    }

    public void setLrr(String lrr) {
        this.lrr = lrr;
    }

    public String getSfsyq() {
        return sfsyq;
    }

    public void setSfsyq(String sfsyq) {
        this.sfsyq = sfsyq;
    }

    public String getLrsj() {
        return lrsj;
    }

    public void setLrsj(String lrsj) {
        this.lrsj = lrsj;
    }

    public String getSjmc() {
        return sjmc;
    }

    public void setSjmc(String sjmc) {
        this.sjmc = sjmc;
    }

    public boolean isIsdb() {
        return isdb;
    }

    public void setIsdb(boolean isdb) {
        this.isdb = isdb;
    }

    public String getGm() {
        return gm;
    }

    public void setGm(String gm) {
        this.gm = gm;
    }

    public String getFsdd() {
        return fsdd;
    }

    public void setFsdd(String fsdd) {
        this.fsdd = fsdd;
    }

    public String getSjly() {
        return sjly;
    }

    public void setSjly(String sjly) {
        this.sjly = sjly;
    }

    public List<TFtZtlzEntity> getAnlist() {
        return anlist;
    }

    public void setAnlist(List<TFtZtlzEntity> anlist) {
        this.anlist = anlist;
    }

    public String getJsr() {
        return jsr;
    }

    public void setJsr(String jsr) {
        this.jsr = jsr;
    }

    public String getJssj() {
        return jssj;
    }

    public void setJssj(String jssj) {
        this.jssj = jssj;
    }
}

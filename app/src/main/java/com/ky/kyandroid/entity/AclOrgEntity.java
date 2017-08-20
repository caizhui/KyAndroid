package com.ky.kyandroid.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Caizhui on 2017/8/20.
 */

public class AclOrgEntity {

    private String id;

    /**
     * ID
     */
//	private java.lang.String id;

    /**
     * 父ID
     */
    private java.lang.String parentId;

    /**
     * 机构名称
     */
    private java.lang.String orgName;

    /**
     * 机构描述
     */
    private java.lang.String orgDesc;

    /**
     * 所属警种
     */
    private java.lang.String belong;

    /**
     * 是否有效
     */
    private java.lang.Integer isValid;

    /**
     * 是否临时机构
     */
    private java.lang.Integer isTemp;

    /**
     * 是否删除
     */
    private java.lang.Integer isDelete;

    /**
     * 办公地址
     */
    private java.lang.String officeAddress;

    /**
     * 联系电话
     */
    private java.lang.String tel;

    /**
     * 手机号码
     */
    private java.lang.String mobile;

    /**
     * 1.审核通过 2.审核不通过 3.新增未审核 4.修改未审核 5.删除未审核
     */
    private java.lang.String auditStatus1;

    /**
     * 1.审核通过 2.审核不通过 3.新增未审核 4.修改未审核 5.删除未审核
     */
    private java.lang.String auditStatus;

    /**
     *
     */
    private java.lang.String isEnabled;

    /**
     * 显示排序
     */
    private java.lang.Integer orderByNum;

    /**
     *
     */
    private java.lang.String orgCode;

    /**
     *
     */
    private java.lang.Integer orderbynum;

    /**
     *
     */
    private java.lang.Integer znbmlx;

    /**
     * 上级机构
     */
    private AclOrgEntity parent;

    /**
     * 下属机构
     */
    private List<AclOrgEntity> subOrgnizations = new ArrayList<AclOrgEntity>();

    private Map<String,Object> valMap = new HashMap<String,Object>();

    private String jwd;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgDesc() {
        return orgDesc;
    }

    public void setOrgDesc(String orgDesc) {
        this.orgDesc = orgDesc;
    }

    public String getBelong() {
        return belong;
    }

    public void setBelong(String belong) {
        this.belong = belong;
    }

    public Integer getIsValid() {
        return isValid;
    }

    public void setIsValid(Integer isValid) {
        this.isValid = isValid;
    }

    public Integer getIsTemp() {
        return isTemp;
    }

    public void setIsTemp(Integer isTemp) {
        this.isTemp = isTemp;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public String getOfficeAddress() {
        return officeAddress;
    }

    public void setOfficeAddress(String officeAddress) {
        this.officeAddress = officeAddress;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAuditStatus1() {
        return auditStatus1;
    }

    public void setAuditStatus1(String auditStatus1) {
        this.auditStatus1 = auditStatus1;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(String isEnabled) {
        this.isEnabled = isEnabled;
    }

    public Integer getOrderByNum() {
        return orderByNum;
    }

    public void setOrderByNum(Integer orderByNum) {
        this.orderByNum = orderByNum;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public Integer getOrderbynum() {
        return orderbynum;
    }

    public void setOrderbynum(Integer orderbynum) {
        this.orderbynum = orderbynum;
    }

    public Integer getZnbmlx() {
        return znbmlx;
    }

    public void setZnbmlx(Integer znbmlx) {
        this.znbmlx = znbmlx;
    }

    public AclOrgEntity getParent() {
        return parent;
    }

    public void setParent(AclOrgEntity parent) {
        this.parent = parent;
    }

    public List<AclOrgEntity> getSubOrgnizations() {
        return subOrgnizations;
    }

    public void setSubOrgnizations(List<AclOrgEntity> subOrgnizations) {
        this.subOrgnizations = subOrgnizations;
    }

    public Map<String, Object> getValMap() {
        return valMap;
    }

    public void setValMap(Map<String, Object> valMap) {
        this.valMap = valMap;
    }

    public String getJwd() {
        return jwd;
    }

    public void setJwd(String jwd) {
        this.jwd = jwd;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

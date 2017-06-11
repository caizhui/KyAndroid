package com.ky.kyandroid.entity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Caizhui on 2017/6/10.
 * 事件录入实体
 */
@Table(name = "t_evententer")
public class EventEntryEntity {

    /**
     * 主键
     */
    @Column(name = "uuid",autoGen = true,isId = true)
    private String uuid;
    /**
     * 事件名称
     */
    @Column(name = "thingname")
    public String thingName;
    /**
     * 发生时间
     */
    @Column(name = "happentime")
    public String happenTime;
    /**
     * 发生地点
     */
    @Column(name = "happenaddress")
    public String happenAddress;
    /**
     * 上访群体
     */
    @Column(name = "petitiongroups")
    public String petitionGroups;
    /**
     * 到场部门
     */
    @Column(name = "fielddepartmen")
    public String fieldDepartmen;
    /**
     * 表现形式
     */
    @Column(name = "patternmanifestation")
    public String patternManifestation;
    /**
     * 规模
     */
    @Column(name = "scope")
    public String scope;
    /**
     * 涉及领域
     */
    @Column(name = "fieldsinvolved")
    public String fieldsInvolved;
    /**
     * 是否涉外
     */
    @Column(name = "foreignrelated")
    public String foreignRelated;
    /**
     * 是否涉疆
     */
    @Column(name = "involvedxinjiang")
    public String involvedXinjiang;
    /**
     * 是否涉舆情
     */
    @Column(name = "involvepublicopinion")
    public String involvePublicOpinion;
    /**
     * 是否公安处置
     */
    @Column(name = "publicsecuritydisposal")
    public String publicSecurityDisposal;
    /**
     * 所属街道
     */
    @Column(name = "belongstreet")
    public String belongStreet;
    /**
     * 所属社区
     */
    @Column(name = "belongcommunity")
    public String belongCommunity;
    /**
     * 主要诉求
     */
    @Column(name = "mainappeals")
    public String mainAppeals;
    /**
     * 事件概要
     */
    @Column(name = "eventsummary")
    public String eventSummary;
    /**
     * >领导批示
     */
    @Column(name = "leadershipinstructions")
    public String leadershipInstructions;


    public EventEntryEntity(){

    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getThingName() {
        return thingName;
    }

    public void setThingName(String thingName) {
        this.thingName = thingName;
    }

    public String getHappenTime() {
        return happenTime;
    }

    public void setHappenTime(String happenTime) {
        this.happenTime = happenTime;
    }

    public String getHappenAddress() {
        return happenAddress;
    }

    public void setHappenAddress(String happenAddress) {
        this.happenAddress = happenAddress;
    }

    public String getPetitionGroups() {
        return petitionGroups;
    }

    public void setPetitionGroups(String petitionGroups) {
        this.petitionGroups = petitionGroups;
    }

    public String getFieldDepartmen() {
        return fieldDepartmen;
    }

    public void setFieldDepartmen(String fieldDepartmen) {
        this.fieldDepartmen = fieldDepartmen;
    }

    public String getPatternManifestation() {
        return patternManifestation;
    }

    public void setPatternManifestation(String patternManifestation) {
        this.patternManifestation = patternManifestation;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getFieldsInvolved() {
        return fieldsInvolved;
    }

    public void setFieldsInvolved(String fieldsInvolved) {
        this.fieldsInvolved = fieldsInvolved;
    }

    public String getForeignRelated() {
        return foreignRelated;
    }

    public void setForeignRelated(String foreignRelated) {
        this.foreignRelated = foreignRelated;
    }

    public String getInvolvedXinjiang() {
        return involvedXinjiang;
    }

    public void setInvolvedXinjiang(String involvedXinjiang) {
        this.involvedXinjiang = involvedXinjiang;
    }

    public String getInvolvePublicOpinion() {
        return involvePublicOpinion;
    }

    public void setInvolvePublicOpinion(String involvePublicOpinion) {
        this.involvePublicOpinion = involvePublicOpinion;
    }

    public String getPublicSecurityDisposal() {
        return publicSecurityDisposal;
    }

    public void setPublicSecurityDisposal(String publicSecurityDisposal) {
        this.publicSecurityDisposal = publicSecurityDisposal;
    }

    public String getBelongStreet() {
        return belongStreet;
    }

    public void setBelongStreet(String belongStreet) {
        this.belongStreet = belongStreet;
    }

    public String getBelongCommunity() {
        return belongCommunity;
    }

    public void setBelongCommunity(String belongCommunity) {
        this.belongCommunity = belongCommunity;
    }

    public String getMainAppeals() {
        return mainAppeals;
    }

    public void setMainAppeals(String mainAppeals) {
        this.mainAppeals = mainAppeals;
    }

    public String getEventSummary() {
        return eventSummary;
    }

    public void setEventSummary(String eventSummary) {
        this.eventSummary = eventSummary;
    }

    public String getLeadershipInstructions() {
        return leadershipInstructions;
    }

    public void setLeadershipInstructions(String leadershipInstructions) {
        this.leadershipInstructions = leadershipInstructions;
    }

    public EventEntryEntity( String thingName, String happenTime, String happenAddress, String petitionGroups, String fieldDepartmen, String patternManifestation) {
        this.thingName = thingName;
        this.happenTime = happenTime;
        this.happenAddress = happenAddress;
        this.petitionGroups = petitionGroups;
        this.fieldDepartmen = fieldDepartmen;
        this.patternManifestation = patternManifestation;
    }
}

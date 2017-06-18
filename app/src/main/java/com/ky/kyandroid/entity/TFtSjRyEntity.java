package com.ky.kyandroid.entity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.util.Date;

/**
 * 
 * @author otom3@163.com
 * @date 2017-03-30
 */
@Table(name = "t_tftsjry")
public class TFtSjRyEntity {


	@Column(name =  "id",isId = true,autoGen = false)
	private String id;
	/**
	 * 外键
	 */
	@Column(name = "sjid")
	private String sjId;
	
	/**
	 * 姓名
	 */
	@Column(name = "xm")
	private String xm;
	
	/**
	 * 性别
	 */
	@Column(name = "xb")
	private String xb;
	
	/**
	 * 民族
	 */
	@Column(name = "mz")
	private String mz;
	
	/**
	 * 证件类型
	 */
	@Column(name = "zjlx")
	private String zjlx;
	
	/**
	 * 证件号码
	 */
	@Column(name = "zjhm")
	private String zjhm;
	
	/**
	 * email
	 */
	@Column(name = "email")
	private String email;
	
	/**
	 * 固定电话
	 */
	@Column(name = "gddh")
	private String gddh;
	
	/**
	 * 移动电话
	 */
	@Column(name = "sjid")
	private String yddh;
	
	/**
	 * 户籍地
	 */
	@Column(name = "hjd")
	private String hjd;
	
	/**
	 * 是否党员
	 */
	@Column(name = "sfdy")
	private String sfdy;
	
	/**
	 * 工作单位
	 */
	@Column(name = "gzdw")
	private String gzdw;
	
	/**
	 * 现住地址
	 */
	@Column(name = "xzdz")
	private String xzdz;
	
	/**
	 * 录入人
	 */
	@Column(name = "lrr")
	private String lrr;
	
	/**
	 * 录入部门
	 */
	@Column(name = "lrbm")
	private String lrbm;
	
	/**
	 * 录入时间
	 */
	@Column(name = "lrsj")
	private Date lrsj;
	
	/**
	 * 备注
	 */
	@Column(name = "comments")
	private String comments;
	/**
	 * 拆分表ID
	 */
	@Column(name = "cfsjID")
	private String cfsjID;
	/*
	 *录入人姓名，这个属性是不用在表中显示，只是方便查询 ，甚至xml都没有增加这个属性
	 */
	private String lrrName;

	public  TFtSjRyEntity(){}
	/**
	 * 获取 外键 的属性值
	 * @return sjId :  外键 
	 * @author otom3@163.com
	 */
	public String getSjId(){
		return this.sjId;
	}

	/**
	 * 设置 外键 的属性值
	 * @param sjId :  外键 
	 * @author otom3@163.com
	 */
	public void setSjId(String sjId){
		this.sjId	= sjId;
	}
	
	/**
	 * 获取 姓名 的属性值
	 * @return xm :  姓名 
	 * @author otom3@163.com
	 */
	public String getXm(){
		return this.xm;
	}

	/**
	 * 设置 姓名 的属性值
	 * @param xm :  姓名 
	 * @author otom3@163.com
	 */
	public void setXm(String xm){
		this.xm	= xm;
	}
	
	/**
	 * 获取 性别 的属性值
	 * @return xb :  性别 
	 * @author otom3@163.com
	 */
	public String getXb(){
		return this.xb;
	}

	/**
	 * 设置 性别 的属性值
	 * @param xb :  性别 
	 * @author otom3@163.com
	 */
	public void setXb(String xb){
		this.xb	= xb;
	}
	
	/**
	 * 获取 民族 的属性值
	 * @return mz :  民族 
	 * @author otom3@163.com
	 */
	public String getMz(){
		return this.mz;
	}

	/**
	 * 设置 民族 的属性值
	 * @param mz :  民族 
	 * @author otom3@163.com
	 */
	public void setMz(String mz){
		this.mz	= mz;
	}
	
	/**
	 * 获取 证件类型 的属性值
	 * @return zjlx :  证件类型 
	 * @author otom3@163.com
	 */
	public String getZjlx(){
		return this.zjlx;
	}

	/**
	 * 设置 证件类型 的属性值
	 * @param zjlx :  证件类型 
	 * @author otom3@163.com
	 */
	public void setZjlx(String zjlx){
		this.zjlx	= zjlx;
	}
	
	/**
	 * 获取 证件号码 的属性值
	 * @return zjhm :  证件号码 
	 * @author otom3@163.com
	 */
	public String getZjhm(){
		return this.zjhm;
	}

	/**
	 * 设置 证件号码 的属性值
	 * @param zjhm :  证件号码 
	 * @author otom3@163.com
	 */
	public void setZjhm(String zjhm){
		this.zjhm	= zjhm;
	}
	
	/**
	 * 获取 email 的属性值
	 * @return email :  email 
	 * @author otom3@163.com
	 */
	public String getEmail(){
		return this.email;
	}

	/**
	 * 设置 email 的属性值
	 * @param email :  email 
	 * @author otom3@163.com
	 */
	public void setEmail(String email){
		this.email	= email;
	}
	
	/**
	 * 获取 固定电话 的属性值
	 * @return gddh :  固定电话 
	 * @author otom3@163.com
	 */
	public String getGddh(){
		return this.gddh;
	}

	/**
	 * 设置 固定电话 的属性值
	 * @param gddh :  固定电话 
	 * @author otom3@163.com
	 */
	public void setGddh(String gddh){
		this.gddh	= gddh;
	}
	
	/**
	 * 获取 移动电话 的属性值
	 * @return yddh :  移动电话 
	 * @author otom3@163.com
	 */
	public String getYddh(){
		return this.yddh;
	}

	/**
	 * 设置 移动电话 的属性值
	 * @param yddh :  移动电话 
	 * @author otom3@163.com
	 */
	public void setYddh(String yddh){
		this.yddh	= yddh;
	}
	
	
	public String getHjd() {
		return hjd;
	}

	public void setHjd(String hjd) {
		this.hjd = hjd;
	}

	public String getSfdy() {
		return sfdy;
	}

	public void setSfdy(String sfdy) {
		this.sfdy = sfdy;
	}

	/**
	 * 获取 工作单位 的属性值
	 * @return gzdw :  工作单位 
	 * @author otom3@163.com
	 */
	public String getGzdw(){
		return this.gzdw;
	}

	/**
	 * 设置 工作单位 的属性值
	 * @param gzdw :  工作单位 
	 * @author otom3@163.com
	 */
	public void setGzdw(String gzdw){
		this.gzdw	= gzdw;
	}
	
	/**
	 * 获取 现住地址 的属性值
	 * @return xzdz :  现住地址 
	 * @author otom3@163.com
	 */
	public String getXzdz(){
		return this.xzdz;
	}

	/**
	 * 设置 现住地址 的属性值
	 * @param xzdz :  现住地址 
	 * @author otom3@163.com
	 */
	public void setXzdz(String xzdz){
		this.xzdz	= xzdz;
	}
	
	/**
	 * 获取 录入人 的属性值
	 * @return lrr :  录入人 
	 * @author otom3@163.com
	 */
	public String getLrr(){
		return this.lrr;
	}

	/**
	 * 设置 录入人 的属性值
	 * @param lrr :  录入人 
	 * @author otom3@163.com
	 */
	public void setLrr(String lrr){
		this.lrr	= lrr;
	}
	
	/**
	 * 获取 录入部门 的属性值
	 * @return lrbm :  录入部门 
	 * @author otom3@163.com
	 */
	public String getLrbm(){
		return this.lrbm;
	}

	/**
	 * 设置 录入部门 的属性值
	 * @param lrbm :  录入部门 
	 * @author otom3@163.com
	 */
	public void setLrbm(String lrbm){
		this.lrbm	= lrbm;
	}
	
	/**
	 * 获取 录入时间 的属性值
	 * @return lrsj :  录入时间 
	 * @author otom3@163.com
	 */
	public Date getLrsj(){
		return this.lrsj;
	}

	/**
	 * 设置 录入时间 的属性值
	 * @param lrsj :  录入时间 
	 * @author otom3@163.com
	 */
	public void setLrsj(Date lrsj){
		this.lrsj	= lrsj;
	}
	
	/**
	 * 获取 备注 的属性值
	 * @return comments :  备注 
	 * @author otom3@163.com
	 */
	public String getComments(){
		return this.comments;
	}

	/**
	 * 设置 备注 的属性值
	 * @param comments :  备注 
	 * @author otom3@163.com
	 */
	public void setComments(String comments){
		this.comments	= comments;
	}

	public String getCfsjID() {
		return cfsjID;
	}

	public void setCfsjID(String cfsjID) {
		this.cfsjID = cfsjID;
	}

	public String getLrrName() {
		return lrrName;
	}

	public void setLrrName(String lrrName) {
		this.lrrName = lrrName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
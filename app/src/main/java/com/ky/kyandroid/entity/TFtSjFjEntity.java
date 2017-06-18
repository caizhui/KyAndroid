package com.ky.kyandroid.entity;

import java.util.Date;

/**
 * 
 * @author otom3@163.com
 * @date 2017-03-30
 */
public class TFtSjFjEntity {
	

	/**
	 * 事件ID
	 */
	private String sjId;
	
	/**
	 * 附件类型 1.事件附件  2 事件处理附件
	 */
	private String fjlx;
	
	/**
	 * 文件类型 DOC  JPEG...
	 */
	private String wjlx;
	
	/**
	 * 文件名称
	 */
	private String wjmc;
	
	/**
	 * 文件描述
	 */
	private String wjms;
	
	/**
	 * 文件地址
	 */
	private String url;
	
	/**
	 * 
	 */
	private String lrr;
	
	/**
	 * 
	 */
	private String lrbm;
	
	/**
	 * 
	 */
	private Date lrsj;
	
	/**
	 * 
	 */
	private String comments;
	/**
	 * 拆分表ID
	 */
	private String cfSjId;
	
	/*
	 *录入人姓名，这个属性是不用在表中显示，只是方便查询 ，甚至xml都没有增加这个属性
	 */
	private String lrrName;
	
	private String clgcid;
	
	private TFtSjClgcEntity clgc;

	
	/**
	 * 获取 事件ID 的属性值
	 * @return sjId :  事件ID 
	 * @author otom3@163.com
	 */
	public String getSjId(){
		return this.sjId;
	}

	/**
	 * 设置 事件ID 的属性值
	 * @param sjId :  事件ID 
	 * @author otom3@163.com
	 */
	public void setSjId(String sjId){
		this.sjId	= sjId;
	}
	
	/**
	 * 获取 附件类型 1.事件附件  2 事件处理附件 的属性值
	 * @return fjlx :  附件类型 1.事件附件  2 事件处理附件 
	 * @author otom3@163.com
	 */
	public String getFjlx(){
		return this.fjlx;
	}

	/**
	 * 设置 附件类型 1.事件附件  2 事件处理附件 的属性值
	 * @param fjlx :  附件类型 1.事件附件  2 事件处理附件 
	 * @author otom3@163.com
	 */
	public void setFjlx(String fjlx){
		this.fjlx	= fjlx;
	}
	
	/**
	 * 获取 文件类型 DOC  JPEG... 的属性值
	 * @return wjlx :  文件类型 DOC  JPEG... 
	 * @author otom3@163.com
	 */
	public String getWjlx(){
		return this.wjlx;
	}

	/**
	 * 设置 文件类型 DOC  JPEG... 的属性值
	 * @param wjlx :  文件类型 DOC  JPEG... 
	 * @author otom3@163.com
	 */
	public void setWjlx(String wjlx){
		this.wjlx	= wjlx;
	}
	
	/**
	 * 获取 文件名称 的属性值
	 * @return wjmc :  文件名称 
	 * @author otom3@163.com
	 */
	public String getWjmc(){
		return this.wjmc;
	}

	/**
	 * 设置 文件名称 的属性值
	 * @param wjmc :  文件名称 
	 * @author otom3@163.com
	 */
	public void setWjmc(String wjmc){
		this.wjmc	= wjmc;
	}
	
	/**
	 * 获取 文件描述 的属性值
	 * @return wjms :  文件描述 
	 * @author otom3@163.com
	 */
	public String getWjms(){
		return this.wjms;
	}

	/**
	 * 设置 文件描述 的属性值
	 * @param wjms :  文件描述 
	 * @author otom3@163.com
	 */
	public void setWjms(String wjms){
		this.wjms	= wjms;
	}
	
	/**
	 * 获取 文件地址 的属性值
	 * @return url :  文件地址 
	 * @author otom3@163.com
	 */
	public String getUrl(){
		return this.url;
	}

	/**
	 * 设置 文件地址 的属性值
	 * @param url :  文件地址 
	 * @author otom3@163.com
	 */
	public void setUrl(String url){
		this.url	= url;
	}
	
	/**
	 * 获取  的属性值
	 * @return lrr :   
	 * @author otom3@163.com
	 */
	public String getLrr(){
		return this.lrr;
	}

	/**
	 * 设置  的属性值
	 * @param lrr :   
	 * @author otom3@163.com
	 */
	public void setLrr(String lrr){
		this.lrr	= lrr;
	}
	
	/**
	 * 获取  的属性值
	 * @return lrbm :   
	 * @author otom3@163.com
	 */
	public String getLrbm(){
		return this.lrbm;
	}

	/**
	 * 设置  的属性值
	 * @param lrbm :   
	 * @author otom3@163.com
	 */
	public void setLrbm(String lrbm){
		this.lrbm	= lrbm;
	}
	
	/**
	 * 获取  的属性值
	 * @return lrsj :   
	 * @author otom3@163.com
	 */
	public Date getLrsj(){
		return this.lrsj;
	}

	/**
	 * 设置  的属性值
	 * @param lrsj :   
	 * @author otom3@163.com
	 */
	public void setLrsj(Date lrsj){
		this.lrsj	= lrsj;
	}
	
	/**
	 * 获取  的属性值
	 * @return comments :   
	 * @author otom3@163.com
	 */
	public String getComments(){
		return this.comments;
	}

	/**
	 * 设置  的属性值
	 * @param comments :   
	 * @author otom3@163.com
	 */
	public void setComments(String comments){
		this.comments	= comments;
	}

	public String getCfSjId() {
		return cfSjId;
	}

	public void setCfSjId(String cfSjId) {
		this.cfSjId = cfSjId;
	}

	public String getLrrName() {
		return lrrName;
	}

	public void setLrrName(String lrrName) {
		this.lrrName = lrrName;
	}

	public String getClgcid() {
		return clgcid;
	}

	public void setClgcid(String clgcid) {
		this.clgcid = clgcid;
	}

	public TFtSjClgcEntity getClgc() {
		return clgc;
	}

	public void setClgc(TFtSjClgcEntity clgc) {
		this.clgc = clgc;
	}

	
	
}
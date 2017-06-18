package com.ky.kyandroid.entity;

import java.util.Date;
import java.util.List;

/**
 * 
 * @author otom3@163.com
 * @date 2017-04-06
 */
public class TFtSjClgcEntity {

	private String id;
	

	/**
	 * 事件ID
	 */
	private String sjId;
	
	/**
	 * 处理时间
	 */
	private Date clsj;
	
	/**
	 * 处理情况
	 */
	private String clqk;
	
	/**
	 * 处理人
	 */
	private String clr;
	
	/**
	 * 录入人
	 */
	private String lrr;
	
	/**
	 * 录入时间
	 */
	private Date lrsj;
	
	/**
	 * 备注
	 */
	private String comments;
	
	
	private String clbm_id;
	
	private String clfj;
	
	private String fjmc;
	
	private String fjlx;
	
	private String fjid;
	
	private List<TFtSjFjEntity> clgcfj;
	
	private String clbmmc;

	/**
	 * 获取 主键 的属性值
	 * @return id :  主键 
	 * @author otom3@163.com
	 */
	public String getId(){
		return this.id;
	}

	/**
	 * 设置 主键 的属性值
	 * @param id :  主键 
	 * @author otom3@163.com
	 */
	public void setId(String id){
		this.id	= id;
	}
	
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
	 * 获取 处理时间 的属性值
	 * @return clsj :  处理时间 
	 * @author otom3@163.com
	 */
	public Date getClsj(){
		return this.clsj;
	}

	/**
	 * 设置 处理时间 的属性值
	 * @param clsj :  处理时间 
	 * @author otom3@163.com
	 */
	public void setClsj(Date clsj){
		this.clsj	= clsj;
	}
	
	/**
	 * 获取 处理情况 的属性值
	 * @return clqk :  处理情况 
	 * @author otom3@163.com
	 */
	public String getClqk(){
		return this.clqk;
	}

	/**
	 * 设置 处理情况 的属性值
	 * @param clqk :  处理情况 
	 * @author otom3@163.com
	 */
	public void setClqk(String clqk){
		this.clqk	= clqk;
	}
	
	/**
	 * 获取 处理人 的属性值
	 * @return clr :  处理人 
	 * @author otom3@163.com
	 */
	public String getClr(){
		return this.clr;
	}

	/**
	 * 设置 处理人 的属性值
	 * @param clr :  处理人 
	 * @author otom3@163.com
	 */
	public void setClr(String clr){
		this.clr	= clr;
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

	public String getClbm_id() {
		return clbm_id;
	}

	public void setClbm_id(String clbm_id) {
		this.clbm_id = clbm_id;
	}

	public String getClfj() {
		return clfj;
	}

	public void setClfj(String clfj) {
		this.clfj = clfj;
	}

	public String getFjmc() {
		return fjmc;
	}

	public void setFjmc(String fjmc) {
		this.fjmc = fjmc;
	}

	public String getFjlx() {
		return fjlx;
	}

	public void setFjlx(String fjlx) {
		this.fjlx = fjlx;
	}

	public String getFjid() {
		return fjid;
	}

	public void setFjid(String fjid) {
		this.fjid = fjid;
	}

	public List<TFtSjFjEntity> getClgcfj() {
		return clgcfj;
	}

	public void setClgcfj(List<TFtSjFjEntity> clgcfj) {
		this.clgcfj = clgcfj;
	}

	public String getClbmmc() {
		return clbmmc;
	}

	public void setClbmmc(String clbmmc) {
		this.clbmmc = clbmmc;
	}
	


}
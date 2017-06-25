package com.ky.kyandroid.entity;

import java.io.Serializable;

/**
 *
 * @author otom3@163.com
 * @String 2017-04-05
 */
public class TFtSjGlsjEntity  implements Serializable{

	/**
	 * ID
	 */
	private String id;


	/**
	 * 事件ID
	 */
	private String sjId;

	/**
	 * 关联事件ID
	 */
	private String glsjId;

	/**
	 * 关联关系(字典GLGX)
	 */
	private String glgx;

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
	 *
	 */
	private String comments;

	private String lrrName;


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
	 * 获取 关联事件ID 的属性值
	 * @return glsjId :  关联事件ID
	 * @author otom3@163.com
	 */
	public String getGlsjId(){
		return this.glsjId;
	}

	/**
	 * 设置 关联事件ID 的属性值
	 * @param glsjId :  关联事件ID
	 * @author otom3@163.com
	 */
	public void setGlsjId(String glsjId){
		this.glsjId	= glsjId;
	}

	/**
	 * 获取 关联关系(字典GLGX) 的属性值
	 * @return glgx :  关联关系(字典GLGX)
	 * @author otom3@163.com
	 */
	public String getGlgx(){
		return this.glgx;
	}

	/**
	 * 设置 关联关系(字典GLGX) 的属性值
	 * @param glgx :  关联关系(字典GLGX)
	 * @author otom3@163.com
	 */
	public void setGlgx(String glgx){
		this.glgx	= glgx;
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
	public String getLrsj(){
		return this.lrsj;
	}

	/**
	 * 设置 录入时间 的属性值
	 * @param lrsj :  录入时间
	 * @author otom3@163.com
	 */
	public void setLrsj(String lrsj){
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
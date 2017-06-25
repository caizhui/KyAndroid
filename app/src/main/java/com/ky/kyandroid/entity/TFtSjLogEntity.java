package com.ky.kyandroid.entity;

import java.io.Serializable;

/**
 * 
 * @author otom3@163.com
 * @String 2017-04-05
 */
public class TFtSjLogEntity implements Serializable{
	
  private String id;
	
	/**
	 * 事件ID
	 */
	private String sjId;
	
	/**
	 * 用户名
	 */
	private String yhm;
	
	/**
	 * 用户部门
	 */
	private String yhbm;
	
	/**
	 * 操作
	 */
	private String cz;
	
	/**
	 * 操作时间
	 */
	private String czsj;
	
	/**
	 * 处理效果
	 */
	private String czqzt;
	
	/**
	 * 完成时间
	 */
	private String czhzt;
	
	/**
	 * 操作描述，比如显示退回原因
	 */
	private String czms;
	
	
	/**
	 * 
	 */
	private String comments;


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
	 * 获取 用户名 的属性值
	 * @return yhm :  用户名 
	 * @author otom3@163.com
	 */
	public String getYhm(){
		return this.yhm;
	}

	/**
	 * 设置 用户名 的属性值
	 * @param yhm :  用户名 
	 * @author otom3@163.com
	 */
	public void setYhm(String yhm){
		this.yhm	= yhm;
	}
	
	/**
	 * 获取 用户部门 的属性值
	 * @return yhbm :  用户部门 
	 * @author otom3@163.com
	 */
	public String getYhbm(){
		return this.yhbm;
	}

	/**
	 * 设置 用户部门 的属性值
	 * @param yhbm :  用户部门 
	 * @author otom3@163.com
	 */
	public void setYhbm(String yhbm){
		this.yhbm	= yhbm;
	}
	
	/**
	 * 获取 操作 的属性值
	 * @return cz :  操作 
	 * @author otom3@163.com
	 */
	public String getCz(){
		return this.cz;
	}

	/**
	 * 设置 操作 的属性值
	 * @param cz :  操作 
	 * @author otom3@163.com
	 */
	public void setCz(String cz){
		this.cz	= cz;
	}
	
	/**
	 * 获取 操作时间 的属性值
	 * @return czsj :  操作时间 
	 * @author otom3@163.com
	 */
	public String getCzsj(){
		return this.czsj;
	}

	/**
	 * 设置 操作时间 的属性值
	 * @param czsj :  操作时间 
	 * @author otom3@163.com
	 */
	public void setCzsj(String czsj){
		this.czsj	= czsj;
	}
	
	/**
	 * 获取 处理效果 的属性值
	 * @return czqzt :  处理效果 
	 * @author otom3@163.com
	 */
	public String getCzqzt(){
		return this.czqzt;
	}

	/**
	 * 设置 处理效果 的属性值
	 * @param czqzt :  处理效果 
	 * @author otom3@163.com
	 */
	public void setCzqzt(String czqzt){
		this.czqzt	= czqzt;
	}
	
	/**
	 * 获取 完成时间 的属性值
	 * @return czhzt :  完成时间 
	 * @author otom3@163.com
	 */
	public String getCzhzt(){
		return this.czhzt;
	}

	/**
	 * 设置 完成时间 的属性值
	 * @param czhzt :  完成时间 
	 * @author otom3@163.com
	 */
	public void setCzhzt(String czhzt){
		this.czhzt	= czhzt;
	}
	
	/**
	 * 获取 操作描述，比如显示退回原因 的属性值
	 * @return czms :  操作描述，比如显示退回原因 
	 * @author otom3@163.com
	 */
	public String getCzms(){
		return this.czms;
	}

	/**
	 * 设置 操作描述，比如显示退回原因 的属性值
	 * @param czms :  操作描述，比如显示退回原因 
	 * @author otom3@163.com
	 */
	public void setCzms(String czms){
		this.czms	= czms;
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


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
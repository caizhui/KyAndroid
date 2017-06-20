package com.ky.kyandroid.entity;

import com.ky.kyandroid.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.List;




/**
 * 类名称：事件操作参数封装类<br/>
 * 类描述：<br/>
 * 
 * 创建人： Cz <br/>
 * 创建时间：2017年6月20日 上午9:50:50 <br/>
 * @updateRemark 修改备注：
 *     
 */
public class SjHandleParams {
	
	/** 用户ID */
	private String userId;
	
	/** 事件ID */
	private String sjId;

	/** 操作ACTION */
	private String action;

	/** 操作名称 */
	private String actionName;
	
	/** 当前状态 */
	private String zt;
	
	/** 变更状态 */
	private String nextZt;
	
	/** 按钮列表 */
	private List<TFtZtlzEntity> anlist;

	/**
	 * 操作原因
	 */
	private String czyy;
	
	
	/**
	 * 参数生成
	 * 
	 * @return
	 */
	public String generateParamsStr(){
		StringBuffer sb = new StringBuffer();
		sb.append("userId=").append(StringUtils.isBlank(this.userId)?"":this.userId).append("&");
		sb.append("sjId=").append(StringUtils.isBlank(this.sjId)?"":this.sjId).append("&");
		sb.append("action=").append(StringUtils.isBlank(this.action)?"":this.action).append("&");
		sb.append("actionName=").append(StringUtils.isBlank(this.actionName)?"":this.actionName).append("&");
		sb.append("zt=").append(StringUtils.isBlank(this.zt)?"":this.zt).append("&");
		sb.append("nextZt=").append(StringUtils.isBlank(this.nextZt)?"":this.nextZt);
		return sb.toString();
	}


	/**
	 * 参数生成
	 *
	 * @return
	 */
	public String generateParamsStr(SjHandleParams entity ){
		StringBuffer sb = new StringBuffer();
		sb.append("userId=").append(StringUtils.isBlank(entity.getUserId())?"":entity.getUserId()).append("&");
		sb.append("sjId=").append(StringUtils.isBlank(entity.getSjId())?"":entity.getSjId()).append("&");
		sb.append("action=").append(StringUtils.isBlank(entity.getAction())?"":entity.getAction()).append("&");
		sb.append("actionName=").append(StringUtils.isBlank(entity.getActionName())?"":entity.getActionName()).append("&");
		sb.append("zt=").append(StringUtils.isBlank(entity.getZt())?"":this.getZt()).append("&");
		sb.append("nextZt=").append(StringUtils.isBlank(entity.getNextZt())?"":entity.getNextZt());
		return sb.toString();
	}
	
	public static void main(String[] args) {
		SjHandleParams sj = new SjHandleParams();
		System.out.println(sj.generateParamsStr());
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSjId() {
		return sjId;
	}

	public void setSjId(String sjId) {
		this.sjId = sjId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		if (!StringUtils.isBlank(actionName)) {
			try {
				actionName = new String(actionName.getBytes("ISO-8859-1"),"utf-8");
			} catch (UnsupportedEncodingException e) {
				actionName = "";
			}
		}
		this.actionName = actionName;
	}

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

	public String getNextZt() {
		return nextZt;
	}

	public void setNextZt(String nextZt) {
		this.nextZt = nextZt;
	}

	public List<TFtZtlzEntity> getAnlist() {
		return anlist;
	}

	public void setAnlist(List<TFtZtlzEntity> anlist) {
		this.anlist = anlist;
	}

	public String getCzyy() {
		return czyy;
	}

	public void setCzyy(String czyy) {
		this.czyy = czyy;
	}
}

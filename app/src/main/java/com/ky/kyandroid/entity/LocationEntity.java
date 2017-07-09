package com.ky.kyandroid.entity;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 类名称：坐标实体
 * JIRA：
 * 类描述：
 * 
 * 创建人： msi
 * 创建时间：2017年7月8日 下午7:42:34
 * 
 * @author msi
 * @date 2017年7月8日 下午7:42:34
 * @jira JIRA:http://192.168.0.6:88/browse/
 * @updateRemark 修改备注：
 *     
 */
public class LocationEntity implements Serializable {

	/** 用户ID*/
	private String uid;
	/** 经度*/
	private String lng;
	/** 纬度*/
	private String lat;
	/** 备注*/
	private String bz;
	/** 创建时间 */
	private String cjsj;


	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public String getCjsj() {
		return cjsj;
	}

	public void setCjsj(String cjsj) {
		this.cjsj = cjsj;
	}
}

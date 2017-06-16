package com.ky.kyandroid.entity;

/**
 * 类名称：用户Entity<br/>
 * @updateRemark 修改备注：
 *     
 */
public class UserEntity {

	/** 操作员ID - 主键 */
	private String userId;
	/** 账号 */
	private String name;
	/** 昵称 */
	private String userName;
	/** 上级用户ID */
	private String parentId;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	
}

package com.ky.kyandroid.entity;

import java.io.Serializable;

public class TFtZtlzEntity implements Serializable {
	private static final long serialVersionUID = -7620435178023928252L;

	private String id;

	private String actionName;//操作名称

	private String prevZt;//操作前状态

	private String nextZt;//操作后状态

	private String actionRole;//角色

	private String actionCondition;//附加条件

	private String action;//操作ACTION

	private String comments;//备注
	
	private String enabled;//是否允许，1为可用
	
	private String name;

	private int hashvalue;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getPrevZt() {
		return prevZt;
	}

	public void setPrevZt(String prevZt) {
		this.prevZt = prevZt;
	}

	public String getNextZt() {
		return nextZt;
	}

	public void setNextZt(String nextZt) {
		this.nextZt = nextZt;
	}

	public String getActionRole() {
		return actionRole;
	}

	public void setActionRole(String actionRole) {
		this.actionRole = actionRole;
	}

	public String getActionCondition() {
		return actionCondition;
	}

	public void setActionCondition(String actionCondition) {
		this.actionCondition = actionCondition;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

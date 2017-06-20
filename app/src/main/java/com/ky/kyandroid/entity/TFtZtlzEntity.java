package com.ky.kyandroid.entity;

import java.io.Serializable;

public class TFtZtlzEntity implements Serializable {
	private static final long serialVersionUID = -7620435178023928252L;

	private String id;
	
	private String action;//操作ACTION

	private String actionname;//操作名称
	
	private String prevzt;//操作前状态
	
	private String nextzt;//操作后状态
	
	private String comments;//备注
	
	private String actionrole;//角色
	
	private String actioncondition;//附加条件
	
	private String enabled;//是否允许，1为可用
	
	private String name;

	private int hashvalue;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getActionname() {
		return actionname;
	}

	public void setActionname(String actionname) {
		this.actionname = actionname;
	}

	public String getPrevzt() {
		return prevzt;
	}

	public void setPrevzt(String prevzt) {
		this.prevzt = prevzt;
	}

	public String getNextzt() {
		return nextzt;
	}

	public void setNextzt(String nextzt) {
		this.nextzt = nextzt;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getActionrole() {
		return actionrole;
	}

	public void setActionrole(String actionrole) {
		this.actionrole = actionrole;
	}

	public String getActioncondition() {
		return actioncondition;
	}

	public void setActioncondition(String actioncondition) {
		this.actioncondition = actioncondition;
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

	public int getHashvalue() {
		return hashvalue;
	}

	public void setHashvalue(int hashvalue) {
		this.hashvalue = hashvalue;
	}
}

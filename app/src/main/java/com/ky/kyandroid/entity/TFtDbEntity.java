package com.ky.kyandroid.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author otom3@163.com
 * @date 2017-04-10
 */
public class TFtDbEntity implements Serializable{
	
	/** long */
	private static final long serialVersionUID = 1L;

	private String id;

	/**
	 * 督办名称
	 */
	private String dbmc;
	
	/**
	 * 督办类型
	 */
	private String dblx;
	
	/**
	 * 被督办单位
	 */
	private String bdbdw;
	
	/**
	 * 督办要求
	 */
	private String dbyq;
	
	/**
	 * 反馈时限
	 */
	private String fksx;
	
	/**
	 * 督办时间
	 */
	private String dbsj;
	
	/**
	 * 督办反馈结果
	 */
	private String dbfkjg;

	/**
	 * 被转派部门
	 */
	private String org_name;
	
	/**
	 * 关联事件
	 */
	private String sjid;
	
	/**
	 * 督办单位
	 */
	private String dbdw;
	
	/**
	 * 督办人
	 */
	private String dbr;
	
	/**
	 * 转派人（补督办单位人员登录后将它转派给具体的人（本单位用户））
	 */
	private String zpr;
	
	/**
	 * 转派给了谁
	 */
	private String zpjsr;
	
	/**
	 * 转派原因
	 */
	private String zpyy;
	
	/**
	 * 受理人
	 */
	private String slr;
	
	/**
	 * 退回人
	 */
	private String thr;
	
	/**
	 * 退回原因
	 */
	private String thyy;
	
	/**
	 * 
	 */
	private String comments;
	
	/**
	 * 状态
	 */
	private String zt;

	/**
	 * 被督办单位名称
	 */
	private String bdbdwname;

	/**
	 * 状态名称
	 */
	private String statusname;

	private String sjmc;

	private List<DescEntity> zpbmlist;

	private List<DbAnEntity>  anlist;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSjmc() {
		return sjmc;
	}

	public void setSjmc(String sjmc) {
		this.sjmc = sjmc;
	}

	/**
	 * 获取 督办名称 的属性值
	 * @return dbmc :  督办名称 
	 * @author otom3@163.com
	 */
	public String getDbmc(){
		return this.dbmc;
	}

	/**
	 * 设置 督办名称 的属性值
	 * @param dbmc :  督办名称 
	 * @author otom3@163.com
	 */
	public void setDbmc(String dbmc){
		this.dbmc	= dbmc;
	}
	
	/**
	 * 获取 督办类型 的属性值
	 * @return dblx :  督办类型 
	 * @author otom3@163.com
	 */
	public String getDblx(){
		return this.dblx;
	}

	/**
	 * 设置 督办类型 的属性值
	 * @param dblx :  督办类型 
	 * @author otom3@163.com
	 */
	public void setDblx(String dblx){
		this.dblx	= dblx;
	}
	
	/**
	 * 获取 被督办单位 的属性值
	 * @return bdbdw :  被督办单位 
	 * @author otom3@163.com
	 */
	public String getBdbdw(){
		return this.bdbdw;
	}

	/**
	 * 设置 被督办单位 的属性值
	 * @param bdbdw :  被督办单位 
	 * @author otom3@163.com
	 */
	public void setBdbdw(String bdbdw){
		this.bdbdw	= bdbdw;
	}
	
	/**
	 * 获取 督办要求 的属性值
	 * @return dbyq :  督办要求 
	 * @author otom3@163.com
	 */
	public String getDbyq(){
		return this.dbyq;
	}

	/**
	 * 设置 督办要求 的属性值
	 * @param dbyq :  督办要求 
	 * @author otom3@163.com
	 */
	public void setDbyq(String dbyq){
		this.dbyq	= dbyq;
	}
	
	/**
	 * 获取 反馈时限 的属性值
	 * @return fksx :  反馈时限 
	 * @author otom3@163.com
	 */
	public String getFksx(){
		return this.fksx;
	}

	/**
	 * 设置 反馈时限 的属性值
	 * @param fksx :  反馈时限 
	 * @author otom3@163.com
	 */
	public void setFksx(String fksx){
		this.fksx	= fksx;
	}
	
	/**
	 * 获取 督办时间 的属性值
	 * @return dbsj :  督办时间 
	 * @author otom3@163.com
	 */
	public String getDbsj(){
		return this.dbsj;
	}

	/**
	 * 设置 督办时间 的属性值
	 * @param dbsj :  督办时间 
	 * @author otom3@163.com
	 */
	public void setDbsj(String dbsj){
		this.dbsj	= dbsj;
	}
	
	/**
	 * 获取 督办反馈结果 的属性值
	 * @return dbfkjg :  督办反馈结果 
	 * @author otom3@163.com
	 */
	public String getDbfkjg(){
		return this.dbfkjg;
	}

	/**
	 * 设置 督办反馈结果 的属性值
	 * @param dbfkjg :  督办反馈结果 
	 * @author otom3@163.com
	 */
	public void setDbfkjg(String dbfkjg){
		this.dbfkjg	= dbfkjg;
	}

	public String getSjid() {
		return sjid;
	}

	public void setSjid(String sjid) {
		this.sjid = sjid;
	}

	/**
	 * 获取 督办单位 的属性值
	 * @return dbdw :  督办单位 
	 * @author otom3@163.com
	 */
	public String getDbdw(){
		return this.dbdw;
	}

	/**
	 * 设置 督办单位 的属性值
	 * @param dbdw :  督办单位 
	 * @author otom3@163.com
	 */
	public void setDbdw(String dbdw){
		this.dbdw	= dbdw;
	}
	
	/**
	 * 获取 督办人 的属性值
	 * @return dbr :  督办人 
	 * @author otom3@163.com
	 */
	public String getDbr(){
		return this.dbr;
	}

	/**
	 * 设置 督办人 的属性值
	 * @param dbr :  督办人 
	 * @author otom3@163.com
	 */
	public void setDbr(String dbr){
		this.dbr	= dbr;
	}
	
	/**
	 * 获取 转派人（补督办单位人员登录后将它转派给具体的人（本单位用户）） 的属性值
	 * @return zpr :  转派人（补督办单位人员登录后将它转派给具体的人（本单位用户）） 
	 * @author otom3@163.com
	 */
	public String getZpr(){
		return this.zpr;
	}

	/**
	 * 设置 转派人（补督办单位人员登录后将它转派给具体的人（本单位用户）） 的属性值
	 * @param zpr :  转派人（补督办单位人员登录后将它转派给具体的人（本单位用户）） 
	 * @author otom3@163.com
	 */
	public void setZpr(String zpr){
		this.zpr	= zpr;
	}
	
	/**
	 * 获取 转派给了谁 的属性值
	 * @return zpjsr :  转派给了谁 
	 * @author otom3@163.com
	 */
	public String getZpjsr(){
		return this.zpjsr;
	}

	/**
	 * 设置 转派给了谁 的属性值
	 * @param zpjsr :  转派给了谁 
	 * @author otom3@163.com
	 */
	public void setZpjsr(String zpjsr){
		this.zpjsr	= zpjsr;
	}
	
	/**
	 * 获取 转派原因 的属性值
	 * @return zpyy :  转派原因 
	 * @author otom3@163.com
	 */
	public String getZpyy(){
		return this.zpyy;
	}

	/**
	 * 设置 转派原因 的属性值
	 * @param zpyy :  转派原因 
	 * @author otom3@163.com
	 */
	public void setZpyy(String zpyy){
		this.zpyy	= zpyy;
	}
	
	/**
	 * 获取 受理人 的属性值
	 * @return slr :  受理人 
	 * @author otom3@163.com
	 */
	public String getSlr(){
		return this.slr;
	}

	/**
	 * 设置 受理人 的属性值
	 * @param slr :  受理人 
	 * @author otom3@163.com
	 */
	public void setSlr(String slr){
		this.slr	= slr;
	}
	
	/**
	 * 获取 退回人 的属性值
	 * @return thr :  退回人 
	 * @author otom3@163.com
	 */
	public String getThr(){
		return this.thr;
	}

	/**
	 * 设置 退回人 的属性值
	 * @param thr :  退回人 
	 * @author otom3@163.com
	 */
	public void setThr(String thr){
		this.thr	= thr;
	}
	
	/**
	 * 获取 退回原因 的属性值
	 * @return thyy :  退回原因 
	 * @author otom3@163.com
	 */
	public String getThyy(){
		return this.thyy;
	}

	/**
	 * 设置 退回原因 的属性值
	 * @param thyy :  退回原因 
	 * @author otom3@163.com
	 */
	public void setThyy(String thyy){
		this.thyy	= thyy;
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

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

	public List<DbAnEntity> getAnlist() {
		return anlist;
	}

	public void setAnlist(List<DbAnEntity> anlist) {
		this.anlist = anlist;
	}

	public List<DescEntity> getZpbmList() {
		return zpbmlist;
	}

	public void setZpbmList(List<DescEntity> zpbmlist) {
		this.zpbmlist = zpbmlist;
	}

	public String getBdbdwname() {
		return bdbdwname;
	}

	public void setBdbdwname(String bdbdwname) {
		this.bdbdwname = bdbdwname;
	}

	public String getStatusname() {
		return statusname;
	}

	public void setStatusname(String statusname) {
		this.statusname = statusname;
	}

	public String getOrg_name() {
		return org_name;
	}

	public void setOrg_name(String org_name) {
		this.org_name = org_name;
	}
}
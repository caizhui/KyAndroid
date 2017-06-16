package com.ky.kyandroid.bean;

/**
 * 类名称：请求类别<br/>
 * 类描述：<br/>
 * 
 * 创建人： Cz <br/>
 * 创建时间：2016年9月20日 上午11:42:54 <br/>
 * @updateRemark 修改备注：
 *     
 */
public enum AckEnum {
	/** 不可识别的请求 */
	ACK_00("不可识别的请求"),
	/** 用户名或密码不正确 */
	ACK_01("用户名、密码不正确，拒绝访问"),
	/** 请求传入参数不符合规范要求 */
	ACK_02("请求传入参数不符合规范要求"),
	/** ACCESS_TOKEN无效 */
	ACK_03("ACCESS_TOKEN无效"),
	/** 接口连接超时 */
	ACK_04("接口连接超时"),
	/** 系统繁忙 */
	ACK_05("系统繁忙"),
	/** 查询不到数据 */
	ACK_06("查询不到数据"),
	/** 修改失败 */
	ACK_07("修改失败"),
	/** 保存失败 */
	ACK_08("保存失败"),
	/** 其它原因失败 */
	ACK_99("其它原因失败");

	/** label */
	private String msg;

	private AckEnum(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

}

package com.ky.kyandroid.bean;

/**
 * 类名称：CodeValue
 * 类描述：
 * 
 * 创建人： Cz
 * 创建时间：2016年9月7日 下午4:15:16
 * @updateRemark 修改备注：
 *     
 */
public class CodeValue{
	
	/** 代码 */
	private String code;
	
	/** 值 */
	private String value;
	
	public CodeValue(){}
	
	public CodeValue(String code, String value) {
		this.code = code;
		this.value = value;
	}

	@Override
	public String toString() {
		// 为什么要重写toString()呢？因为适配器在显示数据的时候，如果传入适配器的对象不是字符串的情况下，直接就使用对象.toString()
		return value;
	}

	public String getCode() {
		return code;
	}


	public String getValue() {
		return value;
	}


}

package com.ky.kyandroid.util;


import android.annotation.SuppressLint;

/**
 * 类名称：字符串工具类
 *     
 */
@SuppressLint("DefaultLocale")
public class StringUtils {

	/**
	 * 是否是纯数字
	 * @param str
	 * @return
	 */
	public static boolean isNum(String str) {
		return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
	}

	/**
	 * 重载父类isBlank(String)方法，专为对象进行判断<br/>
	 * 传入的对象中将转换为String进行判断，案例如下:<br/>
	 * KYStringUtils.isBlank(null) = false<br/>
	 * KYStringUtils.isBlank("") = false<br/>
	 * KYStringUtils.isBlank(" ") = true<br/>
	 * KYStringUtils.isBlank("         ") = true<br/>
	 * KYStringUtils.isBlank("hello lady") = true<br/>
	 * KYStringUtils.isBlank(" hello lady ") = true <br/>
	 * @param value
	 * @return boolean
	 */
	public static boolean isBlank(Object value) {
		if (value == null) {
			return true;
		} else {
			if (value.toString().trim().length() == 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 重载父类isNotBlank(String)方法，专为对象进行判断<br/>
	 * 传入的对象中将转换为String进行判断，案例如下:<br/>
	 * KYStringUtils.isNotBlank(null) = false<br/>
	 * KYStringUtils.isNotBlank("") = false<br/>
	 * KYStringUtils.isNotBlank(" ") = true<br/>
	 * KYStringUtils.isNotBlank("         ") = true<br/>
	 * KYStringUtils.isNotBlank("hello lady") = true<br/>
	 * KYStringUtils.isNotBlank(" hello lady ") = true <br/>
	 * 
	 * @param value
	 * @return boolean
	 */
	public static boolean isNotBlank(Object value) {
		if (value == null) {
			return false;
		} else {
			if (value.toString().trim().length() == 0) {
				return false;
			}
			if ("null".equals(value.toString().trim())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 获得YYYY-MM-DD格式的日期字符串
	 * 
	 * @return {String}
	 */
	public static String getYMDDateString(String oldDate) {
		String newDate = oldDate.replaceAll("年", "-");
		newDate = newDate.replaceAll("月", "-");
		newDate = newDate.replaceAll("日", "");
		return newDate.trim();
	}
}

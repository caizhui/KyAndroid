package com.ky.kyandroid.util;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 类名称：手机工具类
 * JIRA：
 * 类描述：
 * 
 * 创建人： cz
 * 创建时间：2015年3月31日 下午1:35:53
 * 
 * @updateRemark 修改备注：
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


	/**
	 * 格式化string为Date
	 * 
	 * @param datestr
	 * @return date
	 */
	public static Date parseDate(String datestr) {
		if (null == datestr || "".equals(datestr)) {
			return null;
		}
		try {
			String fmtstr = null;
			if (datestr.indexOf(':') > 0) {
				fmtstr = "yyyy-MM-dd HH:mm:ss";
			} else {

				fmtstr = "yyyy-MM-dd";
			}
			SimpleDateFormat sdf = new SimpleDateFormat(fmtstr, Locale.UK);
			return sdf.parse(datestr);
		} catch (Exception e) {
			return null;
		}
	}

}

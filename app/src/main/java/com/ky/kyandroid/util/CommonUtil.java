package com.ky.kyandroid.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 工具类
 * 
 * @author
 * 
 */
public class CommonUtil {

	
	/**
	 * 校验手机号码
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("^1[3|4|5|7|8]\\d{9}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	/**
	 * 邮箱检验
	 * @param strEmail
	 * @return
	 */
	public static boolean isEmail(String strEmail) {
		String strPattern = "^[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(strEmail);
		return m.matches();
	}

	/**
	 * 是否是纯数字
	 * @param str
	 * @return
	 */
	public static boolean isNum(String str) {
		return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
	}

	/**
	 * 身份证号
	 * @param idNmber
	 * @return
	 */
	public static boolean isIDNumber(String idNmber) {
		String strPattern = "^(\\d{15}$|^\\d{18}$|^\\d{17}(\\d|X|x))$";
		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(idNmber);
		return m.matches();
	}


	/**
	 * 验证手机号码（支持国际格式，+86135xxxx...（中国内地），+00852137xxxx...（中国香港））
	 * @param mobile 移动、联通、电信运营商的号码段
	 *<p>移动的号段：134(0-8)、135、136、137、138、139、147（预计用于TD上网卡）
	 *、150、151、152、157（TD专用）、158、159、187（未启用）、188（TD专用）</p>
	 *<p>联通的号段：130、131、132、155、156（世界风专用）、185（未启用）、186（3g）</p>
	 *<p>电信的号段：133、153、180（未启用）、189</p>
	 * @return 验证成功返回true，验证失败返回false
	 */
	public static boolean isMobile(String mobile){
		String regex = "(\\+\\d+)?1[3458]\\d{9}$";
		return Pattern.matches(regex, mobile);
	}
	/**
	 * 区号+座机号码+分机号码
	 * @param fixedPhone
	 * @return
	 */
	public static boolean isFixedPhone(String fixedPhone){
		String reg="(?:(\\(\\+?86\\))(0[0-9]{2,3}\\-?)?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?)|" +
				"(?:(86-?)?(0[0-9]{2,3}\\-?)?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?)";
		return Pattern.matches(reg, fixedPhone);
	}
	/**
	 * 匹配中国邮政编码
	 * @param
	 * @return 验证成功返回true，验证失败返回false
	 */
	public static boolean isPostCode(String postCode){
		String reg = "[1-9]\\d{5}";
		return Pattern.matches(reg, postCode);
	}

}

package com.ky.kyandroid.util;

import java.util.UUID;


/**
 * 获得ID的类，实现UUID的生成
 * 
 * @author leonelwong@126.com
 * @version 1.0 2012-05-08
 */
public final class IDHelper {

	/**
	 * 生成UUID，去掉-，并转成小写
	 * 
	 * @return String
	 */
	public static String getUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
	}

}

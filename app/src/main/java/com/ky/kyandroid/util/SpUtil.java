package com.ky.kyandroid.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 类名称：SharedPreferences工具类
 * 类描述：
 * 
 * 创建人： Cz
 * 创建时间：2016年9月7日 下午3:10:56
 * @updateRemark 修改备注：
 *     
 */
public class SpUtil {
	private static final String NAME="CnSp";
	
	/**
	 * 获取SharedPreferences
	 * 
	 * @param context
	 * @return
	 */
	public static SharedPreferences getSharePerference(Context context){
		return context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
	}
	
	/**
	 * 是否第一次
	 * 
	 * @param sp
	 * @return
	 */
	public static boolean isFirst(SharedPreferences sp){
		return sp.getBoolean("isFirst", false);
	}
	
	
	/**
	 * 设置String属性
	 * 
	 * @param sp 
	 * @param key 键
	 * @param value 值
	 */
	public static void setStringSharedPerference(SharedPreferences sp,String key,String value){
		Editor editor=sp.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	/**
	 * 设置boolean属性
	 * 
	 * @param sp
	 * @param key 键
	 * @param value true/false
	 */
	public static void setBooleanSharedPerference(SharedPreferences sp,String key,boolean value){
		Editor editor=sp.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
}

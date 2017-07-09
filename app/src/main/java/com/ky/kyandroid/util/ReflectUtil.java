package com.ky.kyandroid.util;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 类名称：Reflect工具类
 * 类描述：
 * 
 * @updateRemark 修改备注：
 *     
 */
public class ReflectUtil {

	/**
	 * 获取方法名，get或set方法<br>
	 * 
	 * @param flag
	 * @param attribute
	 * @return
	 */
	public static String getMethodName(boolean flag, String attribute) {
		String firstElemntOfAttribute = attribute.substring(0, 1).toUpperCase();
		String restElementOfAttrute = attribute.substring(1);
		return (flag ? "get" : "set") + firstElemntOfAttribute + restElementOfAttrute;
	}

	/**
	 * 调用bean的get方法<br>
	 * 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object invokeGetterMethod(Object obj, String attribute) throws Exception {
		Class clazz = obj.getClass();
		Method method = clazz.getMethod(getMethodName(true, attribute));
		return method.invoke(obj);
	}

	/**
	 * 调用bean的set方法<br>
	 * 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void invokeSetterMethod(Object obj, String attribute, Object value) throws Exception {
		Class clazz = obj.getClass();
		Field field = clazz.getDeclaredField(attribute);
		Class paramType = field.getType();
		Method method = clazz.getMethod(getMethodName(false, attribute), paramType);
		method.invoke(obj, value);
	}

	public static void printAllFieldValues(Object obj) {
		Class<?> clazz = obj.getClass();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			try {
				System.out.println(invokeGetterMethod(obj, field.getName()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 实体装换成map
	 * 
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings("rawtypes")
	public static Map<String, String> getRequestParamMap(Object object) {
		Map<String, String> strParamMap = null;
		try {
			Class clazz = object.getClass();
			Field[] fields = clazz.getDeclaredFields();
			strParamMap = new HashMap<String, String>();
			 for (Field field :fields)  
		      {  
				field.setAccessible(true);
		        String key = field.getName();
		        String value =String.valueOf(field.get(object));
		        if(!StringUtils.isBlank(value) && !"null".equals(value)){
		        	strParamMap.put(key, value);  
		        }
		        
		      }  
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strParamMap;
	}

}

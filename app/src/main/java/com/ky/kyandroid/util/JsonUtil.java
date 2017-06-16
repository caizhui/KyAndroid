package com.ky.kyandroid.util;


import android.util.Log;

import com.ky.kyandroid.adapter.DateTimestampTypeAdapter;
import com.solidfire.gson.Gson;
import com.solidfire.gson.GsonBuilder;
import com.solidfire.gson.internal.bind.DateTypeAdapter;
import com.solidfire.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 类名称：JSON序列化与反序列化工具类
 * @author caizhui
 */
public class JsonUtil {

	/**
	 * 空的 {@code JSON} 数据 -
	 * 
	 * <pre>
	 * &quot;{}&quot;
	 * </pre>
	 * 
	 * 。
	 */
	public static final String EMPTY_JSON = "{}";

	/** 空的 {@code JSON} 数组(集合)数据 - {@code "[]"}。 */
	public static final String EMPTY_JSON_ARRAY = "[]";

	/** 默认的 {@code JSON} 日期/时间字段的格式化模式。 */
	public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

	/**
	 * {@code Google Gson} 的
	 * 
	 * <pre>
	 * @Since
	 * </pre>
	 * 
	 * 注解常用的版本号常量 - {@code 1.0}。
	 */
	public static final double SINCE_VERSION_10 = 1.0d;

	/**
	 * {@code Google Gson} 的
	 * 
	 * <pre>
	 * @Since
	 * </pre>
	 * 
	 * 注解常用的版本号常量 - {@code 1.1}。
	 */
	public static final double SINCE_VERSION_11 = 1.1d;

	/**
	 * {@code Google Gson} 的
	 * 
	 * <pre>
	 * @Since
	 * </pre>
	 * 
	 * 注解常用的版本号常量 - {@code 1.2}。
	 */
	public static final double SINCE_VERSION_12 = 1.2d;

	/**
	 * {@code Google Gson} 的
	 * 
	 * <pre>
	 * @Until
	 * </pre>
	 * 
	 * 注解常用的版本号常量 - {@code 1.0}。
	 */
	public static final double UNTIL_VERSION_10 = SINCE_VERSION_10;

	/**
	 * {@code Google Gson} 的
	 * 
	 * <pre>
	 * @Until
	 * </pre>
	 * 
	 * 注解常用的版本号常量 - {@code 1.1}。
	 */
	public static final double UNTIL_VERSION_11 = SINCE_VERSION_11;

	/**
	 * {@code Google Gson} 的
	 * 
	 * <pre>
	 * @Until
	 * </pre>
	 * 
	 * 注解常用的版本号常量 - {@code 1.2}。
	 */
	public static final double UNTIL_VERSION_12 = SINCE_VERSION_12;

	/**
	 * <p>
	 * 
	 * <pre>
	 * JSONUtils
	 * </pre>
	 * 
	 * instances should NOT be constructed in standard programming. Instead, the
	 * class should be used as
	 * 
	 * <pre>
	 * JSONUtils.fromJson(&quot;foo&quot;);
	 * </pre>
	 * 
	 * .
	 * </p>
	 * <p>
	 * This constructor is public to permit tools that require a JavaBean
	 * instance to operate.
	 * </p>
	 */
	public JsonUtil() {
		super();
	}

	/**
	 * 将给定的目标对象根据指定的条件参数转换成 {@code JSON} 格式的字符串。
	 * <p />
	 * <strong>该方法转换发生错误时，不会抛出任何异常。若发生错误时，曾通对象返回
	 * 
	 * <pre>
	 * &quot;{}&quot;
	 * </pre>
	 * 
	 * ； 集合或数组对象返回
	 * 
	 * <pre>
	 * &quot;[]&quot;
	 * </pre>
	 * 
	 * </strong>
	 * 
	 * @param target
	 *            目标对象。
	 * @param targetType
	 *            目标对象的类型。
	 * @param isSerializeNulls
	 *            是否序列化 {@code null} 值字段。
	 * @param version
	 *            字段的版本号注解。
	 * @param datePattern
	 *            日期字段的格式化模式。
	 * @param excludesFieldsWithoutExpose
	 *            是否排除未标注 {@literal @Expose} 注解的字段。
	 * @return 目标对象的 {@code JSON} 格式的字符串。
	 * @since 1.0
	 */
	public static String toJson(Object target, Type targetType,
			boolean isSerializeNulls, Double version, String datePattern,
			boolean excludesFieldsWithoutExpose) {
		if (target == null)
			return EMPTY_JSON;
		GsonBuilder builder = new GsonBuilder();
		if (isSerializeNulls)
			builder.serializeNulls();
		if (version != null)
			builder.setVersion(version.doubleValue());
		if (isBlank(datePattern))
			datePattern = DEFAULT_DATE_PATTERN;
		builder.setDateFormat(datePattern);
		if (excludesFieldsWithoutExpose)
			builder.excludeFieldsWithoutExposeAnnotation();
		return toJson(target, targetType, builder);
	}

	/**
	 * 将给定的目标对象转换成 {@code JSON} 格式的字符串。<strong>此方法只用来转换普通的 {@code JavaBean}
	 * 对象。</strong>
	 * <ul>
	 * <li>该方法只会转换标有 {@literal @Expose} 注解的字段；</li>
	 * <li>该方法不会转换 {@code null} 值字段；</li>
	 * <li>该方法会转换所有未标注或已标注 {@literal @Since} 的字段；</li>
	 * <li>该方法转换时使用默认的 日期/时间 格式化模式 - {@code yyyy-MM-dd HH:mm:ss SSS}；</li>
	 * </ul>
	 * 
	 * @param target
	 *            要转换成 {@code JSON} 的目标对象。
	 * @return 目标对象的 {@code JSON} 格式的字符串。
	 * @since 1.0
	 */
	public static String toJson(Object target) {
		return toJson(target, null, false, null, null, false);
	}

	/**
	 * 将给定的目标对象转换成 {@code JSON} 格式的字符串。<strong>此方法只用来转换普通的 {@code JavaBean}
	 * 对象。</strong>
	 * <ul>
	 * <li>该方法只会转换标有 {@literal @Expose} 注解的字段；</li>
	 * <li>该方法不会转换 {@code null} 值字段；</li>
	 * <li>该方法会转换所有未标注或已标注 {@literal @Since} 的字段；</li>
	 * </ul>
	 * 
	 * @param target
	 *            要转换成 {@code JSON} 的目标对象。
	 * @param datePattern
	 *            日期字段的格式化模式。
	 * @return 目标对象的 {@code JSON} 格式的字符串。
	 * @since 1.0
	 */
	public static String toJson(Object target, String datePattern) {
		return toJson(target, null, false, null, datePattern, true);
	}

	/**
	 * 将给定的目标对象转换成 {@code JSON} 格式的字符串。<strong>此方法只用来转换普通的 {@code JavaBean}
	 * 对象。</strong>
	 * <ul>
	 * <li>该方法只会转换标有 {@literal @Expose} 注解的字段；</li>
	 * <li>该方法不会转换 {@code null} 值字段；</li>
	 * <li>该方法转换时使用默认的 日期/时间 格式化模式 - {@code yyyy-MM-dd HH:mm:ss SSS}；</li>
	 * </ul>
	 * 
	 * @param target
	 *            要转换成 {@code JSON} 的目标对象。
	 * @param version
	 *            字段的版本号注解({@literal @Since})。
	 * @return 目标对象的 {@code JSON} 格式的字符串。
	 * @since 1.0
	 */
	public static String toJson(Object target, Double version) {
		return toJson(target, null, false, version, null, true);
	}

	/**
	 * 将给定的目标对象转换成 {@code JSON} 格式的字符串。<strong>此方法只用来转换普通的 {@code JavaBean}
	 * 对象。</strong>
	 * <ul>
	 * <li>该方法不会转换 {@code null} 值字段；</li>
	 * <li>该方法会转换所有未标注或已标注 {@literal @Since} 的字段；</li>
	 * <li>该方法转换时使用默认的 日期/时间 格式化模式 - {@code yyyy-MM-dd HH:mm:ss SSS}；</li>
	 * </ul>
	 * 
	 * @param target
	 *            要转换成 {@code JSON} 的目标对象。
	 * @param excludesFieldsWithoutExpose
	 *            是否排除未标注 {@literal @Expose} 注解的字段。
	 * @return 目标对象的 {@code JSON} 格式的字符串。
	 * @since 1.0
	 */
	public static String toJson(Object target,
			boolean excludesFieldsWithoutExpose) {
		return toJson(target, null, false, null, null,
				excludesFieldsWithoutExpose);
	}

	/**
	 * 将给定的目标对象转换成 {@code JSON} 格式的字符串。<strong>此方法只用来转换普通的 {@code JavaBean}
	 * 对象。</strong>
	 * <ul>
	 * <li>该方法不会转换 {@code null} 值字段；</li>
	 * <li>该方法转换时使用默认的 日期/时间 格式化模式 - {@code yyyy-MM-dd HH:mm:ss SSS}；</li>
	 * </ul>
	 * 
	 * @param target
	 *            要转换成 {@code JSON} 的目标对象。
	 * @param version
	 *            字段的版本号注解({@literal @Since})。
	 * @param excludesFieldsWithoutExpose
	 *            是否排除未标注 {@literal @Expose} 注解的字段。
	 * @return 目标对象的 {@code JSON} 格式的字符串。
	 * @since 1.0
	 */
	public static String toJson(Object target, Double version,
			boolean excludesFieldsWithoutExpose) {
		return toJson(target, null, false, version, null,
				excludesFieldsWithoutExpose);
	}

	/**
	 * 将给定的目标对象转换成 {@code JSON} 格式的字符串。<strong>此方法通常用来转换使用泛型的对象。</strong>
	 * <ul>
	 * <li>该方法只会转换标有 {@literal @Expose} 注解的字段；</li>
	 * <li>该方法不会转换 {@code null} 值字段；</li>
	 * <li>该方法会转换所有未标注或已标注 {@literal @Since} 的字段；</li>
	 * <li>该方法转换时使用默认的 日期/时间 格式化模式 - {@code yyyy-MM-dd HH:mm:ss SSSS}；</li>
	 * </ul>
	 * 
	 * @param target
	 *            要转换成 {@code JSON} 的目标对象。
	 * @param targetType
	 *            目标对象的类型。
	 * @return 目标对象的 {@code JSON} 格式的字符串。
	 * @since 1.0
	 */
	public static String toJson(Object target, Type targetType) {
		return toJson(target, targetType, false, null, null, true);
	}

	/**
	 * 将给定的目标对象转换成 {@code JSON} 格式的字符串。<strong>此方法通常用来转换使用泛型的对象。</strong>
	 * <ul>
	 * <li>该方法只会转换标有 {@literal @Expose} 注解的字段；</li>
	 * <li>该方法不会转换 {@code null} 值字段；</li>
	 * <li>该方法转换时使用默认的 日期/时间 格式化模式 - {@code yyyy-MM-dd HH:mm:ss SSSS}；</li>
	 * </ul>
	 * 
	 * @param target
	 *            要转换成 {@code JSON} 的目标对象。
	 * @param targetType
	 *            目标对象的类型。
	 * @param version
	 *            字段的版本号注解({@literal @Since})。
	 * @return 目标对象的 {@code JSON} 格式的字符串。
	 * @since 1.0
	 */
	public static String toJson(Object target, Type targetType, Double version) {
		return toJson(target, targetType, false, version, null, true);
	}

	/**
	 * 将给定的目标对象转换成 {@code JSON} 格式的字符串。<strong>此方法通常用来转换使用泛型的对象。</strong>
	 * <ul>
	 * <li>该方法不会转换 {@code null} 值字段；</li>
	 * <li>该方法会转换所有未标注或已标注 {@literal @Since} 的字段；</li>
	 * <li>该方法转换时使用默认的 日期/时间 格式化模式 - {@code yyyy-MM-dd HH:mm:ss SSS}；</li>
	 * </ul>
	 * 
	 * @param target
	 *            要转换成 {@code JSON} 的目标对象。
	 * @param targetType
	 *            目标对象的类型。
	 * @param excludesFieldsWithoutExpose
	 *            是否排除未标注 {@literal @Expose} 注解的字段。
	 * @return 目标对象的 {@code JSON} 格式的字符串。
	 * @since 1.0
	 */
	public static String toJson(Object target, Type targetType,
			boolean excludesFieldsWithoutExpose) {
		return toJson(target, targetType, false, null, null,
				excludesFieldsWithoutExpose);
	}

	/**
	 * 将给定的目标对象转换成 {@code JSON} 格式的字符串。<strong>此方法通常用来转换使用泛型的对象。</strong>
	 * <ul>
	 * <li>该方法不会转换 {@code null} 值字段；</li>
	 * <li>该方法转换时使用默认的 日期/时间 格式化模式 - {@code yyyy-MM-dd HH:mm:ss SSS}；</li>
	 * </ul>
	 * 
	 * @param target
	 *            要转换成 {@code JSON} 的目标对象。
	 * @param targetType
	 *            目标对象的类型。
	 * @param version
	 *            字段的版本号注解({@literal @Since})。
	 * @param excludesFieldsWithoutExpose
	 *            是否排除未标注 {@literal @Expose} 注解的字段。
	 * @return 目标对象的 {@code JSON} 格式的字符串。
	 * @since 1.0
	 */
	public static String toJson(Object target, Type targetType, Double version,
			boolean excludesFieldsWithoutExpose) {
		return toJson(target, targetType, false, version, null,
				excludesFieldsWithoutExpose);
	}

	/**
	 * 将给定的 {@code JSON} 字符串转换成指定的类型对象。<strong>此方法通常用来转换普通的 {@code JavaBean}
	 * 对象。</strong>
	 * 
	 * @param <T>
	 *            要转换的目标类型。
	 * @param json
	 *            给定的 {@code JSON} 字符串。
	 * @param clazz
	 *            要转换的目标类。
	 * @param datePattern
	 *            日期格式模式。
	 * @return 给定的 {@code JSON} 字符串表示的指定的类型对象。
	 * @since 1.0
	 */
	public static <T> T fromJson(String json, Class<T> clazz, String datePattern) {
		if (isBlank(json)) {
			return null;
		}
		GsonBuilder builder = new GsonBuilder();
		if (isBlank(datePattern)) {
			builder.setDateFormat(DEFAULT_DATE_PATTERN);
		}
		Gson gson = builder.create();
		try {
			return gson.fromJson(json, clazz);
		} catch (Exception ex) {
			String aa = json + " 无法转换为 " + clazz.getName() + " 对象!"+"aaaaaa"+ex.getMessage();
			 Log.e(json + " 无法转换为 " + clazz.getName() + " 对象!",ex.getMessage());
			return null;
		}
	}
	
	/**
	 * 将给定的 {@code JSON} 字符串转换成指定的类型对象。
	 * 
	 * @param <T>
	 *            要转换的目标类型。
	 * @param json
	 *            给定的 {@code JSON} 字符串。
	 * @param token
	 *            {@code com.google.gson.reflect.TypeToken} 的类型指示类对象。
	 * @return 给定的 {@code JSON} 字符串表示的指定的类型对象。
	 * @since 1.0
	 */
	public static <T> T fromJson(String json, TypeToken<T> token) {
		return fromJson(json, token, null);
	}
	
	/**
	 * 将给定的 {@code JSON} 字符串转换成指定的类型对象。
	 * 
	 * @param <T>
	 *            要转换的目标类型。
	 * @param json
	 *            给定的 {@code JSON} 字符串。
	 * @param token
	 *            {@code com.google.gson.reflect.TypeToken} 的类型指示类对象。
	 * @param datePattern
	 *            日期格式模式。
	 * @return 给定的 {@code JSON} 字符串表示的指定的类型对象。
	 * @since 1.0
	 */
	public static <T> T fromJson(String json, TypeToken<T> token,
			String datePattern) {
		if (isBlank(json)) {
			return null;
		}
		GsonBuilder builder = new GsonBuilder();
		builder.setDateFormat(DateFormat.LONG);
		builder.registerTypeAdapter(Timestamp.class, new DateTimestampTypeAdapter());
		builder.registerTypeAdapter(Date.class, new DateTypeAdapter());
		if (isBlank(datePattern)) {
			builder.setDateFormat(DEFAULT_DATE_PATTERN);
		}
		Gson gson = builder.create();
		try {
			return gson.fromJson(json, token.getType());
		} catch (Exception ex) {
			ex.printStackTrace();
			// Log.e(json + " 无法转换为 " + token.getRawType().getName() + " 对象!",
			// ex.getMessage());
			return null;
		}
	}

	/**
	 * 将给定的 {@code JSON} 字符串转换成指定的类型对象。<strong>此方法通常用来转换普通的 {@code JavaBean}
	 * 对象。</strong>
	 * 
	 * @param <T>
	 *            要转换的目标类型。
	 * @param json
	 *            给定的 {@code JSON} 字符串。
	 * @param clazz
	 *            要转换的目标类。
	 * @return 给定的 {@code JSON} 字符串表示的指定的类型对象。
	 * @since 1.0
	 */
	public static <T> T fromJson(String json, Class<T> clazz) {
		return fromJson(json, clazz, null);
	}

	/**
	 * 将给定的目标对象根据{@code GsonBuilder} 所指定的条件参数转换成 {@code JSON} 格式的字符串。
	 * <p />
	 * 该方法转换发生错误时，不会抛出任何异常。若发生错误时，{@code JavaBean} 对象返回
	 * 
	 * <pre>
	 * &quot;{}&quot;
	 * </pre>
	 * 
	 * ； 集合或数组对象返回
	 * 
	 * <pre>
	 * &quot;[]&quot;
	 * </pre>
	 * 
	 * 。 其本基本类型，返回相应的基本值。
	 * 
	 * @param target
	 *            目标对象。
	 * @param targetType
	 *            目标对象的类型。
	 * @param builder
	 *            可定制的{@code Gson} 构建器。
	 * @return 目标对象的 {@code JSON} 格式的字符串。
	 * @since 1.1
	 */
	public static String toJson(Object target, Type targetType,
			GsonBuilder builder) {
		if (target == null)
			return EMPTY_JSON;
		Gson gson = null;
		if (builder == null) {
			gson = new Gson();
		} else {
			gson = builder.create();
		}
		String result = EMPTY_JSON;
		try {
			if (targetType == null) {
				result = gson.toJson(target);
			} else {
				result = gson.toJson(target, targetType);
			}
		} catch (Exception ex) {
			// Log.w("目标对象 " + target.getClass().getName() +
			// " 转换 JSON 字符串时，发生异常！", ex.getMessage());
			if (target instanceof Collection<?>
					|| target instanceof Iterator<?>
					|| target instanceof Enumeration<?>
					|| target.getClass().isArray()) {
				result = EMPTY_JSON_ARRAY;
			}
		}
		return result;
	}

	private static boolean isBlank(String text) {
		return null == text || "".equals(text.trim());
	}

	// ////////////////////////////////////////

	/**
	 * 将 Map 类型数据转换成json
	 * 
	 * @param map
	 *            需要转换的 Map 数据
	 * @return
	 */
	public static String map2Json(Map<String, Object> map) {
		Gson gson = new Gson();
		String json = gson.toJson(map, Map.class);
		return json;
	}

	/**
	 * 将 List 类型数据转换成json
	 * 
	 * @param list
	 *            需要转换的 List 数据
	 * @return
	 */
	public static String listMap2Json(List<Map<String, Object>> list) {
		Gson gson = new Gson();
		String json = "";
		try {
			 json = gson.toJson(list, List.class);
		} catch (Exception e) {
			e.printStackTrace();
			return "[]";
		}

		return json;
	}

	/**
	 * 将 List 类型数据转换成json，并移除最外两边的中括号
	 * 
	 * @param list
	 *            需要转换的 List 数据
	 * @return
	 */
	public static String listMap2JsonRemoveArrayBracket(
			List<Map<String, Object>> list) {
		String json = listMap2Json(list);
		json = json.substring(1, json.length() - 1);
		return json;
	}
	
	
	public static List<Map<String,Object>> transMapObject2Str(List<Map<String, Object>> list){
		if (list != null && list.size() > 0) {
			List<Map<String,Object>> entryList = new ArrayList<Map<String,Object>>();
			for (Map<String, Object> li : list) {
				Iterator<Entry<String, Object>> iteratorMap = li.entrySet().iterator();
				Map<String, Object> map = new HashMap<String,Object>();
				while (iteratorMap.hasNext()) {
					Entry<String, Object> entry = iteratorMap.next();
					map.put(entry.getKey(),String.valueOf(entry.getValue() == null?"":entry.getValue()));
				}
				entryList.add(map);
			}
			return entryList;
		}
		return null;
	}
	
	/**
	 * 测试用
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ryxxdjsj", "2012-02-27 19:25:20.0");
		map.put("name", "2012-");
		map.put("name1",1);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list.add(map);
//		 System.out.println(listMap2JsonRemoveArrayBracket(list));
		// String json =
		// "[{'id': '1', 'czyid': '2', 'zhid':'3', 'xcrq': '2010-09-22 00:00:00', 'xcsj': '2014-08-29 00:00:00'}]";
		// List<RcjcGps> list1 = JsonUtils.fromJson(json,new
		// TypeToken<List<RcjcGps>>() {});
		Gson gson = new Gson();
		System.out.println(gson.toJson(list));

	}
	
	
	

}

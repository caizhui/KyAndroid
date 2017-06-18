package com.ky.kyandroid;

/**
 * 类名称：常量类
 */
public class Constants {
	
	/** 服务器连接基础Url */

	public final  static String SERVICE_BASE_URL="http://192.168.1.100:8080/ft/";

	/** 成功标识 */
	public final static String SUCCEED = "SUCCEED";
	
	/** 失败标识 */
	public final static String FAILURE = "FAIL";

	//登录接口
    public final static String SERVICE_LOGIN = "kyAndroid/login.action";

	//查询事件接口
	public final static String SERVICE_QUERY_EVENTENTRY= "kyAndroid/sjList.action";

	//保存事件接口
	public final static String SERVICE_SAVE_EVENTENTRY= "kyAndroid/sjSave.action";

	//事件详情接口
	public final static String SERVICE_DETAIL_EVENT= "kyAndroid/sjDetial.action";

    //保存字典项接口
    public final static String SERVICE_QUERY_DESC= "kyAndroid/getDictAll.action";

}

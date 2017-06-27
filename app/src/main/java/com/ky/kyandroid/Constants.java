package com.ky.kyandroid;

/**
 * 类名称：常量类
 */
public class Constants {
	
	/** 服务器连接基础Url */

	public static String SERVICE_BASE_URL="http://192.168.1.101:8080/";

	/** 成功标识 */
	public final static String SUCCESS = "SUCCESS";

	/** 失败标识 */
	public final static String FAILURE = "FAIL";

	//登录接口
    public final static String SERVICE_LOGIN = "ft/kyAndroid/login.action";

	//查询事件接口
	public final static String SERVICE_QUERY_EVENTENTRY= "ft/kyAndroid/sjList.action";

	//保存事件接口
	public final static String SERVICE_SAVE_EVENTENTRY= "ft/kyAndroid/sjSave.action";

	//事件详情接口
	public final static String SERVICE_DETAIL_EVENT= "ft/kyAndroid/sjDetial.action";

	//修改事件状态接口
	public final static String SERVICE_EDIT_EVENT= "ft/kyAndroid/sjEdit.action";

    //保存字典项接口
    public final static String SERVICE_QUERY_DESC= "ft/kyAndroid/getDictAll.action";

	//查询我的任务接口
	public final static String SERVICE_QUERY_TASK= "ft/kyAndroid/myTasks.action";

	//修改我的任务状态接口
	public final static String SERVICE_QUERY_TASKRECV= "ft/kyAndroid/taskRecv.action";

	//街道自行处理接口
	public final static String SERVICE_TASK_HADLE= "ft/kyAndroid/taskHandle.action";

	//测试提交成功与否
	public final static String TEST_SUCCESS_MSG= "小鸡追追女仔";

}

package com.ky.kyandroid;

/**
 * 类名称：常量类
 */
public class Constants {
	
	/** 服务器连接基础Url */

	public static String SERVICE_BASE_URL="http://192.168.1.103:8080/";

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

	//自行处理 - 反馈
	public final static String SERVICE_ZXCLBANJ_EVENT= "ft/kyAndroid/zxclBanj.action";

	//任务退回状态接口
	public final static String SERVICE_TASK_BACK= "ft/kyAndroid/taskBack.action";

	//任务申请延期状态接口
	public final static String SERVICE_EDIT_YANQI= "ft/kyAndroid/taskYanqi.action";

	//任务申请办结状态接口
	public final static String SERVICE_EDIT_BANJI= "ft/kyAndroid/taskBanj.action";

    //获取字典项接口
    public final static String SERVICE_QUERY_DESC= "ft/kyAndroid/getDictAll.action";

    //获取区域列表接口
    public final static String SERVICE_QUERY_QH= "ft/kyAndroid/ftQhList.action";

	//查询我的任务接口
	public final static String SERVICE_QUERY_TASK= "ft/kyAndroid/myTasks.action";

	//修改我的任务状态接口
	public final static String SERVICE_QUERY_TASKRECV= "ft/kyAndroid/taskRecv.action";

	//任务处理接口
	public final static String SERVICE_TASK_HADLE= "ft/kyAndroid/taskHandle.action";

	//街道处理接口
	public final static String SERVICE_ZXCL_HADLE= "ft/kyAndroid/zxclDispatch.action";

	//消息最新条目数处理接口
	public final static String SERVICE_NOTICE_NUM_HADLE= "ft/kyAndroid/msgNotice.action";

	//消息最新修改处理接口
	public final static String SERVICE_NOTICE_EDIT_HADLE= "ft/kyAndroid/msgEdit.action";

	//消息最新延期申请处理
	public final static String SERVICE_NOTICE_EXTENSION_HADLE= "ft/kyAndroid/msgExtension.action";

	//消息最新查询处理接口
	public final static String SERVICE_NOTICE_LIST_HADLE= "ft/kyAndroid/msgList.action";

	//街道派遣处理接口
	public final static String SERVICE_TASK_DISPATCH= "ft/kyAndroid/taskDispatch.action";

	//街道派遣保存处理接口
	public final static String SERVICE_TASK_DISPATCH_SAVE= "ft/kyAndroid/taskDispatchSave.action";

	//街道派遣保存处理接口
	public final static String SERVICE_TASK_DISPATCH_DELETE= "ft/kyAndroid/taskDispatchDelete.action";

	//坐标点保存接口
	public final static String SERVICE_LOCATION_SAVE = "ft/kyAndroid/jwSave.action";


	/**督查督办start**/

	//督查督办查询接口
	public final static String SERVICE_DBLIST = "ft/kyAndroid/dbList.action";

	//督查督办操作接口
	public final static String SERVICE_DBDBEXCEUTE= "ft/kyAndroid/dbdbExecute.action";

	/**督查督办end**/

}

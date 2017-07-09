package com.ky.kyandroid.util;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 类名称：服务工具类<br/>
 * 类描述：<br/>
 * 
 * 创建人： Cz <br/>
 * 创建时间：2016年10月20日 上午9:56:44 <br/>
 *
 */
public class ServiceUtil {

	/** TAG */
	private static final String TAG = "ServiceUtil";
	
	/**
	 * 是否当前服务在运行
	 * 
	 */
	private static boolean isServiceRunning(Context context, String className) {
		boolean isRunning = false;
		// 获取ActivityManager服务
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceInfos = activityManager.getRunningServices(50);

		if (null == serviceInfos || serviceInfos.size() < 1) {
			return false;
		}

		for (int i = 0; i < serviceInfos.size(); i++) {
			if (serviceInfos.get(i).service.getClassName().contains(className)) {
				isRunning = true;
				break;
			}
		}
		Log.i(TAG,"_isServiceRunning_" + className + " isRunning =  " + isRunning);
		return isRunning;
	}

	/**
	 * 执行定时Alarm
	 * 
	 */
	public static void invokeAlarmService(Context context,Class<?> serviceClazz,String action,long intervalMillis) {
		Log.i(TAG,"_invokeTimerPOIService_,服务开启...");
		if (!isServiceRunning(context,action)) {
			PendingIntent alarmSender = null;
			Intent startIntent = new Intent(context, serviceClazz);
			startIntent.setAction(action);
			try {
				alarmSender = PendingIntent.getService(context, 0, startIntent,PendingIntent.FLAG_UPDATE_CURRENT);
			} catch (Exception e) {
				Log.i(TAG, "_invokeTimerPOIService_服务开启...失败." +  e.toString());
			}
			// 定时管理Alarm
			AlarmManager am = (AlarmManager) context.getSystemService(Activity.ALARM_SERVICE);
			am.setInexactRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(), intervalMillis, alarmSender);
		}
	}

	/**
	 * 取消定时
	 * 
	 */
	public static void cancleAlarmManager(Context context,Class<?> serviceClazz,String action) {
		Log.i(TAG, "cancleAlarmManager - 取消PendingIntent...");
		Intent intent = new Intent(context, serviceClazz);
		intent.setAction(action);
		// 封装动作Intent
		PendingIntent pendingIntent = PendingIntent.getService(context, 0,intent, PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager alarm = (AlarmManager) context.getSystemService(Activity.ALARM_SERVICE);
		alarm.cancel(pendingIntent);
	}

}

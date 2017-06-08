package com.ky.kyandroid;

import android.app.Application;

import org.xutils.x;

/**
 * Created by caizhui on 2016-11-28.
 * 备注：全局应用程序类：用于保存和调用全局应用配置
 */
public class AppContext extends Application {
	
		/** AppContext */
		private static AppContext sApplication = null;
	
	@Override
	public void onCreate() {
		super.onCreate();
		sApplication = this;
		// 初始化XUtils3
		x.Ext.init(this);
		// 设置debug模式
		x.Ext.setDebug(true);
	}
	
	/**
	 * 获取Application实例
	 * 
	 * @return
	 */
	public static AppContext getInstance() {
		if (sApplication == null) {
			throw new IllegalStateException("Application is not created.");
		}
		return sApplication;
	}
}

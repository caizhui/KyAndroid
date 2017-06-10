package com.ky.kyandroid.bean;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

import java.lang.reflect.Method;

/**
 * 类名称：网络连接工具类<br/>
 * 类描述：<br/>
 * 
 * 创建人： Cz <br/>
 * 创建时间：2016年9月22日 下午4:33:42 <br/>
 * @updateRemark 修改备注：
 *     
 */
public class NetWorkConnection {

	/** ConnectivityManager */
	private static ConnectivityManager mConnectivityManager = null;

	public NetWorkConnection(Context context) {
		mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	}

	/**
	 * 开GPRS
	 */
	public void setMobileNetEnable() {

		Object[] arg = null;
		try {
			boolean isMobileDataEnable = invokeMethod("getMobileDataEnabled",
					arg);
			if (!isMobileDataEnable) {
				invokeBooleanArgMethod("setMobileDataEnabled", true);

			}
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	/**
	 *  关RPRS
	 */
	public void setMobileNetUnable() {
		Object[] arg = null;
		try {
			boolean isMobileDataEnable = invokeMethod("getMobileDataEnabled",
					arg);
			if (isMobileDataEnable) {
				invokeBooleanArgMethod("setMobileDataEnabled", false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param methodName
	 * @param arg
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean invokeMethod(String methodName, Object[] arg)
			throws Exception {
		Class ownerClass = mConnectivityManager.getClass();

		Class[] argsClass = null;
		if (arg != null) {
			argsClass = new Class[1];
			argsClass[0] = arg.getClass();
		}

		Method method = ownerClass.getMethod(methodName, argsClass);
		Boolean isOpen = (Boolean) method.invoke(mConnectivityManager, arg);

		return isOpen;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object invokeBooleanArgMethod(String methodName, boolean value)
			throws Exception {

		Class ownerClass = mConnectivityManager.getClass();

		Class[] argsClass = new Class[1];
		argsClass[0] = boolean.class;

		Method method = ownerClass.getMethod(methodName, argsClass);

		return method.invoke(mConnectivityManager, value);
	}
	
	/**
	 * 是否是WIFI连接
	 * @return
	 */
	public boolean isWIFIConnection(){
		NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
		// 注意，这个判断一定要的哦，要不然会出错
		if (networkInfo != null) { 
			return networkInfo.isAvailable();
		}
		return false;
	}

	/**
	 * 检测是否有网络
	 * */
	public boolean checkNetWork() {
		boolean flag = true;
		// 判断wifi是否打开
		if (checkWifiStatus()) {
			if (!isWifiConnected()) {
				flag = false;
			}
		} else if (checkGprsStatus()) {
			if (!isMobileConnected()) {
				flag = false;
			}
		} else {
			flag = false;
		}
		return flag;
	}

	/** 
	 * Check the Wifi is open or not. 
	 * 
	 */
	public boolean checkWifiStatus() {
		State wifi = mConnectivityManager.getNetworkInfo(
				ConnectivityManager.TYPE_WIFI).getState();
		if (wifi == State.CONNECTED || wifi == State.CONNECTING) {
			return true;
		}
		return false;
	}

	/** 
	 * Check the Gprs is open or not.
	 * 
	 */
	public boolean checkGprsStatus() {
		State gprs = mConnectivityManager.getNetworkInfo(
				ConnectivityManager.TYPE_MOBILE).getState();
		if (gprs == State.CONNECTED || gprs == State.CONNECTING) {
			return true;
		}
		return false;
	}

	/** 
	 * 判断WIFI网络是否可用
	 * 
	 */
	public boolean isWifiConnected() {
		NetworkInfo mWiFiNetworkInfo = mConnectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (mWiFiNetworkInfo != null) {
			return mWiFiNetworkInfo.isAvailable();
		}
		return false;
	}

	/**
	 * 判断MOBILE网络是否可用
	 * 
	 */
	public boolean isMobileConnected() {
		NetworkInfo mMobileNetworkInfo = mConnectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (mMobileNetworkInfo != null) {
			return mMobileNetworkInfo.isAvailable();
		}
		return false;
	}

}

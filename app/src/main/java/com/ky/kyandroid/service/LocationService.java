package com.ky.kyandroid.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import android.app.IntentService;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.ky.kyandroid.AppContext;
import com.ky.kyandroid.Constants;
import com.ky.kyandroid.activity.LoginActivity;
import com.ky.kyandroid.bean.AckMessage;
import com.ky.kyandroid.entity.LocationEntity;
import com.ky.kyandroid.util.JsonUtil;
import com.ky.kyandroid.util.OkHttpUtil;
import com.ky.kyandroid.util.ReflectUtil;
import com.ky.kyandroid.util.SpUtil;
import com.ky.kyandroid.util.StringUtils;

/**
 * 类名称：坐标服务<br/>
 * 类描述：<br/>
 * 
 * 创建人： Cz <br/>
 * 创建时间：2016年10月19日 下午6:01:11 <br/>
 * @updateRemark 修改备注：
 *     
 */
public class LocationService extends IntentService {

	/** String */
	public static final String LOCATION_SERVICE_NAME = "com.ky.kyandroid.service.LocationService";

	/** 开启时间隔间 */
	public static final long START_INTERVAL = 15 * 60 * 1000;

	/** 延迟开启时间 */
	public static final int DELAY_INTERVAL = 1 * 60 * 1000;

	/** 标识 */
	private static final String TAG = "LocationService";

	/** LocationClient */
	private LocationClient mLocationClient = null;
	/** 定位监听 */
	public BDLocationListener myListener = null;

	/** SharedPreferences */
	private SharedPreferences sp;
	/** 同步锁 */
	private Object objLock = new Object();

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			// 提示信息
			String message = String.valueOf(msg.obj);
			switch (msg.what) {
			case 0:
				Log.i(TAG, "坐标保存异常 >> ... " + message);
				break;
			case 1:
				Log.i(TAG, "访问成功,解释坐标中 >> ...  ");
				handleTransation(message);
				break;
			default:
				break;
			}
		};
	};

	@Override
	public void onCreate() {
		super.onCreate();
		synchronized (objLock) {
			sp = SpUtil.getSharePerference(AppContext.getInstance());
			if (mLocationClient == null) {
				// 初始监听
				myListener = new MyLocationListener();
				// 声明LocationClient类
				mLocationClient = new LocationClient(AppContext.getInstance());
				// 注册监听
				mLocationClient.registerLocationListener(myListener);
				// 初始化属性
				initLocation();
			}
		}
	}

	public LocationService() {
		super(TAG);
	}

	/**
	 * 特殊说明:
	 * LocationMode.Hight_Accuracy</br>
	 * 1.高精度定位模式：这种定位模式下，会同时使用网络定位和GPS定位，优先返回最高精度的定位结果；</br>
	 * 2.低功耗定位模式：这种定位模式下，不会使用GPS进行定位，只会使用网络定位（WiFi定位和基站定位）；</br>
	 * 3.仅用设备定位模式：这种定位模式下，不需要连接网络，只使用GPS进行定位，这种模式下不支持室内环境的定位。
	 */
	private void initLocation() {
		LocationClientOption mOption = new LocationClientOption();
		mOption = new LocationClientOption();
		mOption.setLocationMode(LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		mOption.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
		mOption.setScanSpan(LocationService.DELAY_INTERVAL);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		mOption.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
		mOption.setIsNeedLocationDescribe(true);// 可选，设置是否需要地址描述
		mOption.setNeedDeviceDirect(false);// 可选，设置是否需要设备方向结果
		mOption.setLocationNotify(false);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
		mOption.setIgnoreKillProcess(true);// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
		mOption.setIsNeedLocationDescribe(true);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
		mOption.setIsNeedLocationPoiList(true);// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
		mOption.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
		mOption.setIsNeedAltitude(false);// 可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用
		mLocationClient.setLocOption(mOption);
	}

	/**
	 * 开启
	 */
	private void startLocationClient() {
		synchronized (objLock) {
			if (mLocationClient != null && !mLocationClient.isStarted()) {
				mLocationClient.start();
				mLocationClient.requestLocation();
			}
		}
	}

	/**
	 * 停止
	 */
	private void stopLocationClient() {
		synchronized (objLock) {
			if (mLocationClient != null && mLocationClient.isStarted()) {
				mLocationClient.stop();
			}
		}
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Context app = AppContext.getInstance();
		if (!isOPen(app)) {
			// 强制开户位置服务
			openGPS(app);
		}
		// 开启定位服务
		startLocationClient();
		Log.i(TAG, "定位开始...是否启动成功: " + mLocationClient.isStarted());
	}

	
	/** 
	* 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的 
	* @param context 
	* @return true 表示开启 
	*/
	private static final boolean isOPen(final Context context) {
		LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		// 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
		boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		// 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
		boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		if (gps || network) {
			return true;
		}
		return false;
	}

	/** 
	 * 强制帮用户打开GPS 
	 * @param context 
	 */
	private static final void openGPS(Context context) {
		try {
			Intent GPSIntent = new Intent();
			GPSIntent.setClassName("com.android.settings",
					"com.android.settings.widget.SettingsAppWidgetProvider");
			GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
			GPSIntent.setData(Uri.parse("custom:3"));
			PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
		} catch (CanceledException e) {

		}
	}

	/**
	 * 发送信息执行处理
	 * 
	 * @param location
	 */
	private LocationEntity createLocationEntity(BDLocation location) {
		StringBuffer sb = new StringBuffer();
		sb.append("半径: ");// 半径
		sb.append(location.getRadius());
		sb.append(";国家码: ");// 国家码
		sb.append(location.getCountryCode());
		sb.append(";国家名称: ");// 国家名称
		sb.append(location.getCountry());
		sb.append(";城市编码: ");// 城市编码
		sb.append(location.getCityCode());
		sb.append(";城市: ");// 城市
		sb.append(location.getCity());
		sb.append(";区: ");// 区
		sb.append(location.getDistrict());
		sb.append(";街道 : ");// 街道
		sb.append(location.getStreet());
		sb.append(";地址信息: ");// 地址信息
		sb.append(location.getAddrStr());
		sb.append("用户室内外判断结果: ");// *****返回用户室内外判断结果*****
		sb.append(location.getUserIndoorState());
		LocationEntity entity = new LocationEntity();
		entity.setLng(String.valueOf(location.getLongitude()));
		entity.setLat(String.valueOf(location.getLatitude()));
		entity.setCjsj(location.getTime());
		entity.setUid(sp.getString(LoginActivity.USER_ID, ""));
		entity.setBz(sb.toString());
		return entity;
	}

	/**
	 * 处理后续流程
	 * 
	 * @param
	 */
	private void handleTransation(String body) {
		if (StringUtils.isBlank(body)) {
			Log.i(TAG, "解释响应body失败...");
		} else {
			// 处理响应信息
			AckMessage ackMsg = JsonUtil.fromJson(body, AckMessage.class);
			if (AckMessage.SUCCESS.equals(ackMsg.getAckCode())) {
				Log.i(TAG, "坐标信息保存成功...");
				stopLocationClient();
			} else {
				Log.i(TAG, "坐标信息保存失败...");
			}
		}
	}

	/**
	 * 坐标结果监听
	 * 
	 * 创建人： Cz <br/>
	 * 创建时间：2016年10月19日 下午6:18:23 <br/>
	 *
	 */
	class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			try {
				Log.i(TAG, "响应时间：" + location.getTime());
				Log.i(TAG, "结果类型：" + location.getLocType());
				Log.i(TAG, "经度坐标:" + location.getLongitude());
				Log.i(TAG, "纬度坐标:" + location.getLatitude());
				Log.i(TAG, "定位地址:" + location.getAddrStr());
				Log.i(TAG, "详细地址:" + location.getLocationDescribe());
				if (!location.isIndoorLocMode()) {
					// 开启室内定位模式，只有支持室内定位功能的定位SDK版本才能调用该接口
					mLocationClient.startIndoorMode();
					Log.i(TAG, "开启室内定位...");
				}
				if (location.getLocType() == BDLocation.TypeGpsLocation || location.getLocType() == BDLocation.TypeNetWorkLocation) {
					LocationEntity entity = createLocationEntity(location);
					// 没登录前不记录坐标
					if (StringUtils.isNotBlank(entity.getUid())){
						Map<String, String> params = new HashMap<String,String>();
						params.put("jsonData",JsonUtil.toJson(entity));
						OkHttpUtil.sendRequest(Constants.SERVICE_LOCATION_SAVE, params,
								new Callback() {
									@Override
									public void onResponse(Call arg0,Response response) throws IOException {
										Message msg = new Message();
										msg.what = 0;
										if (response.isSuccessful()) {
											msg.what = 1;
											msg.obj = response.body().string();
										} else {
											msg.obj = "网络异常,坐标保存失败...";
										}
										mHandler.sendMessage(msg);
									}

									@Override
									public void onFailure(Call arg0,IOException arg1) {
										Log.i(TAG,"坐标保存失败,原因>>:" + arg1.getMessage());
									}
								});
					}
				}

			} catch (Exception e) {
				Log.i(TAG, "逻辑发生异常,终止定位...");
				stopLocationClient();
			}
		}

		@Override
		public void onConnectHotSpotMessage(String s, int i) {
			Log.i(TAG, i + "网络不可用,坐标获取中止..." + s);
		}

	}
}

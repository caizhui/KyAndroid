package com.ky.kyandroid.service;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.ky.kyandroid.Constants;
import com.ky.kyandroid.bean.AckMessage;
import com.ky.kyandroid.db.BaseDao;
import com.ky.kyandroid.db.dao.DescEntityDao;
import com.ky.kyandroid.db.dao.TFtQhEntityDao;
import com.ky.kyandroid.entity.DescEntity;
import com.ky.kyandroid.entity.TFtQhEntity;
import com.ky.kyandroid.util.JsonUtil;
import com.ky.kyandroid.util.OkHttpUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SaveBDdescService extends IntentService{
	
	/** 标识 */
	private static final String TAG = "SaveBDdescService";
	
	@SuppressLint("HandlerLeak")
	private Handler mHandler=new Handler(){
		public void handleMessage(Message msg) {
			// 提示信息
			String message = String.valueOf(msg.obj == null ? "系统繁忙,请稍后再试" : msg.obj);
			switch (msg.what) {
			case 0:
				Log.i(TAG, "数据请求失败 >>: " + message);
				break;
			case 1:
				Log.i(TAG, "字典项解释 >>: " + message);
				HandleDescData(message);
				break;
			case 2:
				Log.i(TAG, "区域列表解释 >>: " + message);
				HandleQhData(message);
				break;
			default:
				break;
			}
		};
	};

	public SaveBDdescService() {
		super("SaveBDdescService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// 字典列表数据请求
		initRequestHandle(new DescEntityDao(),Constants.SERVICE_QUERY_DESC,null,1);
		// 区域列表数据请求
		initRequestHandle(new TFtQhEntityDao(),Constants.SERVICE_QUERY_QH,null,2);
	}



	/**
	 * 处理数据
	 *
	 * @param url
	 * @param paramsMap
	 * @param mWhat
	 */
	private void initRequestHandle(BaseDao basedao, String url, Map<String, String> paramsMap, final int mWhat){
		if (basedao != null){
			if(!basedao.ifExist()){
				// 发送请求
				OkHttpUtil.sendRequest(url, paramsMap,new Callback() {
					@Override
					public void onResponse(Call arg0, Response response) throws IOException {
						Message msg = new Message();
						msg.what = 0;
						if (response.isSuccessful()) {
							msg.what = mWhat;
							msg.obj = response.body().string();
						} else {
							msg.obj = "网络异常,请确认网络情况";
						}
						mHandler.sendMessage(msg);
					}
					@Override
					public void onFailure(Call arg0, IOException arg1) {
						mHandler.sendEmptyMessage(0);
					}
				});
			}
		}
	}
	
	/**
	 * 字典处理返回的数据
	 * @param response
	 */
	public void HandleDescData(String response){
		AckMessage ackMessage = JsonUtil.fromJson(response, AckMessage.class);
		if(AckMessage.SUCCESS.equals(ackMessage.getAckCode())){
			//成功后操作 - 解释数据 
			List<DescEntity> descList=new ArrayList<DescEntity>();
			List<?> list = ackMessage.getData();
			for (int i = 0; i < list.size(); i++) {
				String entityStr = JsonUtil.toJson(list.get(i));
				DescEntity bd_desc=JsonUtil.fromJson(entityStr, DescEntity.class);
				String []code =String.valueOf(bd_desc.getCode()).split("\\.");
				if(code.length>0){
					bd_desc.setCode(code[0]);
				}
				descList.add(bd_desc);
			}
			DescEntityDao descDao=new DescEntityDao();
			for (int i = 0; i < descList.size(); i++) {
				descDao.saveDescEntity(descList.get(i));
			}
			
		}else if(Constants.FAILURE.equals(ackMessage.getAckCode())){
			//失败后操作
		}
	}

	/**
	 * 区域处理返回的数据
	 * @param response
	 */
	public void HandleQhData(String response){
		AckMessage ackMessage = JsonUtil.fromJson(response, AckMessage.class);
		if(AckMessage.SUCCESS.equals(ackMessage.getAckCode())){
			//成功后操作 - 解释数据
			List<?> list = ackMessage.getData();
			TFtQhEntityDao dbDao = new TFtQhEntityDao();
			for (int i = 0; i < list.size(); i++) {
				String entityStr = JsonUtil.toJson(list.get(i));
				TFtQhEntity dbEntity = JsonUtil.fromJson(entityStr, TFtQhEntity.class);
				if(dbEntity != null){
					dbDao.saveQhEntity(dbEntity);
				}
			}
		}else if(Constants.FAILURE.equals(ackMessage.getAckCode())){
			//失败后操作
		}
	}

}

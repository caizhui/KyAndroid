package com.ky.kyandroid.service;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.ky.kyandroid.Constants;
import com.ky.kyandroid.bean.AckMessage;
import com.ky.kyandroid.db.dao.DescEntityDao;
import com.ky.kyandroid.entity.DescEntity;
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
			Log.i(TAG, "字典项解释 >>: " + message);
			switch (msg.what) {
			case 0:
				break;
			case 1:
				HandleData(message);
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
		//创建字典表，如果存在则不创建
		DescEntityDao descDao=new DescEntityDao();
		if(!descDao.ifExist()){
			Map<String, String> paramsMap = new HashMap<String, String>();
			paramsMap.put("type", "");
			// 发送请求
			OkHttpUtil.sendRequest(Constants.SERVICE_QUERY_DESC, paramsMap,new Callback() {
				@Override
				public void onResponse(Call arg0, Response response)
						throws IOException {
					Message msg = new Message();
					msg.what = 0;
					if (response.isSuccessful()) {
						msg.what = 1;
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
	
	
	/**
	 * 处理返回的数据
	 * @param response
	 */
	public void HandleData(String response){
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

}

package com.ky.kyandroid.util;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 
 * 类名称：<br/>
 * 类描述：<br/>
 * 
 * 创建人： caizhui <br/>
 * 创建时间：2016年9月26日 下午3:39:18 <br/>
 * @param <>
 * 
 * @updateRemark 修改备注：刷新工具类
 *
 */
public class SwipeRefreshUtil {

	private OnRefreshListener listener;

	private SwipeRefreshLayout swipeRefreshLayout;

	private String url;

	private Handler handler;

	public SwipeRefreshUtil(SwipeRefreshLayout swipeRefreshLayout, String url,
			Handler handler) {
		this.swipeRefreshLayout = swipeRefreshLayout;
		// 设置转圈颜色
		this.swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#ff00ddff"),
				Color.parseColor("#ff99cc00"), Color.parseColor("#ffffbb33"),
				Color.parseColor("#ffff4444"));
		this.url = url;
		this.handler = handler;
	}

	/**
	 * 取消圈圈
	 */
	public void dismissRefreshing() {
		// 取消圈圈
		swipeRefreshLayout.post(new Runnable() {
			@Override
			public void run() {
				swipeRefreshLayout.setRefreshing(false);
			}
		});
	}
	
	/**
	 * 显示圈圈
	 */
	public void showRefreshing() {
		// 取消圈圈
		swipeRefreshLayout.post(new Runnable() {
			@Override
			public void run() {
				swipeRefreshLayout.setRefreshing(true);
			}
		});
	}
	
	/**
	 * 设置监听
	 * 
	 * @param mListener
	 */
	public void setRefreshListener(OnRefreshListener mListener){
		swipeRefreshLayout.setOnRefreshListener(mListener);
	}

	/**
	 * 刷新加载 -- 房屋刷新加载暂没使用此方法
	 */
	public void setSwipeRefresh(final Map<String, String> map, final int type) {
		// type 1表示刷新，2表示加载
		if ("1".equals(type + "")) {
			listener = new OnRefreshListener() {
				@Override
				public void onRefresh() {
					//刷新都是拿最前的几条，所有当前页永远是第一页
					map.put("currentPage", 1+"");
					refreshSetDate(map,type);
				}
			};
	
			swipeRefreshLayout.setOnRefreshListener(listener);
	
			// 刷新
			swipeRefreshLayout.post(new Runnable() {
				@Override
				public void run() {
					swipeRefreshLayout.setRefreshing(true);
				}
			});
			listener.onRefresh();
		} else {
			refreshSetDate(map, type);
		}
	}

	/**
	 * 刷新加载
	 * 
	 * @param map
	 *            参数
	 * @param type
	 *            类型
	 */
	public void refreshSetDate(Map<String, String> map, final int type) {
		OkHttpUtil.sendRequest(url, map, new Callback() {
			@Override
			public void onResponse(Call arg0, Response response)
					throws IOException {
				try {
					// 加载时长久点
					Thread.sleep(1500);
					Message msg = new Message();
					if (response.isSuccessful()) {
						msg.what = type;
						msg.obj = response.body().string();
					} else {
						if ("1".equals(type)) {
							// 刷新失败
							msg.what = 3;
						} else {
							// 加载失败
							msg.what = 4;
						}
						msg.obj = "网络异常,请确认网络情况";
					}
					handler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Call arg0, IOException arg1) {
				// 显示长些再执行往下操作
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
				}
				handler.sendEmptyMessage(4);
			}

		});

	}

}

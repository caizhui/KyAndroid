package com.ky.kyandroid.util;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.ky.kyandroid.Constants;
import com.ky.kyandroid.activity.LoginActivity;
import com.ky.kyandroid.bean.NetWorkConnection;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by msi on 2017/7/1.
 */
public class MsgThreadUtil {

    private static final String TAG = "MsgThreadUtil";

    /**
     * 启动任务消息访问runable
     */
    public static void initInfoRunable(final String userId,final Handler mHandler, final NetWorkConnection nwConnection) {
        Thread xiaoxiT = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Log.i(TAG, "消息线程开启开始...");
                        if (nwConnection.isWIFIConnection()) {
                            Map<String, String> paramsMap = new HashMap<String, String>();
                            // 设置用户ID
                            paramsMap.put("userId", userId);
                            // 发送请求
                            OkHttpUtil.sendRequest(Constants.SERVICE_NOTICE_NUM_HADLE, paramsMap, new Callback() {
                                @Override
                                public void onResponse(Call arg0, Response response)
                                        throws IOException {
                                    Message msg = new Message();
                                    if (response.isSuccessful()) {
                                        msg.what = 1;
                                        msg.obj = response.body().string();
                                    } else {
                                        msg.what = 0;
                                    }
                                    mHandler.sendMessage(msg);
                                }

                                @Override
                                public void onFailure(Call arg0, IOException arg1) {
                                    mHandler.sendEmptyMessage(0);
                                }
                            });
                        } else {
                            Log.i(TAG, "检测到无法WIFI连接,消息接收失败");
                        }
                        Thread.sleep(30 * 1000);
                    } catch (InterruptedException e) {
                        mHandler.sendEmptyMessage(0);
                    }
                }
            }
        });
        xiaoxiT.start();
    }
}

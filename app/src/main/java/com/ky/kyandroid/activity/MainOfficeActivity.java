package com.ky.kyandroid.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ky.kyandroid.Constants;
import com.ky.kyandroid.R;
import com.ky.kyandroid.activity.evententry.EventEntryListActivity;
import com.ky.kyandroid.activity.msg.MsgNoticeActivity;
import com.ky.kyandroid.activity.supervision.SuperVisionAddActivity;
import com.ky.kyandroid.bean.AckMessage;
import com.ky.kyandroid.bean.NetWorkConnection;
import com.ky.kyandroid.bean.PageBean;
import com.ky.kyandroid.util.JsonUtil;
import com.ky.kyandroid.util.OkHttpUtil;
import com.ky.kyandroid.util.SpUtil;
import com.ky.kyandroid.util.StringUtils;
import com.ky.kyandroid.view.BadgeView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Caizhui on 2017-6-8.
 * 区维稳办主界面
 */

public class MainOfficeActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    /**
     * 导航栏左右按钮
     */
    @BindView(R.id.left_btn)
    ImageView leftBtn;
    /**
     * 导航栏中间文字
     */
    @BindView(R.id.center_text)
    TextView centerText;
    /**
     * 导航栏右边按钮
     */
    @BindView(R.id.right_btn)
    Button rightBtn;
    /**
     * 消息提醒铃铛
     */
    @BindView(R.id.notice_icon)
    ImageView noticeIcon;
    /**
     * 我的事件
     */
    @BindView(R.id.event_img)
    ImageView eventImageView;

    /**
     * 督查督办
     */
    @BindView(R.id.supervision_img)
    ImageView supervisionImageView;


    /**
     * 消息条目控制
     */
    BadgeView badge;
    /**
     * BadgeView-animation
     */
    Animation anim;

    /**
     * 网路工具类
     */
    private NetWorkConnection netWorkConnection;

    /**
     * SharedPreferences
     */
    private SharedPreferences sp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_office);
        ButterKnife.bind(this);
        centerText.setText("维稳办信息");
        rightBtn.setVisibility(View.INVISIBLE);
        initEvent();
        initInfoRunable();
    }

    /**
     * 回调handle
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // 提示信息
            String message = String.valueOf(msg.obj == null ? "系统繁忙,请稍后再试" : msg.obj);
            // 提示信息
            switch (msg.what) {
                // 失败
                case 0:
                    Log.i(TAG, "消息接收/发送异常...");
                    break;
                // 成功跳转
                case 1:
                    Log.i(TAG, "消息接收成功...");
                    handleTransation(message);
                    break;
            }
        }
    };


    /**
     * 初始化事件
     */
    private void initEvent() {
        // 初始化网络工具 与 sp
        sp = SpUtil.getSharePerference(this);
        netWorkConnection = new NetWorkConnection(this);
        // 初始化信息标识
        badge = new BadgeView(this, noticeIcon);
        badge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
        badge.setBadgeBackgroundColor(Color.parseColor("#FF0033"));
        badge.setVisibility(View.GONE);
        anim = new TranslateAnimation(-100, 0, 0, 0);
        anim.setInterpolator(new BounceInterpolator());
        anim.setDuration(1000);
    }

    /**
     * 启动任务消息访问runable
     */
    private void initInfoRunable() {
        Thread xiaoxiT = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Log.i(TAG, "消息线程开启开始...");
                        handleXiaoxiThread();
                        Thread.sleep(30 * 1000);
                    } catch (InterruptedException e) {
                        mHandler.sendEmptyMessage(0);
                    }
                }
            }
        });
        xiaoxiT.start();
    }

    private void handleXiaoxiThread() {
        if (netWorkConnection.isWIFIConnection()) {
            Map<String, String> paramsMap = new HashMap<String, String>();
            String userId = SpUtil.getSharePerference(this).getString(LoginActivity.USER_ID,"userId");
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

    }

    /**
     * 处理消息后续流程
     *
     * @param
     */
    private void handleTransation(String body) {
        if (StringUtils.isBlank(body)) {
            Log.i(TAG, "解释响应body失败...");
        } else {
            // 处理响应信息
            AckMessage ackMsg = JsonUtil.fromJson(body, AckMessage.class);
            if (ackMsg != null) {
                PageBean pageBean = ackMsg.getPageBean();
                if (pageBean != null) {
                    int count = pageBean.getTotalCount();
                    String badText = (String) badge.getText();
                    if (count > 0) {
                        if (StringUtils.isBlank(badText)) {
                            badge.toggle(anim, null);
                        }
                        badge.setText(count + "");
                    } else {
                        if (!StringUtils.isBlank(badText)) {
                            badge.setText("");
                            badge.toggle(anim, null);
                        }
                    }
                }
            }
        }
    }


    @OnClick({R.id.left_btn, R.id.event_img, R.id.supervision_img,R.id.notice_icon})
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            /** 返回键 **/
            case R.id.left_btn:
                onBackPressed();
                break;
            /**我的事件*/
            case R.id.event_img:
                intent.setClass(this, EventEntryListActivity.class);
                startActivity(intent);
                break;
            /** 信息提醒 **/
            case R.id.notice_icon:
                intent.setClass(this, MsgNoticeActivity.class);
                startActivity(intent);
                break;
            /**督查督办*/
            case R.id.supervision_img:
                intent.setClass(this, SuperVisionAddActivity.class);
                startActivity(intent);
                break;
        }
    }
}

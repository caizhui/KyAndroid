package com.ky.kyandroid.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ky.kyandroid.R;
import com.ky.kyandroid.activity.dbpj.DbpjlActivity;
import com.ky.kyandroid.activity.evententry.EventEntryListActivity;
import com.ky.kyandroid.activity.job.JobBulletinAddActivity;
import com.ky.kyandroid.activity.msg.MsgNoticeActivity;
import com.ky.kyandroid.activity.supervision.SuperVisionListActivity;
import com.ky.kyandroid.activity.task.TaskListActivity;
import com.ky.kyandroid.bean.AckMessage;
import com.ky.kyandroid.bean.NetWorkConnection;
import com.ky.kyandroid.bean.PageBean;
import com.ky.kyandroid.util.JsonUtil;
import com.ky.kyandroid.util.MsgThreadUtil;
import com.ky.kyandroid.util.SpUtil;
import com.ky.kyandroid.util.StringUtils;
import com.ky.kyandroid.util.SweetAlertDialogUtil;
import com.ky.kyandroid.util.ViewUtil;
import com.ky.kyandroid.view.BadgeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Caizhui on 2017-6-8.
 * 主界面
 */

public class MainAllActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    /**
     * 导航栏左右按钮
     */
    @BindView(R.id.left_btn)
    ImageView leftBtn;
    /**
     * 右下角文本
     */
    @BindView(R.id.bottom_text)
    TextView bottom_text;
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
    @BindView(R.id.message_img)
    ImageView message_img;
    /**
     * 我的事件
     */
    @BindView(R.id.event_img)
    ImageView eventImageView;
    /**
     * 我的任务
     */
    @BindView(R.id.task_img)
    ImageView taskImageView;
    /**
     * 督查督办
     */
    @BindView(R.id.supervision_img)
    ImageView supervisionImageView;

    /**
     * 监督评价
     */
    @BindView(R.id.jdpj_img)
    ImageView jdpjImg;

    /**
     * 工作简报
     */
    @BindView(R.id.gzjb_img)
    ImageView gzjbImg;

    /**
     * 主布局id
     */
    @BindView(R.id.main_layout)
    LinearLayout main_layout;

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
     * 提示框工具
     */
    private SweetAlertDialogUtil sweetAlertDialogs;

    /**
     * SharedPreferences
     */
    private SharedPreferences sp;

    /**
     * 登出菜单项 - view
     */
    private View mPopView;
    /**
     * 登出菜单项 - PopupWindow
     */
    private PopupWindow mPopupWindow;
    /**
     * 登出菜单项 - 按钮列
     */
    private TextView pop_change_user, pop_exit, pop_cancle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_all);
        ButterKnife.bind(this);
        initEvent();
        initPopMenuView();
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
        sweetAlertDialogs = new SweetAlertDialogUtil(this);
        centerText.setText("维稳办信息");
        rightBtn.setVisibility(View.INVISIBLE);
        // 初始化信息标识
        badge = ViewUtil.createBeView(this,message_img);
        anim = new TranslateAnimation(-100, 0, 0, 0);
        anim.setInterpolator(new BounceInterpolator());
        anim.setDuration(1000);
        // 启动任务消息访问runable
        String userId = SpUtil.getSharePerference(this).getString(LoginActivity.USER_ID,"userId");
        MsgThreadUtil.initInfoRunable(userId,mHandler,netWorkConnection);
        String bottomText = "欢迎您！" + sp.getString(LoginActivity.NAME,"");
        bottom_text.setText(bottomText);
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

    /**
     * 初始化登出菜单
     */
    private void initPopMenuView() {
        /**************** 登出菜单项 ******************/
        mPopView = LayoutInflater.from(this).inflate(R.layout.app_exit, null);
        pop_change_user = (TextView) mPopView.findViewById(R.id.pop_change_user);
        pop_exit = (TextView) mPopView.findViewById(R.id.pop_exit);
        pop_cancle = (TextView) mPopView.findViewById(R.id.pop_cancle);
        // 初始popupWindow对象
        mPopupWindow = new PopupWindow(mPopView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        // 取消操作
        pop_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });
        // 用户注销
        pop_change_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sweetAlertDialogs.showAlertDialogConfirm("信息提示", "是否确定注销用户?", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        SpUtil.setBooleanSharedPerference(sp, LoginActivity.IS_LOGIN, false);
                        Intent intent = new Intent(MainAllActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });
        // 退出系统
        pop_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sweetAlertDialogs.showAlertDialogConfirm("信息提示", "是否退出程序?", new SweetAlertDialog.OnSweetClickListener() {

                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        SpUtil.setBooleanSharedPerference(sp, LoginActivity.IS_LOGIN, false);
                        System.exit(0);
                    }
                });
            }
        });

    }

    /**
     * 显示pop菜单
     *
     * @param
     */
    private void showPopMenu(View parent) {
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#b0000000")));
        mPopupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        mPopupWindow.setAnimationStyle(R.style.pop_menu);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.update();
    }


    @OnClick({R.id.left_btn, R.id.event_img, R.id.task_img, R.id.supervision_img,R.id.message_img,R.id.jdpj_img,R.id.gzjb_img})
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
            /**我的任务*/
            case R.id.task_img:
                intent.setClass(this, TaskListActivity.class);
                startActivity(intent);
                break;
            /** 信息提醒 **/
             case R.id.message_img:
             intent.setClass(this, MsgNoticeActivity.class);
             startActivity(intent);
             break;
            /**督查督办*/
            case R.id.supervision_img:
                intent.setClass(this, SuperVisionListActivity.class);
                startActivity(intent);
                break;
            /**监督评价**/
            case R.id.jdpj_img:
                intent.setClass(this, DbpjlActivity.class);
                startActivity(intent);
                break;
            case R.id.gzjb_img:
                intent.setClass(this, JobBulletinAddActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Log.i(TAG,"MAIN 按下了back键 onBackPressed()");
        showPopMenu((View) main_layout.getParent());
    }
}

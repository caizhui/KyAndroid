package com.ky.kyandroid.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ky.kyandroid.R;
import com.ky.kyandroid.activity.evententry.EventEntryListActivity;
import com.ky.kyandroid.activity.supervision.SuperVisionAddActivity;
import com.ky.kyandroid.activity.task.TaskListActivity;
import com.ky.kyandroid.util.SpUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Caizhui on 2017-6-8.
 * 主界面
 */

public class MainActivity extends AppCompatActivity {

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
     * 我的事件LinearLayout
     */
    @BindView(R.id.event_linearlayout)
    LinearLayout eventLinearlayout;
    /**
     * 我的任务LinearLayout
     */
    @BindView(R.id.task_linearlayout)
    LinearLayout taskLinearlayout;
    /**
     * 督查督办LinearLayout
     */
    @BindView(R.id.supervision_linearlayout)
    LinearLayout supervisionLinearlayout;
    @BindView(R.id.event_text)
    TextView eventText;
    @BindView(R.id.task_text)
    TextView taskText;
    @BindView(R.id.supervision_text)
    TextView supervisionText;

    /**
     * SharedPreferences
     */
    private SharedPreferences sp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        centerText.setText("维稳办信息");
        rightBtn.setVisibility(View.INVISIBLE);
        initEvent();
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        sp = SpUtil.getSharePerference(this);
        String name = sp.getString("name", "");
        if ("街道办工作人员".equals(name)) {
            taskLinearlayout.setVisibility(View.GONE);
            supervisionLinearlayout.setVisibility(View.GONE);
        } else if ("街道职能部门".equals(name) || "区职能部门".equals(name)) {
            eventLinearlayout.setVisibility(View.GONE);
        } else if ("区维稳办".equals(name)) {
            taskLinearlayout.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.left_btn, R.id.event_img, R.id.task_img, R.id.supervision_img})
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
            /**督查督办*/
            case R.id.supervision_img:
                intent.setClass(this, SuperVisionAddActivity.class);
                startActivity(intent);
                break;
        }
    }
}

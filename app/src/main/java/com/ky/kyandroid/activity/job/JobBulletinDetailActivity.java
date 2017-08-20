package com.ky.kyandroid.activity.job;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ky.kyandroid.R;
import com.ky.kyandroid.entity.TFtAppReportEntity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;

/**
 * Created by Caizhui on 2017/8/15.
 * 工作简报新增页面
 */

public class JobBulletinDetailActivity extends AppCompatActivity {

    /**
     * 标题栏左边按钮
     */
    @BindView(R.id.left_btn)
    ImageView leftBtn;

    /**
     * 标题栏中间标题
     */
    @BindView(R.id.center_text)
    TextView centerText;

    /**
     * 标题栏右边按钮
     */
    @BindView(R.id.right_btn)
    Button rightBtn;

    /**
     * 标题
     */
    @BindView(R.id.job_name_edt)
    TextView jobNameEdt;
    /**
     * 时间
     */
    @BindView(R.id.job_time_edt)
    TextView jobTimeEdt;
    /**
    * 上报部门
     */
    @BindView(R.id.job_departmen_edt)
    TextView jobDepartmenEdt;
    @BindView(R.id.field_departmen_img)
    ImageView fieldDepartmenImg;
    @BindView(R.id.job_departmen_layout)
    LinearLayout jobDepartmenLayout;

    /**
     * 汇报人
     */
    @BindView(R.id.job_hbr_edt)
    TextView jobHbredt;

    /**
     * 全局LinearLayout
     */
    @BindView(R.id.linear_evententry)
    LinearLayout linearEvententry;
    /**
     * 内容
     */
    @BindView(R.id.job_content_edt)
    TextView jobContentEdt;

    @BindView(R.id.one_scroll)
    ScrollView one_scroll;

    @BindView(R.id.two_scroll)
    ScrollView two_scroll;

    /**
     * SharedPreferences
     */
    private SharedPreferences sp;


    private TFtAppReportEntity tFtAppReportEntity;


    private Intent intent;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobjy_detail);
        ButterKnife.bind(this);
        intent = getIntent();
        tFtAppReportEntity = (TFtAppReportEntity) intent.getSerializableExtra("tFtAppReportEntity");
        initToolbar();
        setDate();
    }

    /**
     * 初始化标题栏e
     */
    private void initToolbar() {

        /** 设置toolbar标题 **/
        centerText.setText("工作简报详情");
    }

    public void setDate(){
       if(tFtAppReportEntity!=null){
           jobNameEdt.setText(tFtAppReportEntity.getTitle());
           jobTimeEdt.setText(tFtAppReportEntity.getRepTime());
           jobHbredt.setText(tFtAppReportEntity.getRepBy());
           jobDepartmenEdt.setText(tFtAppReportEntity.getRepToUser());
           jobContentEdt.setText(tFtAppReportEntity.getContent());
       }
    }

    @OnClick({R.id.left_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            /** 返回键 **/
            case R.id.left_btn:
                onBackPressed();
                break;
        }
    }

    @OnTouch({R.id.one_scroll,R.id.two_scroll})
    public boolean OnTouchListener(View v, MotionEvent event) {
        v.getParent().requestDisallowInterceptTouchEvent(true);
        return false;
    }

}

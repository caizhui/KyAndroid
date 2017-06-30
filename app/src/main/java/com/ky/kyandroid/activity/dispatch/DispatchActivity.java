package com.ky.kyandroid.activity.dispatch;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ky.kyandroid.Constants;
import com.ky.kyandroid.R;
import com.ky.kyandroid.bean.NetWorkConnection;
import com.ky.kyandroid.entity.TFtSjEntity;
import com.ky.kyandroid.util.OkHttpUtil;
import com.ky.kyandroid.util.SpUtil;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Caizhui on 2017/6/30.
 * 街道派遣Activity
 */

public class DispatchActivity extends AppCompatActivity {

    /**
     * 新增派遣部门按钮
     */
    @BindView(R.id.add_department)
    Button addDepartment;
    /**
     *  派遣部门List
     */
    @BindView(R.id.department_list)
    ListView departmentList;
    /**
     * 导航栏左边的按钮
     */
    @BindView(R.id.left_btn)
    ImageView leftBtn;
    /**
     * 导航栏中间的文字
     */
    @BindView(R.id.center_text)
    TextView centerText;
    /**
     *
     */
    @BindView(R.id.notice_icon)
    ImageView noticeIcon;
    /**
     * 导航栏右边的按钮
     */
    @BindView(R.id.right_btn)
    Button rightBtn;

    /**
     *  部门类型
     */
    private EditText departmentTypeEdt;

    /**
     * 部门类型图标
     */
    private ImageView departmentTypeImg;

    /**
     * 部门
     */
    private EditText departmenTextEdt;

    /**
     * 部门图标
     */
    private ImageView departmenTextImg;

    /**
     * 处理时限
     */
    private EditText handlerTimeEdt;

    /**
     * 处理内容
     */
    private EditText handlerTextEdt;


    private TFtSjEntity tFtSjEntity;


    private Intent intent;

    private String  uuid;

    /**
     * SharedPreferences
     */
    private SharedPreferences sp;
    /**
     * 网路工具类
     */
    private NetWorkConnection netWorkConnection;

    /**
     * 用户ID
     */
    public static final String USER_ID = "userId";

    private  String userId;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // 提示信息
            String message = String.valueOf(msg.obj == null ? "系统繁忙,请稍后再试"
                    : msg.obj);
            switch (msg.what) {
                // 失败
                case 0:
                    break;
                // 成功跳转
                case 1:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_street_display);
        ButterKnife.bind(this);
        centerText.setText("事件派遣");
        noticeIcon.setVisibility(View.INVISIBLE);
        rightBtn.setVisibility(View.INVISIBLE);
        intent = getIntent();
        tFtSjEntity = (TFtSjEntity) intent.getSerializableExtra("tFtSjEntity");
        if(tFtSjEntity!=null){
            uuid = tFtSjEntity.getId();
        }
        initEvent();
        initData();
    }


    /**
     * 初始化事件
     */
    private void initEvent() {
        sp = SpUtil.getSharePerference(this);
        // 初始化网络工具
        netWorkConnection = new NetWorkConnection(this);
        userId = sp.getString(USER_ID, "");
    }

    /**
     * 初始化数据
     */
    public void initData(){
        final Message msg = new Message();
        if (netWorkConnection.isWIFIConnection()) {
            msg.what = 0;
            // 参数列表 - 账号、密码（加密）
            Map<String, String> paramsMap = new HashMap<String, String>();
            paramsMap.put("userId", userId);
            paramsMap.put("sjId", uuid);
            // 发送请求
            OkHttpUtil.sendRequest(Constants.SERVICE_TASK_DISPATCH, paramsMap, new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    mHandler.sendEmptyMessage(0);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        msg.what = 1;
                        msg.obj = response.body().string();
                    } else {
                        msg.obj = "网络异常,请确认网络情况";
                    }
                    mHandler.sendMessage(msg);
                }
            });
        } else {
            msg.obj = "WIFI网络不可用,请检查网络连接情况";
            mHandler.sendMessage(msg);
        }
    }

    @OnClick({R.id.add_department,R.id.left_btn})
    public void OnClick(View view) {
        switch (view.getId()){
            case R.id.left_btn:
                onBackPressed();
                break;
            case R.id.add_department:
                addDepartmentInfo();
                break;
        }
    }

    /**
     * 新增派遣部门信息
     */
    public void addDepartmentInfo(){
        View dialogView = LayoutInflater.from(DispatchActivity.this).inflate(R.layout.dialog_street_display, null);
        departmentTypeEdt = ButterKnife.findById(dialogView, R.id.department_type_edt);
        departmentTypeImg =  ButterKnife.findById(dialogView, R.id.department_type_img);
        departmenTextEdt = ButterKnife.findById(dialogView, R.id.department_text_edt);
        departmenTextImg =  ButterKnife.findById(dialogView, R.id.department_text_img);
        handlerTimeEdt = ButterKnife.findById(dialogView, R.id.handler_time_edt);
        handlerTextEdt = ButterKnife.findById(dialogView, R.id.handler_text_edt);
        handlerTimeEdt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    handlerTimeEdt.clearFocus();
                    Calendar c = Calendar.getInstance();
                    new DatePickerDialog(DispatchActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            Date date = new Date(System.currentTimeMillis());
                            SimpleDateFormat dateFormat = new SimpleDateFormat(" HH:mm:ss");
                            String time = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                            time += dateFormat.format(date);
                            handlerTimeEdt.setText(time);
                        }
                    }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
                }
                return false;
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(DispatchActivity.this);
        builder.setTitle("派遣部门信息");
        builder.setView(dialogView);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.create().show();
    }
}

package com.ky.kyandroid.activity.supervision;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ky.kyandroid.Constants;
import com.ky.kyandroid.R;
import com.ky.kyandroid.bean.AckMessage;
import com.ky.kyandroid.bean.CodeValue;
import com.ky.kyandroid.bean.NetWorkConnection;
import com.ky.kyandroid.entity.DescEntity;
import com.ky.kyandroid.entity.TFtDbEntity;
import com.ky.kyandroid.util.DateTimePickDialogUtil;
import com.ky.kyandroid.util.JsonUtil;
import com.ky.kyandroid.util.OkHttpUtil;
import com.ky.kyandroid.util.SpUtil;
import com.ky.kyandroid.util.SweetAlertDialogUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Caizhui on 2017/6/14.
 * 督察督办新增
 */

public class SuperVisionAddActivity extends AppCompatActivity {

    /**
     * 导航栏左边按钮
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
     * 督办名称
     */
    @BindView(R.id.supervisor_name_edt)
    EditText supervisorNameEdt;
    /**
     * 督办类型
     */
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;

    @BindView(R.id.radioButton01)
    RadioButton radioButton01;

    @BindView(R.id.radioButton02)
    RadioButton radioButton02;

    @BindView(R.id.radioButton03)
    RadioButton radioButton03;
    /**
     * 被督办单位
     */
    @BindView(R.id.be_supervisor_unit_spinner)
    Spinner beSupervisorUnitSpinner;
    /**
     * 反馈时限
     */
    @BindView(R.id.feedback_time_edt)
    EditText feedbackTimeEdt;

    /**
     * 关联事件
     */
    @BindView(R.id.supervisor_glsj)
    Spinner supervisorGlsj;

    /**
     * 督办要求
     */
    @BindView(R.id.supervisor_require_edt)
    EditText supervisorRequireEdt;
    /**
     * 备注
     */
    @BindView(R.id.remark_text_edt)
    EditText remarkTextEdt;
    /**
     * 保存
     */
    @BindView(R.id.supervision_add_btn)
    Button supervisionAddBtn;
    /**
     * 取消
     */
    @BindView(R.id.supervision_cancel_btn)
    Button supervisionCancelBtn;

    String message ="";

    private TFtDbEntity tFtDbEntity;

    /**
     * 督办类型标识符
     */
    private String dblx;


    /**
     * SharedPreferences
     */
    private SharedPreferences sp;
    /**
     * 网路工具类
     */
    private NetWorkConnection netWorkConnection;


    /**
     * 弹出框工具类
     */
    private SweetAlertDialogUtil sweetAlertDialogUtil;

    private String userId;


    private String orgId;

    /**
     * 设置Spinner控件的初始值
     */
    public List<CodeValue> glsjList;

    /**
     * 数组 配置器 下拉菜单赋值用
     */
    ArrayAdapter<CodeValue> glsjAdapter;

    /**
     * 设置Spinner控件的初始值
     */
    public List<CodeValue> bdcbmList;

    /**
     * 数组 配置器 下拉菜单赋值用
     */
    ArrayAdapter<CodeValue> bdcbmAdapter;

    private String sjId;

    private boolean flag =true;;

    private  CodeValue glsjCodeValue;

    private  CodeValue ddbbmCodeValue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervision_add);
        ButterKnife.bind(this);
        initView();
        initEvent();
        sweetAlertDialogUtil.loadAlertDialog("Loading...");
        dbAddData();
        spinnerSelect();

    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        sp = SpUtil.getSharePerference(this);
        // 初始化网络工具
        netWorkConnection = new NetWorkConnection(this);
        sweetAlertDialogUtil = new SweetAlertDialogUtil(SuperVisionAddActivity.this);
        userId = sp.getString("userId", "");
        orgId = sp.getString("orgId", "");

    }

    /**
     * 控件赋初值
     */
    public  void initView(){
        /** 设置toolbar标题 **/
        centerText.setText("督察信息录入");

        /** 将右边按钮隐藏*/
        rightBtn.setVisibility(View.INVISIBLE);

        if(radioGroup!=null){
            radioButton01.setChecked(true);
            dblx="1";
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (radioButton01.getId() == checkedId) {
                        dblx="1";
                    } else if (radioButton02.getId() == checkedId) {
                        dblx="2";
                    } else if (radioButton03.getId() == checkedId) {
                        dblx="3";
                    }
                }
            });
        }

    }

    /**
     * Spinner item点击
     */
    public void spinnerSelect(){
        supervisorGlsj.setSelection(0, false);
        beSupervisorUnitSpinner.setSelection(0, false);
        supervisorGlsj.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                glsjCodeValue = (CodeValue) adapterView.getItemAtPosition(position);
                sjId = glsjCodeValue.getCode();
                dbAddData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        beSupervisorUnitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                ddbbmCodeValue = (CodeValue) adapterView.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

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
                    sweetAlertDialogUtil.dismissAlertDialog();
                    Toast.makeText(SuperVisionAddActivity.this, message, Toast.LENGTH_SHORT).show();
                    break;
                // 成功跳转
                case 1:
                    HandleDescData(message);
                    break;
                // 成功跳转
                case 2:
                    Intent intent =new Intent(SuperVisionAddActivity.this,SuperVisionListActivity.class);
                    intent.putExtra("businessType", "initList");
                    startActivity(intent);
                    Toast.makeText(SuperVisionAddActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
                    sweetAlertDialogUtil.dismissAlertDialog();
                    break;
            }
        }
    };


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
            glsjList = new ArrayList<CodeValue>();
            bdcbmList = new ArrayList<CodeValue>();
            for (int i = 0; i < list.size(); i++) {
                String entityStr = JsonUtil.toJson(list.get(i));
                DescEntity bd_desc=JsonUtil.fromJson(entityStr, DescEntity.class);
                String []code =String.valueOf(bd_desc.getCode()).split("\\.");
                if(code.length>0){
                    bd_desc.setCode(code[0]);
                }
                if("glsj".equals(bd_desc.getType())){
                    //关联事件
                    glsjList.add(new CodeValue(bd_desc.getCode(),bd_desc.getValue()));
                }else{
                    //被督察部门
                    bdcbmList.add(new CodeValue(bd_desc.getCode(),bd_desc.getValue()));
                }
            }
            if(glsjList!=null && glsjList.size()>0) {
                //将可选内容与ArrayAdapter连接起来
                glsjAdapter = new ArrayAdapter<CodeValue>(SuperVisionAddActivity.this, android.R.layout.simple_spinner_item, glsjList);
                //设置下拉列表的风格
                glsjAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                supervisorGlsj.setAdapter(glsjAdapter);//将adapter 添加到表现形式spinner中
            }
            if(bdcbmList!=null && bdcbmList.size()>0){
                //将可选内容与ArrayAdapter连接起来
                bdcbmAdapter = new ArrayAdapter<CodeValue>(SuperVisionAddActivity.this, android.R.layout.simple_spinner_item, bdcbmList);
                //设置下拉列表的风格
                bdcbmAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                beSupervisorUnitSpinner.setAdapter(bdcbmAdapter);//将adapter 添加到表现形式spinner中
                sweetAlertDialogUtil.dismissAlertDialog();
            }
            }else if(Constants.FAILURE.equals(ackMessage.getAckCode())){
            //失败后操作
        }

    }

    @OnTouch({R.id.feedback_time_edt})
    public boolean OnTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            /** 点击发生时间控件 **/
            case R.id.feedback_time_edt:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    feedbackTimeEdt.clearFocus();
                    DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
                            SuperVisionAddActivity.this, "");
                    dateTimePicKDialog.dateTimePicKDialog(feedbackTimeEdt);
                    return false;
                }
                break;
        }
        return false;
    }

    public void dbAddData(){
        final Message msg = new Message();
        if (netWorkConnection.isWIFIConnection()) {
            msg.what = 0;
            // 参数列表 - 账号、密码（加密）
            Map<String, String> paramsMap = new HashMap<String, String>();
            paramsMap.put("userId", userId);
            paramsMap.put("sjId", sjId);
            // 发送请求
            OkHttpUtil.sendRequest(Constants.SERVICE_DBADD_DATA, paramsMap, new Callback() {

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

    @OnClick({R.id.left_btn,R.id.feedback_time_edt,R.id.supervision_add_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            /** 返回键 **/
            case R.id.left_btn:
                onBackPressed();
                break;
            /** 点击发生时间控件 **/
            case R.id.feedback_time_edt:
                feedbackTimeEdt.clearFocus();
                DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
                        SuperVisionAddActivity.this, "");
                dateTimePicKDialog.dateTimePicKDialog(feedbackTimeEdt);
                break;
            case R.id.supervision_add_btn:
                if(tFtDbEntity==null){
                    tFtDbEntity = new TFtDbEntity();
                }
                if("".equals(supervisorNameEdt.getText())){
                    message+="督办名称不能为空\n";
                }else{
                    tFtDbEntity.setDbmc(supervisorNameEdt.getText().toString());
                }
                if("".equals(supervisorGlsj.getTextAlignment())){
                    message+="关联事件不能为空\n";
                }else{
                    tFtDbEntity.setSj_id(glsjCodeValue.getCode());
                }
                if("".equals(dblx)){
                    message+="督办类型不能为空\n";
                }else{
                    tFtDbEntity.setDblx(dblx);
                }
                if("".equals(beSupervisorUnitSpinner.getTextAlignment())){
                    message+="被督办名称不能为空\n";
                }else{
                    tFtDbEntity.setBdbdw(ddbbmCodeValue.getCode());
                }
                if("".equals(feedbackTimeEdt.getText())){
                    message+="反馈时限不能为空\n";
                }else{
                    tFtDbEntity.setFksx(feedbackTimeEdt.getText().toString());
                }
                if("".equals(supervisorRequireEdt.getText())){
                    message+="督办要求不能为空\n";
                }else{
                    tFtDbEntity.setDbyq(supervisorRequireEdt.getText().toString());
                }
                tFtDbEntity.setComments(remarkTextEdt.getText().toString());
                if("".equals(message)){
                    sendData();
                }else{
                    Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public  void sendData(){
        final Message msg = new Message();
        if (netWorkConnection.isWIFIConnection()) {
            sweetAlertDialogUtil.loadAlertDialog("Loading...");
            msg.what = 0;
            // 参数列表 - 账号、密码（加密）
            Map<String, String> paramsMap = new HashMap<String, String>();
            // 转成json格式
            String mapJson = JsonUtil.toJson(tFtDbEntity);
            paramsMap.put("userId", userId);
            paramsMap.put("TFtDb",mapJson);
            paramsMap.put("requestType","save");
            // 发送请求
            OkHttpUtil.sendRequest(Constants.SERVICE_DBDBEXCEUTE, paramsMap, new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    mHandler.sendEmptyMessage(0);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        msg.what = 2;
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
}

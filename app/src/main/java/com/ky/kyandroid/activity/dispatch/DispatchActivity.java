package com.ky.kyandroid.activity.dispatch;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ky.kyandroid.Constants;
import com.ky.kyandroid.R;
import com.ky.kyandroid.activity.evententry.EventEntryListActivity;
import com.ky.kyandroid.adapter.DisplayDepartmentListAdapter;
import com.ky.kyandroid.bean.AckMessage;
import com.ky.kyandroid.bean.CodeValue;
import com.ky.kyandroid.bean.NetWorkConnection;
import com.ky.kyandroid.entity.DispatchEntity;
import com.ky.kyandroid.entity.KpqbmEntity;
import com.ky.kyandroid.entity.OrgsEntity;
import com.ky.kyandroid.entity.TFtSjEntity;
import com.ky.kyandroid.entity.YpqbmEntity;
import com.ky.kyandroid.util.DateTimePickDialogUtil;
import com.ky.kyandroid.util.JsonUtil;
import com.ky.kyandroid.util.OkHttpUtil;
import com.ky.kyandroid.util.SpUtil;
import com.ky.kyandroid.util.StringUtils;
import com.ky.kyandroid.util.SweetAlertDialogUtil;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;
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
     * 派遣按钮
     */
    @BindView(R.id.add_dispatch)
    Button addDispatch;
    /**
     * 派遣部门List
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
     * 导航栏右边的按钮
     */
    @BindView(R.id.right_btn)
    Button rightBtn;

    /**
     * 部门类型
     */
    private Spinner departmentTypeSpinner;

    /**
     * 部门类型图标
     */
    private ImageView departmentTypeImg;

    /**
     * 部门
     */
    private Spinner departmenTextSpinner;

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

    private String uuid;

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

    private String userId;

    /**
     * 可派遣部门
     */
    private List<KpqbmEntity> kpqbmList;

    /**
     * 已派遣部门
     */
    private List<YpqbmEntity> ypqbmList;

    private DisplayDepartmentListAdapter adapter;

    /**
     * 设置Spinner控件的初始值
     */
    public List<CodeValue> spinnerList;

    ArrayAdapter arrayAdapter;

    private YpqbmEntity ypqbmEntity;

    /**
     * 是否查看详情
     */
    private boolean isDetail;

    /**
     * 弹出框工具类
     */
    private SweetAlertDialogUtil sweetAlertDialogUtil;

    /**
     * List临时位置
     */
    private int tempPosition;

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
                    //Toast.makeText(DispatchActivity.this, message, Toast.LENGTH_SHORT).show();
                    break;
                // 成功跳转
                case 1:
                    sweetAlertDialogUtil.dismissAlertDialog();
                    handleTransation(message);
                    break;
                case 2:
                    sweetAlertDialogUtil.dismissAlertDialog();
                    Toast.makeText(DispatchActivity.this, "派遣成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DispatchActivity.this, EventEntryListActivity.class);
                    intent.putExtra("businessType", "isfrash");
                    intent.putExtra("message", message);
                    startActivity(intent);
                    break;
                case 3:
                    sweetAlertDialogUtil.dismissAlertDialog();
                    Toast.makeText(DispatchActivity.this, "派遣失败", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    sweetAlertDialogUtil.dismissAlertDialog();
                    Toast.makeText(DispatchActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                    if (ypqbmList.get(tempPosition) != null) {
                        ypqbmList.remove(tempPosition);
                    }
                    adapter.notifyDataSetChanged(ypqbmList);
                    break;
                case 5:
                    sweetAlertDialogUtil.dismissAlertDialog();
                    Toast.makeText(DispatchActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
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
        rightBtn.setVisibility(View.INVISIBLE);
        intent = getIntent();
        tFtSjEntity = (TFtSjEntity) intent.getSerializableExtra("tFtSjEntity");
        if (tFtSjEntity != null) {
            uuid = tFtSjEntity.getId();
        }
        ypqbmEntity = new YpqbmEntity();
        kpqbmList = new ArrayList<KpqbmEntity>();
        ypqbmList = new ArrayList<YpqbmEntity>();
        adapter = new DisplayDepartmentListAdapter(ypqbmList, this);
        departmentList.setAdapter(adapter);
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
        sweetAlertDialogUtil = new SweetAlertDialogUtil(this);
        sweetAlertDialogUtil.loadAlertDialog();
    }

    /**
     * 初始化数据
     */
    public void initData() {
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

    @OnItemClick(R.id.department_list)
    public void OnItemClick(int position) {
        ypqbmEntity = (YpqbmEntity) adapter.getItem(position);
        isDetail = true;
        tempPosition = position;
        addDepartmentInfo();
    }

    @OnItemLongClick(R.id.department_list)
    public boolean OnItemLongClick(int position) {
        tempPosition = position;
        ypqbmEntity = (YpqbmEntity) adapter.getItem(position);
        if ("8".equals(ypqbmEntity.getClzt()) || "1".equals(ypqbmEntity.getIsDelete())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(DispatchActivity.this);
            builder.setTitle("信息");
            builder.setMessage("确定要删除该条记录吗？");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if ("1".equals(ypqbmEntity.getIsDelete())) {
                        Toast.makeText(DispatchActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                        if (ypqbmList.get(tempPosition) != null) {
                            ypqbmList.remove(tempPosition);
                        }
                        adapter.notifyDataSetChanged(ypqbmList);
                    } else {
                        final Message msg = new Message();
                        if (netWorkConnection.isWIFIConnection()) {
                            sweetAlertDialogUtil.loadAlertDialog();
                            Map<String, String> paramsMap = new HashMap<String, String>();
                            paramsMap.put("userId", userId);
                            paramsMap.put("clbmId", ypqbmEntity.getId());
                            // 发送请求
                            OkHttpUtil.sendRequest(Constants.SERVICE_TASK_DISPATCH_DELETE, paramsMap, new Callback() {

                                @Override
                                public void onFailure(Call call, IOException e) {
                                    mHandler.sendEmptyMessage(0);
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    if (response.isSuccessful()) {
                                        msg.what = 4;
                                        msg.obj = response.body().string();
                                    } else {
                                        msg.what = 5;
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
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            builder.create().show();
        }
        return false;
    }

    @OnClick({R.id.add_department, R.id.left_btn, R.id.add_dispatch})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.left_btn:
                onBackPressed();
                break;
            case R.id.add_department:
                addDepartmentInfo();
                break;
            case R.id.add_dispatch:
                final Message msg = new Message();
                if (ypqbmList != null && ypqbmList.size() > 0) {
                    if (netWorkConnection.isWIFIConnection()) {
                        sweetAlertDialogUtil.loadAlertDialog();
                        Map<String, String> paramsMap = new HashMap<String, String>();
                        String ftSjClbmList = JsonUtil.toJson(ypqbmList);
                        paramsMap.put("userId", userId);
                        paramsMap.put("sjId", uuid);
                        paramsMap.put("ftSjClbmList", ftSjClbmList);
                        // 发送请求
                        OkHttpUtil.sendRequest(Constants.SERVICE_TASK_DISPATCH_SAVE, paramsMap, new Callback() {

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
                                    msg.what = 3;
                                    msg.obj = "网络异常,请确认网络情况";
                                }
                                mHandler.sendMessage(msg);
                            }
                        });
                    } else {
                        msg.obj = "WIFI网络不可用,请检查网络连接情况";
                        mHandler.sendMessage(msg);
                    }
                } else {
                    Toast.makeText(DispatchActivity.this, "没有可派遣的部门！", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    /**
     * 处理后续流程
     *
     * @param
     */
    private void handleTransation(String body) {
        if (!StringUtils.isBlank(body)) {
            // 处理响应信息
            AckMessage ackMsg = JsonUtil.fromJson(body, AckMessage.class);
            if (ackMsg != null) {
                if (AckMessage.SUCCESS.equals(ackMsg.getAckCode())) {
                    Object object = ackMsg.getEntity();
                    //先将获取的Object对象转成String
                    String entityStr = JsonUtil.toJson(object);
                    //先将获取的json象转成实体
                    DispatchEntity dispatchEntity = JsonUtil.fromJson(entityStr, DispatchEntity.class);
                    OrgsEntity orgsEntity = dispatchEntity.getOrgs();
                    if (orgsEntity != null) {
                        kpqbmList = orgsEntity.getKpqbmList();
                        ypqbmList = orgsEntity.getYpqbmList();
                    }
                    if (ypqbmList != null && ypqbmList.size() > 0) {
                        adapter.notifyDataSetChanged(ypqbmList);
                    }
                }
            }
        }
    }

    /**
     * 新增派遣部门信息
     */
    public void addDepartmentInfo() {
        View dialogView = LayoutInflater.from(DispatchActivity.this).inflate(R.layout.dialog_street_display, null);
        departmentTypeSpinner = ButterKnife.findById(dialogView, R.id.department_type_spinner);
        /*departmentTypeImg =  ButterKnife.findById(dialogView, R.id.department_type_img);*/
        departmenTextSpinner = ButterKnife.findById(dialogView, R.id.department_text_spinner);
       /* departmenTextImg =  ButterKnife.findById(dialogView, R.id.department_text_img);*/
        handlerTimeEdt = ButterKnife.findById(dialogView, R.id.handler_time_edt);
        handlerTextEdt = ButterKnife.findById(dialogView, R.id.handler_text_edt);
        handlerTimeEdt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    handlerTimeEdt.clearFocus();
                   /* Calendar c = Calendar.getInstance();
                    new DatePickerDialog(DispatchActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            Date date = new Date(System.currentTimeMillis());
                            SimpleDateFormat dateFormat = new SimpleDateFormat(" HH:mm:ss");
                            String time = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                            time += dateFormat.format(date);
                            handlerTimeEdt.setText(time);
                        }
                    }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();*/
                    DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
                            DispatchActivity.this, "");
                    dateTimePicKDialog.dateTimePicKDialog(handlerTimeEdt);
                }
                return false;
            }
        });
        spinnerList = new ArrayList<CodeValue>();
        spinnerList.add(new CodeValue("1", "事权单位"));
        spinnerList.add(new CodeValue("2", "稳控单位"));
        spinnerList.add(new CodeValue("3", "协办单位"));
        spinnerList.add(new CodeValue("4", "责任单位"));
        //将可选内容与ArrayAdapter连接起来
        arrayAdapter = new ArrayAdapter<CodeValue>(DispatchActivity.this, android.R.layout.simple_spinner_item, spinnerList);
        //设置下拉列表的风格
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        departmentTypeSpinner.setAdapter(arrayAdapter);//将adapter 添加到部门类型spinner中

        spinnerList = new ArrayList<CodeValue>();
        if (kpqbmList != null && kpqbmList.size() > 0) {
            for (int i = 0; i < kpqbmList.size(); i++) {
                spinnerList.add(new CodeValue(kpqbmList.get(i).getID(), kpqbmList.get(i).getORG_NAME()));
            }
        }
        //将可选内容与ArrayAdapter连接起来
        arrayAdapter = new ArrayAdapter<CodeValue>(DispatchActivity.this, android.R.layout.simple_spinner_item, spinnerList);
        //设置下拉列表的风格
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        departmenTextSpinner.setAdapter(arrayAdapter);//将adapter 添加到部门spinner中

        if (isDetail) {
            departmentTypeSpinner.setSelection(Integer.parseInt(ypqbmEntity.getBmlx()) - 1);
            departmenTextSpinner.setSelection(1);
            handlerTimeEdt.setText(ypqbmEntity.getClsx());
            handlerTextEdt.setText(ypqbmEntity.getRwnr());
        }
        if ("8.2".equals(ypqbmEntity.getClzt())) {
            departmentTypeSpinner.setEnabled(false);
            departmenTextSpinner.setEnabled(false);
            handlerTimeEdt.setEnabled(false);
            handlerTextEdt.setEnabled(false);
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(DispatchActivity.this);
        builder.setCancelable(false);
        builder.setTitle("派遣部门信息");
        builder.setView(dialogView);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                CodeValue departmentTypeCodeValue = (CodeValue) departmentTypeSpinner.getSelectedItem();
                CodeValue departmentCodeValue = (CodeValue) departmenTextSpinner.getSelectedItem();
                String handlerTime = handlerTimeEdt.getText().toString();
                String handlerText = handlerTextEdt.getText().toString();
                YpqbmEntity tempYpqbmEntity = new YpqbmEntity();
                tempYpqbmEntity.setBmlx(departmentTypeCodeValue.getCode());
                tempYpqbmEntity.setBmmc(departmentCodeValue.getValue());
                tempYpqbmEntity.setBm_id(departmentCodeValue.getCode());
                tempYpqbmEntity.setSjId(tFtSjEntity.getId());
                tempYpqbmEntity.setIsDelete("1");
                String message = "";
                if ("".equals(handlerTime)) {
                    message += "处理时限不能为空\n";
                } else {
                    tempYpqbmEntity.setClsx(handlerTime);
                }
                if ("".equals(handlerText)) {
                    message += "处理内容不能为空\n";
                } else {
                    tempYpqbmEntity.setRwnr(handlerText);
                }
                if ("".equals(message)) {
                    //如果在list的item已经存在数据，则表示是修改，将之前的数据去掉，重新加载
                    if (ypqbmList != null && ypqbmList.size() > 0) {
                        if (isDetail) {
                            if (ypqbmList.get(tempPosition) != null) {
                                tempYpqbmEntity.setId(ypqbmEntity.getId());
                                ypqbmList.remove(tempPosition);
                                isDetail = false;
                            }
                        }
                    }
                    ypqbmList.add(tempYpqbmEntity);
                    if (ypqbmList != null && ypqbmList.size() > 0) {
                        adapter.notifyDataSetChanged(ypqbmList);
                    }
                    closeDialog(dialogInterface,true);
                } else {
                    closeDialog(dialogInterface,false);
                    Toast.makeText(DispatchActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                closeDialog(dialogInterface,true);
                isDetail = false;
            }
        });
        builder.create().show();
    }

    /**
     * 关闭弹出框  isClose =false 关闭，否则 不关闭
     * @param isClose
     */
    public void  closeDialog(DialogInterface dialogInterface,boolean isClose){
        //不关闭
        try{
            Field field = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
            field.setAccessible(true);
            field.set(dialogInterface, isClose);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

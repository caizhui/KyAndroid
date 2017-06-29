package com.ky.kyandroid.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ky.kyandroid.Constants;
import com.ky.kyandroid.R;
import com.ky.kyandroid.bean.AckMessage;
import com.ky.kyandroid.bean.NetWorkConnection;
import com.ky.kyandroid.entity.UserEntity;
import com.ky.kyandroid.service.SaveBDdescService;
import com.ky.kyandroid.util.JsonUtil;
import com.ky.kyandroid.util.OkHttpUtil;
import com.ky.kyandroid.util.SpUtil;
import com.ky.kyandroid.util.StringUtils;
import com.ky.kyandroid.util.SweetAlertDialogUtil;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 类名称：登录界面Activity<br/>
 * 创建人： Cz <br/>
 *
 * @updateRemark 修改备注：
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * 用户ID
     */
    public static final String USER_ID = "userId";
    /**
     * 用户名称
     */
    public static final String USER_NAME = "userName";

    /**
     * 用户职能名称
     */
    public static final String NAME = "name";
    /**
     * 用户身份证号码
     */
    public static final String USER_SFZHM = "gmsfhm";
    /**
     * 标识
     */
    private static final String TAG = "LoginActivity";
    /**
     * 用户名
     */
    @BindView(R.id.et_account)
    EditText etAccount;
    /**
     * 密码
     */
    @BindView(R.id.et_password)
    EditText etPassword;
    /**
     * 登陆
     */
    @BindView(R.id.btn_login)
    Button btnLogin;

    /**
     * 设置IP
     */
    @BindView(R.id.setting_ip)
    TextView settingIpText;
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

    /**
     * IP输入框
     */
    EditText etIp;
    /**
     * 端口输入框
     */
    EditText etPort;

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
                    Log.i(TAG, "登录失败...");
                    sweetAlertDialogUtil.dismissAlertDialog();
                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    break;
                // 成功跳转
                case 1:
                    sweetAlertDialogUtil.dismissAlertDialog();
                    Log.i(TAG, "登录成功...");
                    handleTransation(message);
                    break;
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        sweetAlertDialogUtil = new SweetAlertDialogUtil(LoginActivity.this);
        initEvent();
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        sp = SpUtil.getSharePerference(this);
        Log.i(TAG, "createView开始...");
        // 初始化网络工具
        netWorkConnection = new NetWorkConnection(this);
        Log.i(TAG, "createView结束...");
    }

    /**
     * 登陆请求
     *
     * @param v
     */
    @OnClick({R.id.btn_login,R.id.setting_ip})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_ip:
                settingIp();
                break;
            case R.id.btn_login:
                // 账号与密码
                String account = String.valueOf(etAccount.getText());
                String password = String.valueOf(etPassword.getText());
                final Message msg = new Message();
                //IP
                String ip=sp.getString("ip", "").trim();
                //端口
                String port=sp.getString("port", "").trim();
                if (StringUtils.isBlank(account) || StringUtils.isBlank(password)) {
                    msg.obj = "登录名或密码不能为空";
                    mHandler.sendMessage(msg);
                }else if(StringUtils.isBlank(ip)|| StringUtils.isBlank(port)){
                    Toast.makeText(this,"服务器IP和端口不能为空，请设置IP和端口",Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    //拼凑IP和端口
                    Constants.SERVICE_BASE_URL="http://"+ip+":"+port+"/";
                    if (netWorkConnection.isWIFIConnection()) {
                        sweetAlertDialogUtil.loadAlertDialog();
                        msg.what = 0;
                        // 参数列表 - 账号、密码（加密）
                        Map<String, String> paramsMap = new HashMap<String, String>();
                        paramsMap.put("userName", account);
                        paramsMap.put("password", password);
                        // 发送请求
                        OkHttpUtil.sendRequest(Constants.SERVICE_LOGIN, paramsMap, new Callback() {

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
                break;
        }
    }

    /**
     * 处理后续流程
     *
     * @param
     */
    private void handleTransation(String body) {
        if (StringUtils.isBlank(body)) {
            Log.i(TAG, "解释响应body失败...");
            Toast.makeText(this, "登录名或密码错误", Toast.LENGTH_SHORT).show();
        } else {
            // 处理响应信息
            AckMessage ackMsg = JsonUtil.fromJson(body, AckMessage.class);
            if (setUserMessage(ackMsg)) {
                Log.i(TAG, "设置用户信息成功...");
                startService(new Intent(this, SaveBDdescService.class));
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } else {
                Log.i(TAG, "设置用户信息失败...");
                Toast.makeText(this, "登录名或密码错误", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 设置用户信息
     *
     * @param ackMsg
     * @return
     */
    private boolean setUserMessage(AckMessage ackMsg) {
        if (ackMsg != null) {
            if (AckMessage.SUCCESS.equals(ackMsg.getAckCode())) {
                Object object = ackMsg.getEntity();
                //先将获取的Object对象转成String
                String entityStr = JsonUtil.toJson(object);
                //先将获取的json象转成实体
                UserEntity user = JsonUtil.fromJson(entityStr, UserEntity.class);
                if (user != null) {
                    SpUtil.setStringSharedPerference(sp, USER_ID, user.getId());
                    SpUtil.setStringSharedPerference(sp, USER_NAME, user.getUserName());
                    SpUtil.setStringSharedPerference(sp, NAME, user.getName());
                    SpUtil.setStringSharedPerference(sp, USER_SFZHM, user.getGmsfhm());
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 弹出自定义对话框设置IP和端口
     */
    public void settingIp(){
        final View mView = LayoutInflater.from(this).inflate(R.layout.activity_settingip, null);
        etIp = ButterKnife.findById(mView,R.id.et_ip);
        etPort = ButterKnife.findById(mView,R.id.et_port);
        //IP
        String ip=sp.getString("ip", "");
        //端口
        String port=sp.getString("port", "");
        if(!"".equals(ip)){
            etIp.setText(ip);
        }
        if(!"".equals(port)){
            etPort.setText(port);
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("设置IP和端口信息");
        builder.setView(mView);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String ip = etIp.getText().toString();
                String port = etPort.getText().toString();
                String message = "";
                if ("".equals(ip)) {
                    message += "IP不能为空\n";
                } else {
                    SpUtil.setStringSharedPerference(sp, "ip", ip);
                }
                if ("".equals(port)) {
                    message += "端口不能为空";
                } else {
                    SpUtil.setStringSharedPerference(sp, "port", port);
                }
                if (!"".equals(message)) {
                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    builder.setCancelable(false);
                    //canCloseDialog(dialogInterface, false);//不关闭对话框
                    return;
                }else{
                    Toast.makeText(LoginActivity.this, "设置IP成功", Toast.LENGTH_SHORT).show();
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

    /**
     * 不关闭对话框
     * @param dialogInterface
     * @param close
     */
    private void canCloseDialog(DialogInterface dialogInterface, boolean close) {
        try {
            Field field = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
            field.setAccessible(true);
            field.set(dialogInterface, close);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

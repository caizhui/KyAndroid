package com.ky.kyandroid.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ky.kyandroid.R;
import com.ky.kyandroid.activity.evententry.EventEntryListActivity;
import com.ky.kyandroid.bean.NetWorkConnection;
import com.ky.kyandroid.util.SpUtil;
import com.ky.kyandroid.util.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 类名称：登录界面Activity<br/>
 * 创建人： Cz <br/>
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
     * SharedPreferences
     */
    private SharedPreferences sp;
    /**
     * 网路工具类
     */
    private NetWorkConnection netWorkConnection;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initEvent();
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        sp = SpUtil.getSharePerference(this);
    }

    /**
     * 登陆请求
     * @param v
     */
    @OnClick(R.id.btn_login)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                // 账号与密码
                String account = String.valueOf(etAccount.getText());
                String password = String.valueOf(etPassword.getText());
                if (StringUtils.isBlank(account) || StringUtils.isBlank(password)) {
                    Toast.makeText(this,"用户名或密码不能为空",Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(this, EventEntryListActivity.class);
                    startActivity(intent);
                    finish();

                }
                break;
        }
    }

}

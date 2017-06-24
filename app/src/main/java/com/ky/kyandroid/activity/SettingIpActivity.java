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
import com.ky.kyandroid.util.SpUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Caizhui on 2017-6-8.
 * 主界面
 */

public class SettingIpActivity extends AppCompatActivity {

    /**
     * IP输入框
     */
    @BindView(R.id.et_ip)
    EditText etIp;
    /**
     * 端口输入框
     */
    @BindView(R.id.et_port)
    EditText etPort;

    /**
     * 保存按钮
     */
    @BindView(R.id.addip_btn)
    Button addipBtn;

    /**
     * SharedPreferences
     */
    private SharedPreferences sp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settingip);
        ButterKnife.bind(this);
        initEvent();
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        sp = SpUtil.getSharePerference(this);
    }

    @OnClick({R.id.addip_btn})
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            /**保存IP和端口*/
            case R.id.addip_btn:
                String ip=etIp.getText().toString();
                String port =etPort.getText().toString();
                String message="";
                if("".equals(ip)){
                    message ="IP不能为空\n";
                }else{
                    SpUtil.setStringSharedPerference(sp, "ip", ip);
                }
                if("".equals(port)){
                    message ="端口不能为空\n";
                }else{
                    SpUtil.setStringSharedPerference(sp, "port", port);
                }
                if("".equals(message)){
                    intent.setClass(this, LoginActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }
}

package com.ky.kyandroid.activity.dbpj;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ky.kyandroid.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Caizhui on 2017-6-8.
 * 主界面
 */

public class DbpjlActivity extends AppCompatActivity {


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
     * 部门评价
     */
    @BindView(R.id.bmpj_img)
    ImageView bmpjImg;
    @BindView(R.id.bmpj_text)
    TextView bmpjText;
    /**
     * 岗位评价
     */
    @BindView(R.id.gwpj_img)
    ImageView gwpjImg;
    @BindView(R.id.gwpj_text)
    TextView gwpjText;
    /**
     * 机构评价
     */
    @BindView(R.id.jgpj_img)
    ImageView jgpjImg;
    @BindView(R.id.jgpj_text)
    TextView jgpjText;

    @BindView(R.id.main_layout)
    LinearLayout mainLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbpj);
        ButterKnife.bind(this);
        initEvent();
    }



    /**
     * 初始化事件
     */
    private void initEvent() {
        centerText.setText("监督评价");
        rightBtn.setVisibility(View.INVISIBLE);
    }




    @OnClick({R.id.left_btn, R.id.bmpj_img, R.id.gwpj_img, R.id.jgpj_img})
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            /** 返回键 **/
            case R.id.left_btn:
                onBackPressed();
                break;
            /**部门评价*/
            case R.id.bmpj_img:
                intent.setClass(this, BmpjListActivity.class);
                startActivity(intent);
                break;
            /**岗位评价*/
            case R.id.gwpj_img:
                intent.setClass(this, GwpjListActivity.class);
                startActivity(intent);
                break;
            /** 机构评价 **/
            case R.id.jgpj_img:
                intent.setClass(this, JgpjListActivity.class);
                startActivity(intent);
                break;
        }
    }

}

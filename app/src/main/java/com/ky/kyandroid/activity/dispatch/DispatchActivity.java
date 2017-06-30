package com.ky.kyandroid.activity.dispatch;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ky.kyandroid.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_street_display);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.add_department,R.id.left_btn})
    public void OnClick(View view) {
        switch (view.getId()){
            case R.id.left_btn:
                onBackPressed();
                break;
            case R.id.add_department:
                break;
        }
    }
}

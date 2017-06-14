package com.ky.kyandroid.activity.supervision;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.ky.kyandroid.R;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    @BindView(R.id.supervisor_type_spinner)
    Spinner supervisorTypeSpinner;
    /**
     * 被督办单位
     */
    @BindView(R.id.be_supervisor_unit_spinner)
    Spinner beSupervisorUnitSpinner;
    /**
     * 反馈时限
     */
    @BindView(R.id.feedback_time_edt)
    TextView feedbackTimeEdt;
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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervision_add);
        ButterKnife.bind(this);
        initView();

    }

    /**
     * 控件赋初值
     */
    public  void initView(){
        /** 设置toolbar标题 **/
        centerText.setText("督察信息录入");

        /** 将右边按钮隐藏*/
        rightBtn.setVisibility(View.INVISIBLE);
    }


    @OnClick({R.id.left_btn,R.id.feedback_time_edt})
    public void onClick(View v) {
        switch (v.getId()) {
            /** 返回键 **/
            case R.id.left_btn:
                onBackPressed();
                break;
            /** 点击发生时间控件 **/
            case R.id.feedback_time_edt:
                    Calendar c = Calendar.getInstance();
                new DatePickerDialog(SuperVisionAddActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        feedbackTimeEdt.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                break;
        }
    }
}

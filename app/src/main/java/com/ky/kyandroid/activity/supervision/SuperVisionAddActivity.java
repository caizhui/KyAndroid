package com.ky.kyandroid.activity.supervision;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ky.kyandroid.R;
import com.ky.kyandroid.entity.TFtDbEntity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

    String message ="";

    private TFtDbEntity tFtDbEntity;

    /**
     * 督办类型标识符
     */
    private String dblx;


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
                Calendar c = Calendar.getInstance();
                new DatePickerDialog(SuperVisionAddActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Date date = new Date(System.currentTimeMillis());
                        SimpleDateFormat dateFormat = new SimpleDateFormat(" HH:mm:ss");
                        String time = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        time += dateFormat.format(date);
                        feedbackTimeEdt.setText(time);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
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
                if("".equals(dblx)){
                    message+="督办类型不能为空\n";
                }else{
                    tFtDbEntity.setDblx(dblx);
                }
                if("".equals(beSupervisorUnitSpinner.getTextAlignment())){
                    message+="被督办名称不能为空\n";
                }else{
                    tFtDbEntity.setBdbdw(beSupervisorUnitSpinner.getSelectedItem().toString());
                }
                if("".equals(feedbackTimeEdt.getText())){
                    message+="反馈时限不能为空\n";
                }else{
                    tFtDbEntity.setDbsj(feedbackTimeEdt.getText().toString());
                }
                if("".equals(supervisorRequireEdt.getText())){
                    message+="督办要求不能为空\n";
                }else{
                    tFtDbEntity.setDbyq(supervisorRequireEdt.getText().toString());
                }
                if("".equals(message)){

                }else{
                    Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}

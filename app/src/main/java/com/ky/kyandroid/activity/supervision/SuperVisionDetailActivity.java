package com.ky.kyandroid.activity.supervision;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ky.kyandroid.R;
import com.ky.kyandroid.entity.TFtDbEntity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Caizhui on 2017/6/14.
 * 督察督办新增
 */

public class SuperVisionDetailActivity extends AppCompatActivity {

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
    EditText beSupervisorUnitSpinner;
    /**
     * 反馈时限
     */
    @BindView(R.id.feedback_time_edt)
    TextView feedbackTimeEdt;

    /**
     * 关联事件
     */
    @BindView(R.id.supervisor_glsj)
    EditText supervisorGlsj;
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

    private Intent intent;

    private TFtDbEntity tFtDbEntity;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervision_detail);
        ButterKnife.bind(this);
        intent= getIntent();
        tFtDbEntity = (TFtDbEntity) intent.getSerializableExtra("tFtDbEntity");
        initView();
        initData();

    }

    /**
     * 控件赋初值
     */
    public  void initView(){
        /** 设置toolbar标题 **/
        centerText.setText("督察信息详情");

        /** 将右边按钮隐藏*/
        rightBtn.setVisibility(View.INVISIBLE);
    }


    public void initData(){
        if(tFtDbEntity!=null){
            supervisorNameEdt.setText(tFtDbEntity.getDbmc());
            if("1".equals(tFtDbEntity.getDblx())){
                radioButton01.setChecked(true);
            }else if("2".equals(tFtDbEntity.getDblx())){
                radioButton02.setChecked(true);
            }else if("3".equals(tFtDbEntity.getDblx())){
                radioButton03.setChecked(true);
            }
            beSupervisorUnitSpinner.setText(tFtDbEntity.getBdbdw());
            supervisorGlsj.setText(tFtDbEntity.getSjmc());
            feedbackTimeEdt.setText(tFtDbEntity.getDbsj());
            supervisorRequireEdt.setText(tFtDbEntity.getDbyq());
            remarkTextEdt.setText("");
        }
    }


    @OnClick({R.id.left_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            /** 返回键 **/
            case R.id.left_btn:
                onBackPressed();
                break;
        }
    }
}

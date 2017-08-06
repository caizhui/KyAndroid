package com.ky.kyandroid.activity.supervision;

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
import com.ky.kyandroid.activity.evententry.EventEntryDetailActivity;
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
    TextView supervisorNameEdt;


    /**
     * 关联事件
     */
    @BindView(R.id.supervisor_glsj_edt)
    TextView supervisorGlsjEdt;


    /**
     * 督办类型
     */
    @BindView(R.id.supervisor_dblx_edt)
    TextView supervisorDblxEdt;


    /**
     * 被督办单位
     */
    @BindView(R.id.be_supervisor_unit_spinner)
    TextView beSupervisorUnitSpinner;


    /**
     *  督办时限
     */
    @BindView(R.id.feedback_time_edt)
    TextView feedbackTimEdt;


    /**
     * 反馈时限
     */
    @BindView(R.id.feedback_fksj_edt)
    TextView fksjEdt;


    /**
     *  转派接收人
     */
    @BindView(R.id.feedback_zpjsr_edt)
    TextView feedbackZpjsrEdt;


    /**
     * 转拍原因
     */
    @BindView(R.id.feedback_zpyy_edt)
    TextView feedbackZpyyEdt;


    /**
     * 退回原因
     */
    @BindView(R.id.feedback_thyy_edt)
    TextView feedbackThyyEdt;


    /**
     * 督办要求
     */
    @BindView(R.id.supervisor_require_edt)
    TextView supervisorRequireEdt;


    /**
     * 反馈结果
     */
    @BindView(R.id.supervisor_fkjg_edt)
    TextView supervisorFkjgEdt;


    /**
     *  备注
     */
    @BindView(R.id.remark_text_edt)
    TextView remarkTextEdt;

    /**
     *  状态
     */
    @BindView(R.id.zt_edt)
    TextView ztEdt;

    /**
     * 关联事件LinearLayout
     */
    @BindView(R.id.glsj_linearLayout)
    LinearLayout glsjLinearLayout;


    /**
     *
     */
    @BindView(R.id.linear_evententry)
    LinearLayout linearEvententry;


    private Intent intent;

    private TFtDbEntity tFtDbEntity;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervision_detail);
        ButterKnife.bind(this);
        intent = getIntent();
        tFtDbEntity = (TFtDbEntity) intent.getSerializableExtra("tFtDbEntity");
        initView();
        initData();

    }

    /**
     * 控件赋初值
     */
    public void initView() {
        /** 设置toolbar标题 **/
        centerText.setText("督查督办详情");
    }


    public void initData() {
        if (tFtDbEntity != null) {
            supervisorNameEdt.setText(tFtDbEntity.getDbmc());
            supervisorGlsjEdt.setText(tFtDbEntity.getSjmc());

            if ("1".equals(tFtDbEntity.getDblx())) {
                supervisorDblxEdt.setText("超时");
            } else if ("2".equals(tFtDbEntity.getDblx())) {
                supervisorDblxEdt.setText("不落实");
            } else if ("3".equals(tFtDbEntity.getDblx())) {
                supervisorDblxEdt.setText("落实不彻底");
            }
            beSupervisorUnitSpinner.setText(tFtDbEntity.getBdbdwname());
            feedbackTimEdt.setText(tFtDbEntity.getDbsj());
            fksjEdt.setText(tFtDbEntity.getFksx());
            feedbackZpjsrEdt.setText(tFtDbEntity.getOrg_name());
            feedbackZpyyEdt.setText(tFtDbEntity.getZpyy());
            feedbackThyyEdt.setText(tFtDbEntity.getThyy());
             supervisorFkjgEdt.setText(tFtDbEntity.getDbfkjg());
            supervisorRequireEdt.setText(tFtDbEntity.getDbyq());
            ztEdt.setText("已"+tFtDbEntity.getStatusname());
            remarkTextEdt.setText(tFtDbEntity.getComments());
        }
    }


    @OnClick({R.id.left_btn,R.id.glsj_linearLayout})
    public void onClick(View v) {
        switch (v.getId()) {
            /** 返回键 **/
            case R.id.left_btn:
                onBackPressed();
                break;
            /**跳到关联事件详情里面去**/
            case R.id.glsj_linearLayout:
                Intent intent =new Intent(this, EventEntryDetailActivity.class);
                intent.putExtra("sjId",tFtDbEntity.getSj_id());
                startActivity(intent);
                break;
        }
    }
}

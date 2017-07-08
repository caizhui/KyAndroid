package com.ky.kyandroid.activity.task;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.ky.kyandroid.R;
import com.ky.kyandroid.bean.CodeValue;
import com.ky.kyandroid.db.dao.DescEntityDao;
import com.ky.kyandroid.entity.TaskEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Caizhui on 2017-6-9.
 * 我的任务基本信息
 */

@SuppressLint("ValidFragment")
public class TaskFragment_Basic extends Fragment {

    /**
     * 事件名称
     */
    @BindView(R.id.thing_name_edt)
    EditText thingNameEdt;
    /**
     * 发生时间
     */
    @BindView(R.id.happen_time_edt)
    EditText happenTimeEdt;
    /**
     * 发生地点
     */
    @BindView(R.id.happen_address_edt)
    EditText happenAddressEdt;
    /**
     * 上访群体
     */
    @BindView(R.id.petition_groups_edt)
    EditText petitionGroupsEdt;
    /**
     * 到场部门LinearLayout
     */
    @BindView(R.id.field_departmen_layout)
    LinearLayout fieldDepartmenLayout;
    /**
     * 到场部门
     */
    @BindView(R.id.field_departmen_edt)
    EditText fieldDepartmenEdt;

    /**
     * 到场部门图标
     */
    @BindView(R.id.field_departmen_img)
    ImageView fieldDepartmenImg;
    /**
     * 表现形式
     */
    @BindView(R.id.pattern_manifestation_spinner)
    Spinner patternManifestationSpinner;

    /**
     * 现场态势
     */
    @BindView(R.id.field_morphology_spinner)
    Spinner fieldMorpholoySpinner;

    /**
     * 现场态势LinearLayout
     */
    @BindView(R.id.xcts_linear)
    LinearLayout xctsLinear;
    /**
     * 规模
     */
    @BindView(R.id.scope_text_spinner)
    Spinner scopeTextSpinner;
    /**
     * 涉及领域LinearLayout
     */
    @BindView(R.id.fields_involved_linearlayout)
    LinearLayout fieldsInvolvedLinearLayout;
    /**
     * 涉及领域
     */
    @BindView(R.id.fields_involved_edt)
    EditText fieldsInvolvedEdt;
    /**
     * 涉及领域img
     */
    @BindView(R.id.fields_involved_img)
    ImageView fieldsInvolvedImg;
    /**
     * 是否涉外
     */
    @BindView(R.id.foreign_related_spinner)
    Spinner foreignRelatedSpinner;
    /**
     * 是否涉疆
     */
    @BindView(R.id.involved_xinjiang_spinner)
    Spinner involvedXinjiangSpinner;
    /**
     * 是否涉舆情
     */
    @BindView(R.id.involve_public_opinion_spinner)
    Spinner involvePublicOpinionSpinner;
    /**
     * 是否公安处置
     */
    @BindView(R.id.public_security_disposal_spinner)
    Spinner publicSecurityDisposalSpinner;
    /**
     * 所属街道
     */
    @BindView(R.id.belong_street_edt)
    EditText belongStreetEdt;
    /**
     * 所属社区
     */
    @BindView(R.id.belong_community_spinner)
    Spinner belongCommunitySpinner;
    /**
     * 主要诉求
     */
    @BindView(R.id.main_appeals_edt)
    EditText mainAppealsEdt;
    /**
     * 事件概要
     */
    @BindView(R.id.event_summary_edt)
    EditText eventSummaryEdt;
    /**
     * >领导批示
     */
    @BindView(R.id.leadership_instructions_edt)
    EditText leadershipInstructionsEdt;


    /**
     * 设置Spinner控件的初始值
     */
    public List<CodeValue> spinnerList;

    /**
     * 数组 配置器 下拉菜单赋值用
     */
    ArrayAdapter<CodeValue> adapter;

    public TaskEntity taskEntity;


    public DescEntityDao descEntityDao;

    private Intent intent;

    public  TaskFragment_Basic(Intent intent){
        this.intent = intent;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.evententeradd_basic_fragment, container, false);
        ButterKnife.bind(this, view);
        descEntityDao = new DescEntityDao();
        taskEntity = (TaskEntity) intent.getSerializableExtra("taskEntity");
        initData();
        return view;
    }

    /**
     * 新增页面跟查看详情是同一个页面，初始化页面基本信息
     */
    public void initData() {
        spinnerList = descEntityDao.queryListForCV("sfsw");
        if (spinnerList == null) {
            //设置Spinner控件的初始值
            spinnerList = new ArrayList<CodeValue>();
        }
        //将可选内容与ArrayAdapter连接起来
        adapter = new ArrayAdapter<CodeValue>(TaskFragment_Basic.this.getActivity(), android.R.layout.simple_spinner_item, spinnerList);
        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        foreignRelatedSpinner.setAdapter(adapter);//将adapter 添加到spinner中
        involvedXinjiangSpinner.setAdapter(adapter);//将adapter 添加到spinner中
        involvePublicOpinionSpinner.setAdapter(adapter);//将adapter 添加到spinner中
        publicSecurityDisposalSpinner.setAdapter(adapter);//将adapter 添加到spinner中

        spinnerList = descEntityDao.queryListForCV("BXXS");
        if (spinnerList == null) {
            //设置Spinner控件的初始值
            spinnerList = new ArrayList<CodeValue>();
        }
        //将可选内容与ArrayAdapter连接起来
        adapter = new ArrayAdapter<CodeValue>(TaskFragment_Basic.this.getActivity(), android.R.layout.simple_spinner_item, spinnerList);
        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        patternManifestationSpinner.setAdapter(adapter);//将adapter 添加到表现形式spinner中

        spinnerList = descEntityDao.queryListForCV("XCTS");
        if (spinnerList == null) {
            //设置Spinner控件的初始值
            spinnerList = new ArrayList<CodeValue>();
        }
        //将可选内容与ArrayAdapter连接起来
        adapter = new ArrayAdapter<CodeValue>(TaskFragment_Basic.this.getActivity(), android.R.layout.simple_spinner_item, spinnerList);
        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fieldMorpholoySpinner.setAdapter(adapter);//将adapter 添加到现场态势spinner中

        spinnerList = descEntityDao.queryListForCV("sjgm");
        if (spinnerList == null) {
            //设置Spinner控件的初始值
            spinnerList = new ArrayList<CodeValue>();
        }
        //将可选内容与ArrayAdapter连接起来
        adapter = new ArrayAdapter<CodeValue>(TaskFragment_Basic.this.getActivity(), android.R.layout.simple_spinner_item, spinnerList);
        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        scopeTextSpinner.setAdapter(adapter);//将adapter 添加到规模spinner中

        spinnerList = new ArrayList<CodeValue>();
        spinnerList.add(new CodeValue("0", "社区1"));
        spinnerList.add(new CodeValue("1", "社区2"));


        //将可选内容与ArrayAdapter连接起来
        adapter = new ArrayAdapter<CodeValue>(TaskFragment_Basic.this.getActivity(), android.R.layout.simple_spinner_item, spinnerList);
        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        belongCommunitySpinner.setAdapter(adapter);//将adapter 添加到所属社区spinner中

        if (taskEntity != null) {
            thingNameEdt.setEnabled(false);
            happenTimeEdt.setEnabled(false);
            happenAddressEdt.setEnabled(false);
            petitionGroupsEdt.setEnabled(false);
            fieldDepartmenEdt.setEnabled(false);
            fieldsInvolvedEdt.setEnabled(false);
            belongStreetEdt.setEnabled(false);
            mainAppealsEdt.setEnabled(false);
            eventSummaryEdt.setEnabled(false);
            leadershipInstructionsEdt.setEnabled(false);
            //以下为下拉框控件
            patternManifestationSpinner.setEnabled(false);
            fieldMorpholoySpinner.setEnabled(false);
            scopeTextSpinner.setEnabled(false);
            foreignRelatedSpinner.setEnabled(false);
            involvedXinjiangSpinner.setEnabled(false);
            involvePublicOpinionSpinner.setEnabled(false);
            publicSecurityDisposalSpinner.setEnabled(false);
            belongCommunitySpinner.setEnabled(false);


            thingNameEdt.setText(taskEntity.getSjmc());
            happenTimeEdt.setText(taskEntity.getFssj());
            happenAddressEdt.setText(taskEntity.getFsdd());
            petitionGroupsEdt.setText(taskEntity.getSfsqqt());

            if (taskEntity.getDcbm() != null && !"".equals(taskEntity.getDcbm())) {
                String []dcbms = taskEntity.getDcbm().split(",");
                String dcbm = "";
                if(dcbms.length>0){
                    for(int i = 0 ;i<dcbms.length;i++){
                        dcbm += descEntityDao.queryName("dcbm", dcbms[i])+",";
                    }
                    dcbm=dcbm.substring(0,dcbm.length()-1);
                }
                fieldDepartmenEdt.setText(dcbm);
            }
            if (taskEntity.getSjly() != null && !"".equals(taskEntity.getSjly())) {
                String []sjlys = taskEntity.getSjly().split(",");
                String sjly = "";
                if(sjlys.length>0){
                    for(int i = 0 ;i<sjlys.length;i++){
                        sjly += descEntityDao.queryName("sjly", sjlys[i])+",";
                    }
                    sjly=sjly.substring(0,sjly.length()-1);
                }
                //String sjlyName = descEntityDao.queryName("sjly", taskEntity.getSjly().split(",")[0]);
                fieldsInvolvedEdt.setText(sjly);
            }


            belongStreetEdt.setText(taskEntity.getSsjd());
            mainAppealsEdt.setText(taskEntity.getZysq());
            eventSummaryEdt.setText(taskEntity.getSjgyqk());
            leadershipInstructionsEdt.setText(taskEntity.getLdps());
            //以下为下拉控件设置默认值
            if (taskEntity.getBxxs() != null) {
                patternManifestationSpinner.setSelection(Integer.valueOf(taskEntity.getBxxs().split(",")[0]) - 1);
            }

            //if (taskEntity.getXcts() != null) {
            //    fieldMorpholoySpinner.setSelection(Integer.valueOf(taskEntity.getXcts())-1);
            //}
            xctsLinear.setVisibility(View.GONE);
            if (taskEntity.getGm() != null) {
                scopeTextSpinner.setSelection(Integer.valueOf(taskEntity.getGm())-1);
            }
            if (taskEntity.getSfsw() != null) {
                foreignRelatedSpinner.setSelection(Integer.valueOf(taskEntity.getSfsw()));
            }
            if (taskEntity.getSfsj() != null) {
                involvedXinjiangSpinner.setSelection(Integer.valueOf(taskEntity.getSfsj()));
            }
            if (taskEntity.getSfsyq() != null) {
                involvePublicOpinionSpinner.setSelection(Integer.valueOf(taskEntity.getSfsyq()));
            }
            if (taskEntity.getSfgacz() != null) {
                publicSecurityDisposalSpinner.setSelection(Integer.valueOf(taskEntity.getSfgacz()));
            }

        }

    }

}

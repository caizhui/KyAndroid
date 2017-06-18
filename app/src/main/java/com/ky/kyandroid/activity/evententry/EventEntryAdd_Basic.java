package com.ky.kyandroid.activity.evententry;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ky.kyandroid.R;
import com.ky.kyandroid.bean.CodeValue;
import com.ky.kyandroid.db.dao.DescEntityDao;
import com.ky.kyandroid.db.dao.EventEntryDao;
import com.ky.kyandroid.entity.EventEntity;
import com.ky.kyandroid.util.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Caizhui on 2017-6-9.
 * 事件录入基本信息
 */

@SuppressLint("ValidFragment")
public class EventEntryAdd_Basic extends Fragment {


    /**
     * 事件名称
     */
    @BindView(R.id.thing_name_edt)
    EditText thingNameEdt;
    /**
     * 发生时间
     */
    @BindView(R.id.happen_time_edt)
    TextView happenTimeEdt;
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
     * 到场部门
     */
    @BindView(R.id.field_departmen_edt)
    EditText fieldDepartmenEdt;
    /**
     * 表现形式
     */
    @BindView(R.id.pattern_manifestation_spinner)
    Spinner patternManifestationSpinner;
    /**
     * 规模
     */
    @BindView(R.id.scope_text_spinner)
    Spinner scopeTextSpinner;
    /**
     * 涉及领域
     */
    @BindView(R.id.fields_involved_edt)
    EditText fieldsInvolvedEdt;
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

    public EventEntryDao eventEntryDao;


    /**
     * 提示信息
     */
    private String message = "";

    private Intent intent;

    /**
     * type 0：新增 1：修改
     */
    public String type;

    public EventEntity eventEntity;


    public DescEntityDao descEntityDao;

    @SuppressLint("ValidFragment")
    public EventEntryAdd_Basic(Intent intent) {
        this.intent = intent;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.evententeradd_basic_fragment, container, false);
        ButterKnife.bind(this, view);
        eventEntryDao = new EventEntryDao();
        descEntityDao = new DescEntityDao();
        type = intent.getStringExtra("type");
        eventEntity = (EventEntity) intent.getSerializableExtra("eventEntity");
        initData();
        return view;
    }

    public void initData() {
        if (eventEntity != null) {
            //当type等于1的时候，只能查看信息
            if ("1".equals(type)) {
               /* thingNameEdt.setText(eventEntity.getThingName());
                happenTimeEdt.setText(eventEntity.getHappenTime());
                happenAddressEdt.setText(eventEntity.getHappenAddress());
                petitionGroupsEdt.setText(eventEntity.getPetitionGroups());
                fieldDepartmenEdt.setText(eventEntity.getFieldDepartmen());
                fieldsInvolvedEdt.setText(eventEntity.getFieldsInvolved());
                belongStreetEdt.setText(eventEntity.getBelongStreet());
                mainAppealsEdt.setText(eventEntity.getMainAppeals());
                eventSummaryEdt.setText(eventEntity.getEventSummary());
                leadershipInstructionsEdt.setText(eventEntity.getLeadershipInstructions());*/
            }
        }
        //设置Spinner控件的初始值
        spinnerList = new ArrayList<CodeValue>();
        spinnerList = descEntityDao.queryListForCV("sfsw");

        //将可选内容与ArrayAdapter连接起来
        adapter = new ArrayAdapter<CodeValue>(EventEntryAdd_Basic.this.getActivity(), android.R.layout.simple_spinner_item, spinnerList);
        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        foreignRelatedSpinner.setAdapter(adapter);//将adapter 添加到spinner中
        involvedXinjiangSpinner.setAdapter(adapter);//将adapter 添加到spinner中
        involvePublicOpinionSpinner.setAdapter(adapter);//将adapter 添加到spinner中
        publicSecurityDisposalSpinner.setAdapter(adapter);//将adapter 添加到spinner中

        spinnerList = new ArrayList<CodeValue>();
        spinnerList = descEntityDao.queryListForCV("BXXS");

        //将可选内容与ArrayAdapter连接起来
        adapter = new ArrayAdapter<CodeValue>(EventEntryAdd_Basic.this.getActivity(), android.R.layout.simple_spinner_item, spinnerList);
        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        patternManifestationSpinner.setAdapter(adapter);//将adapter 添加到表现形式spinner中

        spinnerList = new ArrayList<CodeValue>();
        spinnerList = descEntityDao.queryListForCV("sjgm");

        //将可选内容与ArrayAdapter连接起来
        adapter = new ArrayAdapter<CodeValue>(EventEntryAdd_Basic.this.getActivity(), android.R.layout.simple_spinner_item, spinnerList);
        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        scopeTextSpinner.setAdapter(adapter);//将adapter 添加到规模spinner中


        spinnerList = new ArrayList<CodeValue>();
        spinnerList.add(new CodeValue("0", "社区1"));
        spinnerList.add(new CodeValue("1", "社区2"));


        //将可选内容与ArrayAdapter连接起来
        adapter = new ArrayAdapter<CodeValue>(EventEntryAdd_Basic.this.getActivity(), android.R.layout.simple_spinner_item, spinnerList);
        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        belongCommunitySpinner.setAdapter(adapter);//将adapter 添加到所属社区spinner中
    }

    @OnClick({R.id.happen_time_edt})
    public void onClick(View v) {
        switch (v.getId()) {
            /** 点击发生时间控件 **/
            case R.id.happen_time_edt:
                Calendar c = Calendar.getInstance();
                new DatePickerDialog(EventEntryAdd_Basic.this.getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        happenTimeEdt.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
                break;
        }
    }

    /**
     * 封装数据
     */
    public EventEntity PackageData() {
        //每次保存时先清空message
        message = "";
        if(eventEntity == null){
            eventEntity = new EventEntity();
        }
        String thingNameString = thingNameEdt.getText().toString();
        String happenTimeString = happenTimeEdt.getText().toString();
        String happenAddressString = happenAddressEdt.getText().toString();
        String petitionGroupsString = petitionGroupsEdt.getText().toString();
        String fieldDepartmenString = fieldDepartmenEdt.getText().toString();
        String patternManifestationString =descEntityDao.queryCodeByName("BXXS",patternManifestationSpinner.getSelectedItem().toString());
        String scopeTextString = descEntityDao.queryCodeByName("sjgm",scopeTextSpinner.getSelectedItem().toString());
        String fieldsInvolved = fieldsInvolvedEdt.getText().toString();
        String foreignRelatedString = descEntityDao.queryCodeByName("sfsw",foreignRelatedSpinner.getSelectedItem().toString());
        String involvedXinjiangString = descEntityDao.queryCodeByName("sfsw",involvedXinjiangSpinner.getSelectedItem().toString());
        String involvePublicOpinionString = descEntityDao.queryCodeByName("sfsw",involvePublicOpinionSpinner.getSelectedItem().toString());
        String publicSecurityDisposalString = descEntityDao.queryCodeByName("sfsw",publicSecurityDisposalSpinner.getSelectedItem().toString());
        String belongStreetString = belongStreetEdt.getText().toString();
        String belongCommunityString = belongCommunitySpinner.getSelectedItem().toString();
        String mainAppealsString = mainAppealsEdt.getText().toString();
        String eventSummaryString = eventSummaryEdt.getText().toString();
        String leadershipInstructionsString = leadershipInstructionsEdt.getText().toString();
        if (StringUtils.isBlank(thingNameString)) {
            message += "事件名称不能为空\n";
        } else {
            eventEntity.setSjmc(thingNameString);
        }
        if (StringUtils.isBlank(happenTimeString)) {
            message += "发生时间不能为空\n";
        } else {
            eventEntity.setFssj(happenTimeString);
        }
        if (StringUtils.isBlank(happenAddressString)) {
            message += "发生地点不能为空\n";
        } else {
            eventEntity.setFsdd(happenAddressString);
        }
        eventEntity.setSfsqqt(petitionGroupsString);
        if (StringUtils.isBlank(fieldDepartmenString)) {
            message += "到场部门不能为空\n";
        } else {
            eventEntity.setDcbm(fieldDepartmenString);
        }
        if (StringUtils.isBlank(patternManifestationString)) {
            message += "表现形式不能为空\n";
        } else {
            eventEntity.setBxxs(patternManifestationString);
        }
        if (StringUtils.isBlank(scopeTextString)) {
            message += "规模不能为空\n";
        } else {
            eventEntity.setGm(scopeTextString);
        }
        if (StringUtils.isBlank(fieldsInvolved)) {
            message += "涉及领域不能为空\n";
        } else {
            eventEntity.setSjly(fieldsInvolved);
        }
        eventEntity.setSfsw(foreignRelatedString);
        eventEntity.setSfsj(involvedXinjiangString);
        eventEntity.setSfsyq(involvePublicOpinionString);
        eventEntity.setSfgacz(publicSecurityDisposalString);
        eventEntity.setSsjd(belongStreetString);
        eventEntity.setSssq(belongCommunityString);
        if (StringUtils.isBlank(mainAppealsString)) {
            message += "主要诉求不能为空\n";
        } else {
            eventEntity.setZysq(mainAppealsString);
        }
        if (StringUtils.isBlank(eventSummaryString)) {
            message += "事件概要不能为空\n";
        } else {
            eventEntity.setSjgyqk(eventSummaryString);
        }
        eventEntity.setLdps(leadershipInstructionsString);
        if(!"".equals(message)){
            Toast.makeText(EventEntryAdd_Basic.this.getActivity(),message,Toast.LENGTH_SHORT).show();
        }else{
            return eventEntity;
        }
        return  null;
    }

    public EventEntity getEventEntity() {
        return eventEntity;
    }

    public void setEventEntity(EventEntity eventEntity) {
        this.eventEntity = eventEntity;
    }
}

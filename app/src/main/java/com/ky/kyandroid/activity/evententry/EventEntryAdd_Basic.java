package com.ky.kyandroid.activity.evententry;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.ky.kyandroid.R;
import com.ky.kyandroid.bean.CodeValue;
import com.ky.kyandroid.db.dao.DescEntityDao;
import com.ky.kyandroid.entity.TFtSjEntity;
import com.ky.kyandroid.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
     * 表现形式
     */
    @BindView(R.id.field_morphology_spinner)
    Spinner fieldMorpholoySpinner;
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


    /**
     * 提示信息
     */
    private String message = "";

    private Intent intent;

    /**
     * type 0：新增 1：修改
     */
    public String type;

    public TFtSjEntity tFtSjEntity;


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
        descEntityDao = new DescEntityDao();
        type = intent.getStringExtra("type");
        tFtSjEntity = (TFtSjEntity) intent.getSerializableExtra("tFtSjEntity");
        initData();
        return view;
    }

    /**
     * 新增页面跟查看详情是同一个页面，初始化页面基本信息
     */
    public void initData() {
        if (tFtSjEntity != null) {
            //当状态等于1的时候，表示为草稿，可以修改，其他的时候只能查看信息
            if (!"1".equals(tFtSjEntity.getZt())) {
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
            }
            thingNameEdt.setText(tFtSjEntity.getSjmc());
            happenTimeEdt.setText(tFtSjEntity.getFssj());
            happenAddressEdt.setText(tFtSjEntity.getFsdd());
            petitionGroupsEdt.setText(tFtSjEntity.getSfsqqt());
            fieldDepartmenEdt.setText(tFtSjEntity.getDcbm());
            fieldsInvolvedEdt.setText(tFtSjEntity.getSjly());
            belongStreetEdt.setText(tFtSjEntity.getSsjd());
            mainAppealsEdt.setText(tFtSjEntity.getZysq());
            eventSummaryEdt.setText(tFtSjEntity.getSjgyqk());
            leadershipInstructionsEdt.setText(tFtSjEntity.getLdps());
        }

        spinnerList = descEntityDao.queryListForCV("sfsw");
        if (spinnerList == null) {
            //设置Spinner控件的初始值
            spinnerList = new ArrayList<CodeValue>();
        }
        //将可选内容与ArrayAdapter连接起来
        adapter = new ArrayAdapter<CodeValue>(EventEntryAdd_Basic.this.getActivity(), android.R.layout.simple_spinner_item, spinnerList);
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
        adapter = new ArrayAdapter<CodeValue>(EventEntryAdd_Basic.this.getActivity(), android.R.layout.simple_spinner_item, spinnerList);
        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        patternManifestationSpinner.setAdapter(adapter);//将adapter 添加到表现形式spinner中

        spinnerList = descEntityDao.queryListForCV("XCTS");
        if (spinnerList == null) {
            //设置Spinner控件的初始值
            spinnerList = new ArrayList<CodeValue>();
        }
        //将可选内容与ArrayAdapter连接起来
        adapter = new ArrayAdapter<CodeValue>(EventEntryAdd_Basic.this.getActivity(), android.R.layout.simple_spinner_item, spinnerList);
        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fieldMorpholoySpinner.setAdapter(adapter);//将adapter 添加到现场态势spinner中

        spinnerList = descEntityDao.queryListForCV("sjgm");
        if (spinnerList == null) {
            //设置Spinner控件的初始值
            spinnerList = new ArrayList<CodeValue>();
        }
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
                happenTimeEdt.clearFocus();
                happenTimeEdt.setInputType(InputType.TYPE_NULL);
                InputMethodManager imm = (InputMethodManager) EventEntryAdd_Basic.this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(happenTimeEdt.getWindowToken(), 0);
                Calendar c = Calendar.getInstance();
                new DatePickerDialog(EventEntryAdd_Basic.this.getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Date date = new Date(System.currentTimeMillis());
                        SimpleDateFormat dateFormat = new SimpleDateFormat(" HH:mm:ss");
                        String time = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        time += dateFormat.format(date);
                        happenTimeEdt.setText(time);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
                break;
        }
    }

    /**
     * 封装数据
     */
    public TFtSjEntity PackageData() {
        //每次保存时先清空message
        message = "";
        if (tFtSjEntity == null) {
            tFtSjEntity = new TFtSjEntity();
        }
        String thingNameString = thingNameEdt.getText().toString();
        String happenTimeString = happenTimeEdt.getText().toString();
        String happenAddressString = happenAddressEdt.getText().toString();
        String petitionGroupsString = petitionGroupsEdt.getText().toString();
        String fieldDepartmenString = fieldDepartmenEdt.getText().toString();
        String patternManifestationString = descEntityDao.queryCodeByName("BXXS", patternManifestationSpinner.getSelectedItem().toString());
        String fieldMorpholoySpinnerString = descEntityDao.queryCodeByName("XCTS", fieldMorpholoySpinner.getSelectedItem().toString());
        String scopeTextString = descEntityDao.queryCodeByName("sjgm", scopeTextSpinner.getSelectedItem().toString());
        String fieldsInvolved = fieldsInvolvedEdt.getText().toString();
        String foreignRelatedString = descEntityDao.queryCodeByName("sfsw", foreignRelatedSpinner.getSelectedItem().toString());
        String involvedXinjiangString = descEntityDao.queryCodeByName("sfsw", involvedXinjiangSpinner.getSelectedItem().toString());
        String involvePublicOpinionString = descEntityDao.queryCodeByName("sfsw", involvePublicOpinionSpinner.getSelectedItem().toString());
        String publicSecurityDisposalString = descEntityDao.queryCodeByName("sfsw", publicSecurityDisposalSpinner.getSelectedItem().toString());
        String belongStreetString = belongStreetEdt.getText().toString();
        String belongCommunityString = belongCommunitySpinner.getSelectedItem().toString();
        String mainAppealsString = mainAppealsEdt.getText().toString();
        String eventSummaryString = eventSummaryEdt.getText().toString();
        String leadershipInstructionsString = leadershipInstructionsEdt.getText().toString();
        if (StringUtils.isBlank(thingNameString)) {
            message += "事件名称不能为空\n";
        } else {
            tFtSjEntity.setSjmc(thingNameString);
        }
        if (StringUtils.isBlank(happenTimeString)) {
            message += "发生时间不能为空\n";
        } else {
            tFtSjEntity.setFssj(happenTimeString);
        }
        if (StringUtils.isBlank(happenAddressString)) {
            message += "发生地点不能为空\n";
        } else {
            tFtSjEntity.setFsdd(happenAddressString);
        }
        tFtSjEntity.setSfsqqt(petitionGroupsString);
        if (StringUtils.isBlank(fieldDepartmenString)) {
            message += "到场部门不能为空\n";
        } else {
            tFtSjEntity.setDcbm(fieldDepartmenString);
        }
        if (StringUtils.isBlank(patternManifestationString)) {
            message += "表现形式不能为空\n";
        } else {
            tFtSjEntity.setBxxs(patternManifestationString);
        }
        if (StringUtils.isBlank(fieldMorpholoySpinnerString)) {
            message += "现场态势不能为空\n";
        } else {
            tFtSjEntity.setXcts(fieldMorpholoySpinnerString);
        }
        if (StringUtils.isBlank(scopeTextString)) {
            message += "规模不能为空\n";
        } else {
            tFtSjEntity.setGm(scopeTextString);
        }
        if (StringUtils.isBlank(fieldsInvolved)) {
            message += "涉及领域不能为空\n";
        } else {
            tFtSjEntity.setSjly(fieldsInvolved);
        }
        tFtSjEntity.setSfsw(foreignRelatedString);
        tFtSjEntity.setSfsj(involvedXinjiangString);
        tFtSjEntity.setSfsyq(involvePublicOpinionString);
        tFtSjEntity.setSfgacz(publicSecurityDisposalString);
        tFtSjEntity.setSsjd(belongStreetString);
        tFtSjEntity.setSssq(belongCommunityString);
        if (StringUtils.isBlank(mainAppealsString)) {
            message += "主要诉求不能为空\n";
        } else {
            tFtSjEntity.setZysq(mainAppealsString);
        }
        if (StringUtils.isBlank(eventSummaryString)) {
            message += "事件概要不能为空\n";
        } else {
            tFtSjEntity.setSjgyqk(eventSummaryString);
        }
        tFtSjEntity.setLdps(leadershipInstructionsString);
        if (!"".equals(message)) {
            Toast.makeText(EventEntryAdd_Basic.this.getActivity(), message, Toast.LENGTH_SHORT).show();
        } else {
            return tFtSjEntity;
        }
        return null;
    }
}

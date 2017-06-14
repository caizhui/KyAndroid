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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ky.kyandroid.R;
import com.ky.kyandroid.activity.supervision.SuperVisionAddActivity;
import com.ky.kyandroid.db.dao.EventEntryDao;
import com.ky.kyandroid.entity.EventEntryEntity;
import com.ky.kyandroid.entity.KeyValueEntity;
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
     * 上报领导按钮
     */
    @BindView(R.id.reporting_leadership_btn)
    Button reportingLeadershipBtn;
    /**
     * 保存按钮
     */
    @BindView(R.id.save_draft_btn)
    Button saveDraftBtn;


    /**
     * 设置Spinner控件的初始值
     */
    public List<KeyValueEntity> spinnerList;

    /**
     * 数组 配置器 下拉菜单赋值用
     */
    ArrayAdapter<KeyValueEntity> adapter;

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

    public EventEntryEntity eventEntryEntity;

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
        type = intent.getStringExtra("type");
        eventEntryEntity = (EventEntryEntity) intent.getSerializableExtra("eventEntryEntity");
        initData();
        return view;
    }

    public void initData() {
        if (eventEntryEntity != null) {
            //当type等于1的时候，只能查看信息
            if ("1".equals(type)) {
                thingNameEdt.setText(eventEntryEntity.getThingName());
                happenTimeEdt.setText(eventEntryEntity.getHappenTime());
                happenAddressEdt.setText(eventEntryEntity.getHappenAddress());
                petitionGroupsEdt.setText(eventEntryEntity.getPetitionGroups());
                fieldDepartmenEdt.setText(eventEntryEntity.getFieldDepartmen());
                fieldsInvolvedEdt.setText(eventEntryEntity.getFieldsInvolved());
                belongStreetEdt.setText(eventEntryEntity.getBelongStreet());
                mainAppealsEdt.setText(eventEntryEntity.getMainAppeals());
                eventSummaryEdt.setText(eventEntryEntity.getEventSummary());
                leadershipInstructionsEdt.setText(eventEntryEntity.getLeadershipInstructions());
            }
        }
        //设置Spinner控件的初始值
        spinnerList = new ArrayList<KeyValueEntity>();
        spinnerList.add(new KeyValueEntity("0", "否"));
        spinnerList.add(new KeyValueEntity("1", "是"));

        //将可选内容与ArrayAdapter连接起来
        adapter = new ArrayAdapter<KeyValueEntity>(EventEntryAdd_Basic.this.getActivity(), android.R.layout.simple_spinner_item, spinnerList);
        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        foreignRelatedSpinner.setAdapter(adapter);//将adapter 添加到spinner中
        involvedXinjiangSpinner.setAdapter(adapter);//将adapter 添加到spinner中
        involvePublicOpinionSpinner.setAdapter(adapter);//将adapter 添加到spinner中
        publicSecurityDisposalSpinner.setAdapter(adapter);//将adapter 添加到spinner中

        spinnerList = new ArrayList<KeyValueEntity>();
        spinnerList.add(new KeyValueEntity("", "请选择"));
        spinnerList.add(new KeyValueEntity("0", "上访"));
        spinnerList.add(new KeyValueEntity("1", "自杀"));
        spinnerList.add(new KeyValueEntity("2", "闹事"));
        spinnerList.add(new KeyValueEntity("3", "聚众"));
        spinnerList.add(new KeyValueEntity("4", "纠纷"));
        spinnerList.add(new KeyValueEntity("5", "拉横幅"));

        //将可选内容与ArrayAdapter连接起来
        adapter = new ArrayAdapter<KeyValueEntity>(EventEntryAdd_Basic.this.getActivity(), android.R.layout.simple_spinner_item, spinnerList);
        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        patternManifestationSpinner.setAdapter(adapter);//将adapter 添加到表现形式spinner中

        spinnerList = new ArrayList<KeyValueEntity>();
        spinnerList.add(new KeyValueEntity("0", "1-5人"));
        spinnerList.add(new KeyValueEntity("1", "5-30人"));
        spinnerList.add(new KeyValueEntity("2", "30-300人"));
        spinnerList.add(new KeyValueEntity("3", "300-1000人"));
        spinnerList.add(new KeyValueEntity("4", "1000人以上"));

        //将可选内容与ArrayAdapter连接起来
        adapter = new ArrayAdapter<KeyValueEntity>(EventEntryAdd_Basic.this.getActivity(), android.R.layout.simple_spinner_item, spinnerList);
        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        scopeTextSpinner.setAdapter(adapter);//将adapter 添加到规模spinner中


        spinnerList = new ArrayList<KeyValueEntity>();
        spinnerList.add(new KeyValueEntity("0", "社区1"));
        spinnerList.add(new KeyValueEntity("1", "社区2"));


        //将可选内容与ArrayAdapter连接起来
        adapter = new ArrayAdapter<KeyValueEntity>(EventEntryAdd_Basic.this.getActivity(), android.R.layout.simple_spinner_item, spinnerList);
        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        belongCommunitySpinner.setAdapter(adapter);//将adapter 添加到所属社区spinner中
    }

    @OnClick({R.id.happen_time_edt, R.id.reporting_leadership_btn, R.id.save_draft_btn})
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
            /** 上报领导按钮*/
            case R.id.reporting_leadership_btn:
                Intent intent1 = new Intent(this.getActivity(), SuperVisionAddActivity.class);
                startActivity(intent1);
                break;
            /**保存草稿按钮*/
            case R.id.save_draft_btn:
                PackageData();
                if ("".equals(message)) {
                    boolean flag ;
                    if("1".equals(type)){
                        flag = eventEntryDao.updateEventEntry(eventEntryEntity);
                        if (flag) {
                            Toast.makeText(EventEntryAdd_Basic.this.getActivity(), "修改成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(this.getActivity(), EventEntryListActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(EventEntryAdd_Basic.this.getActivity(),   "修改失败", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        //事件保存为1，事件提交为2
                        eventEntryEntity.setStatus("1");
                        flag = eventEntryDao.saveEventEntryEntity(eventEntryEntity);
                        if (flag) {
                            Toast.makeText(EventEntryAdd_Basic.this.getActivity(), "保存成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(this.getActivity(), EventEntryListActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(EventEntryAdd_Basic.this.getActivity(),   "保存失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(EventEntryAdd_Basic.this.getActivity(), message, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 封装数据
     */
    public void PackageData() {
        //每次保存时先清空message
        message = "";
        if(eventEntryEntity == null){
            eventEntryEntity = new EventEntryEntity();
        }
        String thingNameString = thingNameEdt.getText().toString();
        String happenTimeString = happenTimeEdt.getText().toString();
        String happenAddressString = happenAddressEdt.getText().toString();
        String petitionGroupsString = petitionGroupsEdt.getText().toString();
        String fieldDepartmenString = fieldDepartmenEdt.getText().toString();
        String patternManifestationString = patternManifestationSpinner.getSelectedItem().toString();
        String scopeTextString = scopeTextSpinner.getSelectedItem().toString();
        String fieldsInvolved = fieldsInvolvedEdt.getText().toString();
        String foreignRelatedString = foreignRelatedSpinner.getSelectedItem().toString();
        String involvedXinjiangString = involvedXinjiangSpinner.getSelectedItem().toString();
        String involvePublicOpinionString = involvePublicOpinionSpinner.getSelectedItem().toString();
        String publicSecurityDisposalString = publicSecurityDisposalSpinner.getSelectedItem().toString();
        String belongStreetString = belongStreetEdt.getText().toString();
        String belongCommunityString = belongCommunitySpinner.getSelectedItem().toString();
        String mainAppealsString = mainAppealsEdt.getText().toString();
        String eventSummaryString = eventSummaryEdt.getText().toString();
        String leadershipInstructionsString = leadershipInstructionsEdt.getText().toString();
        if (StringUtils.isBlank(thingNameString)) {
            message += "事件名称不能为空\n";
        } else {
            eventEntryEntity.setThingName(thingNameString);
        }
        if (StringUtils.isBlank(happenTimeString)) {
            message += "发生时间不能为空\n";
        } else {
            eventEntryEntity.setHappenTime(happenTimeString);
        }
        if (StringUtils.isBlank(happenAddressString)) {
            message += "发生地点不能为空\n";
        } else {
            eventEntryEntity.setHappenAddress(happenAddressString);
        }
        eventEntryEntity.setPetitionGroups(petitionGroupsString);
        if (StringUtils.isBlank(fieldDepartmenString)) {
            message += "到场部门不能为空\n";
        } else {
            eventEntryEntity.setFieldDepartmen(fieldDepartmenString);
        }
        if (StringUtils.isBlank(patternManifestationString)) {
            message += "表现形式不能为空\n";
        } else {
            eventEntryEntity.setPatternManifestation(patternManifestationString);
        }
        if (StringUtils.isBlank(scopeTextString)) {
            message += "规模不能为空\n";
        } else {
            eventEntryEntity.setScope(scopeTextString);
        }
        if (StringUtils.isBlank(fieldsInvolved)) {
            message += "涉及领域不能为空\n";
        } else {
            eventEntryEntity.setFieldsInvolved(fieldsInvolved);
        }
        eventEntryEntity.setForeignRelated(foreignRelatedString);
        eventEntryEntity.setInvolvedXinjiang(involvedXinjiangString);
        eventEntryEntity.setInvolvePublicOpinion(involvePublicOpinionString);
        eventEntryEntity.setPublicSecurityDisposal(publicSecurityDisposalString);
        eventEntryEntity.setBelongStreet(belongStreetString);
        eventEntryEntity.setBelongCommunity(belongCommunityString);
        if (StringUtils.isBlank(mainAppealsString)) {
            message += "主要诉求不能为空\n";
        } else {
            eventEntryEntity.setMainAppeals(mainAppealsString);
        }
        if (StringUtils.isBlank(eventSummaryString)) {
            message += "事件概要不能为空\n";
        } else {
            eventEntryEntity.setEventSummary(eventSummaryString);
        }
        eventEntryEntity.setLeadershipInstructions(leadershipInstructionsString);
    }
}

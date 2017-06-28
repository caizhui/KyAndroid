package com.ky.kyandroid.activity.evententry;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import butterknife.OnTouch;

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
   /* *//**
     * 到场部门
     *//*
    @BindView(R.id.field_departmen_edt)
    EditText fieldDepartmenEdt;*/
    @BindView(R.id.field_departmen_spinner_one)
    Spinner fieldDepartmenSpinnerOne;

    @BindView(R.id.field_departmen_spinner_two)
    Spinner fieldDepartmenSpinnerTwo;

    @BindView(R.id.field_departmen_spinner_three)
    Spinner fieldDepartmenSpinnerThree;
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

    private Spinner provinceSpinner = null; // 省（省、直辖市）
    private Spinner citySpinner = null; // 市
    private Spinner countrySpinner = null; // 区

    private ArrayAdapter<String> fieldDepartmenSpinnerOneAdapter = null; // 省
    private ArrayAdapter<String> fieldDepartmenSpinnerTwoAdapter = null; // 市
    private ArrayAdapter<String> fieldDepartmenSpinnerThreeAdapter = null; // 区

    private int provincePosition = 3;

    // 省级选项值
    private String[] province = new String[] { "北京", "上海", "天津", "广东" };// ,"重庆","黑龙江","江苏","山东","浙江","香港","澳门"};

    // 市级选项值
    private String[][] city = new String[][] {
            { "东城区", "西城区", "崇文区", "宣武区", "朝阳区", "海淀区", "丰台区", "石景山区", "门头沟区","房山区", "通州区", "顺义区", "大兴区", "昌平区", "平谷区", "怀柔区", "密云县","延庆县" },
            { "长宁区", "静安区", "普陀区", "闸北区", "虹口区" },
            { "和平区", "河东区", "河西区", "南开区", "河北区", "红桥区", "塘沽区", "汉沽区", "大港区","东丽区" },
            { "广州", "深圳", "韶关" // ,"珠海","汕头","佛山","湛江","肇庆","江门","茂名","惠州","梅州",
                    // "汕尾","河源","阳江","清远","东莞","中山","潮州","揭阳","云浮"
            } };

    // 区县级选项值
    private String[][][] country = new String[][][]
            {
                    {   //北京
                            {"无"},{"无"},{"无"},{"无"},{"无"},{"无"},{"无"},{"无"},{"无"},{"无"},
                            {"无"},{"无"},{"无"},{"无"},{"无"},{"无"},{"无"},{"无"}
                    },
                    {    //上海
                            {"无"},{"无"},{"无"},{"无"},{"无"}
                    },
                    {    //天津
                            {"无"},{"无"},{"无"},{"无"},{"无"},{"无"},{"无"},{"无"},{"无"},{"无"}
                    },
                    {    //广东
                            {"海珠区","荔湾区","越秀区","白云区","萝岗区","天河区","黄埔区","花都区","从化市","增城市","番禺区","南沙区"}, //广州
                            {"宝安区","福田区","龙岗区","罗湖区","南山区","盐田区"}, //深圳
                            {"武江区","浈江区","曲江区","乐昌市","南雄市","始兴县","仁化县","翁源县","新丰县","乳源县"}  //韶关
                    }
            };


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
        if (tFtSjEntity != null) {
            //当状态等于1的时候，表示为草稿，可以修改，其他的时候只能查看信息
            if (!"0".equals(tFtSjEntity.getZt()) && !"3".equals(tFtSjEntity.getZt())) {
                thingNameEdt.setEnabled(false);
                happenTimeEdt.setEnabled(false);
                happenAddressEdt.setEnabled(false);
                petitionGroupsEdt.setEnabled(false);
                //fieldDepartmenEdt.setEnabled(false);
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
            }
            thingNameEdt.setText(tFtSjEntity.getSjmc());
            happenTimeEdt.setText(tFtSjEntity.getFssj());
            happenAddressEdt.setText(tFtSjEntity.getFsdd());
            petitionGroupsEdt.setText(tFtSjEntity.getSfsqqt());
            //fieldDepartmenEdt.setText(tFtSjEntity.getDcbm());
            fieldsInvolvedEdt.setText(tFtSjEntity.getSjly());
            belongStreetEdt.setText(tFtSjEntity.getSsjd());
            mainAppealsEdt.setText(tFtSjEntity.getZysq());
            eventSummaryEdt.setText(tFtSjEntity.getSjgyqk());
            leadershipInstructionsEdt.setText(tFtSjEntity.getLdps());
            //以下为下拉控件设置默认值
            if(tFtSjEntity.getBxxs()!=null) {
                patternManifestationSpinner.setSelection(Integer.valueOf(tFtSjEntity.getBxxs().split(",")[0]) - 1);
            }
            if(tFtSjEntity.getXcts()!=null){
                fieldMorpholoySpinner.setSelection(Integer.valueOf(tFtSjEntity.getXcts()));
            }
            if(tFtSjEntity.getGm()!=null){
                scopeTextSpinner.setSelection(Integer.valueOf(tFtSjEntity.getGm()));
            }
            if(tFtSjEntity.getSfsw()!=null){
                foreignRelatedSpinner.setSelection(Integer.valueOf(tFtSjEntity.getSfsw()));
            }
            if(tFtSjEntity.getSfsj()!=null){
                involvedXinjiangSpinner.setSelection(Integer.valueOf(tFtSjEntity.getSfsj()));
            }
            if(tFtSjEntity.getSfsyq()!=null){
                involvePublicOpinionSpinner.setSelection(Integer.valueOf(tFtSjEntity.getSfsyq()));
            }
            if(tFtSjEntity.getSfgacz()!=null) {
                publicSecurityDisposalSpinner.setSelection(Integer.valueOf(tFtSjEntity.getSfgacz()));
            }

        }

    }

    @OnTouch({R.id.happen_time_edt})
    public boolean OnTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            /** 点击发生时间控件 **/
            case R.id.happen_time_edt:
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    happenTimeEdt.clearFocus();
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
                    return false;
                }
                break;
        }
        return false;
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
       // String fieldDepartmenString = fieldDepartmenEdt.getText().toString();
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
       /* if (StringUtils.isBlank(fieldDepartmenString)) {
            message += "到场部门不能为空\n";
        } else {
            tFtSjEntity.setDcbm(fieldDepartmenString);
        }*/
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

    public  void setFieldDepartmen(){
        // 装载适配器和值
        fieldDepartmenSpinnerOneAdapter = new ArrayAdapter<String>(EventEntryAdd_Basic.this.getActivity(),
                android.R.layout.simple_spinner_item, province);
        fieldDepartmenSpinnerOne.setAdapter(fieldDepartmenSpinnerOneAdapter);

        fieldDepartmenSpinnerTwoAdapter = new ArrayAdapter<String>(EventEntryAdd_Basic.this.getActivity(),
                android.R.layout.simple_spinner_item, city[3]);
        citySpinner.setAdapter(fieldDepartmenSpinnerTwoAdapter);

        fieldDepartmenSpinnerThreeAdapter = new ArrayAdapter<String>(EventEntryAdd_Basic.this.getActivity(),
                android.R.layout.simple_spinner_item, country[3][0]);
        countrySpinner.setAdapter(fieldDepartmenSpinnerThreeAdapter);

        // 省下拉框监听
        provinceSpinner
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long arg3) {

                        fieldDepartmenSpinnerTwoAdapter = new ArrayAdapter<String>(
                                EventEntryAdd_Basic.this.getActivity().getApplication(),
                                android.R.layout.simple_spinner_item,
                                city[position]);
                        citySpinner.setAdapter(fieldDepartmenSpinnerTwoAdapter);

                        provincePosition = position; // 记录当前省级序号，留给下面修改县级适配器时用
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {

                    }

                });

        // 市级下拉监听
        citySpinner
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long arg3) {
                        fieldDepartmenSpinnerThreeAdapter = new ArrayAdapter<String>(
                                EventEntryAdd_Basic.this.getActivity().getApplication(),
                                android.R.layout.simple_spinner_item,
                                country[provincePosition][position]);
                        countrySpinner.setAdapter(fieldDepartmenSpinnerThreeAdapter);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {

                    }
                });
    }
}

package com.ky.kyandroid.activity.evententry;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ky.kyandroid.R;
import com.ky.kyandroid.activity.LoginActivity;
import com.ky.kyandroid.adapter.GroupAdapter;
import com.ky.kyandroid.bean.CodeValue;
import com.ky.kyandroid.db.dao.DescEntityDao;
import com.ky.kyandroid.db.dao.TFtQhEntityDao;
import com.ky.kyandroid.entity.TFtQhEntity;
import com.ky.kyandroid.entity.TFtSjEntity;
import com.ky.kyandroid.util.DateTimePickDialogUtil;
import com.ky.kyandroid.util.SpUtil;
import com.ky.kyandroid.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
    /**
     * 到场部门LinearLayout
     */
    @BindView(R.id.field_departmen_layout)
    LinearLayout fieldDepartmenLayout;
    /**
     * 全局LinearLayout
     */
    @BindView(R.id.linear_evententry)
    LinearLayout linearEvententry;
    /**
     * 到场部门
     */
    @BindView(R.id.field_departmen_edt)
    TextView fieldDepartmenEdt;

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
    TextView fieldsInvolvedEdt;
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
    /**
     * SharedPreferences
     */
    private SharedPreferences sp;

    /**
     * 字典DAO
     */
    public DescEntityDao descEntityDao;

    /**
     * 字典DAO
     */
    public TFtQhEntityDao tFtQhEntityDao;

    View showPupWindow = null; // 选择区域的view

    /**
     * 一级菜单名称数组
     **/
    String[][] GroupNameArray;
    /**
     * 二级菜单名称数组
     **/
    String[][] childNameArray;
    /**
     * 三级菜单名称数组
     **/
    String[][] child2NameArray;

    ListView groupListView = null;
    ListView childListView = null;
    /**
     * 到场部门的第三层子节点ListView
     */
    ListView childListView2 = null;
    GroupAdapter groupAdapter = null;

    Button btnCancel, btnConfirm;

    TextView btntext;
    /**
     * 到场部门第二层adapter
     */
    GroupAdapter childTwoAdapter = null;


    GroupAdapter childAdapter = null;

    TranslateAnimation animation;// 出现的动画效果
    // 屏幕的宽高
    public static int screen_width = 0;
    public static int screen_height = 0;

    private boolean[] tabStateArr = new boolean[4];// 标记tab的选中状态，方便设置

    PopupWindow mPopupWindow = null;

    @SuppressLint("ValidFragment")
    public EventEntryAdd_Basic(Intent intent) {
        this.intent = intent;
    }

    /**
     * 判断是那个树 dcbm(到场部门),sjly(涉及领域)
     */
    private String spinnerType;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.evententeradd_basic_fragment, container, false);
        ButterKnife.bind(this, view);
        descEntityDao = new DescEntityDao();
        tFtQhEntityDao = new TFtQhEntityDao();
        sp = SpUtil.getSharePerference(getActivity());
        type = intent.getStringExtra("type");
        tFtSjEntity = (TFtSjEntity) intent.getSerializableExtra("tFtSjEntity");
        DisplayMetrics dm = new DisplayMetrics();
        EventEntryAdd_Basic.this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm); // 获取手机屏幕的大小
        screen_width = dm.widthPixels;
        screen_height = dm.heightPixels;
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
            Toast.makeText(EventEntryAdd_Basic.this.getActivity(), "字典项加载不完全,请重新加载", Toast.LENGTH_SHORT).show();
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

        // 根据用户ORG_CODE确定所属街道
        String jddm = sp.getString(LoginActivity.ORG_CODE, "");
        List<TFtQhEntity> qhList = tFtQhEntityDao.queryList(jddm);
        spinnerList = new ArrayList<CodeValue>();
        if (qhList != null && qhList.size() > 0) {
            belongStreetEdt.setText(qhList.get(0).getJdmc());
            for (TFtQhEntity entity : qhList) {
                if (StringUtils.isNotBlank(entity.getSqgzz())) {
                    spinnerList.add(new CodeValue(entity.getId(), entity.getSqgzz()));
                }
            }
        }

        //将可选内容与ArrayAdapter连接起来
        adapter = new ArrayAdapter<CodeValue>(EventEntryAdd_Basic.this.getActivity(), android.R.layout.simple_spinner_item, spinnerList);
        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        belongCommunitySpinner.setAdapter(adapter);//将adapter 添加到所属社区spinner中

        // 编辑下
        if (tFtSjEntity != null) {

            thingNameEdt.setText(tFtSjEntity.getSjmc());
            happenTimeEdt.setText(tFtSjEntity.getFssj());
            happenAddressEdt.setText(tFtSjEntity.getFsdd());
            petitionGroupsEdt.setText(tFtSjEntity.getSfsqqt());

            if (tFtSjEntity.getDcbm() != null && !"".equals(tFtSjEntity.getDcbm())) {
                String[] dcbms = tFtSjEntity.getDcbm().split(",");
                String dcbm = "";
                if (dcbms.length > 0) {
                    for (int i = 0; i < dcbms.length; i++) {
                        dcbm += descEntityDao.queryName("dcbm", dcbms[i]) + ",";
                    }
                    dcbm = dcbm.substring(0, dcbm.length() - 1);
                }
                fieldDepartmenEdt.setText(dcbm);
            }
            if (tFtSjEntity.getSjly() != null && !"".equals(tFtSjEntity.getSjly())) {
                String[] sjlys = tFtSjEntity.getSjly().split(",");
                String sjly = "";
                if (sjlys.length > 0) {
                    for (int i = 0; i < sjlys.length; i++) {
                        sjly += descEntityDao.queryName("sjly", sjlys[i]) + ",";
                    }
                    sjly = sjly.substring(0, sjly.length() - 1);
                }
                //String sjlyName = descEntityDao.queryName("sjly", tFtSjEntity.getSjly().split(",")[0]);
                fieldsInvolvedEdt.setText(sjly);
            }


            belongStreetEdt.setText(tFtSjEntity.getSsjd());
            mainAppealsEdt.setText(tFtSjEntity.getZysq());
            eventSummaryEdt.setText(tFtSjEntity.getSjgyqk());
            leadershipInstructionsEdt.setText(tFtSjEntity.getLdps());
            //以下为下拉控件设置默认值
            if (tFtSjEntity.getBxxs() != null) {
                patternManifestationSpinner.setSelection(Integer.valueOf(tFtSjEntity.getBxxs().split(",")[0]) - 1);
            }

            if (tFtSjEntity.getXcts() != null) {
                fieldMorpholoySpinner.setSelection(Integer.valueOf(tFtSjEntity.getXcts()) - 1);
            }
            if (tFtSjEntity.getGm() != null) {
                scopeTextSpinner.setSelection(Integer.valueOf(tFtSjEntity.getGm()) - 1);
            }
            if (tFtSjEntity.getSfsw() != null) {
                foreignRelatedSpinner.setSelection(Integer.valueOf(tFtSjEntity.getSfsw()));
            }
            if (tFtSjEntity.getSfsj() != null) {
                involvedXinjiangSpinner.setSelection(Integer.valueOf(tFtSjEntity.getSfsj()));
            }
            if (tFtSjEntity.getSfsyq() != null) {
                involvePublicOpinionSpinner.setSelection(Integer.valueOf(tFtSjEntity.getSfsyq()));
            }
            if (tFtSjEntity.getSfgacz() != null) {
                publicSecurityDisposalSpinner.setSelection(Integer.valueOf(tFtSjEntity.getSfgacz()));
            }

        }
    }

    @OnTouch({R.id.happen_time_edt})
    public boolean OnTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            /** 点击发生时间控件 **/
            case R.id.happen_time_edt:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    happenTimeEdt.clearFocus();
                    /*Calendar c = Calendar.getInstance();
                    new DatePickerDialog(EventEntryAdd_Basic.this.getActivity(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            Date date = new Date(System.currentTimeMillis());
                            SimpleDateFormat dateFormat = new SimpleDateFormat(" HH:mm:ss");
                            String time = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                            time += dateFormat.format(date);
                            happenTimeEdt.setText(time);
                        }
                    }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();*/
                    DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
                            EventEntryAdd_Basic.this.getActivity(), "");
                    dateTimePicKDialog.dateTimePicKDialog(happenTimeEdt);
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
        String fieldDepartmenString="";
        if(!"".equals(fieldDepartmenEdt.getText().toString())){
            String[] dcbms = fieldDepartmenEdt.getText().toString().split(",");
            for(int i=0;i<dcbms.length;i++){
                fieldDepartmenString += descEntityDao.queryCodeByName("dcbm", dcbms[i])+",";
            }
            fieldDepartmenString = fieldDepartmenString.substring(0,fieldDepartmenString.length()-1);

        }
        //String fieldDepartmenString = descEntityDao.queryCodeByName("dcbm", fieldDepartmenEdt.getText().toString());
        //String fieldDepartmenString = fieldDepartmenEdt.getText().toString();
        String patternManifestationString = descEntityDao.queryCodeByName("BXXS", patternManifestationSpinner.getSelectedItem().toString());
        String fieldMorpholoySpinnerString = descEntityDao.queryCodeByName("XCTS", fieldMorpholoySpinner.getSelectedItem().toString());
        String scopeTextString = descEntityDao.queryCodeByName("sjgm", scopeTextSpinner.getSelectedItem().toString());
        String fieldsInvolved="";
        if(!"".equals(fieldsInvolvedEdt.getText().toString())){
            String[] sjlys = fieldsInvolvedEdt.getText().toString().split(",");
            for(int i=0;i<sjlys.length;i++){
                fieldsInvolved += descEntityDao.queryCodeByName("sjly", sjlys[i])+",";
            }
            fieldsInvolved = fieldsInvolved.substring(0,fieldsInvolved.length()-1);

        }
        //String fieldsInvolved = descEntityDao.queryCodeByName("sjly", fieldsInvolvedEdt.getText().toString());
        String foreignRelatedString = descEntityDao.queryCodeByName("sfsw", foreignRelatedSpinner.getSelectedItem().toString());
        String involvedXinjiangString = descEntityDao.queryCodeByName("sfsw", involvedXinjiangSpinner.getSelectedItem().toString());
        String involvePublicOpinionString = descEntityDao.queryCodeByName("sfsw", involvePublicOpinionSpinner.getSelectedItem().toString());
        String publicSecurityDisposalString = descEntityDao.queryCodeByName("sfsw", publicSecurityDisposalSpinner.getSelectedItem().toString());
        String belongStreetString = belongStreetEdt.getText().toString();
        String belongCommunityString = ((CodeValue) belongCommunitySpinner.getSelectedItem()).getCode();
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
        // 上访群体
        tFtSjEntity.setSfsqqt(petitionGroupsString);
        // 到场部门
        tFtSjEntity.setDcbm(fieldDepartmenString);
        // 表现形式
        tFtSjEntity.setBxxs(patternManifestationString);
        // 现场态势
        tFtSjEntity.setXcts(fieldMorpholoySpinnerString);
        // 规模
        tFtSjEntity.setGm(scopeTextString);
        // 涉及领域
        tFtSjEntity.setSjly(fieldsInvolved);
        // 以下必填项由于PC改变而去除
        /*
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
         */
        tFtSjEntity.setSfsw(foreignRelatedString);
        tFtSjEntity.setSfsj(involvedXinjiangString);
        tFtSjEntity.setSfsyq(involvePublicOpinionString);
        tFtSjEntity.setSfgacz(publicSecurityDisposalString);
        tFtSjEntity.setSsjd(belongStreetString);
        tFtSjEntity.setSssq(belongCommunityString);

        //主要诉求
        tFtSjEntity.setZysq(mainAppealsString);
        // 事件概要
        tFtSjEntity.setSjgyqk(eventSummaryString);
        /*
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

        */
        tFtSjEntity.setLdps(leadershipInstructionsString);
        if (!"".equals(message)) {
            Toast.makeText(EventEntryAdd_Basic.this.getActivity(), message, Toast.LENGTH_SHORT).show();
            return null;
        } else {
            return tFtSjEntity;
        }
    }

    @OnClick({R.id.field_departmen_layout, R.id.fields_involved_linearlayout})
    public void onClick(View v) {
        int[] location = new int[2];
        // 获取控件在屏幕中的位置,方便展示Popupwindow
        fieldDepartmenLayout.getLocationOnScreen(location);
        switch (v.getId()) {
            // 到场部门
            case R.id.field_departmen_layout:
                spinnerType = "dcbm";
                break;
            case R.id.fields_involved_linearlayout:
                spinnerType = "sjly";
                break;
        }
        animation = null;
        // location[1] 改成 0
        animation = new TranslateAnimation(0, 0, -700, 0);
        animation.setDuration(500);
        List<CodeValue> codeValueList = descEntityDao.queryPidList(spinnerType);
        /** 一级菜单名称数组 **/
        GroupNameArray = new String[codeValueList.size()][];
        if (codeValueList != null && codeValueList.size() > 0) {
            for (int i = 0; i < codeValueList.size(); i++) {
                String[] cg = new String[2];
                // 0 是未选中,1 是选中
                cg[0] = "0";
                cg[1] = codeValueList.get(i).getValue();
                GroupNameArray[i] = cg;
            }
            CodeValue cv = codeValueList.get(0);
            /** 二级菜单名称数组 **/
            List<CodeValue> childCodeValueList = descEntityDao.queryValueListByPid(spinnerType, cv.getCode());
            childNameArray = new String[childCodeValueList.size()][];
            if (childCodeValueList != null && childCodeValueList.size() > 0) {
                for (int i = 0; i < childCodeValueList.size(); i++) {
                    String[] cg = new String[2];
                    // 0 是未选中,1 是选中
                    cg[0] = "0";
                    cg[1] = childCodeValueList.get(i).getValue();
                    childNameArray[i] = cg;
                }
            }
        }
        showPupupWindow();
    }

    /**
     * 初始化 PopupWindow
     *
     * @param view
     */
    public void initPopuWindow(View view) {
        /* 第一个参数弹出显示view 后两个是窗口大小 */
        mPopupWindow = new PopupWindow(view, screen_width, screen_height);
        /* 设置背景显示 */
        mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.mypop_bg));
        /* 设置触摸外面时消失 */
        // mPopupWindow.setOutsideTouchable(true);
        // 设置动画
        mPopupWindow.setAnimationStyle(R.style.pop_menu);

        mPopupWindow.setTouchable(true);
        /* 设置点击menu以外其他地方以及返回键退出 */
        mPopupWindow.setFocusable(true);
        mPopupWindow.update();
        /**
         * 1.解决再次点击MENU键无反应问题 2.sub_view是PopupWindow的子View
         */
        view.setFocusableInTouchMode(true);
    }

    /**
     * 展示区域选择的对话框
     */
    private void showPupupWindow() {
        showPupWindow = LayoutInflater.from(EventEntryAdd_Basic.this.getActivity()).inflate(
                R.layout.bottom_layout, null);
        initPopuWindow(showPupWindow);
        // 初始化三个ListView
        groupListView = showPupWindow.findViewById(R.id.listView1);
        childListView = showPupWindow.findViewById(R.id.listView2);
        childListView2 = showPupWindow.findViewById(R.id.listView3);

        // 初始化点击事件 - 取消
        btnCancel = showPupWindow.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
            }
        });

        // 初始化点击事件 - 确定
        btnConfirm = showPupWindow.findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 填充选择框
                fillListValues();
            }
        });

        // 标题
        btntext = showPupWindow.findViewById(R.id.btn_text);

        // 设置视图 一级菜单
        groupAdapter = new GroupAdapter(EventEntryAdd_Basic.this.getActivity(), GroupNameArray);
        groupListView.setAdapter(groupAdapter);
        groupAdapter.notifyDataSetChanged();
        groupListView.setOnItemClickListener(new MyItemClick());

        // 设置视图 二级菜单
        childAdapter = new GroupAdapter(EventEntryAdd_Basic.this.getActivity(), childNameArray);
        childListView.setAdapter(childAdapter);
        childAdapter.notifyDataSetChanged();
        childListView.setOnItemClickListener(new MyChildItemClick());

        //因为到场部门有3层结构，而涉及领域只有2层
        if ("dcbm".equals(spinnerType)) {
            btntext.setText("到场部门列表");
            childListView2.setVisibility(View.VISIBLE);
        } else if ("sjly".equals(spinnerType)) {
            btntext.setText("涉及领域列表");
            childListView2.setVisibility(View.GONE);
        }

        mPopupWindow.showAtLocation(linearEvententry, Gravity.CENTER, 0, 0);
        /*
        if ("sjly".equals(spinnerType)) {
            mPopupWindow.showAsDropDown(fieldsInvolvedLinearLayout, -5, 10);
        } else {
            mPopupWindow.showAsDropDown(fieldDepartmenLayout, -5, 10);
        }*/
    }

    /**
     * 填充列表框
     */
    private void fillListValues() {
        StringBuffer sb = new StringBuffer();
        // 菜单列表项
        List<GroupAdapter> adapterList = new ArrayList<GroupAdapter>();
        adapterList.add((GroupAdapter) groupListView.getAdapter());
        adapterList.add((GroupAdapter) childListView.getAdapter());
        if (childListView2.getAdapter() != null) {
            adapterList.add((GroupAdapter) childListView2.getAdapter());
        }

        // 判断是否有选中
        if (checkGroupAdapter(sb, adapterList)) {
            Toast.makeText(EventEntryAdd_Basic.this.getActivity(), "请选择任意一项", Toast.LENGTH_LONG).show();
            return;
        } else {
            if ("sjly".equals(spinnerType)) {
                // 涉及领域
                fieldsInvolvedEdt.setText(sb.deleteCharAt(sb.length() - 1).toString());
            } else if ("dcbm".equals(spinnerType)) {
                fieldDepartmenEdt.setText(sb.deleteCharAt(sb.length() - 1).toString());
            }
            mPopupWindow.dismiss();
        }
    }


    private boolean checkGroupAdapter(StringBuffer selectedStr, List<GroupAdapter> adapters) {
        boolean flag = true;
        if (adapters != null && adapters.size() > 0) {
            for (GroupAdapter adap : adapters) {
                String[][] mitems = adap.getmGroupItems();
                for (int i = 0; i < mitems.length; i++) {
                    if ("1".equals(mitems[i][0])) {
                        flag = false;
                        selectedStr.append(mitems[i][1]).append(",");
                    }
                }
            }
        }
        return flag;
    }

    class MyItemClick implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            groupAdapter.setSelectedPosition(position);
            String[] adCg = (String[]) groupAdapter.getItem(position);
            String pidCode = descEntityDao.queryCodeByName(spinnerType, adCg[1]);
            // 获取列表集合
            List<CodeValue> childCodeValueList = descEntityDao.queryValueListByPid(spinnerType, pidCode);
            childNameArray = new String[childCodeValueList.size()][];
            String[][] groupItems = childAdapter.getmGroupItems();
            boolean flag = true;
            if (childCodeValueList.size() > 0) {
                for (int i = 0; i < childCodeValueList.size(); i++) {
                    String[] cg = new String[2];
                    // 0 是未选中,1 是选中
                    cg[0] = "0";
                    cg[1] = childCodeValueList.get(i).getValue();
                    // 如果名称有一个一样就换值不刷新
                    if (groupItems != null && i < groupItems.length) {
                        if (cg[1].equals(groupItems[i][1])) {
                            flag = false;
                            break;
                        }

                    }
                    childNameArray[i] = cg;
                }
            } else {
                flag = true;
            }
            if (flag) {
                childAdapter.setChildData(childNameArray);
            }
            handler.sendEmptyMessage(20);
        }

    }


    class MyChildItemClick implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            childAdapter.setSelectedPosition(position);
            String[] adCg = (String[]) childAdapter.getItem(position);
            String pidCode = descEntityDao.queryCodeByName(spinnerType, adCg[1]);
            // 获取列表集合
            List<CodeValue> childCodeValueList = descEntityDao.queryValueListByPid(spinnerType, pidCode);
            child2NameArray = new String[childCodeValueList.size()][];

            boolean flag = true;
            if (childCodeValueList.size() > 0) {
                for (int i = 0; i < childCodeValueList.size(); i++) {
                    String[] cg = new String[2];
                    // 0 是未选中,1 是选中
                    cg[0] = "0";
                    cg[1] = childCodeValueList.get(i).getValue();
                    // 如果名称一样就换值不刷新
                    if (childTwoAdapter != null) {
                        String[][] groupItems = childTwoAdapter.getmGroupItems();
                        if (groupItems != null && i < groupItems.length) {
                            if (cg[1].equals(groupItems[i][1])) {
                                flag = false;
                                break;
                            }

                        }
                    }
                    child2NameArray[i] = cg;
                }
            } else {
                flag = true;
            }
            if (flag) {
                if (childTwoAdapter == null) {
                    childTwoAdapter = new GroupAdapter(EventEntryAdd_Basic.this.getActivity(), child2NameArray);
                }
                childTwoAdapter.setChildData(child2NameArray);
            }
            handler.sendEmptyMessage(22);
        }

    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 20:
                    // 刷新二级菜单列表
                    childAdapter.notifyDataSetChanged();
                    groupAdapter.notifyDataSetChanged();
                    break;
                case 22:
                    if (childTwoAdapter != null) {
                        childListView2.setAdapter(childTwoAdapter);
                        childTwoAdapter.notifyDataSetChanged();
                    }
                    childAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }

        }

        ;
    };

}

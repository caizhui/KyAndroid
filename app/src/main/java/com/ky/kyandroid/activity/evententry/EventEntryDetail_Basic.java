package com.ky.kyandroid.activity.evententry;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ky.kyandroid.R;
import com.ky.kyandroid.bean.CodeValue;
import com.ky.kyandroid.db.dao.DescEntityDao;
import com.ky.kyandroid.db.dao.TFtQhEntityDao;
import com.ky.kyandroid.entity.TFtQhEntity;
import com.ky.kyandroid.entity.TFtSjEntity;
import com.ky.kyandroid.util.SpUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTouch;

/**
 * Created by Caizhui on 2017-6-9.
 * 事件基本信息详情页面
 */

@SuppressLint("ValidFragment")
public class EventEntryDetail_Basic extends Fragment {

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
    EditText patternManifestationSpinner;

    /**
     * 现场态势
     */
    @BindView(R.id.field_morphology_spinner)
    EditText fieldMorpholoySpinner;
    /**
     * 规模
     */
    @BindView(R.id.scope_text_spinner)
    EditText scopeTextSpinner;
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
    EditText foreignRelatedSpinner;
    /**
     * 是否涉疆
     */
    @BindView(R.id.involved_xinjiang_spinner)
    EditText involvedXinjiangSpinner;
    /**
     * 是否涉舆情
     */
    @BindView(R.id.involve_public_opinion_spinner)
    EditText involvePublicOpinionSpinner;
    /**
     * 是否公安处置
     */
    @BindView(R.id.public_security_disposal_spinner)
    EditText publicSecurityDisposalSpinner;
    /**
     * 所属街道
     */
    @BindView(R.id.belong_street_edt)
    EditText belongStreetEdt;
    /**
     * 所属社区
     */
    @BindView(R.id.belong_community_spinner)
    EditText belongCommunitySpinner;
    /**
     * 主要诉求
     */
    @BindView(R.id.main_appeals_edt)
    TextView mainAppealsEdt;
    /**
     * 事件概要
     */
    @BindView(R.id.event_summary_edt)
    TextView eventSummaryEdt;
    /**
     * >领导批示
     */
    @BindView(R.id.leadership_instructions_edt)
    TextView leadershipInstructionsEdt;

    @BindView(R.id.parent_scroll)
    ScrollView parent_scroll;

    @BindView(R.id.one_scroll)
    ScrollView one_scroll;

    @BindView(R.id.twe_scroll)
    ScrollView twe_scroll;

    @BindView(R.id.three_scroll)
    ScrollView three_scroll;

    @BindView(R.id.four_scroll)
    ScrollView four_scroll;


    /**
     * 数组 配置器 下拉菜单赋值用
     */
    ArrayAdapter<CodeValue> adapter;
    @BindView(R.id.dj_btb)
    Button djBtb;
    @BindView(R.id.dj_img)
    ImageView djImg;
    @BindView(R.id.sl_btn)
    Button slBtn;
    @BindView(R.id.sl_img)
    ImageView slImg;
    @BindView(R.id.fl_btn)
    Button flBtn;
    @BindView(R.id.fl_img)
    ImageView flImg;
    @BindView(R.id.cl_img)
    ImageView clImd;
    @BindView(R.id.gd_btn)
    Button gdBtn;
    @BindView(R.id.hfhc_btn)
    Button hfhcBtn;
    @BindView(R.id.hfhc_img)
    ImageView hchfImg;
    @BindView(R.id.cl_btn)
    Button clBtn;

    private Intent intent;

    public TFtSjEntity tFtSjEntity;

    /**
     * 事件跟踪
     */
    public List<String> progressList;

    @SuppressLint("ValidFragment")
    public EventEntryDetail_Basic(Intent intent) {
        this.intent = intent;
    }


    private DescEntityDao descEntityDao;

    /**
     * SharedPreferences
     */
    private SharedPreferences sp;

    /**
     * 字典DAO
     */
    public TFtQhEntityDao tFtQhEntityDao;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.eventdetail_basic_fragment, container, false);
        ButterKnife.bind(this, view);
        sp = SpUtil.getSharePerference(getActivity());
        descEntityDao = new DescEntityDao();
        tFtQhEntityDao = new TFtQhEntityDao();
        // tFtSjEntity = (TFtSjEntity) intent.getSerializableExtra("tFtSjEntity");
        initData();
        return view;
    }

    /**
     * 新增页面跟查看详情是同一个页面，初始化页面基本信息
     */
    public void initData() {

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
                        dcbm += descEntityDao.queryName("dcbm", dcbms[i].trim()) + ",";
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
                        sjly += descEntityDao.queryName("sjly", sjlys[i].trim()) + ",";
                    }
                    sjly = sjly.substring(0, sjly.length() - 1);
                }
                //String sjlyName = descEntityDao.queryName("sjly", tFtSjEntity.getSjly().split(",")[0]);
                fieldsInvolvedEdt.setText(sjly);
            }

            mainAppealsEdt.setText(tFtSjEntity.getZysq());
            eventSummaryEdt.setText(tFtSjEntity.getSjgyqk());
            leadershipInstructionsEdt.setText(tFtSjEntity.getLdps());
            //以下为下拉控件设置默认值
            if (tFtSjEntity.getBxxs() != null && !"".equals(tFtSjEntity.getBxxs())) {
                String[] bxxss = tFtSjEntity.getBxxs().split(",");
                String bxxs = "";
                if (bxxss.length > 0) {
                    for (int i = 0; i < bxxss.length; i++) {
                        bxxs += descEntityDao.queryName("BXXS", bxxss[i]) + ",";
                    }
                    bxxs = bxxs.substring(0, bxxs.length() - 1);
                }
                patternManifestationSpinner.setText(bxxs);
            }
            if (tFtSjEntity.getXcts() != null) {
                fieldMorpholoySpinner.setText(descEntityDao.queryName("XCTS", tFtSjEntity.getXcts()));
            }
            if (tFtSjEntity.getGm() != null) {
                scopeTextSpinner.setText(descEntityDao.queryName("sjgm", tFtSjEntity.getGm()));
            }
            if (tFtSjEntity.getSfsw() != null) {
                foreignRelatedSpinner.setText(descEntityDao.queryName("sfsw", tFtSjEntity.getSfsw()));
            }
            if (tFtSjEntity.getSfsj() != null) {
                involvedXinjiangSpinner.setText(descEntityDao.queryName("sfsw", tFtSjEntity.getSfsj()));
            }
            if (tFtSjEntity.getSfsyq() != null) {
                involvePublicOpinionSpinner.setText(descEntityDao.queryName("sfsw", tFtSjEntity.getSfsyq()));
            }
            if (tFtSjEntity.getSfgacz() != null) {
                publicSecurityDisposalSpinner.setText(descEntityDao.queryName("sfsw", tFtSjEntity.getSfgacz()));
            }
            List<TFtQhEntity> qhList = tFtQhEntityDao.queryListById(tFtSjEntity.getSssq());
            if (qhList != null && qhList.size() > 0) {
                belongStreetEdt.setText(qhList.get(0).getJdmc());
                belongCommunitySpinner.setText(qhList.get(0).getSqgzz());
            }


        }

        // 初始化scroller事件
        parent_scroll.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                one_scroll.getParent().requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });
    }

    @OnTouch({R.id.one_scroll,R.id.twe_scroll,R.id.three_scroll,R.id.four_scroll})
    public boolean OnTouchListener(View v, MotionEvent event) {
        v.getParent().requestDisallowInterceptTouchEvent(true);
        return false;
    }


    public void settTftSjEntityEntity(TFtSjEntity tFtSjEntity) {

        if(progressList!=null && progressList.size()>0){
            if(progressList.size()==1){
                djBtb.setBackgroundResource(R.drawable.border_bccgbutton_bg);
            }else if (progressList.size()==2){
                djBtb.setBackgroundResource(R.drawable.border_bccgbutton_bg);
                djImg.setImageResource(R.mipmap.jtthree);
                slBtn.setBackgroundResource(R.drawable.border_bccgbutton_bg);
            }else if (progressList.size()==3){
                djBtb.setBackgroundResource(R.drawable.border_bccgbutton_bg);
                djImg.setImageResource(R.mipmap.jtthree);
                slBtn.setBackgroundResource(R.drawable.border_bccgbutton_bg);
                slImg.setImageResource(R.mipmap.jtthree);
                flBtn.setBackgroundResource(R.drawable.border_bccgbutton_bg);
            }else if (progressList.size()==4){
                djBtb.setBackgroundResource(R.drawable.border_bccgbutton_bg);
                djImg.setImageResource(R.mipmap.jtthree);
                slBtn.setBackgroundResource(R.drawable.border_bccgbutton_bg);
                slImg.setImageResource(R.mipmap.jtthree);
                flBtn.setBackgroundResource(R.drawable.border_bccgbutton_bg);
                flImg.setImageResource(R.mipmap.jtone);
                clBtn.setBackgroundResource(R.drawable.border_bccgbutton_bg);
            }else if (progressList.size()==5){
                djBtb.setBackgroundResource(R.drawable.border_bccgbutton_bg);
                djImg.setImageResource(R.mipmap.jtthree);
                slBtn.setBackgroundResource(R.drawable.border_bccgbutton_bg);
                slImg.setImageResource(R.mipmap.jtthree);
                flBtn.setBackgroundResource(R.drawable.border_bccgbutton_bg);
                flImg.setImageResource(R.mipmap.jtone);
                clBtn.setBackgroundResource(R.drawable.border_bccgbutton_bg);
                clImd.setImageResource(R.mipmap.jtfive);
                hfhcBtn.setBackgroundResource(R.drawable.border_bccgbutton_bg);
            }else if (progressList.size()==6){
                djBtb.setBackgroundResource(R.drawable.border_bccgbutton_bg);
                djImg.setImageResource(R.mipmap.jtone);
                slBtn.setBackgroundResource(R.drawable.border_bccgbutton_bg);
                slImg.setImageResource(R.mipmap.jtone);
                flBtn.setBackgroundResource(R.drawable.border_bccgbutton_bg);
                flImg.setImageResource(R.mipmap.jtone);
                clBtn.setBackgroundResource(R.drawable.border_bccgbutton_bg);
                clImd.setImageResource(R.mipmap.jtone);
                hfhcBtn.setBackgroundResource(R.drawable.border_bccgbutton_bg);
                hchfImg.setImageResource(R.mipmap.jtone);
                gdBtn.setBackgroundResource(R.drawable.border_bccgbutton_bg);
            }
        }
    }

}

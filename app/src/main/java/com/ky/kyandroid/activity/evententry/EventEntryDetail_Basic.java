package com.ky.kyandroid.activity.evententry;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.ky.kyandroid.R;
import com.ky.kyandroid.adapter.ChildAdapter;
import com.ky.kyandroid.adapter.GroupAdapter;
import com.ky.kyandroid.bean.CodeValue;
import com.ky.kyandroid.db.dao.DescEntityDao;
import com.ky.kyandroid.entity.TFtSjEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    public TFtSjEntity tFtSjEntity;

    View showPupWindow = null; // 选择区域的view

    /**
     * 一级菜单名称数组
     **/
    String[] GroupNameArray;
    /**
     * 二级菜单名称数组
     **/
    String[] childNameArray;

    ListView groupListView = null;
    ListView childListView = null;
    /**
     * 稻城部门的第二层子节点ListView
     */
    ListView childListView2 = null;
    GroupAdapter groupAdapter = null;

    /**
     * 到场部门第二层adapter
     */
    ChildAdapter childTwoAdapter = null;


    ChildAdapter childAdapter = null;

    TranslateAnimation animation;// 出现的动画效果
    // 屏幕的宽高
    public static int screen_width = 0;
    public static int screen_height = 0;

    private boolean[] tabStateArr = new boolean[4];// 标记tab的选中状态，方便设置

    PopupWindow mPopupWindow = null;

    @SuppressLint("ValidFragment")
    public EventEntryDetail_Basic(Intent intent) {
        this.intent = intent;
    }

    /**
     * 判断是那个树 dcbm(到场部门),sjly(涉及领域)
     */
    private String spinnerType;

    /**
     * 子列表是否已经点击
     */
    private boolean isChildClick;

    /**
     * 第二个子列表是否已经点击
     */
    private boolean isTwoChildClick;

    /**
     * 父列表是否已经点击
     */
    private boolean isGroupClick;


    /**
     * 父节点临时Position
     */
    private int oneTempPosition;

    /**
     * 第二层节点临时Position
     */
    private int twoTempPosition;

    /**
     * 第三层节点临时Position
     */
    private int threeTempPosition;

    private DescEntityDao descEntityDao;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.evententerdetail_basic_fragment, container, false);
        ButterKnife.bind(this, view);
        descEntityDao= new DescEntityDao();
        tFtSjEntity = (TFtSjEntity) intent.getSerializableExtra("tFtSjEntity");
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
                String []dcbms = tFtSjEntity.getDcbm().split(",");
                String dcbm = "";
                if(dcbms.length>0){
                    for(int i = 0 ;i<dcbms.length;i++){
                        dcbm += descEntityDao.queryName("dcbm", dcbms[i])+",";
                    }
                    dcbm=dcbm.substring(0,dcbm.length()-1);
                }
                fieldDepartmenEdt.setText(dcbm);
            }
            if (tFtSjEntity.getSjly() != null && !"".equals(tFtSjEntity.getSjly())) {
                String []sjlys = tFtSjEntity.getSjly().split(",");
                String sjly = "";
                if(sjlys.length>0){
                    for(int i = 0 ;i<sjlys.length;i++){
                        sjly += descEntityDao.queryName("sjly", sjlys[i])+",";
                    }
                    sjly=sjly.substring(0,sjly.length()-1);
                }
                //String sjlyName = descEntityDao.queryName("sjly", tFtSjEntity.getSjly().split(",")[0]);
                fieldsInvolvedEdt.setText(sjly);
            }

            belongStreetEdt.setText(tFtSjEntity.getSsjd());
            mainAppealsEdt.setText(tFtSjEntity.getZysq());
            eventSummaryEdt.setText(tFtSjEntity.getSjgyqk());
            leadershipInstructionsEdt.setText(tFtSjEntity.getLdps());
            //以下为下拉控件设置默认值
            if (tFtSjEntity.getBxxs() != null && !"".equals(tFtSjEntity.getBxxs())) {
                String []bxxss = tFtSjEntity.getBxxs().split(",");
                String bxxs = "";
                if(bxxss.length>0){
                    for(int i = 0 ;i<bxxss.length;i++){
                        bxxs += descEntityDao.queryName("BXXS", bxxss[i])+",";
                    }
                    bxxs=bxxs.substring(0,bxxs.length()-1);
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
            belongCommunitySpinner.setText("社区一");

        }
    }

}

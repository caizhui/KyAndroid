package com.ky.kyandroid.activity.task;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.ky.kyandroid.R;
import com.ky.kyandroid.adapter.EventPersonListAdapter;
import com.ky.kyandroid.bean.CodeValue;
import com.ky.kyandroid.db.dao.DescEntityDao;
import com.ky.kyandroid.db.dao.TFtSjRyEntityDao;
import com.ky.kyandroid.entity.TFtSjRyEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.Optional;

/**
 * Created by Caizhui on 2017-6-9.
 * 我的任务人员界面
 */

@SuppressLint("ValidFragment")
public class TaskFragment_Person extends Fragment {
    /**
     * 新增按钮
     */
    @BindView(R.id.add_person)
    Button addPerson;
    /**
     * 当事人List
     */
    @BindView(R.id.person_list)
    ListView personList;

    /**
     * 姓名
     */
    EditText personNameEdt;
    /**
     * 性别
     */
    Spinner personSexSpinner;
    /**
     * 民族
     */
    Spinner personNationSpinner;

    /**
     * 证件类型
     */
    Spinner personIdcardTypeSpinner;
    /**
     * 证件号码
     */
    EditText personIdcardEdt;
    /**
     * 户籍地
     */
    EditText personAddressEdt;
    /**
     * 党员
     */
    Spinner personPartySpinner;
    /**
     * 电子邮件
     */
    EditText personEmailEdt;
    /**
     * 固定电话
     */
    EditText personTelephoneEdt;
    /**
     * 移动电话
     */
    EditText personMobileEdt;

    /**
     * 车牌号码
     */
    EditText personCphEdt;
    /**
     * 工作单位
     */
    EditText personJobaddressEdt;
    /**
     * 现住地址
     */
    EditText personDomilcileEdt;
    /**
     * 备注
     */
    EditText personRemarkEdt;

    TFtSjRyEntity tFtSjRyEntity;

    public List<TFtSjRyEntity> sjryList;

    EventPersonListAdapter adapter;

    /**
     * 是否查看本地详细信息
     */
    private boolean flag = false;

    public String uuid;
    /**
     * 0表示可以新增，1表示可以修改或者查看详情
     */
    public  String type;

    public Intent intent;

    /**
     * 设置Spinner控件的初始值
     */
    public List<CodeValue> spinnerList;

    /**
     * 数组 配置器 下拉菜单赋值用
     */
    ArrayAdapter<CodeValue> arrayAdapter;

    /**
     * 是否查看详情
     */
    private boolean isDetail =false;

    private TFtSjRyEntityDao tFtSjRyEntityDao;
    public DescEntityDao descEntityDao;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.evententeradd_person_fragment, container, false);
        ButterKnife.bind(this, view);
        descEntityDao = new DescEntityDao();
        tFtSjRyEntityDao = new TFtSjRyEntityDao();
        addPerson.setVisibility(View.GONE);
        //tFtSjRyEntityList = new ArrayList<TFtSjRyEntity>();
        //判断是否查看本地详细信息，如果是true就执行下面方法
        if(sjryList==null){
            sjryList  = new ArrayList<TFtSjRyEntity>();
        }
        return view;
    }

    /**
     * 点击列表查看详情
     */
    @OnItemClick(R.id.person_list)
    public  void OnItemClick(int position){
        tFtSjRyEntity = (TFtSjRyEntity) adapter.getItem(position);
        isDetail = true;
        addPersonInfo();
    }



    @OnClick({R.id.add_person})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_person:
                addPersonInfo();
                break;
        }
    }

    /**
     * 新增当事人基本信息
     */
    @Optional
    public void addPersonInfo() {
        View dialogView = LayoutInflater.from(TaskFragment_Person.this.getActivity()).inflate(R.layout.dialog_person_detail, null);
        personNameEdt = ButterKnife.findById(dialogView, R.id.person_name_edt);
        personSexSpinner = ButterKnife.findById(dialogView, R.id.person_sex_edt);
        personNationSpinner = ButterKnife.findById(dialogView, R.id.person_nation_edt);
        personIdcardEdt = ButterKnife.findById(dialogView, R.id.person_idcard_edt);
        personIdcardTypeSpinner= ButterKnife.findById(dialogView, R.id.person_idcard_type_edt);
        personAddressEdt = ButterKnife.findById(dialogView, R.id.person_address_edt);
        personPartySpinner = ButterKnife.findById(dialogView, R.id.person_party_edt);
        personEmailEdt = ButterKnife.findById(dialogView, R.id.person_email_edt);
        personTelephoneEdt = ButterKnife.findById(dialogView, R.id.person_telephone_edt);
        personJobaddressEdt = ButterKnife.findById(dialogView, R.id.person_jobaddress_edt);
        personMobileEdt = ButterKnife.findById(dialogView, R.id.person_mobile_edt);
        personCphEdt = ButterKnife.findById(dialogView, R.id.person_cph_edt);
        personDomilcileEdt = ButterKnife.findById(dialogView, R.id.person_domilcile_edt);
        personRemarkEdt = ButterKnife.findById(dialogView, R.id.person_remark_edt);

        spinnerList = descEntityDao.queryListForCV("sex");
        if (spinnerList == null) {
            //设置Spinner控件的初始值
            spinnerList = new ArrayList<CodeValue>();
        }
        //将可选内容与ArrayAdapter连接起来
        arrayAdapter = new ArrayAdapter<CodeValue>(TaskFragment_Person.this.getActivity(), android.R.layout.simple_spinner_item, spinnerList);
        //设置下拉列表的风格
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        personSexSpinner.setAdapter(arrayAdapter);//将adapter 添加到男女spinner中

        spinnerList = descEntityDao.queryListForCV("crd");
        if (spinnerList == null) {
            //设置Spinner控件的初始值
            spinnerList = new ArrayList<CodeValue>();
        }
        //将可选内容与ArrayAdapter连接起来
        arrayAdapter = new ArrayAdapter<CodeValue>(TaskFragment_Person.this.getActivity(), android.R.layout.simple_spinner_item, spinnerList);
        //设置下拉列表的风格
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        personIdcardTypeSpinner.setAdapter(arrayAdapter);//将adapter 添加到证件类型spinner中


        spinnerList = descEntityDao.queryListForCV("dy");
        if (spinnerList == null) {
            //设置Spinner控件的初始值
            spinnerList = new ArrayList<CodeValue>();
        }
        //将可选内容与ArrayAdapter连接起来
        arrayAdapter = new ArrayAdapter<CodeValue>(TaskFragment_Person.this.getActivity(), android.R.layout.simple_spinner_item, spinnerList);
        //设置下拉列表的风格
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        personPartySpinner.setAdapter(arrayAdapter);//将adapter 添加到党员spinner中

        spinnerList = descEntityDao.queryListForCV("nation");
        if (spinnerList == null) {
            //设置Spinner控件的初始值
            spinnerList = new ArrayList<CodeValue>();
        }
        //将可选内容与ArrayAdapter连接起来
        arrayAdapter = new ArrayAdapter<CodeValue>(TaskFragment_Person.this.getActivity(), android.R.layout.simple_spinner_item, spinnerList);
        //设置下拉列表的风格
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        personNationSpinner.setAdapter(arrayAdapter);//将adapter 添加到spinner中
        if(isDetail){
            personNameEdt.setText(tFtSjRyEntity.getXm());
            personSexSpinner.setSelection(Integer.parseInt(tFtSjRyEntity.getXb())-1);
            if("56".equals(tFtSjRyEntity.getMz())){
                personNationSpinner.setSelection(0);
            }else{
                personNationSpinner.setSelection(Integer.parseInt(tFtSjRyEntity.getMz()));
            }
            personIdcardTypeSpinner.setSelection(Integer.parseInt(tFtSjRyEntity.getZjlx())-1);
            personIdcardEdt.setText(tFtSjRyEntity.getZjhm());
            personAddressEdt.setText(tFtSjRyEntity.getHjd());
            personJobaddressEdt.setText(tFtSjRyEntity.getGzdw());
            personPartySpinner.setSelection(Integer.parseInt(tFtSjRyEntity.getSfdy()));
            personEmailEdt.setText(tFtSjRyEntity.getEmail());
            personTelephoneEdt.setText(tFtSjRyEntity.getGddh());
            personMobileEdt.setText(tFtSjRyEntity.getYddh());
            personCphEdt.setText(tFtSjRyEntity.getCphm());
            personDomilcileEdt.setText(tFtSjRyEntity.getXzdz());
            personRemarkEdt.setText(tFtSjRyEntity.getComments());
            //显示详情之后将isDetail变成false
            isDetail = false;
        }
        personNameEdt.setEnabled(false);
        personSexSpinner.setEnabled(false);
        personNationSpinner.setEnabled(false);
        personIdcardTypeSpinner.setEnabled(false);
        personIdcardEdt.setEnabled(false);
        personAddressEdt.setEnabled(false);
        personPartySpinner.setEnabled(false);
        personEmailEdt.setEnabled(false);
        personJobaddressEdt.setEnabled(false);
        personCphEdt.setEnabled(false);
        personTelephoneEdt.setEnabled(false);
        personMobileEdt.setEnabled(false);
        personDomilcileEdt.setEnabled(false);
        personRemarkEdt.setEnabled(false);
        final AlertDialog.Builder builder = new AlertDialog.Builder(TaskFragment_Person.this.getActivity());
        builder.setTitle("当事人基本信息");
        builder.setView(dialogView);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.create().show();
    }

    /**
     * 当查看已经上报事件详情时初始化数据
     */
    public void setTFtSjRyEntityList(List<TFtSjRyEntity> sjryList) {
        this.sjryList = sjryList;
        if (sjryList != null && sjryList.size() > 0) {
            adapter = new EventPersonListAdapter(sjryList,descEntityDao,tFtSjRyEntityDao,TaskFragment_Person.this.getActivity(),"1".equals(type) ? true : false);
            if (personList != null) {
                personList.setAdapter(adapter);
            }

        }
    }

}

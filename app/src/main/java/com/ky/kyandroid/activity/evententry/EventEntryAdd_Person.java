package com.ky.kyandroid.activity.evententry;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.ky.kyandroid.R;
import com.ky.kyandroid.adapter.EventPersonListAdapter;
import com.ky.kyandroid.entity.TFtSjRyEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by Caizhui on 2017-6-9.
 * 事件录入人员界面
 */

public class EventEntryAdd_Person extends Fragment {
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
    EditText personSexEdt;
    /**
     * 民族
     */
    EditText personNationEdt;
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
    EditText personPartyEdt;
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

    private List<TFtSjRyEntity> tFtSjRyEntityList;

    public List<TFtSjRyEntity> sjryList;

    EventPersonListAdapter adapter;

    /**
     * 是否查看本地详细信息
     */
    private boolean flag = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.evententeradd_person_fragment, container, false);
        ButterKnife.bind(this, view);
        tFtSjRyEntityList = new ArrayList<TFtSjRyEntity>();
        //判断是否查看本地详细信息，如果是true就执行下面方法
        if (flag) {
            if (sjryList != null && sjryList.size() > 0) {
                adapter = new EventPersonListAdapter(sjryList, EventEntryAdd_Person.this.getActivity());
                if (personList != null) {
                    personList.setAdapter(adapter);
                }

            }
        }
        return view;
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
        View view = LayoutInflater.from(EventEntryAdd_Person.this.getActivity()).inflate(R.layout.dialog_person_add, null);
        personNameEdt = ButterKnife.findById(view, R.id.person_name_edt);
        personSexEdt = ButterKnife.findById(view, R.id.person_sex_edt);
        personNationEdt = ButterKnife.findById(view, R.id.person_nation_edt);
        personIdcardEdt = ButterKnife.findById(view, R.id.person_idcard_edt);
        personAddressEdt = ButterKnife.findById(view, R.id.person_address_edt);
        personPartyEdt = ButterKnife.findById(view, R.id.person_party_edt);
        personEmailEdt = ButterKnife.findById(view, R.id.person_email_edt);
        personTelephoneEdt = ButterKnife.findById(view, R.id.person_telephone_edt);
        personMobileEdt = ButterKnife.findById(view, R.id.person_mobile_edt);
        personDomilcileEdt = ButterKnife.findById(view, R.id.person_domilcile_edt);
        personRemarkEdt = ButterKnife.findById(view, R.id.person_remark_edt);
        AlertDialog.Builder builder = new AlertDialog.Builder(EventEntryAdd_Person.this.getActivity());
        builder.setTitle("当事人基本信息");
        builder.setView(view);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                tFtSjRyEntity = new TFtSjRyEntity();
                tFtSjRyEntity.setXm(personNameEdt.getText().toString());
                tFtSjRyEntity.setXb(personSexEdt.getText().toString());
                tFtSjRyEntity.setMz(personNationEdt.getText().toString());
                tFtSjRyEntity.setZjhm(personIdcardEdt.getText().toString());
                tFtSjRyEntity.setHjd(personAddressEdt.getText().toString());
                tFtSjRyEntity.setSfdy(personPartyEdt.getText().toString());
                tFtSjRyEntity.setEmail(personEmailEdt.getText().toString());
                tFtSjRyEntity.setGddh(personTelephoneEdt.getText().toString());
                tFtSjRyEntity.setYddh(personMobileEdt.getText().toString());
                tFtSjRyEntity.setXzdz(personDomilcileEdt.getText().toString());
                tFtSjRyEntity.setComments(personRemarkEdt.getText().toString());
                tFtSjRyEntityList.add(tFtSjRyEntity);
                if (tFtSjRyEntityList != null && tFtSjRyEntityList.size() > 0) {
                    adapter = new EventPersonListAdapter(tFtSjRyEntityList, EventEntryAdd_Person.this.getActivity());
                    personList.setAdapter(adapter);
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.create().show();
    }

    public List<TFtSjRyEntity> tFtSjRyEntityList() {
        return tFtSjRyEntityList;
    }

    /**
     * 当查看已经上报事件详情时初始化数据
     */
    public void setTFtSjRyEntityList(List<TFtSjRyEntity> sjryList) {
        this.sjryList = sjryList;
        if (sjryList != null && sjryList.size() > 0) {
            adapter = new EventPersonListAdapter(sjryList, EventEntryAdd_Person.this.getActivity());
            if (personList != null) {
                personList.setAdapter(adapter);
            }

        }
    }

    /**
     * 当查看本地详情时初始化数据
     */
    public void setTFtSjRyEntityList(List<TFtSjRyEntity> sjryList, boolean flag) {
        this.sjryList = sjryList;
        this.flag = flag;
    }

}

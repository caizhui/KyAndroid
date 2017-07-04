package com.ky.kyandroid.activity.evententry;

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
import android.widget.Toast;

import com.ky.kyandroid.R;
import com.ky.kyandroid.adapter.EventPersonListAdapter;
import com.ky.kyandroid.bean.CodeValue;
import com.ky.kyandroid.db.dao.DescEntityDao;
import com.ky.kyandroid.db.dao.TFtSjRyEntityDao;
import com.ky.kyandroid.entity.TFtSjEntity;
import com.ky.kyandroid.entity.TFtSjRyEntity;
import com.ky.kyandroid.util.CommonUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;
import butterknife.Optional;

/**
 * Created by Caizhui on 2017-6-9.
 * 事件录入人员界面
 */

@SuppressLint("ValidFragment")
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
     * 事件实体
     */
    private TFtSjEntity tFtSjEntity;

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

    @SuppressLint("ValidFragment")
    public EventEntryAdd_Person(Intent intent,String uuid){
        this.intent = intent;
        this.uuid = uuid;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.evententeradd_person_fragment, container, false);
        ButterKnife.bind(this, view);
        tFtSjRyEntityDao = new TFtSjRyEntityDao();
        descEntityDao = new DescEntityDao();
        type = intent.getStringExtra("type");
        tFtSjEntity = (TFtSjEntity) intent.getSerializableExtra("tFtSjEntity");
        if(tFtSjEntity!=null){
            uuid = tFtSjEntity.getId();
        }
        //当type为1时，表示为修改或者查看详情，但是只有事件状态为0草稿或者3街道退回才能修改，其他为查看详情
        if("1".equals(type)&&(!("0".equals(tFtSjEntity.getZt()))&&!("3".equals(tFtSjEntity.getZt())))){
            addPerson.setVisibility(View.GONE);
        }
        //tFtSjRyEntityList = new ArrayList<TFtSjRyEntity>();
        //判断是否查看本地详细信息，如果是true就执行下面方法
        if (flag) {
            if (sjryList != null && sjryList.size() > 0) {
                adapter = new EventPersonListAdapter(sjryList, EventEntryAdd_Person.this.getActivity());
                if (personList != null) {
                    personList.setAdapter(adapter);
                }

            }
        }
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

    @OnItemLongClick(R.id.person_list)
    public boolean OnItemLongClick(int position){
        final TFtSjRyEntity tFtSjRyEntity = (TFtSjRyEntity) adapter.getItem(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(EventEntryAdd_Person.this.getActivity());
        builder.setTitle("信息");
        builder.setMessage("确定要删除该条记录吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                boolean flag = tFtSjRyEntityDao.deleteEventEntry(tFtSjRyEntity.getUuid());
                if(flag){
                    Toast.makeText(EventEntryAdd_Person.this.getActivity(),"删除成功",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(EventEntryAdd_Person.this.getActivity(),"删除失败",Toast.LENGTH_SHORT).show();
                }
                sjryList = tFtSjRyEntityDao.queryListBySjId(tFtSjRyEntity.getSjId());
                if(sjryList!=null){
                    adapter.notifyDataSetChanged(sjryList);
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.create().show();
      return false;
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
        View dialogView = LayoutInflater.from(EventEntryAdd_Person.this.getActivity()).inflate(R.layout.dialog_person_add, null);
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
        arrayAdapter = new ArrayAdapter<CodeValue>(EventEntryAdd_Person.this.getActivity(), android.R.layout.simple_spinner_item, spinnerList);
        //设置下拉列表的风格
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        personSexSpinner.setAdapter(arrayAdapter);//将adapter 添加到男女spinner中

        spinnerList = descEntityDao.queryListForCV("crd");
        if (spinnerList == null) {
            //设置Spinner控件的初始值
            spinnerList = new ArrayList<CodeValue>();
        }
        //将可选内容与ArrayAdapter连接起来
        arrayAdapter = new ArrayAdapter<CodeValue>(EventEntryAdd_Person.this.getActivity(), android.R.layout.simple_spinner_item, spinnerList);
        //设置下拉列表的风格
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        personIdcardTypeSpinner.setAdapter(arrayAdapter);//将adapter 添加到证件类型spinner中


        spinnerList = descEntityDao.queryListForCV("dy");
        if (spinnerList == null) {
            //设置Spinner控件的初始值
            spinnerList = new ArrayList<CodeValue>();
        }
        //将可选内容与ArrayAdapter连接起来
        arrayAdapter = new ArrayAdapter<CodeValue>(EventEntryAdd_Person.this.getActivity(), android.R.layout.simple_spinner_item, spinnerList);
        //设置下拉列表的风格
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        personPartySpinner.setAdapter(arrayAdapter);//将adapter 添加到党员spinner中

        spinnerList = descEntityDao.queryListForCV("nation");
        if (spinnerList == null) {
            //设置Spinner控件的初始值
            spinnerList = new ArrayList<CodeValue>();
        }
        //将可选内容与ArrayAdapter连接起来
        arrayAdapter = new ArrayAdapter<CodeValue>(EventEntryAdd_Person.this.getActivity(), android.R.layout.simple_spinner_item, spinnerList);
        //设置下拉列表的风格
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        personNationSpinner.setAdapter(arrayAdapter);//将adapter 添加到spinner中
        if(isDetail){
            personNameEdt.setText(tFtSjRyEntity.getXm());
            personSexSpinner.setSelection(Integer.parseInt(tFtSjRyEntity.getXb())-1);
            if("56".equals(tFtSjRyEntity.getMz())){
                personNationSpinner.setSelection(0);
            }else{
                personNationSpinner.setSelection(Integer.parseInt(tFtSjRyEntity.getMz())-1);
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
        //当type为1时，表示为修改或者查看详情，但是只有事件状态为1才能修改，其他为查看详情
        if("1".equals(type)&&!("0".equals(tFtSjEntity.getZt()))){
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
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(EventEntryAdd_Person.this.getActivity());
        builder.setTitle("当事人基本信息");
        builder.setView(dialogView);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String message="";
                if(tFtSjRyEntity==null){
                    tFtSjRyEntity = new TFtSjRyEntity();
                }
                if("".equals(personNameEdt.getText().toString())){
                    message += "姓名不能为空\n";
                }else{
                    tFtSjRyEntity.setXm(personNameEdt.getText().toString());
                }
                tFtSjRyEntity.setXb(descEntityDao.queryCodeByName("sex", personSexSpinner.getSelectedItem().toString()));
                tFtSjRyEntity.setMz(descEntityDao.queryCodeByName("nation", personNationSpinner.getSelectedItem().toString()));
                tFtSjRyEntity.setZjlx(descEntityDao.queryCodeByName("crd", personIdcardTypeSpinner.getSelectedItem().toString()));
                if("".equals(personIdcardEdt.getText().toString())){
                    message += "证件号码不能为空\n";
                }else if(!"".equals(personIdcardEdt.getText().toString())&& !CommonUtil.isIDNumber(personIdcardEdt.getText().toString())){
                    message += "请输入正确的证件号码\n";
                }else{
                    tFtSjRyEntity.setZjhm(personIdcardEdt.getText().toString());
                }
                tFtSjRyEntity.setHjd(personAddressEdt.getText().toString());
                tFtSjRyEntity.setGzdw(personJobaddressEdt.getText().toString());
                tFtSjRyEntity.setSfdy(descEntityDao.queryCodeByName("dy", personPartySpinner.getSelectedItem().toString()));
               /* if(!"".equals(personEmailEdt.getText().toString())&& !CommonUtil.isEmail(personEmailEdt.getText().toString())){
                    message += "请输入正确的邮箱\n";
                }else{*/
                    tFtSjRyEntity.setEmail(personEmailEdt.getText().toString());
                //}

                if(!"".equals(personTelephoneEdt.getText().toString())&& !CommonUtil.isFixedPhone(personTelephoneEdt.getText().toString())){
                    message += "请输入正确的固定电话\n";
                }else{
                    tFtSjRyEntity.setGddh(personTelephoneEdt.getText().toString());

                }
                tFtSjRyEntity.setXzdz(personDomilcileEdt.getText().toString());
                if(!"".equals(personMobileEdt.getText().toString())&& !CommonUtil.isMobileNO(personMobileEdt.getText().toString())){
                    message += "请输入正确的移动电话\n";
                }else{
                    tFtSjRyEntity.setYddh(personMobileEdt.getText().toString());

                }
                tFtSjRyEntity.setCphm(personCphEdt.getText().toString());
                tFtSjRyEntity.setComments(personRemarkEdt.getText().toString());
                tFtSjRyEntity.setSjId(uuid);
                if(!"".equals(message)){
                    try{
                        Field field = dialogInterface.getClass()
                                .getSuperclass().getDeclaredField(
                                        "mShowing" );
                        field.setAccessible( true );
                        // 将mShowing变量设为false，表示对话框已关闭
                        field.set(dialogInterface, false );
                        dialogInterface.dismiss();
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    Toast.makeText(EventEntryAdd_Person.this.getActivity(),message,Toast.LENGTH_SHORT).show();
                }else{
                    if(tFtSjRyEntity.getUuid()!=0){
                        flag = tFtSjRyEntityDao.updateTFtSjRyEntity(tFtSjRyEntity);
                        tFtSjRyEntity = null;
                        message="修改";
                    }else{
                        flag = tFtSjRyEntityDao.saveTFtSjRyEntity(tFtSjRyEntity);
                        tFtSjRyEntity = null;
                        message="新增";
                    }
                    if(flag){
                        sjryList  = tFtSjRyEntityDao.queryListBySjId(uuid);
                        if (sjryList != null && sjryList.size() > 0) {
                            adapter = new EventPersonListAdapter(sjryList, EventEntryAdd_Person.this.getActivity());
                            personList.setAdapter(adapter);
                        }
                        Toast.makeText(EventEntryAdd_Person.this.getActivity(),message+"成功",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(EventEntryAdd_Person.this.getActivity(),message+"失败",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                tFtSjRyEntity = null;
            }
        });
        builder.create().show();
    }


    public List<TFtSjRyEntity> tFtSjRyEntityList() {
        return sjryList;
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

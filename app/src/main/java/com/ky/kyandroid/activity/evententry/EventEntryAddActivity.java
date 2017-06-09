package com.ky.kyandroid.activity.evententry;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ky.kyandroid.R;
import com.ky.kyandroid.adapter.FragmentAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by Caizhui on 2017-6-9.
 * 事件录入新增页面
 */

public class EventEntryAddActivity extends FragmentActivity {



    /** TAG */
    private static final String TAG = "RentingHouseAddActivity";

    /** 显示的fragment_viewpager */
    private ViewPager fragment_viewpager;

    @BindView(R.id.radiogroup)
    private RadioGroup radiogroup;

    /**
     * 基本信息按钮
     */
    @BindView(R.id.radiobtn_baseinfo)
    private RadioButton radiobtn_baseinfo;

    /**
     * 当事人按钮
     */
    @BindView(R.id.radiobtn_person)
    private RadioButton  radiobtn_person;

    /**
     * 附件按钮
     */
    @BindView(R.id.radiobtn_attachment)
    private RadioButton  radiobtn_attachment;

    /** 事件录入 - 基本信息 */
    private EventEntryAdd_Basic eventEntryAdd_basic;

    /** 事件录入- 当事人 */
    private EventEntryAdd_Person eventEntryAdd_person;

    /** 事件录入 - 附件 */
    private EventEntryAdd_Attachment eventEntryAdd_attachment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evententryadd);
        initToolbar();
        initPageView();
        initViewData();
    }

    /**
     * 初始化PageView
     */
    @SuppressWarnings("deprecation")
    private void initPageView(){
        fragment_viewpager = (ViewPager) findViewById(R.id.fragment_viewpager);
        eventEntryAdd_basic = new EventEntryAdd_Basic();
        eventEntryAdd_person = new EventEntryAdd_Person();
        eventEntryAdd_attachment = new EventEntryAdd_Attachment();
        // 设置Fragment集合
        List<Fragment> fragmList = new ArrayList<Fragment>();
        fragmList.add(eventEntryAdd_basic);
        fragmList.add(eventEntryAdd_person);
        fragmList.add(eventEntryAdd_attachment);

        // 适配器adapter
        FragmentAdapter fragmentAdapter = new FragmentAdapter(
                getSupportFragmentManager(), fragmList);
        // 添加适配器与监听
        fragment_viewpager.setAdapter(fragmentAdapter);
        fragment_viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int index) {
                changeState2RadioButton(index);
            }

            @Override
            public void onPageScrolled(int position, float offset, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    /**
     * @param index
     */
    private void changeState2RadioButton(int index){
        switch (index) {
            case 0:
                radiobtn_baseinfo.setChecked(true);
                break;
            case 1:
                radiobtn_person.setChecked(true);
                break;
            case 2:
                radiobtn_attachment.setChecked(true);
                break;
        }
    }

    /**
     * 初始化标题栏
     */
    private void initToolbar() {
        /** toolbar控件容器 **/
        View toolbar_layout = findViewById(R.id.toolbar_layout);
        /** 返回键 **/
        ImageView toolbar_back = (ImageView) findViewById(R.id.toolbar_back);
        /** 标题 **/
        TextView toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        /** 右侧按钮，根据需要设置是否需要点击事件 **/
        View toolbar_right_btn = findViewById(R.id.toolbar_right_btn);
        /** 右侧为图片时添加 **/
        ImageView toolbar_right_img = (ImageView) findViewById(R.id.toolbar_right_img);
        /** 右侧为文字时添加 **/
        TextView toolbar_right_tv = (TextView) findViewById(R.id.toolbar_right_tv);

        /** 设置toolbar背景颜色 **/
       // toolbar_layout.setBackgroundResource(color.toolbar_green);
        /** 设置toolbar标题 **/
        toolbar_title.setText("租屋套信息");

        /** 添加点击事件 **/
        //toolbar_back.setOnClickListener(this);
        //toolbar_right_btn.setOnClickListener(this);
    }

    /**
     * 初始化控件数据
     */
    private void initViewData() {

        // 菜单分组栏
        radiobtn_baseinfo.setChecked(true);

        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radiobtn_baseinfo:
                        Log.i(TAG, "切换到基本信息");
                        fragment_viewpager.setCurrentItem(0);
                        break;
                    case R.id.radiobtn_person:
                        Log.i(TAG, "切换到当事人");
                        fragment_viewpager.setCurrentItem(1);
                        break;
                    case R.id.radiobtn_attachment:
                        Log.i(TAG, "切换到附件");
                        fragment_viewpager.setCurrentItem(2);
                        break;
                    default:
                        break;
                }
            }
        });
    }


    @OnClick({R.id.toolbar_back,R.id.toolbar_right_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            /** 返回键 **/
            case R.id.toolbar_back:
                onBackPressed();
                break;
            /** Toolbar右侧按钮 **/
            case R.id.toolbar_right_btn:
                Log.i(TAG, "Toolbar右侧按钮");
                break;
        }
    }

}

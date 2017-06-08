package com.ky.kyandroid.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.ky.kyandroid.R;
import com.ky.kyandroid.adapter.ViewPageAdapter;
import com.ky.kyandroid.fragment.onefragment.OneFragment;
import com.ky.kyandroid.fragment.onefragment.TwoFragment;
import com.ky.kyandroid.view.MyViewPager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Caizhui on 2017-6-8.
 */

public class MainFragment extends FragmentActivity {


    private MyViewPager mViewPager;
    private ArrayList<Fragment> mFragments=null;

    /**
     * 测试Demo
     */
    private OneFragment oneFragment;

    /**
     * 测试Demo
     */
    private TwoFragment twoFragment;

    private ViewPageAdapter mMainPagerAdapter;


    private int mSelectIndex = -1;//当前选择的页面的序号

    /**
     * 第一个滚动界面
     */
    @BindView(R.id.img_one)
    public ImageView img_one;
    /**
     * 第二个滚动界面
     */
    @BindView(R.id.img_two)
    public ImageView img_two;
    /**
     * 第三个滚动界面
     */
    @BindView(R.id.img_three)
    public ImageView img_three;
    /**
     * 第四个滚动界面
     */
    @BindView(R.id.img_four)
    public ImageView img_four;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main_activity);
        ButterKnife.bind(MainFragment.this);
        initView();
        initData();
    }

    private void initView() {
        mViewPager = (MyViewPager) findViewById(R.id.vp_main);
        //控制ViewPager是否可滑动的开关
        mViewPager.setScrollble(true);
    }
    @SuppressWarnings("deprecation")
    private void initData() {

        oneFragment = new   OneFragment();
        twoFragment = new  TwoFragment();
        mFragments = new ArrayList<Fragment>();

        mFragments.add(oneFragment);
        mFragments.add(twoFragment);
        mFragments.add(oneFragment);
        mFragments.add(twoFragment);

        mViewPager.setOffscreenPageLimit(mFragments.size());

        mMainPagerAdapter = new ViewPageAdapter(getSupportFragmentManager());
        mMainPagerAdapter.setFragments(mFragments);
        mViewPager.setAdapter(mMainPagerAdapter);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                switchTab(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

        switchTab(0);

    }

    @OnClick({R.id.img_one, R.id.img_two, R.id.img_three,R.id.img_four} )
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.img_one:
                switchTab(0);
                break;
            case R.id.img_two:
                switchTab(1);
                break;
            case R.id.img_three:
                switchTab(2);
                break;
            case R.id.img_four:
                switchTab(3);
                break;
            default:
                break;
        }
    }

    /**
     * @Title: switchTab
     * @Description: 切换tab
     * @param index
     * @return void
     */
    public void switchTab(int index) {
        if (mSelectIndex == index || index < 0 || index > 4)
            return;
        mSelectIndex = index;
        img_one.setSelected(index==0?true:false);
        img_two.setSelected(index==1?true:false);
        img_three.setSelected(index==2?true:false);
        img_four.setSelected(index==3?true:false);
        mViewPager.setCurrentItem(index, false);
        mMainPagerAdapter.notifyDataSetChanged();

    }
}

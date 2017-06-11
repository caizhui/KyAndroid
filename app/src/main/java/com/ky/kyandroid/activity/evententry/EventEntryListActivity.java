package com.ky.kyandroid.activity.evententry;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ky.kyandroid.R;
import com.ky.kyandroid.adapter.EventEntryListAdapter;
import com.ky.kyandroid.entity.EventEntryEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Caizhui on 2017-6-9.
 * 事件列表页面
 */

public class EventEntryListActivity extends AppCompatActivity {

    /**
     * 标题左边按钮
     */
    @BindView(R.id.left_btn)
    ImageView leftBtn;

    /**
     * 标题中间文字
     */
    @BindView(R.id.center_text)
    TextView centerText;

    /**
     * 标题右边按钮
     */
    @BindView(R.id.right_btn)
    Button rightBtn;

    /**
     *  List列表
     */
    @BindView(R.id.search_evententry_list)
    ListView searchEvententryList;

    /**
     *  刷新控件
     */
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeContainer;

    /**
     * 事件列表
     */
    private List<EventEntryEntity> entryEntityList;


    private EventEntryListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evententry_list);
        ButterKnife.bind(this);
        initData();
    }

    /**
     * 点击标题栏右边按钮
     */
    @OnClick(R.id.right_btn)
    public void onClickRightBtn(){
        Intent intent = new Intent(this, EventEntryAddActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * List列表设置初始化数据
     */
    public  void initData(){
        entryEntityList = new ArrayList<EventEntryEntity>();
        for (int i=0;i<10;i++){
            entryEntityList.add(new EventEntryEntity("事件测试"+i,"广州天河","2017-06-11","2321","1212","121"));
        }
        adapter = new EventEntryListAdapter(entryEntityList,EventEntryListActivity.this);
        searchEvententryList.setAdapter(adapter);
    }
}

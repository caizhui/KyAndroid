package com.ky.kyandroid.activity.evententry;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ky.kyandroid.R;
import com.ky.kyandroid.adapter.EventEntryListAdapter;
import com.ky.kyandroid.db.dao.EventEntryDao;
import com.ky.kyandroid.entity.EventEntryEntity;

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

    private EventEntryDao eventEntryDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evententry_list);
        ButterKnife.bind(this);
        eventEntryDao = new EventEntryDao();
        initData();
    }

    @OnClick({R.id.left_btn,R.id.right_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            /** 返回键 **/
            case R.id.left_btn:
                //onBackPressed();
                finish();
                break;
            case R.id.right_btn:
                Intent intent = new Intent(this, EventEntryAddActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * List列表设置初始化数据
     */
    public  void initData(){
        //entryEntityList = new ArrayList<EventEntryEntity>();
        entryEntityList = eventEntryDao.queryList();
        if(entryEntityList!=null && entryEntityList.size()>0){
            adapter = new EventEntryListAdapter(entryEntityList,EventEntryListActivity.this);
            searchEvententryList.setAdapter(adapter);
        }
    }
}

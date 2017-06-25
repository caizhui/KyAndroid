package com.ky.kyandroid.activity.evententry;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ky.kyandroid.R;
import com.ky.kyandroid.adapter.EventLogListAdapter;
import com.ky.kyandroid.entity.TFtSjLogEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Caizhui on 2017-6-9.
 * 事件办理日志页面
 */

public class EventLogListActivity extends AppCompatActivity {

    /**
     * 导航栏左边按钮
     */
    @BindView(R.id.left_btn)
    ImageView leftBtn;
    /**
     * 导航栏中间文字
     */
    @BindView(R.id.center_text)
    TextView centerText;
    /**
     * 导航栏右边按钮
     */
    @BindView(R.id.right_btn)
    Button rightBtn;
    /**
     * 事件日志列表
     */
    @BindView(R.id.event_log_list)
    ListView eventLogList;

    /**
     * 事件日志列表
     */
    private List<TFtSjLogEntity> tFtSjLogEntityList;

    private EventLogListAdapter adapter;

    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventlog_list);
        ButterKnife.bind(this);
        centerText.setText("事件日志");
        rightBtn.setVisibility(View.INVISIBLE);
        intent =getIntent();
        initData();
    }

    /**
     * 初始化日志数据
     */
    public void initData(){
        tFtSjLogEntityList = (List<TFtSjLogEntity>) intent.getSerializableExtra("tFtSjLogEntityList");
        if(tFtSjLogEntityList!=null){
            centerText.setText("事件日志("+tFtSjLogEntityList.size()+")");
            adapter = new EventLogListAdapter(tFtSjLogEntityList,this);
            eventLogList.setAdapter(adapter);
        }
    }

    @OnClick(R.id.left_btn)
    public void onClick(View v){
        switch (v.getId()){
            case R.id.left_btn:
                onBackPressed();
                break;
        }
    }
}

package com.ky.kyandroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.ky.kyandroid.R;
import com.ky.kyandroid.activity.evententry.EventEntryListActivity;
import com.ky.kyandroid.activity.supervision.SuperVisionAddActivity;
import com.ky.kyandroid.activity.task.TaskListActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * Created by Caizhui on 2017-6-8.
 * 主界面
 */

public class MainActivity extends AppCompatActivity {

    /**
     * 标题栏左边按钮
     */
    @BindView(R.id.left_btn)
    ImageView leftBtn;

    /**
     * 标题栏中间标题
     */
    @BindView(R.id.center_text)
    TextView centerText;

    /**
     * 标题栏右边按钮
     */
    @BindView(R.id.right_btn)
    Button rightBtn;

    @BindView(R.id.gridView)
    GridView gridView;
    private List<Map<String, Object>> data_list;
    private SimpleAdapter sim_adapter;
    // 图片封装为一个数组
    private int[] icon = { R.mipmap.pp, R.mipmap.pp,
            R.mipmap.pp, R.mipmap.pp};
    private String[] iconName = { "我的事件", "我的任务", "督察督办", "地图" };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        centerText.setText("维稳办信息");
        rightBtn.setVisibility(View.INVISIBLE);
        //新建List
        data_list = new ArrayList<Map<String, Object>>();
        //获取数据
        getData();
        //新建适配器
        String [] from ={"image","text"};
        int [] to = {R.id.image,R.id.text};
        sim_adapter = new SimpleAdapter(this, data_list, R.layout.activity_main_item, from, to);
        //配置适配器
        gridView.setAdapter(sim_adapter);

    }

    public List<Map<String, Object>> getData(){
        //cion和iconName的长度是相同的，这里任选其一都可以
        for(int i=0;i<icon.length;i++){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", icon[i]);
            map.put("text", iconName[i]);
            data_list.add(map);
        }

        return data_list;
    }

    @OnItemClick(R.id.gridView)
    public  void OnItemClick(int position){
        Intent intent = new Intent();
        if(position == 0){
            intent.setClass(this, EventEntryListActivity.class);
        }else if(position == 1){
            intent.setClass(this, TaskListActivity.class);
        }else if(position == 2){
            intent.setClass(this, SuperVisionAddActivity.class);
        }
        startActivity(intent);
    }

    @OnClick({R.id.left_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            /** 返回键 **/
            case R.id.left_btn:
                onBackPressed();
                break;
        }
    }
}

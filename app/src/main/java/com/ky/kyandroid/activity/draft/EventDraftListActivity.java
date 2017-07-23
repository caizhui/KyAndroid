package com.ky.kyandroid.activity.draft;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ky.kyandroid.R;
import com.ky.kyandroid.activity.MainAddEventActivity;
import com.ky.kyandroid.activity.evententry.EventEntryAddActivity;
import com.ky.kyandroid.adapter.EventEntityListAdapter;
import com.ky.kyandroid.db.dao.TFtSjEntityDao;
import com.ky.kyandroid.entity.TFtSjEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;

/**
 * Created by Caizhui on 2017-6-9.
 * 事件草稿列表页面
 */

public class EventDraftListActivity extends AppCompatActivity {

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
     * List列表
     */
    @BindView(R.id.search_evententry_list)
    ListView searchEvententryList;

    /**
     * 事件列表
     */
    private List<TFtSjEntity> tFtSjEntityList;


    private EventEntityListAdapter adapter;

    private TFtSjEntityDao tFtSjEntityDao;

    /**
     * 事件实体
     */
    TFtSjEntity tFtSjEntity;





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventdraft_list);
        ButterKnife.bind(this);
        tFtSjEntityDao = new TFtSjEntityDao();
        tFtSjEntityList = new ArrayList<TFtSjEntity>();
        centerText.setText("事件草稿信息");
        adapter = new EventEntityListAdapter(tFtSjEntityList, EventDraftListActivity.this);
        searchEvententryList.setAdapter(adapter);

    }


    @Override
    protected void onResume() {
        super.onResume();
        // List列表设置初始化数据
        initData();
    }

    @OnClick({R.id.left_btn, R.id.right_btn})
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            /** 返回键 **/
            case R.id.left_btn:
                //onBackPressed();
                intent = new Intent(this, MainAddEventActivity.class);
                startActivity(intent);
                //finish();
                break;
            case R.id.right_btn:
                intent = new Intent(this, EventEntryAddActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * List列表设置初始化数据
     */
    public void initData() {
        tFtSjEntityList = tFtSjEntityDao.queryList();
        if(tFtSjEntityList!=null && tFtSjEntityList.size()>0){
            adapter.notifyDataSetChanged(tFtSjEntityList);
        }
    }


    @OnItemLongClick(R.id.search_evententry_list)
    public boolean OnItemLongClick(final int position){
        final TFtSjEntity tFtSjEntity = (TFtSjEntity) adapter.getItem(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(EventDraftListActivity.this);
        builder.setTitle("信息");
        builder.setMessage("确定要删除该条记录吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                boolean  flag = tFtSjEntityDao.deleteEventEntry(tFtSjEntity.getId());
                if (flag) {
                    if(tFtSjEntityList.get(position)!=null){
                        tFtSjEntityList.remove(position);
                    }
                    adapter.notifyDataSetChanged(tFtSjEntityList);
                    Toast.makeText(EventDraftListActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EventDraftListActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                }
                //刷新页面
                initData();
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

    /**
     * 点击List列表Item
     *
     * @param position
     */
    @OnItemClick(R.id.search_evententry_list)
    public void OnItemClick(int position) {
        tFtSjEntity = (TFtSjEntity) adapter.getItem(position);
        Intent intent = new Intent(this, EventEntryAddActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("tFtSjEntity", tFtSjEntity);
        /**type 0：新增 1：修改**/
        intent.putExtra("type", "1");
        intent.putExtras(bundle);
        startActivity(intent);
    }

}

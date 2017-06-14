package com.ky.kyandroid.activity.evententry;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ky.kyandroid.R;
import com.ky.kyandroid.adapter.EventEntryListAdapter;
import com.ky.kyandroid.db.dao.EventEntryDao;
import com.ky.kyandroid.entity.EventEntryEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;

import static com.ky.kyandroid.R.layout.diaog_dispatch_operation;
import static com.ky.kyandroid.R.layout.diaog_manage_operation;
import static com.ky.kyandroid.R.layout.diaog_return_operation;

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

    /**
     * 操作人员权限
     */
    private String[] listViewContent;


    /**
     * 事件实体
     */
    EventEntryEntity eventEntryEntity;

    /**
     * 提示信息
     */
    String message;

    /**
     * 操作是否成功标识
     */
    boolean flag;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evententry_list);
        ButterKnife.bind(this);
        eventEntryDao = new EventEntryDao();
    }


    @Override
    protected void onResume() {
        super.onResume();
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

    /**
     * 点击List列表Item
     * @param position
     */
    @OnItemClick(R.id.search_evententry_list)
    public  void OnItemClick(int position){
        eventEntryEntity = (EventEntryEntity) adapter.getItem(position);
        Intent intent =new Intent(this,EventEntryAddActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("eventEntryEntity",eventEntryEntity);
        /**type 0：新增 1：修改**/
        intent.putExtra("type","1");
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @OnItemLongClick(R.id.search_evententry_list)
    public boolean OnItemLongClick(final int position){
        eventEntryEntity = (EventEntryEntity) adapter.getItem(position);
        //1表示事件提交，3表示街道核实 ，6为街道受理，8为街道自行处理
        if("1".equals(eventEntryEntity.getStatus())){
            listViewContent = new String[]{"删除", "核实", "事件跟踪"};
        }else  if("3".equals(eventEntryEntity.getStatus())){
            listViewContent = new String[]{"退回", "不予立案", "受理","作废","事件跟踪"};
        }else  if("6".equals(eventEntryEntity.getStatus())){
            listViewContent = new String[]{"街道处理", "街道派遣","上报","事件跟踪"};
        }else  if("8".equals(eventEntryEntity.getStatus())){
            listViewContent = new String[]{"自行处理退回","自行处理反馈", "回访核查","事件跟踪"};
        }
        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setItems(listViewContent, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int pos) {
                //事件刚刚录入
                if("1".equals(eventEntryEntity.getStatus())){
                    //删除
                    if(pos==0) {
                        message = "";
                        flag = eventEntryDao.deleteEventEntry(eventEntryEntity.getId());
                        if (flag) {
                            Toast.makeText(EventEntryListActivity.this,  "删除成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(EventEntryListActivity.this,  "删除失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    //街道核实状态为3
                    if(pos ==1) {
                        //操作流程方法，因为所有的流程都是改变状态，所以写一个公共方法。
                        OperatingProcess("3","街道核实");
                    }
                }
                //街道已经核实，做下一步操作
                if("3".equals(eventEntryEntity.getStatus())){
                    if(pos ==0){
                        //街道退回状态为4
                        ReturnOperation(diaog_return_operation,"4","退回");
                    } else if (pos == 1) {
                        //街道不予立案状态为5
                        ReturnOperation(diaog_return_operation,"5","不予立案");
                    }else if (pos == 2) {
                        //街道受理状态为6
                        OperatingProcess("6","街道受理");
                    }else if (pos == 3) {
                        //街道作废状态为7
                        ReturnOperation(diaog_return_operation,"7","作废");
                    }
                }
                //街道已经受理，做下一步操作
                if("6".equals(eventEntryEntity.getStatus())){
                    if (pos == 0) {
                        //街道自行状态为8
                        ReturnOperation(diaog_manage_operation,"8","街道自行处理");
                    }else if (pos == 1) {
                        //街道派遣状态为9
                        ReturnOperation(diaog_dispatch_operation,"9","街道派遣");
                    }else if (pos == 2) {
                        //街道上报状态为16
                        OperatingProcess("16","街道上报");
                    }
                }
                //街道已经自行受理，做下一步操作
                if("8".equals(eventEntryEntity.getStatus())){
                    if (pos == 0) {
                        //街道自行处理退回为25
                        ReturnOperation(diaog_return_operation,"25","自行处理退回");
                    }else if (pos == 1) {
                        //自行处理反馈为
                        //ReturnOperation(diaog_dispatch_operation,"9","自行处理反馈");
                    }else if (pos == 2) {
                        //街道上报状态为12
                        OperatingProcess("12","回访核查");
                    }
                }
                //对页面数据进行刷新
                initData();
               // entryEntityList = eventEntryDao.queryList();
               // adapter.notifyDataSetChanged(entryEntityList);
            }
        });
        builder.create().show();
        return true;
    }


    /**
     * 受理操作流程
     * @param stauts
     */
    public void OperatingProcess(final String stauts, final String message){
        AlertDialog.Builder builder =new AlertDialog.Builder(EventEntryListActivity.this);
        builder.setTitle("信息");
        builder.setMessage("确定要执行次操作吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                eventEntryEntity.setStatus(stauts);
                flag =eventEntryDao.updateEventEntry(eventEntryEntity);
                if (flag) {
                    Toast.makeText(EventEntryListActivity.this,  message+"成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EventEntryListActivity.this,  message+"失败", Toast.LENGTH_SHORT).show();
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

    /**
     * 退回,不予立案操作
     */
    public  void ReturnOperation(int layout,final String status, String title){
        View mView = LayoutInflater.from(EventEntryListActivity.this).inflate(layout, null);
        AlertDialog.Builder builder =new AlertDialog.Builder(EventEntryListActivity.this);
        builder.setTitle(title+"原因");
        builder.setView(mView);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                eventEntryEntity.setStatus(status);
                flag =eventEntryDao.updateEventEntry(eventEntryEntity);
                if (flag) {
                    Toast.makeText(EventEntryListActivity.this,  message+"成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EventEntryListActivity.this,  message+"失败", Toast.LENGTH_SHORT).show();
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
}

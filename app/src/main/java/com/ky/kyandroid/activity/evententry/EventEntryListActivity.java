package com.ky.kyandroid.activity.evententry;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ky.kyandroid.Constants;
import com.ky.kyandroid.R;
import com.ky.kyandroid.adapter.EventEntityListAdapter;
import com.ky.kyandroid.bean.AckMessage;
import com.ky.kyandroid.bean.NetWorkConnection;
import com.ky.kyandroid.bean.PageBean;
import com.ky.kyandroid.db.dao.TFtSjEntityDao;
import com.ky.kyandroid.entity.TFtSjEntity;
import com.ky.kyandroid.util.JsonUtil;
import com.ky.kyandroid.util.SpUtil;
import com.ky.kyandroid.util.StringUtils;
import com.ky.kyandroid.util.SwipeRefreshUtil;
import com.solidfire.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;

import static com.ky.kyandroid.R.layout.add_department;
import static com.ky.kyandroid.R.layout.dialog_dispatch_operation;
import static com.ky.kyandroid.R.layout.dialog_manage_operation;
import static com.ky.kyandroid.R.layout.dialog_return_operation;

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
    private List<TFtSjEntity> tFtSjEntityList;

    /**
     * 每次加载信息List条数
     */
    private List<TFtSjEntity> pList;


    private EventEntityListAdapter adapter;

    private TFtSjEntityDao tFtSjEntityDao;

    /**
     * 操作人员权限
     */
    private String[] listViewContent;


    /**
     * 事件实体
     */
    TFtSjEntity tFtSjEntity;

    /**
     * 提示信息
     */
    String message;

    /**
     * 操作是否成功标识
     */
    boolean flag;

    /**
     * SharedPreferences
     */
    private SharedPreferences sp;
    /**
     * 网路工具类
     */
    private NetWorkConnection netWorkConnection;

    /**
     * 用户ID
     */
    public static final String USER_ID = "userId";

    private LinearLayout list_jiazai;

    boolean ifRefreshOK = false, ifrefresh = false;// 是否刷新完毕（防止第一次进入刷新跟加载同时进行）
    boolean ifDateEnd = false, ifload = false;// 数据是否加载完

    // 当前页
    private int currentPage;

    //每页显示条数
    private int pageSize=5;

    /**
     *
     */
    private PageBean pageBean;

    /**
     * 返回的总条数
     */
    private int totalMumber;

    /**
     * 总条数
     */
    private int total;

    /**
     * 底部标题
     */
    private TextView foot_title;

    /**
     * 加载圆形进度条
     */
    private ProgressBar progressBar;

    /** 下拉刷新容器 */
    private SwipeRefreshUtil swipeRefreshUtil;

    private List<TFtSjEntity> tempList;

    /**
     * 是否已经加载本地数据
     */
    private  boolean isIfload =true;


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // 提示信息
            String message = String.valueOf(msg.obj == null ? "系统繁忙,请稍后再试" : msg.obj);
            switch (msg.what) {
                // 失败
                case 0:
                    Toast.makeText(EventEntryListActivity.this, message, Toast.LENGTH_SHORT).show();
                    break;
                // 刷新成功
                case 1:
                    // 判断是否刷新，刷新true,加载false
                    ifrefresh = true;
                    //判断是否刷新成功
                    ifRefreshOK = true;
                    //判断是否最后加载到最后
                    ifDateEnd = false;
                    currentPage = 2;
                    //刷新时总条数从新设值
                    total=0;
                    //解析数据
                    handleTransation(message);
                    totalMumber = pList.size();
                    if (pList.size() < pageSize) {
                        ifDateEnd = true;
                        if(pageBean!=null ){
                            if(pageBean.getCurrentPage()  == 1){
                                list_jiazai.setVisibility(View.GONE);
                            }
                        }
                    }
                    swipeContainer.post(new Runnable() {

                        @Override
                        public void run() {
                            swipeContainer.setRefreshing(false);
                        }
                    });
                    adapter.notifyDataSetChanged(tFtSjEntityList);
                    break;
                // 加载更多
                case 2:
                    //解析数据
                    handleTransation(message);
                    currentPage = currentPage + 1;
                    //当加载的 条数小鱼每页显示条数时，加载完成
                    if (pList.size() < pageSize) {
                        ifDateEnd = true;
                        if(pageBean!=null ){
                            progressBar.setVisibility(View.GONE);
                            foot_title.setText("已经没有更多数据了");
                        }
                    } else {
                        ifload = false;
                        list_jiazai.setVisibility(View.GONE);
                    }
                    adapter.notifyDataSetChanged(tFtSjEntityList);
                    break;
                //刷新失败
                case 3:
                    swipeContainer.post(new Runnable() {

                        @Override
                        public void run() {
                            swipeContainer.setRefreshing(false);
                        }
                    });
                    break;
                //加载失败
                case 4:
                    list_jiazai.setVisibility(View.GONE);
                    break;
            }
            centerText.setText("事件列表("+total+")");
        }

    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evententry_list);
        ButterKnife.bind(this);
        tFtSjEntityDao = new TFtSjEntityDao();
        tFtSjEntityList = new ArrayList<TFtSjEntity>();
        initEvent();
        initData();
        adapter = new EventEntityListAdapter(tFtSjEntityList,EventEntryListActivity.this);
        searchEvententryList.setAdapter(adapter);
    }


    /**
     * 初始化事件
     */
    private void initEvent() {
        sp = SpUtil.getSharePerference(this);
        // 初始化网络工具
        netWorkConnection = new NetWorkConnection(this);
        // 加载“正在加载”布局文件
        list_jiazai = (LinearLayout) getLayoutInflater().inflate(R.layout.lv_item_jiazai, null);
        list_jiazai.setVisibility(View.GONE);
        foot_title = (TextView) list_jiazai.findViewById(R.id.foot_title);
        progressBar = (ProgressBar)list_jiazai.findViewById(R.id.progressBar);
        searchEvententryList.addFooterView(list_jiazai, null, false);
        searchEvententryList.setSelector(new ColorDrawable(Color.TRANSPARENT));
    }

    @OnClick({R.id.left_btn,R.id.right_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            /** 返回键 **/
            case R.id.left_btn:
                onBackPressed();
                //finish();
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
        final Message msg = new Message();
        msg.what = 0;
        if(netWorkConnection.isWIFIConnection()){
            final Map<String, String> paramsMap = new HashMap<String, String>();
            String userId=sp.getString(USER_ID,"");
            paramsMap.put("userId",userId);
            //1.表示刷新，2表示加载
            swipeRefreshUtil = new SwipeRefreshUtil(swipeContainer, Constants.SERVICE_QUERY_EVENTENTRY, mHandler);
            //刷新操作(1.表示刷新，2表示加载)
            swipeRefreshUtil.setSwipeRefresh(paramsMap,1);

            //加载操作
            searchEvententryList.setOnScrollListener(new AbsListView.OnScrollListener() {

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    // 判断是否滑动到最底层
                    if (view.getLastVisiblePosition() == view.getCount() - 1) {
                        // 判断返回的条数是否小于每页显示的 条数
                        if (totalMumber < pageSize) {
                            list_jiazai.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            foot_title.setText("已经没有更多数据了");
                            return;
                        }
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    if (firstVisibleItem + visibleItemCount == totalItemCount && list_jiazai.getVisibility() == View.GONE
                            && tFtSjEntityList.size() != 0) {
                        if (ifDateEnd) {
                            return;
                        }
                        if (!ifload) {
                            ifload = true;
                            paramsMap.put("currentPage", currentPage+"");
                            list_jiazai.setVisibility(View.VISIBLE);
                            //1.表示刷新，2表示加载
                            swipeRefreshUtil.setSwipeRefresh(paramsMap,2);
                        }
                    }
                }
            });
        }else{
            Toast.makeText(this, "连接不上网络！", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 点击List列表Item
     * @param position
     */
    @OnItemClick(R.id.search_evententry_list)
    public  void OnItemClick(int position){
        tFtSjEntity = (TFtSjEntity) adapter.getItem(position);
        Intent intent =new Intent(this,EventEntryAddActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("tFtSjEntity",tFtSjEntity);
      /*  *//**type 0：新增 1：修改**//*
        intent.putExtra("type","1");*/
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @OnItemLongClick(R.id.search_evententry_list)
    public boolean OnItemLongClick(final int position){
        tFtSjEntity = (TFtSjEntity) adapter.getItem(position);
        //1表示事件提交，3表示街道核实 ，6为街道受理，8为街道自行处理
        if("1".equals(tFtSjEntity.getZt())){
            listViewContent = new String[]{"删除", "核实", "事件跟踪"};
        }else  if("3".equals(tFtSjEntity.getZt())){
            listViewContent = new String[]{"退回", "不予立案", "受理","作废","事件跟踪"};
        }else  if("6".equals(tFtSjEntity.getZt())){
            listViewContent = new String[]{"街道处理", "街道派遣","上报","事件跟踪"};
        }else  if("8".equals(tFtSjEntity.getZt())){
            listViewContent = new String[]{"自行处理退回","自行处理反馈", "回访核查","事件跟踪"};
        }
        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setItems(listViewContent, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int pos) {
                //事件刚刚录入
                if("1".equals(tFtSjEntity.getZt())){
                    //删除
                    if(pos==0) {
                        message = "";
                        flag = tFtSjEntityDao.deleteEventEntry(tFtSjEntity.getId());
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
                if("3".equals(tFtSjEntity.getZt())){
                    if(pos ==0){
                        //街道退回状态为4
                        ReturnOperation(dialog_return_operation,"4","退回");
                    } else if (pos == 1) {
                        //街道不予立案状态为5
                        ReturnOperation(dialog_return_operation,"5","不予立案");
                    }else if (pos == 2) {
                        //街道受理状态为6
                        OperatingProcess("6","街道受理");
                    }else if (pos == 3) {
                        //街道作废状态为7
                        ReturnOperation(dialog_return_operation,"7","作废");
                    }
                }
                //街道已经受理，做下一步操作
                if("6".equals(tFtSjEntity.getZt())){
                    if (pos == 0) {
                        //街道自行状态为8
                        ReturnOperation(dialog_manage_operation,"8","街道自行处理");
                    }else if (pos == 1) {
                        //街道派遣状态为9
                        ReturnOperation(dialog_dispatch_operation,"9","街道派遣");
                    }else if (pos == 2) {
                        //街道上报状态为16
                        OperatingProcess("16","街道上报");
                    }
                }
                //街道已经自行受理，做下一步操作
                if("8".equals(tFtSjEntity.getZt())){
                    if (pos == 0) {
                        //街道自行处理退回为25
                        ReturnOperation(dialog_return_operation,"25","自行处理退回");
                    }else if (pos == 1) {
                        //自行处理反馈为
                        //ReturnOperation(diaog_dispatch_operation,"9","自行处理反馈");
                    }else if (pos == 2) {
                        //街道上报状态为12
                        OperatingProcess("12","回访核查");
                    }
                }
                //对页面数据进行刷新
               // initData();
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
                tFtSjEntity.setZt(stauts);
                flag =tFtSjEntityDao.updateTFtSjEntity(tFtSjEntity);
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
        final View mView = LayoutInflater.from(EventEntryListActivity.this).inflate(layout, null);
        //当点击为街道派遣时执行下面代码
        if(layout == dialog_dispatch_operation){
            //获取弹出框添加按钮
            final View view1= mView.findViewById(R.id.add_diapatch);
            //获取需要添加控件的L
            final LinearLayout linearlayout_dispatch= (LinearLayout) mView.findViewById(R.id.linearlayout_dispatch);
            view1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(EventEntryListActivity.this, "AAAA", Toast.LENGTH_SHORT).show();
                    linearlayout_dispatch.setOrientation(LinearLayout.VERTICAL);
                    //获取自定义文件
                    View view2 = LayoutInflater.from(EventEntryListActivity.this).inflate(add_department, null);
                    linearlayout_dispatch.addView(view2);
                }
            });
        }
        AlertDialog.Builder builder =new AlertDialog.Builder(EventEntryListActivity.this);
        builder.setTitle(title+"原因");
        builder.setView(mView);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                tFtSjEntity.setZt(status);
                flag =tFtSjEntityDao.updateTFtSjEntity(tFtSjEntity);
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

    public void handleTransation(String body) {
        if (StringUtils.isBlank(body)) {
        } else {
            //判断是否已经加载本地草稿数据，并且只添加一次
            if(isIfload){
                tempList = tFtSjEntityDao.queryList();
                if(tempList!=null && tempList.size()>0){
                    tFtSjEntityList.addAll(tempList);
                }
                total+=tFtSjEntityList.size();
                isIfload=false;
            }

            // 处理响应信息
            AckMessage ackMsg = JsonUtil.fromJson(body, AckMessage.class);
            if (ifrefresh) {
                //tFtSjEntityList = setMessage(ackMsg);
                ifrefresh = false;
            }
            tFtSjEntityList.addAll(setMessage(ackMsg));
        }
    }

    /**
     * 设置事件信息
     *
     * @param ackMsg
     * @return
     */
    private List<TFtSjEntity> setMessage(AckMessage ackMsg) {
        pList = new ArrayList<TFtSjEntity>();
        if (ackMsg != null) {
            if (AckMessage.SUCCESS.equals(ackMsg.getAckCode())) {
                pageBean = ackMsg.getPageBean();
                if(pageBean!=null){
                    List<JsonObject> list =pageBean.getDataList();
                    if(list != null && list.size()>0){
                        for (int i = 0; i < list.size(); i++) {
                            //先将获取的Object对象转成String
                            String entityStr = JsonUtil.toJson(list.get(i));
                            TFtSjEntity tFtSjEntity = JsonUtil.fromJson(entityStr.toLowerCase(), TFtSjEntity.class);
                            pList.add(tFtSjEntity);
                        }
                    }
                }
            }
        }
        total +=pList.size();
        return pList;
    }
}

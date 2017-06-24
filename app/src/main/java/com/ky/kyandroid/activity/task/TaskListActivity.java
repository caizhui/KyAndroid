package com.ky.kyandroid.activity.task;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ky.kyandroid.Constants;
import com.ky.kyandroid.R;
import com.ky.kyandroid.adapter.TaskEntityListAdapter;
import com.ky.kyandroid.bean.AckMessage;
import com.ky.kyandroid.bean.NetWorkConnection;
import com.ky.kyandroid.bean.PageBean;
import com.ky.kyandroid.entity.TFtSjEntity;
import com.ky.kyandroid.entity.TFtZtlzEntity;
import com.ky.kyandroid.entity.TaskEntity;
import com.ky.kyandroid.util.JsonUtil;
import com.ky.kyandroid.util.OkHttpUtil;
import com.ky.kyandroid.util.SpUtil;
import com.ky.kyandroid.util.StringUtils;
import com.ky.kyandroid.util.SweetAlertDialogUtil;
import com.ky.kyandroid.util.SwipeRefreshUtil;
import com.solidfire.gson.JsonObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.ky.kyandroid.R.layout.dialog_streethandle_item;
import static com.ky.kyandroid.R.layout.dialog_streethandle_operation;

/**
 * Created by Caizhui on 2017-6-9.
 * 事件列表页面
 */

public class TaskListActivity extends AppCompatActivity {

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
     * 刷新控件
     */
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeContainer;

    /**
     * 事件列表
     */
    private List<TaskEntity> taskEntityList;

    /**
     * 每次加载信息List条数
     */
    private List<TaskEntity> pList;


    private TaskEntityListAdapter adapter;

    /**
     * 操作人员权限名称
     */
    private String[] listViewContent;

    /**
     * 操作人员权限实体
     */
    private TFtZtlzEntity[] tFtZtlzEntities;


    /**
     * 事件实体
     */
    TaskEntity taskEntity;

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
    private int pageSize = 5;

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

    /**
     * 下拉刷新容器
     */
    private SwipeRefreshUtil swipeRefreshUtil;

    private List<TFtSjEntity> tempList;

    /**
     * 是否已经加载本地数据
     */
    private boolean isIfload = true;

    private String userId;

    /**
     * 处理时间
     */
    TextView happenTimeEdt;

    /**
     * 处理原因
     */
    EditText reasonEdt;

    /**
     * 文件新增按钮
     */
    Button fileAddBtn;

    /**
     * 文件名字
     */
    TextView  fileName;

    /****弹出框用到的一些控件end**/

    /**
     * 弹出框工具类
     */
    private SweetAlertDialogUtil sweetAlertDialogUtil;


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
                    Toast.makeText(TaskListActivity.this, message, Toast.LENGTH_SHORT).show();
                    break;
                // 刷新成功
                case 1:
                    //刷新重新初始List
                    taskEntityList = new ArrayList<TaskEntity>();
                    // 判断是否刷新，刷新true,加载false
                    ifrefresh = true;
                    isIfload = true;
                    //判断是否刷新成功
                    ifRefreshOK = true;
                    //判断是否最后加载到最后
                    ifDateEnd = false;
                    currentPage = 2;
                    //刷新时总条数从新设值
                    total = 0;
                    //解析数据
                    handleTransation(message);
                    totalMumber = pList.size();
                    if (pList.size() < pageSize) {
                        ifDateEnd = true;
                        if (pageBean != null) {
                            if (pageBean.getCurrentPage() == 1) {
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
                    adapter.notifyDataSetChanged(taskEntityList);
                    break;
                // 加载更多
                case 2:
                    //解析数据
                    handleTransation(message);
                    currentPage = currentPage + 1;
                    //当加载的 条数小鱼每页显示条数时，加载完成
                    if (pList.size() < pageSize) {
                        ifDateEnd = true;
                        if (pageBean != null) {
                            progressBar.setVisibility(View.GONE);
                            foot_title.setText("已经没有更多数据了");
                        }
                    } else {
                        ifload = false;
                        list_jiazai.setVisibility(View.GONE);
                    }
                    adapter.notifyDataSetChanged(taskEntityList);
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
                //修改状态成功
                case 5:
                    sweetAlertDialogUtil.dismissAlertDialog();
                    Toast.makeText(TaskListActivity.this, "操作成功", Toast.LENGTH_SHORT).show();
                    initData();
                    break;
                //修改状态失败
                case 6:
                    sweetAlertDialogUtil.dismissAlertDialog();
                    Toast.makeText(TaskListActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
                    break;

            }
            centerText.setText("任务列表(" + total + ")");
        }

    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evententry_list);
        ButterKnife.bind(this);
        taskEntityList = new ArrayList<TaskEntity>();
        sweetAlertDialogUtil = new SweetAlertDialogUtil(TaskListActivity.this);
        //初始化事件
        initEvent();
        // List列表设置初始化数据
        initData();
        userId = sp.getString(USER_ID, "");
        adapter = new TaskEntityListAdapter(taskEntityList, TaskListActivity.this);
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
        progressBar = (ProgressBar) list_jiazai.findViewById(R.id.progressBar);
        searchEvententryList.addFooterView(list_jiazai, null, false);
        searchEvententryList.setSelector(new ColorDrawable(Color.TRANSPARENT));
        centerText.setText("我的任务");
        rightBtn.setVisibility(View.INVISIBLE);
    }

    @OnClick({R.id.left_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            /** 返回键 **/
            case R.id.left_btn:
                onBackPressed();
                //finish();
                break;
        }
    }

    /**
     * List列表设置初始化数据
     */
    public void initData() {
        final Message msg = new Message();
        msg.what = 0;
        if (netWorkConnection.isWIFIConnection()) {
            final Map<String, String> paramsMap = new HashMap<String, String>();
            String userId = sp.getString(USER_ID, "");
            paramsMap.put("userId", userId);
            //1.表示刷新，2表示加载
            swipeRefreshUtil = new SwipeRefreshUtil(swipeContainer, Constants.SERVICE_QUERY_TASK, mHandler);
            //刷新操作(1.表示刷新，2表示加载)
            swipeRefreshUtil.setSwipeRefresh(paramsMap, 1);

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
                            && taskEntityList.size() != 0) {
                        if (ifDateEnd) {
                            return;
                        }
                        if (!ifload) {
                            ifload = true;
                            paramsMap.put("currentPage", currentPage + "");
                            list_jiazai.setVisibility(View.VISIBLE);
                            //1.表示刷新，2表示加载
                            swipeRefreshUtil.setSwipeRefresh(paramsMap, 2);
                        }
                    }
                }
            });
        } else {
            Toast.makeText(this, "连接不上网络！", Toast.LENGTH_LONG).show();
        }
    }


    /**
     * 当从后台查询到数据周，做相应的处理，并且会将本地的草稿信息显示出来
     *
     * @param body
     */
    public void handleTransation(String body) {
        if (StringUtils.isBlank(body)) {
        } else {
            // 处理响应信息
            AckMessage ackMsg = JsonUtil.fromJson(body, AckMessage.class);
            if (ifrefresh) {
                //tFtSjEntityList = setMessage(ackMsg);
                ifrefresh = false;
            }
            taskEntityList.addAll(setMessage(ackMsg));
        }
    }

    /**
     * 设置任务信息
     *
     * @param ackMsg
     * @return
     */
    private List<TaskEntity> setMessage(AckMessage ackMsg) {
        pList = new ArrayList<TaskEntity>();
        if (ackMsg != null) {
            if (AckMessage.SUCCESS.equals(ackMsg.getAckCode())) {
                pageBean = ackMsg.getPageBean();
                if (pageBean != null) {
                    List<JsonObject> list = pageBean.getDataList();
                    if (list != null && list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            //先将获取的Object对象转成String
                            String entityStr = JsonUtil.toJson(list.get(i)).toLowerCase();
                            TaskEntity taskEntity = JsonUtil.fromJson(entityStr, TaskEntity.class);
                            pList.add(taskEntity);
                        }
                    }
                }
            }
        }
        total += pList.size();
        return pList;
    }

    /**
     * 点击List列表Item
     *
     * @param position
     */
    @OnItemClick(R.id.search_evententry_list)
    public void OnItemClick(int position) {
        taskEntity = (TaskEntity) adapter.getItem(position);
        Intent intent = new Intent(this, TaskAddActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("taskEntity", taskEntity);
        /**type 0：新增 1：修改**/
        intent.putExtra("type", "1");
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 长按list列表
     *
     * @param position
     * @return
     */
    @OnItemLongClick(R.id.search_evententry_list)
    public boolean OnItemLongClick(final int position) {
        taskEntity = (TaskEntity) adapter.getItem(position);
        //获取按钮list信息
        List<TFtZtlzEntity> tFtZtlzEntityList = taskEntity.getAnlist();
        if (tFtZtlzEntityList != null && tFtZtlzEntityList.size() > 0) {
            listViewContent = new String[tFtZtlzEntityList.size()];
            tFtZtlzEntities = new TFtZtlzEntity[tFtZtlzEntityList.size()];
            for (int i = 0; i < tFtZtlzEntityList.size(); i++) {
                //获取按钮list信息中的name
                listViewContent[i] = tFtZtlzEntityList.get(i).getName();
                //获取按钮list信息中的按钮实体
                tFtZtlzEntities[i] = tFtZtlzEntityList.get(i);
            }

        }
        if (listViewContent != null && listViewContent.length > 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setItems(listViewContent, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int pos) {
                    //根据点击的item项获取该item对应的实体，
                    TFtZtlzEntity tFtZtlzEntity = tFtZtlzEntities[pos];
                    if ("8.2".equals(tFtZtlzEntity.getNextzt())) {
                        //当7街道自行处理的时候，弹出自定义对话框
                        streetHandleOperation(tFtZtlzEntity, dialog_streethandle_operation, tFtZtlzEntity.getActionname());
                    }else{
                        //其他的弹出确定对话框
                        OperatingProcess(tFtZtlzEntity,Constants.SERVICE_QUERY_TASKRECV);
                    }
                    }
            });
            builder.create().show();
        }
        return true;
    }


    /**
     * 街道自行处理操作
     */
    public void streetHandleOperation(final TFtZtlzEntity tFtZtlzEntity, int layout, String title) {
        final View mView = LayoutInflater.from(TaskListActivity.this).inflate(layout, null);
        //处理时间
        happenTimeEdt = ButterKnife.findById(mView, R.id.happen_time_edt);
        //处理原因
        reasonEdt = ButterKnife.findById(mView, R.id.reason_edt);
        fileAddBtn = ButterKnife.findById(mView, R.id.add_btn);
        //获取需要添加控件的L
        final LinearLayout linearlayout_dispatch = (LinearLayout) mView.findViewById(R.id.linearlayout_dispatch);
        fileAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearlayout_dispatch.setOrientation(LinearLayout.VERTICAL);
                //获取自定义文件
                View view2 = LayoutInflater.from(TaskListActivity.this).inflate(dialog_streethandle_item, null);
                linearlayout_dispatch.addView(view2);
                fileName = (TextView) view2.findViewById(R.id.file_name);
            }
        });
        happenTimeEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                new DatePickerDialog(TaskListActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Date date = new Date(System.currentTimeMillis());
                        SimpleDateFormat dateFormat = new SimpleDateFormat(" HH:mm:ss");
                        String time = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        time += dateFormat.format(date);
                        happenTimeEdt.setText(time);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //将修改状态的数据上传到后台
        sendOperation(mView,tFtZtlzEntity,title);
    }


    /**
     * 受理操作流程
     *
     * @param
     */
    public void OperatingProcess(final TFtZtlzEntity tFtZtlzEntity, final String url) {
        final Message msg = new Message();
        //初始状态为6，表示状态修改不成功
        msg.what = 6;
        AlertDialog.Builder builder = new AlertDialog.Builder(TaskListActivity.this);
        builder.setTitle("信息");
        builder.setMessage("确定要执行次操作吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (netWorkConnection.isWIFIConnection()) {
                    sweetAlertDialogUtil.loadAlertDialog();
                    Map<String, String> paramsMap = new HashMap<String, String>();
                    paramsMap.put("userId", userId);
                    paramsMap.put("sjId", taskEntity.getId());
                    paramsMap.put("zt", taskEntity.getZt());
                    paramsMap.put("nextZt", tFtZtlzEntity.getNextzt());
                    paramsMap.put("clId", taskEntity.getClid());
                    if(happenTimeEdt!=null){
                        paramsMap.put("lrclsj", happenTimeEdt.getText().toString());
                    }
                    if(reasonEdt!=null){
                        paramsMap.put("lrclqk", reasonEdt.getText().toString());
                    }
                    // 发送请求
                    OkHttpUtil.sendRequest(url,paramsMap, new Callback() {

                        @Override
                        public void onFailure(Call call, IOException e) {
                            mHandler.sendEmptyMessage(0);
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (response.isSuccessful()) {
                                msg.what = 5;
                                msg.obj = response.body().string();
                            } else {
                                msg.obj = "网络异常,请确认网络情况";
                            }
                            mHandler.sendMessage(msg);
                        }
                    });
                } else {
                    msg.obj = "WIFI网络不可用,请检查网络连接情况";
                    mHandler.sendMessage(msg);
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
     * 将修改状态的数据上传到后台
     */
    public void sendOperation(final View mView, final TFtZtlzEntity tFtZtlzEntity, String title) {
        final Message msg = new Message();
        //初始状态为6，表示状态修改不成功
        msg.what = 6;
        AlertDialog.Builder builder = new AlertDialog.Builder(TaskListActivity.this);
        builder.setTitle(title + "原因");
        builder.setView(mView);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (netWorkConnection.isWIFIConnection()) {
                    sweetAlertDialogUtil.loadAlertDialog();
                    Map<String, String> paramsMap = new HashMap<String, String>();
                    paramsMap.put("userId", userId);
                    paramsMap.put("sjId", taskEntity.getId());
                    paramsMap.put("zt", taskEntity.getZt());
                    paramsMap.put("nextZt", tFtZtlzEntity.getNextzt());
                    paramsMap.put("clId", taskEntity.getClid());
                    if(happenTimeEdt!=null){
                        paramsMap.put("lrclsj", happenTimeEdt.getText().toString());
                    }
                    if(reasonEdt!=null){
                        paramsMap.put("lrclqk", reasonEdt.getText().toString());
                    }
                    // 发送请求
                    OkHttpUtil.sendRequest(Constants.SERVICE_TASK_HADLE,paramsMap, new Callback() {

                        @Override
                        public void onFailure(Call call, IOException e) {
                            mHandler.sendEmptyMessage(0);
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (response.isSuccessful()) {
                                msg.what = 5;
                                msg.obj = response.body().string();
                            } else {
                                msg.obj = "网络异常,请确认网络情况";
                            }
                            mHandler.sendMessage(msg);
                        }
                    });
                } else {
                    msg.obj = "WIFI网络不可用,请检查网络连接情况";
                    mHandler.sendMessage(msg);
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

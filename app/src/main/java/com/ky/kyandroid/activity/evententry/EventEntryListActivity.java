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
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ky.kyandroid.Constants;
import com.ky.kyandroid.R;
import com.ky.kyandroid.activity.dispatch.DispatchActivity;
import com.ky.kyandroid.activity.dispatch.StreetHandleActivity;
import com.ky.kyandroid.adapter.EventEntityListAdapter;
import com.ky.kyandroid.bean.AckMessage;
import com.ky.kyandroid.bean.NetWorkConnection;
import com.ky.kyandroid.bean.PageBean;
import com.ky.kyandroid.db.dao.TFtSjEntityDao;
import com.ky.kyandroid.entity.TFtSjEntity;
import com.ky.kyandroid.entity.TFtZtlzEntity;
import com.ky.kyandroid.util.DateTimePickDialogUtil;
import com.ky.kyandroid.util.JsonUtil;
import com.ky.kyandroid.util.OkHttpUtil;
import com.ky.kyandroid.util.SpUtil;
import com.ky.kyandroid.util.StringUtils;
import com.ky.kyandroid.util.SweetAlertDialogUtil;
import com.ky.kyandroid.util.SwipeRefreshUtil;
import com.solidfire.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
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
    private List<TFtSjEntity> tFtSjEntityList;

    /**
     * 每次加载信息List条数
     */
    private List<TFtSjEntity> pList;


    private EventEntityListAdapter adapter;

    private TFtSjEntityDao tFtSjEntityDao;

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
    private int pageSize = 10;

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


    private String userId;

    /****弹出框用到的一些控件start**/

    /**
     * 自行处理事件输入框
     */
    private EditText happendTImeEdt;

    /**
     * 退回原因输入框
     */
    private EditText returnEdt;

    /**
     * 自行处理反馈处理人输入框
     */
    private EditText handlePersonEdt;

    RadioGroup radioGroup;
    RadioButton radioButton01;
    RadioButton radioButton02;
    RadioButton radioButton03;

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
            String message = String.valueOf((msg.obj == null || "".equals(msg.obj) )? "系统繁忙,请稍后再试" : msg.obj);
            switch (msg.what) {
                // 失败
                case 0:
                    sweetAlertDialogUtil.dismissAlertDialog();
                    swipeContainer.post(new Runnable() {
                        @Override
                        public void run() {
                            swipeContainer.setRefreshing(false);
                        }
                    });
                    Toast.makeText(EventEntryListActivity.this, message, Toast.LENGTH_SHORT).show();
                    break;
                // 刷新成功
                case 1:
                    sweetAlertDialogUtil.dismissAlertDialog();
                    //刷新重新初始List
                    tFtSjEntityList = new ArrayList<TFtSjEntity>();
                    // 判断是否刷新，刷新true,加载false
                    ifrefresh = true;
                    //判断是否刷新成功
                    ifRefreshOK = true;
                    //判断是否最后加载到最后
                    ifDateEnd = false;
                    currentPage = 2;
                    //刷新时总条数从新设值
                    total = 0;
                    //解析数据
                    handleTransation(message);
                    if(pList!=null){
                        totalMumber = pList.size();
                        if (pList.size() < pageSize) {
                            ifDateEnd = true;
                            if (pageBean != null) {
                                if (pageBean.getCurrentPage() == 1) {
                                    list_jiazai.setVisibility(View.GONE);
                                }
                            }
                        }else{
                            ifload = false;
                            list_jiazai.setVisibility(View.GONE);
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
                    sweetAlertDialogUtil.dismissAlertDialog();
                    //解析数据
                    handleTransation(message);
                    currentPage = currentPage + 1;
                    //当加载的 条数小鱼每页显示条数时，加载完成
                    if (pList.size() < pageSize) {
                        totalMumber = pList.size();
                        ifDateEnd = true;
                        if (pageBean != null) {
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
                //修改状态成功
                case 5:
                    sweetAlertDialogUtil.dismissAlertDialog();
                    Toast.makeText(EventEntryListActivity.this, "操作成功", Toast.LENGTH_SHORT).show();
                    initData();
                    break;
                //修改状态失败
                case 6:
                    sweetAlertDialogUtil.dismissAlertDialog();
                    Toast.makeText(EventEntryListActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
                    break;

            }
            centerText.setText("事件列表(" + total + ")");
        }

    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evententry_list);
        ButterKnife.bind(this);
        sweetAlertDialogUtil = new SweetAlertDialogUtil(this);
        tFtSjEntityDao = new TFtSjEntityDao();
        tFtSjEntityList = new ArrayList<TFtSjEntity>();
        //初始化事件
        initEvent();
        // List列表设置初始化数据
        initData();
        userId = sp.getString(USER_ID, "");
        searchEvententryList.setSelector(getResources().getDrawable(R.drawable.item_selector_grey));
        adapter = new EventEntityListAdapter(tFtSjEntityList, EventEntryListActivity.this);
        searchEvententryList.setAdapter(adapter);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initData();
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
    }

    @OnClick({R.id.left_btn, R.id.right_btn})
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
    public void initData() {
        final Message msg = new Message();
        msg.what = 0;
        if (netWorkConnection.isWIFIConnection()) {
            final Map<String, String> paramsMap = new HashMap<String, String>();
            String userId = sp.getString(USER_ID, "");
            paramsMap.put("userId", userId);
            //1.表示刷新，2表示加载
            swipeRefreshUtil = new SwipeRefreshUtil(swipeContainer, Constants.SERVICE_QUERY_EVENTENTRY, mHandler);
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
                            && tFtSjEntityList.size() != 0) {
                        if (ifDateEnd) {
                            return;
                        }
                        if (!ifload) {
                            ifload = true;
                            paramsMap.put("currentPage", currentPage + "");
                            list_jiazai.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.VISIBLE);
                            foot_title.setText("正在加载");
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
            //判断是否已经加载本地草稿数据，并且只添加一次
            /*if (isIfload) {
                tempList = tFtSjEntityDao.queryList();
                if (tempList != null && tempList.size() > 0) {
                    tFtSjEntityList.addAll(tempList);
                }
                isIfload = false;
                total += tFtSjEntityList.size();
            }*/

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
                if (pageBean != null) {
                    List<JsonObject> list = pageBean.getDataList();
                    if (list != null && list.size() > 0) {
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
        tFtSjEntity = (TFtSjEntity) adapter.getItem(position);
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("tFtSjEntity", tFtSjEntity);
        /**zt 0：修改 3,街道退回  否则看详情**/
        if("0".equals(tFtSjEntity.getZt()) || "3".equals(tFtSjEntity.getZt())){
            /**0:新增  1:修改 **/
            intent.putExtra("type", "1");
            intent.setClass(this, EventEntryAddActivity.class);
        }else{
            intent.setClass(this, EventEntryDetailActivity.class);
        }
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
        tFtSjEntity = (TFtSjEntity) adapter.getItem(position);
        //2表示事件提交，3表示街道核实 ，6为街道受理，8为街道自行处理
        List<TFtZtlzEntity> tFtZtlzEntityList = tFtSjEntity.getAnlist();
        if (tFtZtlzEntityList != null && tFtZtlzEntityList.size() > 0) {
            listViewContent = new String[tFtZtlzEntityList.size()];
            tFtZtlzEntities = new TFtZtlzEntity[tFtZtlzEntityList.size()];
            for (int i = 0; i < tFtZtlzEntityList.size(); i++) {
                listViewContent[i] = tFtZtlzEntityList.get(i).getName();
                tFtZtlzEntities[i] = tFtZtlzEntityList.get(i);
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setItems(listViewContent, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int pos) {
                    //事件刚刚录入
                        //根据点击的item项获取该item对应的实体，
                        TFtZtlzEntity tFtZtlzEntity = tFtZtlzEntities[pos];
                        //当3.退回和4.不予受理的时候，弹出自定义对话框
                        if ("3".equals(tFtZtlzEntity.getNextzt()) || "4".equals(tFtZtlzEntity.getNextzt())) {
                            ReturnOperation(tFtZtlzEntity, R.layout.dialog_return_operation, tFtZtlzEntity.getName()+"原因",Constants.SERVICE_EDIT_EVENT);
                        }  else if ("5".equals(tFtZtlzEntity.getNextzt())) {
                            //当5街道自行退回的时候，弹出自定义对话框
                            if("2".equals(tFtSjEntity.getZt())){
                                //当前状态为2，受理。
                                OperatingProcess(tFtZtlzEntity);
                            }else{
                                ReturnOperation(tFtZtlzEntity, R.layout.dialog_streetreturn_operation, tFtZtlzEntity.getName()+"原因",Constants.SERVICE_EDIT_EVENT);
                            }
                        } else if ("6".equals(tFtZtlzEntity.getNextzt())) {
                            //当6事件作废的时候，弹出自定义对话框
                            ReturnOperation(tFtZtlzEntity, R.layout.dialog_cancel_operation, tFtZtlzEntity.getName()+"原因",Constants.SERVICE_EDIT_EVENT);
                        } else if ("7".equals(tFtZtlzEntity.getNextzt())) {
                            //当7街道自行处理的时候，弹出自定义对话框
                            Intent intent = new Intent(EventEntryListActivity.this, StreetHandleActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("tFtSjEntity", tFtSjEntity);
                            bundle.putSerializable("tFtZtlzEntity", tFtZtlzEntity);
                            intent.putExtras(bundle);
                            startActivity(intent);
                           // ReturnOperation(tFtZtlzEntity, dialog_streethandle_operation, tFtZtlzEntity.getActionname(),Constants.SERVICE_ZXCL );
                        }else if ("7.2".equals(tFtZtlzEntity.getNextzt())) {
                            //当7.2事件街道自行处理反馈的时候，弹出自定义对话框 修改为调用接口：Constants.SERVICE_ZXCLBANJ_EVENT
                            ReturnOperation(tFtZtlzEntity, R.layout.dialog_streetfeedback_operation, tFtZtlzEntity.getName(),Constants.SERVICE_ZXCLBANJ_EVENT );
                        }else if("7,8".equals(tFtZtlzEntity.getNextzt())){
                            //当走街道自行处理这条线时，如果当前状态为9，则退回到7街道自行处理，否则退回8，弹出自定义对话框
                          /*  if("9".equals(tFtSjEntity.getZt())){
                                tFtZtlzEntity.setNextzt("7");
                            }else{
                                tFtZtlzEntity.setNextzt("8");
                            }*/
                            //当10回访核查通过或者7,8回访核查不通过的时候，弹出自定义对话框
                            ReturnOperation(tFtZtlzEntity, R.layout.dialog_verification_operation, tFtZtlzEntity.getName()+"原因",Constants.SERVICE_EDIT_EVENT);
                        }else if ("8".equals(tFtZtlzEntity.getNextzt()) || "13".equals(tFtZtlzEntity.getNextzt())) {
                            if("14".equals(tFtZtlzEntity.getPrevzt())){
                                ReturnOperation(tFtZtlzEntity, R.layout.dialog_verification_operation, tFtZtlzEntity.getName()+"原因",Constants.SERVICE_EDIT_EVENT);
                            }else{
                                //当8街道派遣的时候，跳到街道派遣Activity
                                Intent  intent =new Intent(EventEntryListActivity.this, DispatchActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("tFtSjEntity", tFtSjEntity);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }

                        }else if ("10".equals(tFtZtlzEntity.getNextzt()) || "15".equals(tFtZtlzEntity.getNextzt())) {
                            //当10回访核查，弹出自定义对话框
                            ReturnOperation(tFtZtlzEntity, R.layout.dialog_verification_operation, tFtZtlzEntity.getName()+"原因",Constants.SERVICE_EDIT_EVENT);
                        }else if ("18".equals(tFtZtlzEntity.getNextzt())) {
                            //当18合并，因为手机上不支持该操作，所以给出提示
                            Toast.makeText(EventEntryListActivity.this, "移动设备不支持该操作", Toast.LENGTH_SHORT).show();
                        } else {
                            //其他的弹出确定对话框
                            OperatingProcess(tFtZtlzEntity);
                        }

                }
            });
            builder.create().show();
        }else{
            Toast.makeText(this, "权限不足,不支持该操作", Toast.LENGTH_SHORT).show();
        }
        return true;
    }


    /**
     * 受理操作流程
     *
     * @param
     */
    public void OperatingProcess(final TFtZtlzEntity tFtZtlzEntity) {
        final Message msg = new Message();
        //初始状态为6，表示状态修改不成功
        msg.what = 6;
        AlertDialog.Builder builder = new AlertDialog.Builder(EventEntryListActivity.this);
        builder.setTitle("信息");
        builder.setMessage("确定要执行次操作吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (netWorkConnection.isWIFIConnection()) {
                    sweetAlertDialogUtil.loadAlertDialog();
                    Map<String, String> paramsMap = new HashMap<String, String>();
                    paramsMap.put("userId", userId);
                    paramsMap.put("sjId", tFtSjEntity.getId());
                    paramsMap.put("action", tFtZtlzEntity.getAction());
                    paramsMap.put("actionName", tFtZtlzEntity.getActionname());
                    paramsMap.put("zt", tFtSjEntity.getZt());
                    paramsMap.put("nextZt", tFtZtlzEntity.getNextzt());
                    // 发送请求
                    OkHttpUtil.sendRequest(Constants.SERVICE_EDIT_EVENT, paramsMap, new Callback() {

                        @Override
                        public void onFailure(Call call, IOException e) {
                            msg.what=6;
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
     * 退回,不予立案,作废操作
     */
    public void ReturnOperation(final TFtZtlzEntity tFtZtlzEntity, int layout, String title,String url) {
        final View mView = LayoutInflater.from(EventEntryListActivity.this).inflate(layout, null);
        //获取弹出框的属性
        handlePersonEdt = ButterKnife.findById(mView, R.id.handle_person_edt);
        happendTImeEdt = ButterKnife.findById(mView, R.id.happen_time_edt);
        returnEdt = ButterKnife.findById(mView, R.id.return_edt);
        radioGroup = ButterKnife.findById(mView, R.id.radioGroup);
        radioButton01 = ButterKnife.findById(mView, R.id.radioButton01);
        radioButton02 = ButterKnife.findById(mView, R.id.radioButton02);
        radioButton03 = ButterKnife.findById(mView, R.id.radioButton03);
        //不予立案退回备选原因
        if("4".equals(tFtZtlzEntity.getNextzt())){
            radioButton01.setText("未达到立案标准");
            radioButton02.setVisibility(View.INVISIBLE);
        }
        //回放核查不通过
        if("7,8".equals(tFtZtlzEntity.getNextzt()) || ("13".equals(tFtZtlzEntity.getNextzt())&&"14".equals(tFtZtlzEntity.getPrevzt()))){
            radioButton03.setVisibility(View.VISIBLE);
            radioGroup.setOrientation(LinearLayout.VERTICAL);
            radioButton01.setText("街道回访核查不通过原因1");
            radioButton02.setText("街道回访核查不通过原因2");
            radioButton03.setText("街道回访核查不通过原因3");

        }
        //回放核查通过
        if("10".equals(tFtZtlzEntity.getNextzt()) || "15".equals(tFtZtlzEntity.getNextzt())){
            radioButton03.setVisibility(View.VISIBLE);
            radioButton01.setText("人手不足");
            radioButton02.setText("权限不足");
            radioButton03.setText("脱离可控范围");
        }
        if(radioGroup!=null){
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (radioButton01.getId() == checkedId) {
                        returnEdt.setText(radioButton01.getText().toString());
                    } else if (radioButton02.getId() == checkedId) {
                        returnEdt.setText(radioButton02.getText().toString());
                    } else if (radioButton03.getId() == checkedId) {
                        returnEdt.setText(radioButton03.getText().toString());
                    }
                }
            });
        }
        //自行处理时间输入框
        if(happendTImeEdt!=null){
            happendTImeEdt.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                       /* Calendar c = Calendar.getInstance();
                        new DatePickerDialog(EventEntryListActivity.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                Date date = new Date(System.currentTimeMillis());
                                SimpleDateFormat dateFormat = new SimpleDateFormat(" HH:mm:ss");
                                String time = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                time += dateFormat.format(date);
                                happendTImeEdt.setText(time);
                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();*/
                        DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
                                EventEntryListActivity.this, "");
                        dateTimePicKDialog.dateTimePicKDialog(happendTImeEdt);
                    }
                    return false;
                }
            });
        }
        //将修改状态的数据上传到后台
        sendOperation(mView,tFtZtlzEntity,title,url);
    }




    /**
     * 将修改状态的数据上传到后台
     */
    public void sendOperation(final View mView, final TFtZtlzEntity tFtZtlzEntity, String title, final String url) {
        final Message msg = new Message();
        //初始状态为6，表示状态修改不成功
        msg.what = 6;
        AlertDialog.Builder builder = new AlertDialog.Builder(EventEntryListActivity.this);
        builder.setTitle(title);
        builder.setView(mView);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (netWorkConnection.isWIFIConnection()) {
                    String message="";
                    Map<String, String> paramsMap = new HashMap<String, String>();
                    paramsMap.put("userId", userId);
                    paramsMap.put("sjId", tFtSjEntity.getId());
                    paramsMap.put("action", tFtZtlzEntity.getAction());
                    paramsMap.put("actionName", tFtZtlzEntity.getActionname());
                    paramsMap.put("zt", tFtSjEntity.getZt());
                    paramsMap.put("nextZt", tFtZtlzEntity.getNextzt());
                    if(happendTImeEdt!=null){
                        paramsMap.put("lrclsj", happendTImeEdt.getText().toString());
                    }
                    if(returnEdt!=null){
                        //7.2自行处理反馈时，处理人和处理结果为必填项
                        if("7.2".equals(tFtZtlzEntity.getNextzt())){
                            if("".equals(returnEdt.getText().toString())){
                                message +="处理结果不能为空\n";
                            }else{
                                paramsMap.put("czyy", returnEdt.getText().toString());
                            }
                            //回访核查和回访核查不通过原因必填
                        }else if("7,8".equals(tFtZtlzEntity.getNextzt()) || "10".equals(tFtZtlzEntity.getNextzt())){
                            if("".equals(returnEdt.getText().toString())){
                                message += tFtZtlzEntity.getActionname()+"原因不能为空\n";
                            }else{
                                paramsMap.put("czyy", returnEdt.getText().toString());
                            }
                        }else{
                            paramsMap.put("czyy", returnEdt.getText().toString());
                        }
                    }
                    if(handlePersonEdt!=null){
                        if("7.2".equals(tFtZtlzEntity.getNextzt())){
                            if("".equals(handlePersonEdt.getText().toString())){
                                message +="处理人不能为空\n";
                            }else{
                                paramsMap.put("clr", handlePersonEdt.getText().toString());
                            }
                        }else{
                            paramsMap.put("clr", handlePersonEdt.getText().toString());
                        }

                    }
                  if(!"".equals(message)){
                      Toast.makeText(EventEntryListActivity.this,message,Toast.LENGTH_SHORT).show();;
                  }else{
                      sweetAlertDialogUtil.loadAlertDialog();
                      // 发送请求
                      OkHttpUtil.sendRequest(url,paramsMap, new Callback() {

                          @Override
                          public void onFailure(Call call, IOException e) {
                              msg.what=6;
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
                  }
                  }else {
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

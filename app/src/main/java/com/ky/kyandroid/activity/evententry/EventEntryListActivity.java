package com.ky.kyandroid.activity.evententry;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.ky.kyandroid.activity.LoginActivity;
import com.ky.kyandroid.activity.dispatch.DispatchActivity;
import com.ky.kyandroid.activity.dispatch.StreetHandleActivity;
import com.ky.kyandroid.adapter.EventEntityListAdapter;
import com.ky.kyandroid.bean.AckMessage;
import com.ky.kyandroid.bean.NetWorkConnection;
import com.ky.kyandroid.bean.PageBean;
import com.ky.kyandroid.db.dao.TFtSjEntityDao;
import com.ky.kyandroid.entity.SjHandleParams;
import com.ky.kyandroid.entity.TFtSjEntity;
import com.ky.kyandroid.entity.TFtZtlzEntity;
import com.ky.kyandroid.util.DateTimePickDialogUtil;
import com.ky.kyandroid.util.JsonUtil;
import com.ky.kyandroid.util.OkHttpUtil;
import com.ky.kyandroid.util.SpUtil;
import com.ky.kyandroid.util.StringUtils;
import com.ky.kyandroid.util.SweetAlertDialogUtil;
import com.ky.kyandroid.util.SwipeRefreshUtil;
import com.solidfire.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Field;
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
     * TAG
     */
    static final String TAG = "EventEntryListActivity";

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
     * 数据加载
     */
    private List<?> dataList;


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

    /**
     * 默认查询条目数/每页 -- 后台已经写死10条记录
     */
    private String pageSize = "10";

    /**
     *
     */
    private PageBean pageBean;

    /**
     * 总条数
     */
    private int total = 0;

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

    private String name;

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
    TextView fileName;

    /****弹出框用到的一些控件end**/

    /**
     * 弹出框工具类
     */
    private SweetAlertDialogUtil sweetAlertDialogUtil;

    /**
     * 查询参数
     */
    private Map<String, String> paramsMap = null;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // 提示信息
            String message = String.valueOf((msg.obj == null || "".equals(msg.obj)) ? "系统繁忙,请稍后再试" : msg.obj);
            sweetAlertDialogUtil.dismissAlertDialog();
            switch (msg.what) {
                // 默认处理
                case 0:
                    Toast.makeText(EventEntryListActivity.this, message, Toast.LENGTH_SHORT).show();
                    break;
                // 加载失败
                case 1:
                    Log.i(TAG, "刷新操作...");
                    swipeRefreshUtil.dismissRefreshing();
                    if (handleResponseData(message)) {
                        notifyListViewData(false);
                        Toast.makeText(EventEntryListActivity.this, "刷新成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(EventEntryListActivity.this, "刷新失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                // 加载操作
                case 2:
                    Log.i(TAG, "加载操作...");
                    if (handleResponseData(message)) {
                        notifyListViewData(true);
                    }
                    break;
                // 加载失败
                case 4:
                    Log.i(TAG, "加载失败...");
                    swipeRefreshUtil.dismissRefreshing();
                    Toast.makeText(EventEntryListActivity.this, message, Toast.LENGTH_SHORT).show();
                    break;
                // 初始化跳转
                case 7:
                    Log.i(TAG, "初始化成功...");
                    swipeRefreshUtil.dismissRefreshing();
                    if (handleResponseData(message)) {
                        notifyListViewData(false);
                    } else {
                        Toast.makeText(EventEntryListActivity.this, "查询不到符合条件记录", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 8:
                    // 状态修改
                    Log.i(TAG, "加载操作...");
                    if (handleResponseDataState(message)) {
                        Toast.makeText(EventEntryListActivity.this, "操作成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(EventEntryListActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
                    }
                    break;

            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evententry_list);
        ButterKnife.bind(this);
        // 初始化视图
        initViewAndEvent();
        // 设置参数
        initBundle();
        //初始化下拉列表
        initListView();
        // 初始化数据
        initData();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null){
            String type = intent.getStringExtra("businessType");
            if("initList".equals(type)){
                // 初始化数据
                initData();
            }else if ("isfrash".equals(type)){
                //刷新当前一行数据
                String message = intent.getStringExtra("message");
                if(!"".equals(message)){
                    // 状态修改
                    Log.i(TAG, "加载操作...");
                    if (handleResponseDataState(message)) {
                        Toast.makeText(EventEntryListActivity.this, "操作成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(EventEntryListActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }
    }


    /**
     * 初始化视图与事件
     */
    void initViewAndEvent() {
        sp = SpUtil.getSharePerference(this);
        netWorkConnection = new NetWorkConnection(this);
        sweetAlertDialogUtil = new SweetAlertDialogUtil(EventEntryListActivity.this);
        tFtSjEntityDao = new TFtSjEntityDao();
        tFtSjEntityList = new ArrayList<TFtSjEntity>();
        userId = sp.getString(USER_ID, "");
        name = sp.getString("name", "");
        if ("街道办工作人员".equals(name)  || "香蜜湖录入人员".equals(name)) {
            rightBtn.setVisibility(View.VISIBLE);
        }

    }


    /**
     * 获取参数Bundle
     */
    void initBundle() {
        paramsMap = new HashMap<String, String>();
        // 查询自己的消息
        paramsMap.put("userId", sp.getString(LoginActivity.USER_ID, ""));
        // 设置标题及颜色
        centerText.setText("事件列表(" + total + ")");
        //toolbar_layout.setBackgroundColor(Color.parseColor("#A4C639"));
        swipeRefreshUtil = new SwipeRefreshUtil(swipeContainer, Constants.SERVICE_QUERY_EVENTENTRY, mHandler);
        // 上拉刷新初始化
        swipeRefreshUtil.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                paramsMap.put("currentPage", "1");
                paramsMap.put("pageSize", pageSize);
                // 打开圈圈
                swipeContainer.setRefreshing(true);
                swipeRefreshUtil.refreshSetDate(paramsMap, 1);
            }
        });
    }


    /**
     * 初始化列表
     */
    private void initListView() {
        // 加载“正在加载”布局文件
        list_jiazai = (LinearLayout) getLayoutInflater().inflate(R.layout.lv_item_jiazai, null);
        searchEvententryList.addFooterView(list_jiazai, null, false);
        searchEvententryList.setSelector(getResources().getDrawable(R.drawable.item_selector_grey));
        //滚动监听
        searchEvententryList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        // 判断滚动到底部
                        if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                            list_jiazai.setVisibility(View.VISIBLE);
                            if (pageBean.getTotalCount() < pageBean.getPageSize()) {
                                initListFoot("没有更多内容了", View.GONE);
                            } else {
                                initListFoot("正在加载,请稍后...", View.VISIBLE);
                                nextCurrentPage();
                                swipeRefreshUtil.refreshSetDate(paramsMap, 2);
                            }
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        adapter = new EventEntityListAdapter(tFtSjEntityList, EventEntryListActivity.this);
        searchEvententryList.setAdapter(adapter);
    }

    /**
     * 初始化页脚
     */
    private void initListFoot(String message, int display) {
        foot_title = (TextView) list_jiazai.findViewById(R.id.foot_title);
        progressBar = (ProgressBar) list_jiazai.findViewById(R.id.progressBar);
        if (StringUtils.isNotBlank(message)) {
            foot_title.setText(message);
        }
        progressBar.setVisibility(display);
    }

    /**
     * 下一页
     */
    void nextCurrentPage() {
        int currentPage = 1;
        if (pageBean != null) {
            currentPage = pageBean.getCurrentPage() + 1;
        }
        paramsMap.put("currentPage", currentPage + "");
        paramsMap.put("pageSize", pageSize);
    }

    /**
     * 加载数据
     */
    private void notifyListViewData(boolean isAdd) {
        List<TFtSjEntity> entityList = (List<TFtSjEntity>) dataList;
        if (isAdd) {
            // 追加列表
            adapter.addDataSetChanged(entityList);
        } else {
            /*
            String userName = sp.getString(LoginActivity.USER_NAME, "");
            if ("hqb01".equals(userName)) {
                loadLocationSjList(entityList);
            }
            */
            // 刷新列表
            adapter.notifyDataSetChanged(entityList);
        }
        centerText.setText("事件列表(" + adapter.getCount() + ")");
    }

    /**
     * 更新数据列表状态
     *
     * @param sjHandleParams
     */
    private void updateListDataStatie(SjHandleParams sjHandleParams) {
        List<TFtSjEntity> tftsjList = adapter.getList();
        if (tftsjList != null) {
            for (int i = 0; i < tftsjList.size(); i++) {
                TFtSjEntity entity = tftsjList.get(i);
                if (entity.getId().equals(sjHandleParams.getSjId())) {
                    entity.setZt(sjHandleParams.getNextZt());
                    entity.setZtname(sjHandleParams.getZtname());
                    entity.setAnlist(sjHandleParams.getAnlist());
                    break;
                }
            }
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 加载本地草稿数据
     */
    private void loadLocationSjList(List<TFtSjEntity> entityList) {
        // 第一页时加载
        if ("1".equals(String.valueOf(paramsMap.get("currentPage")))) {
            List<TFtSjEntity> locationList = tFtSjEntityDao.queryList();
            if (locationList != null && locationList.size() > 0) {
                entityList.addAll(0, locationList);
            }
        }
    }

    /**
     * 处理响应数据
     */
    public boolean handleResponseData(String body) {
        boolean flag = false;
        if (StringUtils.isBlank(body)) {
            Log.i(TAG, "解释响应body失败...");
        } else {
            // 处理响应信息
            AckMessage ackMsg = JsonUtil.fromJson(body, AckMessage.class);
            if (ackMsg != null) {
                if (AckMessage.SUCCESS.equals(ackMsg.getAckCode())) {
                    Log.i(TAG, "消息通知解释成功...");
                    pageBean = ackMsg.getPageBean();
                    if (null != pageBean) {
                        List<?> dataL = pageBean.getDataList();
                        if (dataL != null && dataL.size() > 0) {
                            // 响应字符串
                            String resultList = JsonUtil.toJson(dataL);
                            dataList = (List<TFtSjEntity>) JsonUtil.fromJson(resultList, new TypeToken<List<TFtSjEntity>>() {
                            });
                            flag = true;
                        }
                    }
                }
            } else {
                Log.i(TAG, "解释列表信息失败...");
            }

        }
        return flag;
    }


    /**
     * 处理响应数据状态
     */
    public boolean handleResponseDataState(String body) {
        boolean flag = false;
        if (StringUtils.isBlank(body)) {
            Log.i(TAG, "解释响应body失败...");
        } else {
            // 处理响应信息
            AckMessage ackMsg = JsonUtil.fromJson(body, AckMessage.class);
            if (ackMsg != null) {
                if (AckMessage.SUCCESS.equals(ackMsg.getAckCode())) {
                    Log.i(TAG, "数据解释成功...");
                    Object entity = ackMsg.getEntity();
                    if (entity != null) {
                        String dataStr = JsonUtil.toJson(entity);
                        SjHandleParams sjHandleParams = JsonUtil.fromJson(dataStr, SjHandleParams.class);
                        if (sjHandleParams != null) {
                            flag = true;
                            // 更新数据列表状态
                            updateListDataStatie(sjHandleParams);
                        }

                    }
                }
            } else {
                Log.i(TAG, "解释列表信息失败...");
            }

        }
        return flag;
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
                intent.putExtra("czlx", "add");
                startActivity(intent);
                break;
        }
    }

    /**
     * List列表设置初始化数据
     */
    public void initData() {
        if (netWorkConnection.isWIFIConnection()) {
            list_jiazai.setVisibility(View.GONE);
            paramsMap.put("currentPage", "1");
            paramsMap.put("pageSize", pageSize);
            swipeRefreshUtil.showRefreshing();
            swipeRefreshUtil.refreshSetDate(paramsMap, 7);
        } else {
            Toast.makeText(this, "WIFI不可用,请确认网络连接", Toast.LENGTH_SHORT).show();
        }
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
        if ("0".equals(tFtSjEntity.getZt()) || "3".equals(tFtSjEntity.getZt())) {
            /**0:新增  1:修改 **/
            intent.putExtra("type", "1");
            intent.setClass(this, EventEntryAddActivity.class);
        } else {
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
                    if ("3".equals(tFtZtlzEntity.getNextZt()) || "4".equals(tFtZtlzEntity.getNextZt())) {
                        ReturnOperation(tFtZtlzEntity, R.layout.dialog_return_operation, tFtZtlzEntity.getName() + "原因", Constants.SERVICE_EDIT_EVENT);
                    } else if ("5".equals(tFtZtlzEntity.getNextZt())) {
                        //当5街道自行退回的时候，弹出自定义对话框
                        if ("2".equals(tFtSjEntity.getZt())) {
                            //当前状态为2，受理。
                            OperatingProcess(tFtZtlzEntity);
                        } else {
                            ReturnOperation(tFtZtlzEntity, R.layout.dialog_streetreturn_operation, tFtZtlzEntity.getName() + "原因", Constants.SERVICE_EDIT_EVENT);
                        }
                    } else if ("6".equals(tFtZtlzEntity.getNextZt())) {
                        //当6事件作废的时候，弹出自定义对话框
                        ReturnOperation(tFtZtlzEntity, R.layout.dialog_cancel_operation, tFtZtlzEntity.getName() + "原因", Constants.SERVICE_EDIT_EVENT);
                    } else if ("7".equals(tFtZtlzEntity.getNextZt())) {
                        //当7街道自行处理的时候，弹出自定义对话框
                        Intent intent = new Intent(EventEntryListActivity.this, StreetHandleActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("tFtSjEntity", tFtSjEntity);
                        bundle.putSerializable("tFtZtlzEntity", tFtZtlzEntity);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        // ReturnOperation(tFtZtlzEntity, dialog_streethandle_operation, tFtZtlzEntity.getActionname(),Constants.SERVICE_ZXCL );
                    } else if ("7.2".equals(tFtZtlzEntity.getNextZt())) {
                        //当7.2事件街道自行处理反馈的时候，弹出自定义对话框 修改为调用接口：Constants.SERVICE_ZXCLBANJ_EVENT
                        ReturnOperation(tFtZtlzEntity, R.layout.dialog_streetfeedback_operation, tFtZtlzEntity.getName(), Constants.SERVICE_ZXCLBANJ_EVENT);
                    } else if ("7,8".equals(tFtZtlzEntity.getNextZt())) {
                        //当10回访核查通过或者7,8回访核查不通过的时候，弹出自定义对话框
                        ReturnOperation(tFtZtlzEntity, R.layout.dialog_verification_operation, tFtZtlzEntity.getName() + "原因", Constants.SERVICE_EDIT_EVENT);
                    } else if ("8".equals(tFtZtlzEntity.getNextZt()) || "13".equals(tFtZtlzEntity.getNextZt())) {
                        if ("14".equals(tFtZtlzEntity.getPrevZt())) {
                            ReturnOperation(tFtZtlzEntity, R.layout.dialog_verification_operation, tFtZtlzEntity.getName() + "原因", Constants.SERVICE_EDIT_EVENT);
                        } else {
                            //当8街道派遣的时候，跳到街道派遣Activity
                            Intent intent = new Intent(EventEntryListActivity.this, DispatchActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("tFtSjEntity", tFtSjEntity);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }

                    } else if ("10".equals(tFtZtlzEntity.getNextZt()) || "15".equals(tFtZtlzEntity.getNextZt())) {
                        //当10回访核查，弹出自定义对话框
                        ReturnOperation(tFtZtlzEntity, R.layout.dialog_verification_operation, tFtZtlzEntity.getName() + "原因", Constants.SERVICE_EDIT_EVENT);
                    } else if ("18".equals(tFtZtlzEntity.getNextZt())) {
                        //当18合并，因为手机上不支持该操作，所以给出提示
                        Toast.makeText(EventEntryListActivity.this, "移动设备不支持该操作", Toast.LENGTH_SHORT).show();
                    } else {
                        //其他的弹出确定对话框
                        OperatingProcess(tFtZtlzEntity);
                    }

                }
            });
            builder.create().show();
        } else {
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
                    paramsMap.put("actionName", tFtZtlzEntity.getActionName());
                    paramsMap.put("zt", tFtSjEntity.getZt());
                    paramsMap.put("nextZt", tFtZtlzEntity.getNextZt());
                    // 发送请求
                    OkHttpUtil.sendRequest(Constants.SERVICE_EDIT_EVENT, paramsMap, new Callback() {

                        @Override
                        public void onFailure(Call call, IOException e) {
                            mHandler.sendEmptyMessage(0);
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (response.isSuccessful()) {
                                msg.what = 8;
                                msg.obj = response.body().string();
                            } else {
                                msg.what = 0;
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
    public void ReturnOperation(final TFtZtlzEntity tFtZtlzEntity, int layout, String title, String url) {
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
        if ("4".equals(tFtZtlzEntity.getNextZt())) {
            radioButton01.setText("未达到立案标准");
            radioButton02.setVisibility(View.INVISIBLE);
        }
        //回放核查不通过 或者反归档操作
        if ("7,8".equals(tFtZtlzEntity.getNextZt()) || ("13".equals(tFtZtlzEntity.getNextZt()) && "14".equals(tFtZtlzEntity.getPrevZt()))) {
            //反归档操作
            if (("sjJdfgd".equals(tFtZtlzEntity.getAction()))) {
                radioButton03.setVisibility(View.GONE);
                radioGroup.setOrientation(LinearLayout.HORIZONTAL);
                radioButton01.setText("事件出现不稳定苗头");
                radioButton02.setText("事件再次爆发");

            }else{
                radioButton02.setVisibility(View.GONE);
                radioButton03.setVisibility(View.GONE);
                radioButton01.setText("事件未处置完");
            }
        }
        //回放核查通过
        if ("10".equals(tFtZtlzEntity.getNextZt()) || "15".equals(tFtZtlzEntity.getNextZt())) {
            radioButton02.setVisibility(View.GONE);
            radioButton03.setVisibility(View.GONE);
            radioButton01.setText("事件已经完结");
        }
        if (radioGroup != null) {
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
        if (happendTImeEdt != null) {
            happendTImeEdt.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
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
        sendOperation(mView, tFtZtlzEntity, title, url);
    }


    /**
     * 将修改状态的数据上传到后台
     */
    public void sendOperation(final View mView, final TFtZtlzEntity tFtZtlzEntity, String title, final String url) {
        final Message msg = new Message();
        AlertDialog.Builder builder = new AlertDialog.Builder(EventEntryListActivity.this);
        builder.setTitle(title);
        builder.setView(mView);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (netWorkConnection.isWIFIConnection()) {
                    String message = "";
                    Map<String, String> paramsMap = new HashMap<String, String>();
                    paramsMap.put("userId", userId);
                    paramsMap.put("sjId", tFtSjEntity.getId());
                    paramsMap.put("action", tFtZtlzEntity.getAction());
                    paramsMap.put("actionName", tFtZtlzEntity.getActionName());
                    paramsMap.put("zt", tFtSjEntity.getZt());
                    paramsMap.put("nextZt", tFtZtlzEntity.getNextZt());
                    if (happendTImeEdt != null) {
                        paramsMap.put("lrclsj", happendTImeEdt.getText().toString());
                    }
                    if (returnEdt != null) {
                        //3 退回，4不予立案，5自行处理退回
                        if ("3".equals(tFtZtlzEntity.getNextZt()) || "4".equals(tFtZtlzEntity.getNextZt()) ||"5".equals(tFtZtlzEntity.getNextZt())) {
                            if ("".equals(returnEdt.getText().toString())) {
                                message += tFtZtlzEntity.getName()+"原因不能为空\n";
                            } else {
                                paramsMap.put("czyy", returnEdt.getText().toString());
                            }
                        }
                        //7.2自行处理反馈时，处理人和处理结果为必填项
                        if ("7.2".equals(tFtZtlzEntity.getNextZt())) {
                            if ("".equals(returnEdt.getText().toString())) {
                                message += "处理结果不能为空\n";
                            } else {
                                paramsMap.put("czyy", returnEdt.getText().toString());
                            }
                            //回访核查和回访核查不通过原因必填
                        } else if ("7,8".equals(tFtZtlzEntity.getNextZt()) || "10".equals(tFtZtlzEntity.getNextZt())) {
                            if ("".equals(returnEdt.getText().toString())) {
                                message += tFtZtlzEntity.getActionName() + "原因不能为空\n";
                            } else {
                                paramsMap.put("czyy", returnEdt.getText().toString());
                            }
                        } else {
                            paramsMap.put("czyy", returnEdt.getText().toString());
                        }
                    }
                    if (handlePersonEdt != null) {
                        if ("7.2".equals(tFtZtlzEntity.getNextZt())) {
                            if ("".equals(handlePersonEdt.getText().toString())) {
                                message += "处理人不能为空\n";
                            } else {
                                paramsMap.put("clr", handlePersonEdt.getText().toString());
                            }
                        } else {
                            paramsMap.put("clr", handlePersonEdt.getText().toString());
                        }

                    }
                    if (!"".equals(message)) {
                        closeDialog(dialogInterface,false);
                        Toast.makeText(EventEntryListActivity.this, message, Toast.LENGTH_SHORT).show();
                    } else {
                        closeDialog(dialogInterface,true);
                        sweetAlertDialogUtil.loadAlertDialog();
                        // 发送请求
                        OkHttpUtil.sendRequest(url, paramsMap, new Callback() {

                            @Override
                            public void onFailure(Call call, IOException e) {
                                mHandler.sendEmptyMessage(0);
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                if (response.isSuccessful()) {
                                    msg.what = 8;
                                    msg.obj = response.body().string();
                                } else {
                                    msg.what = 0;
                                    msg.obj = "网络异常,请确认网络情况";
                                }
                                mHandler.sendMessage(msg);
                            }
                        });
                    }
                } else {
                    msg.obj = "WIFI网络不可用,请检查网络连接情况";
                    mHandler.sendMessage(msg);
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                closeDialog(dialogInterface,true);
            }
        });
        builder.create().show();
    }


    /**
     * 关闭弹出框  isClose =false 关闭，否则 不关闭
     * @param isClose
     */
    public void  closeDialog(DialogInterface dialogInterface,boolean isClose){
        //不关闭
        try{
            Field field = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
            field.setAccessible(true);
            field.set(dialogInterface, isClose);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

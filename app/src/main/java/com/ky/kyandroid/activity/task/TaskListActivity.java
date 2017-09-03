package com.ky.kyandroid.activity.task;

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
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.ky.kyandroid.activity.dispatch.QuHandleActivity;
import com.ky.kyandroid.activity.dispatch.StreetDispatchActivity;
import com.ky.kyandroid.adapter.TaskEntityListAdapter;
import com.ky.kyandroid.bean.AckMessage;
import com.ky.kyandroid.bean.NetWorkConnection;
import com.ky.kyandroid.bean.PageBean;
import com.ky.kyandroid.entity.SjHandleParams;
import com.ky.kyandroid.entity.TFtZtlzEntity;
import com.ky.kyandroid.entity.TaskEntity;
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

public class TaskListActivity extends AppCompatActivity {

    private final String TAG="TaskListActivity";

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
     * 总条数
     */
    private int total = 0;

    /**
     *
     */
    private PageBean pageBean;

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

    /**
     * 数据加载
     */
    private List<?> dataList;

    private String userId;

    /**
     * 处理时间
     */
    TextView happenTimeEdt;

    /**
     * 处理原因
     */
    EditText reasonEdt;


    /****弹出框用到的一些控件end**/

    /**
     * 弹出框工具类
     */
    private SweetAlertDialogUtil sweetAlertDialogUtil;

    /**
     * 查询参数
     */
    private Map<String, String> paramsMap = null;

    RadioGroup radioGroup;
    RadioButton radioButton01;
    RadioButton radioButton02;
    RadioButton radioButton03;

    /**
     * 操作原因输入框
     */
    private EditText czyyEdt;

    private TextView jsr_edit;

    private TextView jssj_edit;

    private EditText clr_edit;

    private CheckBox checkBox01;

    private CheckBox checkBox02;

    private CheckBox checkBox03;

    List<TaskEntity> entityList;

    /**
     * 临时未知
     */
    private int tempPosition;

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
                    Toast.makeText(TaskListActivity.this, message, Toast.LENGTH_SHORT).show();
                    break;
                // 加载失败
                case 1:
                    Log.i(TAG, "刷新操作...");
                    swipeRefreshUtil.dismissRefreshing();
                    if (handleResponseData(message)) {
                        notifyListViewData(false);
                        Toast.makeText(TaskListActivity.this, "刷新成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(TaskListActivity.this, "无数据记录", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(TaskListActivity.this, message, Toast.LENGTH_SHORT).show();
                    break;
                // 初始化跳转
                case 7:
                    Log.i(TAG, "初始化成功...");
                    swipeRefreshUtil.dismissRefreshing();
                    if (handleResponseData(message)) {
                        notifyListViewData(false);
                    } else {
                        Toast.makeText(TaskListActivity.this, "查询不到符合条件记录", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 8:
                    // 状态修改
                    Log.i(TAG, "加载操作...");
                    if (handleResponseDataState(message)) {
                        Toast.makeText(TaskListActivity.this, "操作成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(TaskListActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
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
                       // Toast.makeText(TaskListActivity.this, "操作成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(TaskListActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
                    }
                }

            }
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
     * 初始化视图与事件
     */
    void initViewAndEvent() {
        sp = SpUtil.getSharePerference(this);
        netWorkConnection = new NetWorkConnection(this);
        sweetAlertDialogUtil = new SweetAlertDialogUtil(TaskListActivity.this);
        taskEntityList = new ArrayList<TaskEntity>();
        userId = sp.getString(USER_ID, "");
        centerText.setText("任务列表");
    }


    /**
     * 获取参数Bundle
     */
    void initBundle() {
        paramsMap = new HashMap<String, String>();
        // 查询自己的消息
        paramsMap.put("userId", sp.getString(LoginActivity.USER_ID, ""));
        // 设置标题及颜色
        centerText.setText("任务列表(" + total + ")");
        //toolbar_layout.setBackgroundColor(Color.parseColor("#A4C639"));
        swipeRefreshUtil = new SwipeRefreshUtil(swipeContainer, Constants.SERVICE_QUERY_TASK, mHandler);
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

        adapter = new TaskEntityListAdapter(taskEntityList, TaskListActivity.this);
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
                            dataList = (List<TaskEntity>) JsonUtil.fromJson(resultList, new TypeToken<List<TaskEntity>>() {
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
     * 加载数据
     */
    private void notifyListViewData(boolean isAdd) {
        entityList = (List<TaskEntity>) dataList;
        if (isAdd) {
            // 追加列表
            adapter.addDataSetChanged(entityList);
        } else {
            // 刷新列表
            adapter.notifyDataSetChanged(entityList);
        }
        centerText.setText("任务列表(" + adapter.getCount() + ")");
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
                            if("1".equals(sjHandleParams.getIsRemove())){
                                List<TaskEntity> tftsjList = adapter.getList();
                                //任务退回时，该条记录去掉
                                if(tftsjList.get(tempPosition)!=null){
                                    tftsjList.remove(tempPosition);
                                    adapter.notifyDataSetChanged();
                                }
                            }else{
                                // 更新数据列表状态
                                updateListDataStatie(sjHandleParams);
                            }

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
     * 更新数据列表状态
     *
     * @param sjHandleParams
     */
    private void updateListDataStatie(SjHandleParams sjHandleParams) {
        List<TaskEntity> tftsjList = adapter.getList();
        if (tftsjList != null) {
            for (int i = 0; i < tftsjList.size(); i++) {
                TaskEntity entity = tftsjList.get(i);
                    if (entity.getClid().equals(sjHandleParams.getClbmId())) {
                        entity.setZt(sjHandleParams.getNextZt());
                        entity.setZtname(sjHandleParams.getZtname());
                        entity.setAnlist(sjHandleParams.getAnlist());
                        //当申请办结的时候，因为没有刷新任务列表，所以申请办结的接收人和接收时间放在返回的参数里面，并且更新到当前任务中
                        if(sjHandleParams.getJsr()!=null){
                            entity.setJsr(sjHandleParams.getJsr());
                        }
                        if(sjHandleParams.getJssj()!=null){
                            entity.setJssj(sjHandleParams.getJssj());
                        }
                        break;
                    }
            }
            adapter.notifyDataSetChanged();
        }
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
        tempPosition  = position;
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
            if (listViewContent != null && listViewContent.length > 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setItems(listViewContent, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int pos) {
                        //根据点击的item项获取该item对应的实体，
                        TFtZtlzEntity tFtZtlzEntity = tFtZtlzEntities[pos];
                        if ("8".equals(tFtZtlzEntity.getNextZt())) {
                            //当下个状态为8 的时候，并且上一个状态为8，表示退回，弹出自定义对话框
                            if ("8".equals(tFtZtlzEntity.getPrevZt())) {
                                yanQiOrReturnOperation(tFtZtlzEntity, R.layout.dialog_return_operation, tFtZtlzEntity.getName(), Constants.SERVICE_TASK_BACK);
                            }
                        } else if ("8.1".equals(tFtZtlzEntity.getNextZt())) {
                            //当8.1申请延期的时候，并且上一个状态为8，表示接收，否则表示申请延期，弹出自定义对话框
                            if ("8".equals(tFtZtlzEntity.getPrevZt())) {
                                OperatingProcess(tFtZtlzEntity, Constants.SERVICE_QUERY_TASKRECV);
                            } else if ("8.1".equals(tFtZtlzEntity.getPrevZt())) {
                                //yanQiOrReturnOperation(tFtZtlzEntity, R.layout.dialog_return_operation, tFtZtlzEntity.getName(), Constants.SERVICE_EDIT_YANQI);
                                Intent intent = new Intent(TaskListActivity.this, QuHandleActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("taskEntity", taskEntity);
                                bundle.putSerializable("tFtZtlzEntity", tFtZtlzEntity);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }else if ("8,8.1".equals(tFtZtlzEntity.getPrevZt())) {
                                yanQiOrReturnOperation(tFtZtlzEntity, R.layout.dialog_return_operation, tFtZtlzEntity.getName(), Constants.SERVICE_EDIT_YANQI);
                            }
                        } else if ("8.2".equals(tFtZtlzEntity.getNextZt())) {
                            //街道职能办处理
                            if ("13,8,8.1".equals(tFtZtlzEntity.getPrevZt())) {
                                Intent intent = new Intent(TaskListActivity.this, QuHandleActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("taskEntity", taskEntity);
                                bundle.putSerializable("tFtZtlzEntity", tFtZtlzEntity);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            } else {
                                //当8.2申请办结处理的时候，弹出自定义对话框
                                banJiOperation(tFtZtlzEntity, R.layout.dialog_over_operation, "申请办结", Constants.SERVICE_EDIT_BANJI);
                            }

                        }else if ("13".equals(tFtZtlzEntity.getNextZt()) && "13".equals(tFtZtlzEntity.getPrevZt())) {
                            //当下一个状态为13，并且上一个状态也为13时，表示区维稳办派发给街道维稳办，跳到派遣界面
                            Intent intent = new Intent(TaskListActivity.this, StreetDispatchActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("taskEntity", taskEntity);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }  else if ("13.2".equals(tFtZtlzEntity.getNextZt())) {
                            //当13.3区处理的时候，弹出自定义对话框
                            Intent intent = new Intent(TaskListActivity.this, QuHandleActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("taskEntity", taskEntity);
                            bundle.putSerializable("tFtZtlzEntity", tFtZtlzEntity);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } else {
                            //其他的弹出确定对话框
                            OperatingProcess(tFtZtlzEntity, Constants.SERVICE_QUERY_TASKRECV);
                        }
                    }
                });
                builder.create().show();
            }
        }else {
            Toast.makeText(this, "权限不足,不支持该操作", Toast.LENGTH_SHORT).show();
        }
        return true;
    }


    /**
     * 申请延期或者退回操作
     */
    public void yanQiOrReturnOperation(final TFtZtlzEntity tFtZtlzEntity, int layout, String title,String url) {
        final View mView = LayoutInflater.from(TaskListActivity.this).inflate(layout, null);
        //获取弹出框的属性
        czyyEdt = ButterKnife.findById(mView, R.id.return_edt);
        radioGroup = ButterKnife.findById(mView, R.id.radioGroup);
        radioButton01 = ButterKnife.findById(mView, R.id.radioButton01);
        radioButton02 = ButterKnife.findById(mView, R.id.radioButton02);
        radioButton03 = ButterKnife.findById(mView, R.id.radioButton03);
        //申请延期原因
        if("8.1".equals(tFtZtlzEntity.getNextZt()) || "8".equals(tFtZtlzEntity.getNextZt())){
            if(clr_edit!=null){
                clr_edit = null;
            }
            radioButton02.setVisibility(View.GONE);
            radioButton03.setVisibility(View.GONE);
            radioButton01.setText("当事人未出席会议");
        }
        if(radioGroup!=null){
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (radioButton01.getId() == checkedId) {
                        czyyEdt.setText(radioButton01.getText().toString());
                    } else if (radioButton02.getId() == checkedId) {
                        czyyEdt.setText(radioButton02.getText().toString());
                    } else if (radioButton03.getId() == checkedId) {
                        czyyEdt.setText(radioButton03.getText().toString());
                    }
                }
            });
        }
        //将修改状态的数据上传到后台
        sendOperation(mView,tFtZtlzEntity,title,url);
    }


    /**
     * 申请办结操作
     */
    public void banJiOperation(final TFtZtlzEntity tFtZtlzEntity, int layout, String title,String url) {
        final View mView = LayoutInflater.from(TaskListActivity.this).inflate(layout, null);
        final String[] czyy = {""};
        //获取弹出框的属性
        jsr_edit = ButterKnife.findById(mView, R.id.jsr_edit);
        jssj_edit = ButterKnife.findById(mView, R.id.jssj_edit);
        clr_edit = ButterKnife.findById(mView, R.id.clr_edit);
        checkBox01 = ButterKnife.findById(mView, R.id.checkbox01);
        checkBox02 = ButterKnife.findById(mView, R.id.checkbox02);
        checkBox03 = ButterKnife.findById(mView, R.id.checkbox03);
        czyyEdt = ButterKnife.findById(mView, R.id.czyy_edt);
        jsr_edit.setText(taskEntity.getJsr());
        jssj_edit.setText(taskEntity.getJssj());
        checkBox01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBox01.isChecked()) {
                    czyy[0] +=checkBox01.getText().toString()+",";
                }else{
                    czyy[0]=czyy[0].replace(checkBox01.getText().toString()+",","");
                }
                czyyEdt.setText(czyy[0]);
            }
        });
        checkBox02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBox02.isChecked()) {
                    czyy[0] +=checkBox02.getText().toString()+",";
                }else{
                    czyy[0]=czyy[0].replace(checkBox02.getText().toString()+",","");
                }
                czyyEdt.setText(czyy[0]);
            }
        });
        checkBox03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBox03.isChecked()) {
                    czyy[0] +=checkBox03.getText().toString()+",";
                }else{
                    czyy[0]= czyy[0].replace(checkBox03.getText().toString()+",","");
                }
                czyyEdt.setText(czyy[0]);
            }
        });
        //将修改状态的数据上传到后台
        sendOperation(mView,tFtZtlzEntity,title,url);
    }


    /**
     * 受理操作流程
     *
     * @param
     */
    public void OperatingProcess(final TFtZtlzEntity tFtZtlzEntity, final String url) {
        final Message msg = new Message();
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
                    paramsMap.put("nextZt", tFtZtlzEntity.getNextZt());
                    paramsMap.put("clbmId", taskEntity.getClid());
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
     * 将修改状态的数据上传到后台
     */
    public void sendOperation(final View mView, final TFtZtlzEntity tFtZtlzEntity, String title, final String url) {
        final Message msg = new Message();
        //初始状态为6，表示状态修改不成功
        msg.what = 6;
        AlertDialog.Builder builder = new AlertDialog.Builder(TaskListActivity.this);
        builder.setTitle(title);
        builder.setView(mView);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (netWorkConnection.isWIFIConnection()) {
                    Map<String, String> paramsMap = new HashMap<String, String>();
                    paramsMap.put("userId", userId);
                    paramsMap.put("sjId", taskEntity.getId());
                    paramsMap.put("clbmId", taskEntity.getClid());
                    String message="";
                    if(clr_edit!=null){
                        if("".equals(clr_edit.getText().toString())){
                            message+="处理人不能为空\n";
                        }else{
                            paramsMap.put("clr", clr_edit.getText().toString());
                        }
                    }
                    if(czyyEdt!=null){
                        if("".equals(czyyEdt.getText().toString())){
                            message+="处理原因不能为空\n";
                        }else{
                            paramsMap.put("czyy", czyyEdt.getText().toString());
                        }
                    }
                    if("".equals(message)){
                        closeDialog(dialogInterface,true);
                        sweetAlertDialogUtil.loadAlertDialog();
                        // 发送请求
                        OkHttpUtil.sendRequest(url,paramsMap, new Callback() {

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
                    }else{
                        closeDialog(dialogInterface,false);
                        Toast.makeText(TaskListActivity.this,message,Toast.LENGTH_SHORT).show();
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

package com.ky.kyandroid.activity.supervision;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ky.kyandroid.Constants;
import com.ky.kyandroid.R;
import com.ky.kyandroid.activity.LoginActivity;
import com.ky.kyandroid.adapter.SupervisionListAdapter;
import com.ky.kyandroid.bean.AckMessage;
import com.ky.kyandroid.bean.CodeValue;
import com.ky.kyandroid.bean.NetWorkConnection;
import com.ky.kyandroid.bean.PageBean;
import com.ky.kyandroid.entity.DbAnEntity;
import com.ky.kyandroid.entity.DescEntity;
import com.ky.kyandroid.entity.SjHandleParams;
import com.ky.kyandroid.entity.TFtDbEntity;
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
 * 督查督办列表页面
 */

public class SuperVisionListActivity extends AppCompatActivity {

    private final String TAG = "SuperVisionListActivity";

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
    @BindView(R.id.search_supervision_list)
    ListView searchSupervisionList;

    /**
     * 刷新控件
     */
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeContainer;

    /**
     * 事件列表
     */
    private List<TFtDbEntity> tFtDbEntityList;


    private SupervisionListAdapter adapter;

    /**
     * 事件实体
     */
    TFtDbEntity tFtDbEntity;


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
     * 弹出框工具类
     */
    private SweetAlertDialogUtil sweetAlertDialogUtil;

    /**
     * 查询参数
     */
    private Map<String, String> paramsMap = null;

    List<TFtDbEntity> entityList;

    /**
     * 操作人员权限名称
     */
    private String[] listViewContent;

    /**
     * 操作人员权限实体
     */
    private DbAnEntity[] dbAnEntities;

    /**
     * 临时位置
     */
    private int tempPosition;


    /**
     * 被转派部门的LinearLayout
     */
    private LinearLayout linearlayoutSpinner;


    private Spinner  spinnerText;

    /**
     * 操作标题
     */
    private TextView operationText;

    /**
     * 操作内容
     */
    private EditText  operationContent;

    /**
     * 转派部门
     */
    private CodeValue zpbmCodeValue;

    /**
     * 转派部门Id
     */
    private String zpbmId;

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
                    Toast.makeText(SuperVisionListActivity.this, message, Toast.LENGTH_SHORT).show();
                    break;
                // 加载失败
                case 1:
                    Log.i(TAG, "刷新操作...");
                    if (handleResponseData(message)) {
                        notifyListViewData(false);
                        Toast.makeText(SuperVisionListActivity.this, "刷新成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SuperVisionListActivity.this, "刷新失败", Toast.LENGTH_SHORT).show();
                    }
                    swipeRefreshUtil.dismissRefreshing();
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
                    Toast.makeText(SuperVisionListActivity.this, message, Toast.LENGTH_SHORT).show();
                    break;
                // 初始化跳转
                case 7:
                    Log.i(TAG, "初始化成功...");
                    swipeRefreshUtil.dismissRefreshing();
                    if (handleResponseData(message)) {
                        notifyListViewData(false);
                    } else {
                        Toast.makeText(SuperVisionListActivity.this, "查询不到符合条件记录", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 8:
                    // 状态修改
                    Log.i(TAG, "加载操作...");
                    if (handleResponseDataState(message)) {
                        Toast.makeText(SuperVisionListActivity.this, "操作成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SuperVisionListActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
                    }
                    break;

            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervision_list);
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



    @OnClick({R.id.left_btn,R.id.right_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            /** 返回键 **/
            case R.id.left_btn:
                onBackPressed();
                //finish();
                break;
            case R.id.right_btn:
                Intent intent = new Intent(this, SuperVisionAddActivity.class);
                startActivity(intent);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            String type = intent.getStringExtra("businessType");
            if ("initList".equals(type)) {
                // 初始化数据
                initData();
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
        sweetAlertDialogUtil = new SweetAlertDialogUtil(SuperVisionListActivity.this);
        tFtDbEntityList = new ArrayList<TFtDbEntity>();
        userId = sp.getString(USER_ID, "");
        centerText.setText("督查督办");
        rightBtn.setVisibility(View.VISIBLE);
    }


    /**
     * 获取参数Bundle
     */
    void initBundle() {
        paramsMap = new HashMap<String, String>();
        // 查询自己的消息
        paramsMap.put("userId", sp.getString(LoginActivity.USER_ID, ""));
        // 设置标题及颜色
        centerText.setText("督查督办(" + total + ")");
        //toolbar_layout.setBackgroundColor(Color.parseColor("#A4C639"));
        swipeRefreshUtil = new SwipeRefreshUtil(swipeContainer, Constants.SERVICE_DBLIST, mHandler);
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
        searchSupervisionList.addFooterView(list_jiazai, null, false);
        searchSupervisionList.setSelector(getResources().getDrawable(R.drawable.item_selector_grey));
        //滚动监听
        searchSupervisionList.setOnScrollListener(new AbsListView.OnScrollListener() {
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

        adapter = new SupervisionListAdapter(tFtDbEntityList, SuperVisionListActivity.this);
        searchSupervisionList.setAdapter(adapter);
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
                    if(pageBean!=null){
                        List<?> dataL = pageBean.getDataList();
                        if (dataL != null && dataL.size() > 0) {
                            // 响应字符串
                            String resultList = JsonUtil.toJson(dataL);
                            dataList = (List<TFtDbEntity>) JsonUtil.fromJson(resultList.toLowerCase(), new TypeToken<List<TFtDbEntity>>() {
                            });
                            flag = true;
                        } else {
                            Log.i(TAG, "解释列表信息失败...");
                        }
                    }

                }
            }
        }
        return flag;
    }

    /**
     * 加载数据
     */
    private void notifyListViewData(boolean isAdd) {
        entityList = (List<TFtDbEntity>) dataList;
        if (isAdd) {
            // 追加列表
            adapter.addDataSetChanged(entityList);
        } else {
            // 刷新列表
            adapter.notifyDataSetChanged(entityList);
        }
        centerText.setText("督查督办(" + adapter.getCount() + ")");
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
                    List<TFtDbEntity> tFtDbEntityList = adapter.getList();
                    //任务退回时，该条记录去掉
                    if(tFtDbEntityList.get(tempPosition)!=null){
                        flag = true;
                        tFtDbEntityList.remove(tempPosition);
                        adapter.notifyDataSetChanged();
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
        List<TFtDbEntity> tFtDbEntityList = adapter.getList();
        if (tFtDbEntityList != null) {
            for (int i = 0; i < tFtDbEntityList.size(); i++) {
                TFtDbEntity entity = tFtDbEntityList.get(i);
            }
            adapter.notifyDataSetChanged();
        }
    }


    /**
     * 点击List列表Item
     *
     * @param position
     */
    @OnItemClick(R.id.search_supervision_list)
    public void OnItemClick(int position) {
        tFtDbEntity = (TFtDbEntity) adapter.getItem(position);
        Intent intent = new Intent(this, SuperVisionDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("tFtDbEntity", tFtDbEntity);
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
    @OnItemLongClick(R.id.search_supervision_list)
    public boolean OnItemLongClick(final int position) {
        tFtDbEntity = (TFtDbEntity) adapter.getItem(position);
        tempPosition = position;
        List<DbAnEntity> dbAnEntityList=tFtDbEntity.getAnlist();
        if (dbAnEntityList != null && dbAnEntityList.size() > 0) {
            listViewContent = new String[dbAnEntityList.size()];
            dbAnEntities = new DbAnEntity[dbAnEntityList.size()];
            for (int i = 0; i < dbAnEntityList.size(); i++) {
                listViewContent[i] = dbAnEntityList.get(i).getShowname();
                dbAnEntities[i] = dbAnEntityList.get(i);
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setItems(listViewContent, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int pos) {
                    //根据点击的item项获取该item对应的实体，
                    DbAnEntity dbAnEntity = dbAnEntities[pos];
                    if("0".equals(dbAnEntity.getNextstatus())){
                        //编辑
                        Intent intent =new Intent(SuperVisionListActivity.this,SuperVisionEditActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("tFtDbEntity", tFtDbEntity);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }else if("1".equals(dbAnEntity.getNextstatus())){
                        //转派
                        sendOperation(dbAnEntity,"distribute");
                    }else if("2".equals(dbAnEntity.getNextstatus())){
                        //受理
                        sendOperation(dbAnEntity,"accept");
                    }else if("3".equals(dbAnEntity.getNextstatus())){
                        //退回
                        sendOperation(dbAnEntity,"returned");
                    }else if("4".equals(dbAnEntity.getNextstatus())){
                        //作废
                        OperatingProcess(tFtDbEntity,"abolish");
                    }else{
                        //删除
                        OperatingProcess(tFtDbEntity,"del");
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
     * 操作流程
     *
     * @param
     */
    public void OperatingProcess(final TFtDbEntity tFtDbEntity, final String requestType) {
        final Message msg = new Message();
        AlertDialog.Builder builder = new AlertDialog.Builder(SuperVisionListActivity.this);
        builder.setTitle("信息");
        builder.setMessage("确定要执行此操作吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (netWorkConnection.isWIFIConnection()) {
                    sweetAlertDialogUtil.loadAlertDialog();
                    Map<String, String> paramsMap = new HashMap<String, String>();
                    paramsMap.put("userId", userId);
                    paramsMap.put("dbIds", tFtDbEntity.getId());
                    paramsMap.put("requestType",requestType);

                    // 发送请求
                    OkHttpUtil.sendRequest(Constants.SERVICE_DBDBEXCEUTE, paramsMap, new Callback() {

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
    public void sendOperation(final DbAnEntity dbAnEntity,  final String requestType) {
        final View mView = LayoutInflater.from(SuperVisionListActivity.this).inflate(R.layout.dialog_db_operation, null);
        linearlayoutSpinner = ButterKnife.findById(mView, R.id.linearlayout_spinner);
        spinnerText= ButterKnife.findById(mView, R.id.spinner_text);
        operationText = ButterKnife.findById(mView, R.id.operation_text);
        operationContent = ButterKnife.findById(mView, R.id.operation_content);
        spinnerText.setSelection(0, false);
        spinnerText.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                zpbmCodeValue = (CodeValue) adapterView.getItemAtPosition(position);
                zpbmId = zpbmCodeValue.getCode();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        String title="请填写转派原因!";
        //转派的时候，需要获取转派的部门
        if("1".equals(dbAnEntity.getNextstatus())){
            //设置Spinner控件的初始值
            List<DescEntity> spinnerList = tFtDbEntity.getZpbmList();
            List<CodeValue> codeValueList = new ArrayList<CodeValue>();
            if(spinnerList!=null && spinnerList.size()>0){
                for(int i=0;i<spinnerList.size();i++){
                    codeValueList.add(new CodeValue(spinnerList.get(i).getId(),spinnerList.get(i).getValue()));
                }
            }
            //将可选内容与ArrayAdapter连接起来
            ArrayAdapter arrayAdapter = new ArrayAdapter<CodeValue>(SuperVisionListActivity.this, android.R.layout.simple_spinner_item, codeValueList);
            //设置下拉列表的风格
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerText.setAdapter(arrayAdapter);//将adapter 添加到spinner中
        }
        //2受理的时候，linearlayout_spinner不显示
        if("2".equals(dbAnEntity.getNextstatus())){
            linearlayoutSpinner.setVisibility(View.GONE);
            operationText.setText("督办反馈结果:");
        }
        //3退回的时候，linearlayout_spinner不显示
        if("3".equals(dbAnEntity.getNextstatus())){
            linearlayoutSpinner.setVisibility(View.GONE);
            operationText.setText("退回原因:");
        }
        final Message msg = new Message();
        AlertDialog.Builder builder = new AlertDialog.Builder(SuperVisionListActivity.this);
        builder.setTitle(dbAnEntity.getShowname());
        builder.setView(mView);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (netWorkConnection.isWIFIConnection()) {
                    String message = "";
                    Map<String, String> paramsMap = new HashMap<String, String>();
                    //转派的时候，需要获取转派的部门
                    if("1".equals(dbAnEntity.getNextstatus())){
                        //设置下拉列表的风格
                        if("".equals(spinnerText.getTextAlignment())){
                            message="转派给部门不能为空!";
                        }
                        if("".equals(operationContent.getText().toString())){
                            message="请填写转派原因!";
                        }
                    }
                    //2受理的时候
                    if("2".equals(dbAnEntity.getNextstatus())){
                        if("".equals(operationContent.getText().toString())){
                            message="请填写反馈情况!";
                        }

                    }
                    //3退回的时候
                    if("3".equals(dbAnEntity.getNextstatus())){
                        if("".equals(operationContent.getText().toString())){
                            message ="请填写退回原因!";
                        }
                    }
                    if (!"".equals(message)) {
                        closeDialog(dialogInterface,false);
                        Toast.makeText(SuperVisionListActivity.this, message, Toast.LENGTH_SHORT).show();
                    } else {
                        paramsMap.put("userId", userId);
                        paramsMap.put("dbIds", tFtDbEntity.getId());
                        paramsMap.put("requestType",requestType);
                        paramsMap.put("czyy",operationContent.getText().toString());
                        //转派的时候，需要获取转派的部门
                        if("1".equals(dbAnEntity.getNextstatus())){
                            paramsMap.put("zpbm",zpbmId);
                        }
                        closeDialog(dialogInterface,true);
                        sweetAlertDialogUtil.loadAlertDialog();
                        // 发送请求
                        OkHttpUtil.sendRequest(Constants.SERVICE_DBDBEXCEUTE, paramsMap, new Callback() {

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
     *
     * @param
     */
    public void closeDialog(DialogInterface dialogInterface, boolean isClose) {
        //不关闭
        try {
            Field field = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
            field.setAccessible(true);
            field.set(dialogInterface, isClose);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

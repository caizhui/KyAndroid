package com.ky.kyandroid.activity.supervision;

import android.annotation.SuppressLint;
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
import com.ky.kyandroid.activity.LoginActivity;
import com.ky.kyandroid.adapter.SupervisionListAdapter;
import com.ky.kyandroid.bean.AckMessage;
import com.ky.kyandroid.bean.NetWorkConnection;
import com.ky.kyandroid.bean.PageBean;
import com.ky.kyandroid.entity.SjHandleParams;
import com.ky.kyandroid.entity.TFtDbEntity;
import com.ky.kyandroid.entity.TFtZtlzEntity;
import com.ky.kyandroid.util.JsonUtil;
import com.ky.kyandroid.util.SpUtil;
import com.ky.kyandroid.util.StringUtils;
import com.ky.kyandroid.util.SweetAlertDialogUtil;
import com.ky.kyandroid.util.SwipeRefreshUtil;
import com.solidfire.gson.reflect.TypeToken;

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
            } else if ("isfrash".equals(type)) {
                //刷新当前一行数据
                String message = intent.getStringExtra("message");
                if (!"".equals(message)) {
                    // 状态修改
                    Log.i(TAG, "加载操作...");
                    if (handleResponseDataState(message)) {
                        Toast.makeText(SuperVisionListActivity.this, "操作成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SuperVisionListActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
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
        return false;
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

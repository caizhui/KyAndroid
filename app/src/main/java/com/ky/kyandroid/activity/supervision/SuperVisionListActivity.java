package com.ky.kyandroid.activity.supervision;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
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
import com.ky.kyandroid.adapter.SupervisionListAdapter;
import com.ky.kyandroid.bean.AckMessage;
import com.ky.kyandroid.bean.NetWorkConnection;
import com.ky.kyandroid.bean.PageBean;
import com.ky.kyandroid.entity.TFtDbEntity;
import com.ky.kyandroid.util.JsonUtil;
import com.ky.kyandroid.util.SpUtil;
import com.ky.kyandroid.util.StringUtils;
import com.ky.kyandroid.util.SweetAlertDialogUtil;
import com.ky.kyandroid.util.SwipeRefreshUtil;

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
     * 督办列表
     */
    private List<TFtDbEntity> tFtDbEntityList;

    /**
     * 每次加载信息List条数
     */
    private List<TFtDbEntity> pList;


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

    private String userId;

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
                    Toast.makeText(SuperVisionListActivity.this, message, Toast.LENGTH_SHORT).show();
                    break;
                // 刷新成功
                case 1:
                    sweetAlertDialogUtil.dismissAlertDialog();
                    //刷新重新初始List
                    tFtDbEntityList = new ArrayList<TFtDbEntity>();
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
                    adapter.notifyDataSetChanged(tFtDbEntityList);
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
                    adapter.notifyDataSetChanged(tFtDbEntityList);
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
                    Toast.makeText(SuperVisionListActivity.this, "操作成功", Toast.LENGTH_SHORT).show();
                    initData();
                    break;
                //修改状态失败
                case 6:
                    sweetAlertDialogUtil.dismissAlertDialog();
                    Toast.makeText(SuperVisionListActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
                    break;

            }
        }

    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervision_list);
        ButterKnife.bind(this);
        sweetAlertDialogUtil = new SweetAlertDialogUtil(this);
        tFtDbEntityList = new ArrayList<TFtDbEntity>();
        //初始化事件
        initEvent();
        // List列表设置初始化数据
        initData();
        userId = sp.getString(USER_ID, "");
        searchSupervisionList.setSelector(getResources().getDrawable(R.drawable.item_selector_grey));
        adapter = new SupervisionListAdapter(tFtDbEntityList, SuperVisionListActivity.this);
        searchSupervisionList.setAdapter(adapter);

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
        searchSupervisionList.addFooterView(list_jiazai, null, false);
        searchSupervisionList.setSelector(new ColorDrawable(Color.TRANSPARENT));
        centerText.setText("督查督办列表");
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
                Intent intent = new Intent(this, SuperVisionAddActivity.class);
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
            swipeRefreshUtil = new SwipeRefreshUtil(swipeContainer, Constants.SERVICE_DBLIST, mHandler);
            //刷新操作(1.表示刷新，2表示加载)
            swipeRefreshUtil.setSwipeRefresh(paramsMap, 1);

            //加载操作
            searchSupervisionList.setOnScrollListener(new AbsListView.OnScrollListener() {

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
                            && tFtDbEntityList.size() != 0) {
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
            // 处理响应信息
            AckMessage ackMsg = JsonUtil.fromJson(body, AckMessage.class);
            if (ifrefresh) {
                //tFtSjEntityList = setMessage(ackMsg);
                ifrefresh = false;
            }
            tFtDbEntityList.addAll(setMessage(ackMsg));
        }
    }

    /**
     * 设置事件信息
     *
     * @param ackMsg
     * @return
     */
    private List<TFtDbEntity> setMessage(AckMessage ackMsg) {
        pList = new ArrayList<TFtDbEntity>();
        if (ackMsg != null) {
            if (AckMessage.SUCCESS.equals(ackMsg.getAckCode())) {
                List<Object> list  = (List<Object>) ackMsg.getData();
                if (list != null && list.size()>0) {
                    for (int i = 0; i < list.size(); i++) {
                        //先将获取的Object对象转成String
                        String entityStr = JsonUtil.toJson(list.get(i));
                        TFtDbEntity tFtDbEntity = JsonUtil.fromJson(entityStr.toLowerCase(), TFtDbEntity.class);
                        pList.add(tFtDbEntity);
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
    @OnItemClick(R.id.search_supervision_list)
    public void OnItemClick(int position) {
        tFtDbEntity =  (TFtDbEntity) adapter.getItem(position);
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("tFtDbEntity", tFtDbEntity);
        intent.setClass(this, SuperVisionDetailActivity.class);
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
        return true;
    }

}

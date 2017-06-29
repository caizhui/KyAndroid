package com.ky.kyandroid.activity.msg;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ky.kyandroid.Constants;
import com.ky.kyandroid.R;
import com.ky.kyandroid.activity.LoginActivity;
import com.ky.kyandroid.adapter.MsgNoticeListAdapter;
import com.ky.kyandroid.bean.AckMessage;
import com.ky.kyandroid.bean.NetWorkConnection;
import com.ky.kyandroid.bean.PageBean;
import com.ky.kyandroid.entity.MsgNoticeEntity;
import com.ky.kyandroid.util.JsonUtil;
import com.ky.kyandroid.util.OkHttpUtil;
import com.ky.kyandroid.util.SpUtil;
import com.ky.kyandroid.util.StringUtils;
import com.ky.kyandroid.util.SweetAlertDialogUtil;
import com.ky.kyandroid.util.SwipeRefreshUtil;
import com.ky.kyandroid.view.ListViewTask;
import com.ky.kyandroid.view.SlideView;
import com.solidfire.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by msi on 2017/6/26.
 */
public class MsgNoticeActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * TAG
     */
    static final String TAG = "MsgNoticeActivity";

    /**
     * 返回
     */
    @BindView(R.id.toolbar_back)
    ImageView toolbar_back;

    /**
     * 菜单栏
     */
    @BindView(R.id.toolbar_layout)
    RelativeLayout toolbar_layout;

    /**
     * 菜单栏标题
     */
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;

    /**
     * 菜单栏标题(右侧信息)
     */
    @BindView(R.id.toolbar_count)
    TextView toolbar_count;

    /**
     * 右边按钮
     */
    @BindView(R.id.toolbar_right_btn)
    FrameLayout toolbar_right_btn;

    /**
     * 右边按钮图片
     */
    @BindView(R.id.toolbar_right_img)
    ImageView toolbar_right_img;

    /**
     * 右边按钮文本
     */
    @BindView(R.id.toolbar_right_tv)
    TextView toolbar_right_tv;

    /**
     * 下拉刷新视图
     */
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;

    /**
     * 加载列表foot
     */
    private LinearLayout list_view_foot;
    /**
     * 网路工具类
     */
    private NetWorkConnection netWorkConnection;
    /**
     * 下拉刷新容器
     */
    private SwipeRefreshUtil swipeRefreshUtil;
    /**
     * 数据加载
     */
    private List<?> dataList;
    /**
     * 分页信息
     */
    private PageBean pageBean;
    /**
     * 默认查询条目数/每页
     */
    private String pageSize = "15";
    /**
     * 任务消息adapter
     */
    private MsgNoticeListAdapter msgAdapter;
    /**
     * SharedPreferences
     */
    private SharedPreferences sp;
    /*** 弹出框架工具类 */
    private SweetAlertDialogUtil sweetAlertDialogUtil;
    /**
     * 查询参数
     */
    private Map<String, String> paramsMap = null;
    /**
     * mPopupWindow
     */
    private PopupWindow mPopupWindow;
    // 发送人,保存所在部门,发送时间,事件名称,消息类型,内容
    private TextView tv_msg_fsr_mc, tv_msg_fssj,tv_msg_fsr_bm, tv_msg_sj_text, tv_msg_sj_mc, tv_msg_type, tv_msg_content;
    /**
     * ListViewTask
     */
    private ListViewTask list_view;
    /**
     * 任务滑动
     */
    private SlideView taskSlideView;

    SlideView.OnSlideListener onSlideListener = new SlideView.OnSlideListener() {
        @Override
        public void onSlide(View view, int status) {
            if (taskSlideView != null && taskSlideView != view) {
                taskSlideView.shrink();
            }
            if (status == SLIDE_STATUS_ON) {
                taskSlideView = (SlideView) view;
            }
        }
    };


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // 提示信息
            String message = String.valueOf(msg.obj == null ? "系统繁忙,请稍后再试" : msg.obj);
            switch (msg.what) {
                // 加载失败
                case 1:
                    Log.i(TAG, "刷新操作...");
                    swipeRefreshUtil.dismissRefreshing();
                    if (handleResponseData(message)) {
                        notifyListViewData(false);
                        Toast.makeText(MsgNoticeActivity.this, "刷新成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MsgNoticeActivity.this, "刷新失败", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(MsgNoticeActivity.this, message, Toast.LENGTH_SHORT).show();
                    break;
                // 初始化跳转
                case 7:
                    Log.i(TAG, "初始化成功...");
                    swipeRefreshUtil.dismissRefreshing();
                    if (handleResponseData(message)) {
                        notifyListViewData(false);
                    } else {
                        Toast.makeText(MsgNoticeActivity.this, "查询不到符合条件记录", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msgnotice_list);
        ButterKnife.bind(this);
        // 初始化视图
        InitViewAndEvent();
        // 获取参数Bundle
        initBundle();
        // 初始化Toolbar
        initToolbar();
        // 初始信息显示
        initPopMenuView();
        // 初始化其他
        initListView();
        // 初始化数据
        initData();
        Log.i(TAG, "createView结束...");
    }

    /**
     * 初始化视图与事件
     */
    void InitViewAndEvent() {
        sp = SpUtil.getSharePerference(this);
        netWorkConnection = new NetWorkConnection(this);
        sweetAlertDialogUtil = new SweetAlertDialogUtil(MsgNoticeActivity.this);
    }

    /**
     * 获取参数Bundle
     */
    void initBundle() {
        paramsMap = new HashMap<String, String>();
        // 查询自己的消息
        paramsMap.put("userId", sp.getString(LoginActivity.USER_ID, ""));
        // 设置标题及颜色
        toolbar_title.setText("消息列表");
        toolbar_layout.setBackgroundColor(Color.parseColor("#A4C639"));
        swipeRefreshUtil = new SwipeRefreshUtil(swipeRefreshLayout, Constants.SERVICE_NOTICE_LIST_HADLE, mHandler);
        // 上拉刷新初始化
        swipeRefreshUtil.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                paramsMap.put("currentPage", "1");
                paramsMap.put("pageSize", pageSize);
                // 打开圈圈
                swipeRefreshLayout.setRefreshing(true);
                swipeRefreshUtil.refreshSetDate(paramsMap, 1);
            }
        });
    }

    /**
     * 初始化标题栏
     */
    void initToolbar() {
        /** 添加点击事件 **/
        toolbar_back.setOnClickListener(this);
    }

    /**
     * 初始化登出菜单
     */
    void initPopMenuView() {
        /**************** 消息提醒菜单项 ******************/
        View mPopView = LayoutInflater.from(this).inflate(R.layout.pop_info, null);
        tv_msg_fsr_mc = (TextView) mPopView.findViewById(R.id.tv_msg_fsr_mc);
        tv_msg_fsr_bm = (TextView) mPopView.findViewById(R.id.tv_msg_fsr_bm);
        tv_msg_fssj = (TextView) mPopView.findViewById(R.id.tv_msg_fssj);
        tv_msg_sj_text = (TextView) mPopView.findViewById(R.id.tv_msg_sj_text);
        tv_msg_sj_mc = (TextView) mPopView.findViewById(R.id.tv_msg_sj_mc);
        tv_msg_type = (TextView) mPopView.findViewById(R.id.tv_msg_type);
        tv_msg_content = (TextView) mPopView.findViewById(R.id.tv_msg_content);
        View layout_confirm = mPopView.findViewById(R.id.layout_confirm);
        // 初始popupWindow对象
        mPopupWindow = new PopupWindow(mPopView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        // 取消操作
        layout_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });
    }


    /**
     * 初始化其他
     */
    void initListView() {
        list_view = (ListViewTask) findViewById(R.id.list_view);
        // 加载“正在加载”布局文件
        list_view_foot = (LinearLayout) getLayoutInflater().inflate(R.layout.lv_item_jiazai, null);
        list_view.addFooterView(list_view_foot, null, false);
        list_view.setSelector(getResources().getDrawable(R.drawable.item_selector_default));
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view,
                                    int position, long arg3) {
                MsgNoticeEntity entity = (MsgNoticeEntity) adapter.getItemAtPosition(position);
                if (entity != null) {
                    View childView = adapter.getChildAt(position);

                    // 发送人,保存所在部门,发送时间,事件名称,消息类型,内容
                    tv_msg_fsr_mc.setText(entity.getFsr());
                    tv_msg_fsr_bm.setText(entity.getFsbmmc());
                    tv_msg_fssj.setText(entity.getFssj());
                    if ("1".equals(entity.getLx())) {
                        tv_msg_sj_text.setText("事件名称:");
                    } else {
                        tv_msg_sj_text.setText("督办名称:");
                    }
                    tv_msg_sj_mc.setText(entity.getSjmc());
                    tv_msg_type.setText("1".equals(entity.getLx()) ? "事件处理" : "督办处理");
                    tv_msg_content.setText("     " + entity.getNr());
                    showPopMenu();
                    if (StringUtils.isBlank(entity.getYdsj())) {
                        updateTaskState(entity.getId(), "czlx", "readed");
                    }
                }
            }
        });

        //滚动监听
        list_view.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        // 判断滚动到底部
                        if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                            list_view_foot.setVisibility(View.VISIBLE);
                            if (pageBean.getTotal() < pageBean.getPageSize()) {
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
        // 设值适配器
        msgAdapter = new MsgNoticeListAdapter(this, onSlideListener);
        list_view.setAdapter(msgAdapter);
    }

    /**
     * 初始化数据
     */
    void initData() {
        if (netWorkConnection.isWIFIConnection()) {
            list_view_foot.setVisibility(View.GONE);
            paramsMap.put("currentPage", "1");
            paramsMap.put("pageSize", pageSize);
            swipeRefreshUtil.showRefreshing();
            swipeRefreshUtil.refreshSetDate(paramsMap, 7);
        } else {
            Toast.makeText(this, "WIFI不可用,请确认网络连接", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 初始化页脚
     */
    void initListFoot(String message, int display) {
        ProgressBar foot_bar = (ProgressBar) list_view_foot.findViewById(R.id.progressBar);
        TextView foot_title = (TextView) list_view_foot.findViewById(R.id.foot_title);
        if (StringUtils.isNotBlank(message)) {
            foot_title.setText(message);
        }
        foot_bar.setVisibility(display);
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
     * 更改已读状态
     */
    void updateTaskState(String id, String fileName, String fileValue) {
        if (!StringUtils.isBlank(id)) {
            Map<String, String> requestMap = new HashMap<String, String>();
            requestMap.put("id", id);
            requestMap.put(fileName, fileValue);
            Log.i(TAG, "更新: " + fileName + "=" + fileValue);
            OkHttpUtil.sendRequest(Constants.SERVICE_NOTICE_EDIT_HADLE, requestMap, new Callback() {
                @Override
                public void onResponse(Call arg0, Response response) throws IOException {
                    Log.i("MsgNoticeListAdapter", response.isSuccessful() ? "成功" : "失败");
                }

                @Override
                public void onFailure(Call arg0, IOException arg1) {
                }
            });
        }
    }

    /**
     * 显示pop菜单
     */
    void showPopMenu() {
        //mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
        mPopupWindow.showAtLocation(swipeRefreshLayout, Gravity.CENTER, 0, 0);
        mPopupWindow.setAnimationStyle(R.style.pop_menu2);
        mPopupWindow.setOutsideTouchable(false);
        mPopupWindow.setFocusable(true);
        mPopupWindow.update();
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
                    if(null != pageBean){
                        List<?> dataL = pageBean.getDataList();
                        toolbar_count.setText("(共" + pageBean.getTotal() + "条)");
                        if (dataL != null && dataL.size() > 0) {
                            // 响应字符串
                            String resultList = JsonUtil.toJson(dataL);
                            dataList = (List<MsgNoticeEntity>) JsonUtil.fromJson(resultList, new TypeToken<List<MsgNoticeEntity>>() {
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
    void notifyListViewData(boolean isAdd) {
        List<MsgNoticeEntity> entityList = (List<MsgNoticeEntity>) dataList;
        // 房屋栋
        if (isAdd) {
            // 追加列表
            msgAdapter.addDataSetChanged(entityList);
        } else {
            // 刷新列表
            msgAdapter.notifyDataSetChanged(entityList);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /** 返回键 **/
            case R.id.toolbar_back:
                onBackPressed();
                break;
            default:
                break;
        }
    }
}

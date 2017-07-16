package com.ky.kyandroid.activity.msg;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.ky.kyandroid.entity.TFtSjYqsqEntity;
import com.ky.kyandroid.util.DateTimePickDialogUtil;
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
     * 延期层(默认隐藏)
     */
    private LinearLayout lx3_layout;

    /**
     * 延期层下字段列表 - 申请原因,原处理时限,变化内容,展示文本,
     */
    private TextView tv_msg_yqyy_mc,tv_msg_yclsx,tv_sfty_detial,tv_msg_change_text,tv_detail;

    /**
     * 延期时间（点击同意时出现）,不同意原因（点不同意时出现）
     */
    private EditText et_yqsx,et_btyyy;

    /**
     * 延期层下字段列表 - 是否同意Radio分组制件
     */
    private RadioGroup rg_sfty_btnG;

    /**
     * 不同意 或 同意
     */
    private RadioButton rd_btn_bty,rd_btn_ty;

    /**
     * ListViewTask
     */
    private ListViewTask list_view;
    /**
     * 任务滑动
     */
    private SlideView taskSlideView;

    /**
     * 点击选中的实体
     */
    private MsgNoticeEntity adaterEntity;

    /**
     * 是否同意 1 同意 0 不同意 -1 其他选项
     */
    private int agreedOrNot = -1;

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
                // 默认处理
                case 0:
                    sweetAlertDialogUtil.dismissAlertDialog();
                    Toast.makeText(MsgNoticeActivity.this, message, Toast.LENGTH_SHORT).show();
                    break;
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
                // 延期处理跳转
                case 8:
                    Log.i(TAG, "延期处理...");
                    swipeRefreshUtil.dismissRefreshing();
                    if (handleResponseExtensionData(message)) {
                        Toast.makeText(MsgNoticeActivity.this, "操作成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MsgNoticeActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                // 刷新阅读状态
                case 9:
                    Log.i(TAG, "刷新阅读状态操作...");
                    //toolbar_count.setText("(共" + msgAdapter.getList().size() + "条)");
                    msgAdapter.notifyDataSetChanged();
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
        initViewAndEvent();
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
    void initViewAndEvent() {
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
        //toolbar_layout.setBackgroundColor(Color.parseColor("#A4C639"));
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
     * 初始化信息提示
     */
    void initPopMenuView() {
        /**************** 消息提醒菜单项 ******************/
        View mPopView = LayoutInflater.from(this).inflate(R.layout.pop_info, null);
        tv_msg_fsr_mc = mPopView.findViewById(R.id.tv_msg_fsr_mc);
        tv_msg_fsr_bm = mPopView.findViewById(R.id.tv_msg_fsr_bm);
        tv_msg_fssj = mPopView.findViewById(R.id.tv_msg_fssj);
        tv_msg_sj_text = mPopView.findViewById(R.id.tv_msg_sj_text);
        tv_msg_sj_mc = mPopView.findViewById(R.id.tv_msg_sj_mc);
        tv_msg_type = mPopView.findViewById(R.id.tv_msg_type);
        tv_msg_content = mPopView.findViewById(R.id.tv_msg_content);
        /******************** 延期消息类控件初始化*****************************/
        lx3_layout = mPopView.findViewById(R.id.lx3_layout);
        tv_msg_yqyy_mc = mPopView.findViewById(R.id.tv_msg_yqyy_mc);
        tv_msg_yclsx = mPopView.findViewById(R.id.tv_msg_yclsx);
        tv_sfty_detial = mPopView.findViewById(R.id.tv_sfty_detial);
        tv_msg_change_text = mPopView.findViewById(R.id.tv_msg_change_text);
        tv_detail = mPopView.findViewById(R.id.tv_detail);
        et_yqsx = mPopView.findViewById(R.id.et_yqsx);
        // 设置时间制件
        et_yqsx.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (v.getId()) {
                    /** 点击发生时间控件 **/
                    case R.id.et_yqsx:
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            et_yqsx.clearFocus();
                            DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(MsgNoticeActivity.this, "");
                            dateTimePicKDialog.dateTimePicKDialog(et_yqsx);
                        }
                        break;
                }
                return false;
            }
        });
        et_btyyy = mPopView.findViewById(R.id.et_btyyy);
        // 同意或同意列表
        rg_sfty_btnG = mPopView.findViewById(R.id.rg_sfty_btnG);
        rd_btn_bty = mPopView.findViewById(R.id.rd_btn_bty);
        rd_btn_ty = mPopView.findViewById(R.id.rd_btn_ty);
        // 设置监听
        rg_sfty_btnG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                if(checkedId==rd_btn_bty.getId()){
                    /** 不同意操作 **/
                    tv_msg_change_text.setText("不同意原因:");
                    et_yqsx.setVisibility(View.GONE);
                    et_btyyy.setVisibility(View.VISIBLE);
                    et_yqsx.setText("");
                    et_btyyy.setText("");
                    agreedOrNot = 0;
                }else if(checkedId==rd_btn_ty.getId()){
                    /** 同意操作 **/
                    tv_msg_change_text.setText("新处理时限:");
                    et_yqsx.setVisibility(View.VISIBLE);
                    et_btyyy.setVisibility(View.GONE);
                    et_yqsx.setText("");
                    et_btyyy.setText("");
                    agreedOrNot = 1;
                }else{
                    agreedOrNot = -1;
                }
            }
        });

        View layout_confirm = mPopView.findViewById(R.id.layout_confirm);
        // 初始popupWindow对象
        mPopupWindow = new PopupWindow(mPopView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        // 取消操作
        layout_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (agreedOrNot > -1){
                    boolean flag = true;
                    if (agreedOrNot == 0){
                        // 不同意验证不同意原因是否为空
                        if (StringUtils.isBlank(et_btyyy.getText())){
                            flag = false;
                            Toast.makeText(MsgNoticeActivity.this, "原因不能为空", Toast.LENGTH_SHORT).show();
                        }else{
                            TFtSjYqsqEntity yqsqEntity = adaterEntity.getYqsqEntity();
                            if(yqsqEntity != null){
                                yqsqEntity.setBtyyy(et_btyyy.getText().toString());
                                yqsqEntity.setSfty("0");
                            }
                        }
                    }else if(agreedOrNot == 1){
                        // 同意验证最新处理时限是否为空
                        if (StringUtils.isBlank(et_yqsx.getText())){
                            flag = false;
                            Toast.makeText(MsgNoticeActivity.this, "新处理时限不能为空", Toast.LENGTH_SHORT).show();
                        }else{
                            TFtSjYqsqEntity yqsqEntity = adaterEntity.getYqsqEntity();
                            if(yqsqEntity != null){
                                yqsqEntity.setYqsx(et_yqsx.getText().toString());
                                yqsqEntity.setSfty("1");
                            }
                        }
                    }
                    if(flag){
                        mPopupWindow.dismiss();
                        // 处理列表
                        updateMsgExtension();
                    }
                }else{
                    mPopupWindow.dismiss();
                }
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
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg3) {
                agreedOrNot = -1;
                adaterEntity = (MsgNoticeEntity) adapter.getItemAtPosition(position);
                if (adaterEntity != null) {
                    // 发送人,保存所在部门,发送时间,事件名称,消息类型,内容
                    tv_msg_fsr_mc.setText(adaterEntity.getFsr());
                    tv_msg_fsr_bm.setText(adaterEntity.getFsbmmc());
                    tv_msg_fssj.setText(adaterEntity.getFssj());
                    lx3_layout.setVisibility(View.GONE);
                    /*********** 根据类型做不同判断  *************/
                    if ("1".equals(adaterEntity.getLx())) {
                        tv_msg_sj_text.setText("事件名称:");
                        tv_msg_type.setText("事件处理");
                    } else if("2".equals(adaterEntity.getLx())) {
                        tv_msg_sj_text.setText("督办名称:");
                        tv_msg_type.setText("督办处理");
                    }else if("3".equals(adaterEntity.getLx())){
                        tv_msg_sj_text.setText("任务内容:");
                        tv_msg_type.setText("申请延期");
                        et_yqsx.setText("");
                        et_btyyy.setText("");
                        // 全部先隐藏
                        tv_sfty_detial.setVisibility(View.GONE);
                        rg_sfty_btnG.setVisibility(View.GONE);
                        tv_detail.setVisibility(View.GONE);
                        et_yqsx.setVisibility(View.GONE);
                        et_btyyy.setVisibility(View.GONE);
                        TFtSjYqsqEntity yqsqEntity = adaterEntity.getYqsqEntity();
                        if(yqsqEntity != null){
                            /**展示延期层 ***/
                            lx3_layout.setVisibility(View.VISIBLE);
                            // 申请原因
                            tv_msg_yqyy_mc.setText(yqsqEntity.getYqyy());
                            // 原处理时限
                            tv_msg_yclsx.setText(yqsqEntity.getYclsx());
                            // 根据是否同意去筛选布局
                            if(StringUtils.isBlank(yqsqEntity.getSfty())){
                                // 为空显示编辑布局
                                rg_sfty_btnG.setVisibility(View.VISIBLE); // 显示radio选择
                                rd_btn_bty.setChecked(true); // 默认选中不同意项
                                agreedOrNot = 0;
                                tv_msg_change_text.setText("不同意原因:");
                                et_btyyy.setVisibility(View.VISIBLE);
                            }else{
                                // 不为空显示详情布局
                                tv_sfty_detial.setVisibility(View.VISIBLE); // 翻译同意
                                tv_detail.setVisibility(View.VISIBLE);
                                if ("1".equals(yqsqEntity.getSfty())){
                                    tv_sfty_detial.setText("同意");
                                    tv_msg_change_text.setText("新处理时限:");
                                    // 设值延期时限
                                    tv_detail.setText(yqsqEntity.getYqsx());
                                }else{
                                    tv_sfty_detial.setText("不同意");
                                    tv_msg_change_text.setText("不同意原因:");
                                    // 设值不同意原因
                                    tv_detail.setText(yqsqEntity.getBtyyy());
                                }
                            }
                        }
                    }
                    tv_msg_sj_mc.setText(adaterEntity.getSjmc());
                    tv_msg_content.setText("          " + adaterEntity.getNr());
                    showPopMenu();

                    /******* 更改阅读状态 ********/
                    try {
                        Object up_entity = msgAdapter.getList().get(position);
                        if (up_entity != null){
                            MsgNoticeEntity msg = (MsgNoticeEntity)up_entity;
                            if (StringUtils.isBlank(msg.getYdsj())){
                                updateTaskState(adaterEntity.getId(), "czlx", "readed");
                            }
                            // 临时改阅读状态使用设置与否真更新状态无关
                            msg.setYdsj("readed");
                        }
                    } catch (Exception e) {
                        Log.i(TAG,"滑动未停止,修改失效...");
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
        // 设值适配器
        msgAdapter = new MsgNoticeListAdapter(this,mHandler,onSlideListener);
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
    private void initListFoot(String message, int display) {
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
                    if (response.isSuccessful()){
                        // 刷新阅读状态
                        mHandler.sendEmptyMessage(9);
                    }
                }

                @Override
                public void onFailure(Call arg0, IOException arg1) {
                    mHandler.sendEmptyMessage(0);
                }
            });
        }
    }


    /**
     * 消息延期申请处理
     */
    void updateMsgExtension() {
        if (adaterEntity != null) {
            sweetAlertDialogUtil.loadAlertDialog();
            Map<String, String> requestMap = new HashMap<String, String>();
            requestMap.put("userId", sp.getString(LoginActivity.USER_ID,""));
            requestMap.put("msgid", adaterEntity.getId());
            requestMap.put("yqsqStr", JsonUtil.toJson(adaterEntity.getYqsqEntity()));
            Log.i(TAG, "消息延期处理: requestMap>>" + requestMap.toString());
            OkHttpUtil.sendRequest(Constants.SERVICE_NOTICE_EXTENSION_HADLE, requestMap, new Callback() {
                @Override
                public void onResponse(Call arg0, Response response) throws IOException {
                    Log.i("MsgNoticeListAdapter", response.isSuccessful() ? "成功" : "失败");
                    Message msg = new Message();
                    sweetAlertDialogUtil.dismissAlertDialog();
                    if (response.isSuccessful()) {
                        msg.what = 8;
                        msg.obj = response.body().string();
                    } else {
                        msg.what = 0;
                        msg.obj = "网络异常,请确认网络情况";
                    }
                    mHandler.sendMessage(msg);
                }

                @Override
                public void onFailure(Call arg0, IOException arg1) {
                    mHandler.sendEmptyMessage(0);
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
     * 处理延期响应数据
     */
    public boolean handleResponseExtensionData(String body) {
        boolean flag = false;
        if (StringUtils.isBlank(body)) {
            Log.i(TAG, "解释响应body失败...");
        } else {
            // 处理响应信息
            AckMessage ackMsg = JsonUtil.fromJson(body, AckMessage.class);
            if (ackMsg != null) {
                if (AckMessage.SUCCESS.equals(ackMsg.getAckCode())) {
                    Log.i(TAG, "延期数据解释成功...");
                    Object entity = ackMsg.getEntity();
                    if (entity != null) {
                        String dataStr = JsonUtil.toJson(entity);
                        MsgNoticeEntity msgEntity = JsonUtil.fromJson(dataStr, MsgNoticeEntity.class);
                        if (msgEntity != null) {
                            flag = true;
                            // 更新数据
                            updateListDataStatie(msgEntity);
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
     * @param msg
     */
    private void updateListDataStatie(MsgNoticeEntity msg) {
        List<MsgNoticeEntity> entityList = msgAdapter.getList();
        if (entityList != null) {
            for (int i = 0; i < entityList.size(); i++) {
                MsgNoticeEntity entity = entityList.get(i);
                if (entity.getId().equals(msg.getId())) {
                    entityList.remove(i);
                    entityList.add(i,msg);
                    break;
                }
            }
            msgAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 加载数据
     */
    private void notifyListViewData(boolean isAdd) {
        List<MsgNoticeEntity> entityList = (List<MsgNoticeEntity>) dataList;
        // 消息列表
        if (isAdd) {
            // 追加列表
            msgAdapter.addDataSetChanged(entityList);
        } else {
            // 刷新列表
            msgAdapter.notifyDataSetChanged(entityList);
        }
        //toolbar_count.setText("(共" + msgAdapter.getList().size() + "条)");
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

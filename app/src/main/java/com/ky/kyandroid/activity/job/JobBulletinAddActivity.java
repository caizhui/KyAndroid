package com.ky.kyandroid.activity.job;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.ky.kyandroid.Constants;
import com.ky.kyandroid.R;
import com.ky.kyandroid.adapter.GroupAdapter;
import com.ky.kyandroid.adapter.JobGroupAdapter;
import com.ky.kyandroid.bean.AckMessage;
import com.ky.kyandroid.bean.NetWorkConnection;
import com.ky.kyandroid.db.dao.DescEntityDao;
import com.ky.kyandroid.entity.AclOrgEntity;
import com.ky.kyandroid.entity.OrgAndUserEntity;
import com.ky.kyandroid.entity.TFtAppReportEntity;
import com.ky.kyandroid.entity.UserEntity;
import com.ky.kyandroid.util.DateTimePickDialogUtil;
import com.ky.kyandroid.util.JsonUtil;
import com.ky.kyandroid.util.OkHttpUtil;
import com.ky.kyandroid.util.SpUtil;
import com.ky.kyandroid.util.SweetAlertDialogUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Caizhui on 2017/8/15.
 * 工作简报新增页面
 */

public class JobBulletinAddActivity extends AppCompatActivity {

    /**
     * 标题栏左边按钮
     */
    @BindView(R.id.left_btn)
    ImageView leftBtn;

    /**
     * 标题栏中间标题
     */
    @BindView(R.id.center_text)
    TextView centerText;

    /**
     * 标题栏右边按钮
     */
    @BindView(R.id.right_btn)
    Button rightBtn;

    /**
     * 标题
     */
    @BindView(R.id.job_name_edt)
    EditText jobNameEdt;
    /**
     * 时间
     */
    @BindView(R.id.job_time_edt)
    EditText jobTimeEdt;
    /**
    * 上报部门
     */
    @BindView(R.id.job_departmen_edt)
    TextView jobDepartmenEdt;
    @BindView(R.id.job_departmen_layout)
    LinearLayout jobDepartmenLayout;

    /**
     * 全局LinearLayout
     */
    @BindView(R.id.linear_evententry)
    LinearLayout linearEvententry;
    /**
     * 内容
     */
    @BindView(R.id.job_content_edt)
    EditText jobContentEdt;
    /**
     * 上报领导
     */
    @BindView(R.id.reporting_leadership_btn)
    Button reportingLeadershipBtn;

    @BindView(R.id.one_scroll)
    ScrollView one_scroll;


    View showPupWindow = null; // 选择区域的view

    /**
     * 一级菜单名称数组
     **/
    String[][] GroupNameArray;
    /**
     * 二级菜单名称数组
     **/
    String[][] childNameArray;
    /**
     * 三级菜单名称数组
     **/
    String[][] child2NameArray;

    ListView groupListView = null;
    ListView childListView = null;
    ListView  threeListView = null ;

    TranslateAnimation animation;// 出现的动画效果
    // 屏幕的宽高
    public static int screen_width = 0;
    public static int screen_height = 0;

    private boolean[] tabStateArr = new boolean[4];// 标记tab的选中状态，方便设置

    PopupWindow mPopupWindow = null;

    public DescEntityDao descEntityDao;

    /**
     * 上报部门
     */
    private String spinnerType;

    Button btnCancel, btnConfirm;

    TextView btntext;

    JobGroupAdapter groupAdapter = null;

    GroupAdapter childAdapter = null;

    /**
     * 网路工具类
     */
    private NetWorkConnection netWorkConnection;

    /**
     * 弹出框工具类
     */
    private SweetAlertDialogUtil sweetAlertDialogUtil;

    private String userId;

    /**
     * SharedPreferences
     */
    private SharedPreferences sp;

    /**
     * 所有的部门
     */
    List<AclOrgEntity> orgList;


    /**
     * 所有的用户
     */
    List<UserEntity> userList;

    /**
     * 部门下的用户
     */
    List<UserEntity> userEntityList;


    private TFtAppReportEntity tFtAppReportEntity;


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // 提示信息
            String message = String.valueOf(msg.obj == null ? "系统繁忙,请稍后再试"
                    : msg.obj);
            switch (msg.what) {
                // 失败
                case 0:
                    sweetAlertDialogUtil.dismissAlertDialog();
                    Toast.makeText(JobBulletinAddActivity.this, message, Toast.LENGTH_SHORT).show();
                    break;
                // 成功跳转
                case 1:
                    HandleDescData(message);
                    break;
                // 成功跳转
                case 2:
                    AckMessage ackMessage = JsonUtil.fromJson(message, AckMessage.class);
                    if(ackMessage!=null){
                        if(AckMessage.SUCCESS.equals(ackMessage.getAckCode())){
                            Intent intent =new Intent(JobBulletinAddActivity.this,JobBullentinListActivity.class);
                            intent.putExtra("businessType", "initList");
                            startActivity(intent);
                            Toast.makeText(JobBulletinAddActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(JobBulletinAddActivity.this,"保存失败",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(JobBulletinAddActivity.this,"保存失败",Toast.LENGTH_SHORT).show();
                    }
                    sweetAlertDialogUtil.dismissAlertDialog();
                    break;
            }
        }
    };



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobjy_add);
        ButterKnife.bind(this);
        descEntityDao = new DescEntityDao();
        initToolbar();
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm); // 获取手机屏幕的大小
        screen_width = dm.widthPixels;
        screen_height = dm.heightPixels;
        dbAddData();
    }

    /**
     * 初始化标题栏e
     */
    private void initToolbar() {

        /** 设置toolbar标题 **/
        centerText.setText("工作简报录入");

        sp = SpUtil.getSharePerference(this);
        // 初始化网络工具
        netWorkConnection = new NetWorkConnection(this);
        userId = sp.getString("userId", "");
        sweetAlertDialogUtil = new SweetAlertDialogUtil(JobBulletinAddActivity.this);

    }

    public void dbAddData(){
        final Message msg = new Message();
        if (netWorkConnection.isWIFIConnection()) {
            msg.what = 0;
            // 参数列表 - 账号、密码（加密）
            Map<String, String> paramsMap = new HashMap<String, String>();
            paramsMap.put("userId", userId);
            // 发送请求
            OkHttpUtil.sendRequest(Constants.SERVICE_QUERYORGANDUSER, paramsMap, new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    mHandler.sendEmptyMessage(0);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        msg.what = 1;
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

    /**
     * 字典处理返回的数据
     * @param response
     */
    public void HandleDescData(String response){
        AckMessage ackMessage = JsonUtil.fromJson(response, AckMessage.class);
        if(AckMessage.SUCCESS.equals(ackMessage.getAckCode())) {
            String  objectJson = JsonUtil.toJson(ackMessage.getEntity());
            OrgAndUserEntity object = JsonUtil.fromJson(objectJson, OrgAndUserEntity.class);
            //成功后操作 - 解释数据
            orgList  = object.getOrgList();
            userList = object.getUserList();
            sweetAlertDialogUtil.dismissAlertDialog();
        }else if(Constants.FAILURE.equals(ackMessage.getAckCode())){
            //失败后操作
        }

    }

    @OnTouch({R.id.job_time_edt})
    public boolean OnTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            /** 点击发生时间控件 **/
            case R.id.job_time_edt:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    jobTimeEdt.clearFocus();
                    DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
                            JobBulletinAddActivity.this, "");
                    dateTimePicKDialog.dateTimePicKDialog(jobTimeEdt);
                    return false;
                }
                break;
        }
        return false;
    }

    @OnClick({R.id.left_btn,R.id.job_departmen_layout})
    public void onClick(View v) {
        int[] location = new int[2];
        // 获取控件在屏幕中的位置,方便展示Popupwindow
        jobDepartmenLayout.getLocationOnScreen(location);
        switch (v.getId()) {
            /** 返回键 **/
            case R.id.left_btn:
                onBackPressed();
                break;
            // 到场部门
            case R.id.job_departmen_layout:
                spinnerType = "sjly";
                animation = null;
                // location[1] 改成 0
                animation = new TranslateAnimation(0, 0, -700, 0);
                animation.setDuration(500);
                //List<CodeValue> codeValueList = descEntityDao.queryPidList(spinnerType);
                /** 一级菜单名称数组 **/
                if (orgList != null && orgList.size() > 0) {
                    GroupNameArray = new String[orgList.size()][];
                    for (int i = 0; i < orgList.size(); i++) {
                        String[] cg = new String[2];
                        // 0 是未选中,1 是选中
                        cg[0] = "0";
                        cg[1] = orgList.get(i).getOrgName();
                        GroupNameArray[i] = cg;
                    }
                }
                String  orgCode = orgList.get(0).getId();
                /** 二级菜单名称数组 **/
                if(userList!=null &&  userList.size()>0){
                    userEntityList= new ArrayList<UserEntity>();
                    for(int i=0;i<userList.size();i++){
                        if(orgCode.equals(userList.get(i).getOrgId())){
                            userEntityList.add(userList.get(i));
                        }
                    }
                }
                if (userEntityList != null && userEntityList.size() > 0) {
                    childNameArray = new String[userEntityList.size()][];
                    for (int i = 0; i < userEntityList.size(); i++) {
                        String[] cg = new String[2];
                        // 0 是未选中,1 是选中
                        cg[0] = "0";
                        cg[1] = userEntityList.get(i).getName();
                        childNameArray[i] = cg;
                    }
                }
                showPupupWindow();
                break;
        }
    }

    public void saveData(View v){
        String message="";
        if(tFtAppReportEntity==null){
            tFtAppReportEntity = new TFtAppReportEntity();
        }
        if("".equals(jobNameEdt.getText().toString())){
            message+="标题不能为空\n";
        }else{
            tFtAppReportEntity.setTitle(jobNameEdt.getText().toString());
        }
        if("".equals(jobTimeEdt.getText().toString())){
            message+="时间不能为空\n";
        }else{
            tFtAppReportEntity.setRepTime(jobTimeEdt.getText().toString());
        }
        String jobDepartmen="";
        if(!"".equals(jobDepartmenEdt.getText().toString())){
            String[] sbyhs = jobDepartmenEdt.getText().toString().split(",");
            for(int i=0;i<sbyhs.length;i++){
                for(int j = 0;j<userList.size();j++){
                    if(sbyhs[i].equals(userList.get(j).getName())){
                        jobDepartmen+= userList.get(j).getId()+",";
                    }
                }
            }
            jobDepartmen = jobDepartmen.substring(0,jobDepartmen.length()-1);

        }
        if("".equals(jobDepartmen)){
            message+="上报用户不能为空\n";
        }else{
            tFtAppReportEntity.setRepToUser(jobDepartmen);
        }
        if("".equals(jobContentEdt.getText().toString())){
            message+="内容不能为空\n";
        }else{
            tFtAppReportEntity.setContent(jobContentEdt.getText().toString());
        }
        if("".equals(message)){
            sendData();
        }else{
            Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
        }
    }

    public  void sendData(){
        final Message msg = new Message();
        if (netWorkConnection.isWIFIConnection()) {
            sweetAlertDialogUtil.loadAlertDialog("Loading...");
            msg.what = 0;
            Map<String, String> paramsMap = new HashMap<String, String>();
            // 转成json格式
            String mapJson = JsonUtil.toJson(tFtAppReportEntity);
            paramsMap.put("userId", userId);
            paramsMap.put("TFtAppReport",mapJson);
            // 发送请求
            OkHttpUtil.sendRequest(Constants.SERVICE_SAVEREPORT, paramsMap, new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    mHandler.sendEmptyMessage(0);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        msg.what = 2;
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

    /**
     * 初始化 PopupWindow
     *
     * @param view
     */
    public void initPopuWindow(View view) {
        /* 第一个参数弹出显示view 后两个是窗口大小 */
        mPopupWindow = new PopupWindow(view, screen_width, screen_height);
        /* 设置背景显示 */
        mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.mypop_bg));
        /* 设置触摸外面时消失 */
        // mPopupWindow.setOutsideTouchable(true);
        // 设置动画
        mPopupWindow.setAnimationStyle(R.style.pop_menu);

        mPopupWindow.setTouchable(true);
        /* 设置点击menu以外其他地方以及返回键退出 */
        mPopupWindow.setFocusable(true);
        mPopupWindow.update();
        /**
         * 1.解决再次点击MENU键无反应问题 2.sub_view是PopupWindow的子View
         */
        view.setFocusableInTouchMode(true);
    }

    /**
     * 展示区域选择的对话框
     */
    private void showPupupWindow() {
        showPupWindow = LayoutInflater.from(JobBulletinAddActivity.this).inflate(
                R.layout.bottom_layout, null);
        initPopuWindow(showPupWindow);
        // 初始化三个ListView
        groupListView = showPupWindow.findViewById(R.id.listView1);
        childListView = showPupWindow.findViewById(R.id.listView2);
        threeListView = showPupWindow.findViewById(R.id.listView3);
        threeListView.setVisibility(View.GONE);

        // 初始化点击事件 - 取消
        btnCancel = showPupWindow.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
            }
        });

        // 初始化点击事件 - 确定
        btnConfirm = showPupWindow.findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 填充选择框
                fillListValues();
            }
        });

        // 标题
        btntext = showPupWindow.findViewById(R.id.btn_text);

        // 设置视图 一级菜单
        groupAdapter = new JobGroupAdapter(JobBulletinAddActivity.this, GroupNameArray);
        groupListView.setAdapter(groupAdapter);
        groupAdapter.notifyDataSetChanged();
        groupListView.setOnItemClickListener(new MyItemClick());

        // 设置视图 二级菜单
        childAdapter = new GroupAdapter(JobBulletinAddActivity.this, childNameArray);
        childListView.setAdapter(childAdapter);
        childAdapter.notifyDataSetChanged();

        btntext.setText("上报部门列表");

        mPopupWindow.showAtLocation(linearEvententry, Gravity.CENTER, 0, 0);
    }

    /**
     * 填充列表框
     */
    private void fillListValues() {
        StringBuffer sb = new StringBuffer();
        // 菜单列表项
        List<GroupAdapter> adapterList = new ArrayList<GroupAdapter>();
        //adapterList.add((GroupAdapter) groupListView.getAdapter());
        adapterList.add((GroupAdapter) childListView.getAdapter());

        // 判断是否有选中
        if (checkGroupAdapter(sb, adapterList)) {
            Toast.makeText(JobBulletinAddActivity.this, "请选择任意一项", Toast.LENGTH_LONG).show();
            return;
        } else {
            jobDepartmenEdt.setText(sb.deleteCharAt(sb.length() - 1).toString());
            mPopupWindow.dismiss();
        }
    }

    class MyItemClick implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            groupAdapter.setSelectedPosition(position);
            String[] adCg = (String[]) groupAdapter.getItem(position);
            String orgName = adCg[1];
            String orgCode="";
            for(int i=0;i<orgList.size();i++){
                if(orgName.equals(orgList.get(i).getOrgName())){
                    orgCode = orgList.get(i).getId();
                }
            }
            if(userList!=null &&  userList.size()>0){
                userEntityList= new ArrayList<UserEntity>();
                for(int i=0;i<userList.size();i++){
                    if(orgCode.equals(userList.get(i).getOrgId())){
                        userEntityList.add(userList.get(i));
                    }
                }
            }
            // 获取列表集合
            childNameArray = new String[userEntityList.size()][];
            String[][] groupItems = childAdapter.getmGroupItems();
            boolean flag = true;
            if (userEntityList.size() > 0) {
                for (int i = 0; i < userEntityList.size(); i++) {
                    String[] cg = new String[2];
                    // 0 是未选中,1 是选中
                    cg[0] = "0";
                    cg[1] = userEntityList.get(i).getName();
                    // 如果名称有一个一样就换值不刷新
                    if (groupItems != null && i < groupItems.length) {
                        if (cg[1].equals(groupItems[i][1])) {
                            flag = false;
                            break;
                        }

                    }
                    childNameArray[i] = cg;
                }
            } else {
                flag = true;
            }
            if (flag) {
                childAdapter.setChildData(childNameArray);
            }
            handler.sendEmptyMessage(20);
        }

    }


    private boolean checkGroupAdapter(StringBuffer selectedStr, List<GroupAdapter> adapters) {
        boolean flag = true;
        if (adapters != null && adapters.size() > 0) {
            for (GroupAdapter adap : adapters) {
                String[][] mitems = adap.getmGroupItems();
                for (int i = 0; i < mitems.length; i++) {
                    if ("1".equals(mitems[i][0])) {
                        flag = false;
                        selectedStr.append(mitems[i][1]).append(",");
                    }
                }
            }
        }
        return flag;
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 20:
                    // 刷新菜单列表
                    groupAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }

        }

        ;
    };


    @OnTouch({R.id.one_scroll})
    public boolean OnTouchListener(View v, MotionEvent event) {
        v.getParent().requestDisallowInterceptTouchEvent(true);
        return false;
    }
}

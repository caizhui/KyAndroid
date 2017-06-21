package com.ky.kyandroid.activity.task;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ky.kyandroid.Constants;
import com.ky.kyandroid.R;
import com.ky.kyandroid.adapter.FragmentAdapter;
import com.ky.kyandroid.bean.AckMessage;
import com.ky.kyandroid.bean.NetWorkConnection;
import com.ky.kyandroid.entity.TFtSjDetailEntity;
import com.ky.kyandroid.entity.TFtSjFjEntity;
import com.ky.kyandroid.entity.TFtSjRyEntity;
import com.ky.kyandroid.entity.TaskEntity;
import com.ky.kyandroid.util.JsonUtil;
import com.ky.kyandroid.util.OkHttpUtil;
import com.ky.kyandroid.util.SpUtil;
import com.ky.kyandroid.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * Created by Caizhui on 2017-6-9.
 * 我的任务新增页面
 */

public class TaskAddActivity extends FragmentActivity {


    /**
     * TAG
     */
    private static final String TAG = "TaskAddActivity";

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
     * 显示的fragment_viewpager
     */
    @BindView(R.id.fragment_viewpager)
    public ViewPager fragment_viewpager;


    @BindView(R.id.radiogroup)
    public RadioGroup radiogroup;

    /**
     * 基本信息按钮
     */
    @BindView(R.id.radiobtn_baseinfo)
    public RadioButton radiobtn_baseinfo;

    /**
     * 当事人按钮
     */
    @BindView(R.id.radiobtn_person)
    public RadioButton radiobtn_person;

    /**
     * 附件按钮
     */
    @BindView(R.id.radiobtn_attachment)
    public RadioButton radiobtn_attachment;


    /**
     * 事件录入 - 基本信息
     */
    private TaskFragment_Basic eventEntryAdd_basic;

    /**
     * 事件录入- 当事人
     */
    private TaskFragment_Person eventEntryAdd_person;

    /**
     * 事件录入 - 附件
     */
    private TaskFragment_Attachment eventEntryAdd_attachment;

    private Intent intent;

    /**
     * 临时事件ID;
     */
    private String uuid;

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


    /**
     * type 0：新增 1：修改
     **/
    private String type;

    private TaskEntity taskEntity;

    String userId;

    // 获取事件附件 - 子表信息
    List<TFtSjFjEntity> sjfjList;
    // 人员具体信息
    List<TFtSjRyEntity> sjryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_add);
        ButterKnife.bind(this);
        initEvent();
        userId = sp.getString(USER_ID, "");
        intent = getIntent();
        type = intent.getStringExtra("type");
        taskEntity = (TaskEntity) intent.getSerializableExtra("taskEntity");
        //1表示草稿修改或者查看已经上报的事件信息，则获取传过来的uuid，否则新建
        uuid = taskEntity.getId();
        initToolbar();
        initPageView();
        initViewData();
        //查询我的任务的详细信息
        initOnLineData();
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        sp = SpUtil.getSharePerference(this);
        // 初始化网络工具
        netWorkConnection = new NetWorkConnection(this);
    }

    /**
     * 初始化PageView
     */
    @SuppressWarnings("deprecation")
    private void initPageView() {
        eventEntryAdd_basic = new TaskFragment_Basic(intent);
        eventEntryAdd_person = new TaskFragment_Person();
        eventEntryAdd_attachment = new TaskFragment_Attachment(uuid);
        // 设置Fragment集合
        List<Fragment> fragmList = new ArrayList<Fragment>();
        fragmList.add(eventEntryAdd_basic);
        fragmList.add(eventEntryAdd_person);
        fragmList.add(eventEntryAdd_attachment);

        // 适配器adapter
        FragmentAdapter fragmentAdapter = new FragmentAdapter(
                getSupportFragmentManager(), fragmList);
        // 添加适配器与监听
        fragment_viewpager.setAdapter(fragmentAdapter);
        fragment_viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int index) {
                changeState2RadioButton(index);
            }

            @Override
            public void onPageScrolled(int position, float offset, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    /**
     * @param index
     */
    private void changeState2RadioButton(int index) {
        switch (index) {
            case 0:
                radiobtn_baseinfo.setChecked(true);
                break;
            case 1:
                radiobtn_person.setChecked(true);
                break;
            case 2:
                radiobtn_attachment.setChecked(true);
                break;
        }
    }

    /**
     * 初始化标题栏
     */
    private void initToolbar() {

        /** 设置toolbar标题 **/
        centerText.setText("事件录入信息");

        /** 将右边按钮隐藏*/
        rightBtn.setVisibility(View.INVISIBLE);

        centerText.setText("任务详细信息");
    }

    /**
     * 初始化控件数据
     */
    private void initViewData() {

        // 菜单分组栏
        radiobtn_baseinfo.setChecked(true);

        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radiobtn_baseinfo:
                        Log.i(TAG, "切换到基本信息");
                        fragment_viewpager.setCurrentItem(0);
                        break;
                    case R.id.radiobtn_person:
                        Log.i(TAG, "切换到当事人");
                        fragment_viewpager.setCurrentItem(1);
                        break;
                    case R.id.radiobtn_attachment:
                        Log.i(TAG, "切换到附件");
                        fragment_viewpager.setCurrentItem(2);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // 提示信息
            String message = String.valueOf(msg.obj == null ? "系统繁忙,请稍后再试"
                    : msg.obj);
            switch (msg.what) {
                // 失败
                case 0:
                    Toast.makeText(TaskAddActivity.this, message, Toast.LENGTH_SHORT).show();
                    break;
                // 获取详细信息数据成功
                case 1:
                    handleTransation(message);
                    break;
            }
        }
    };

    /**
     * 获取详细信息之后，将信息分别放入基本信息，当事人，附件页面中
     *
     * @param
     */
    private void handleTransation(String body) {
        if (StringUtils.isBlank(body)) {
        } else {
            // 处理响应信息
            AckMessage ackMsg = JsonUtil.fromJson(body, AckMessage.class);
            if (ackMsg != null) {
                if (AckMessage.SUCCESS.equals(ackMsg.getAckCode())) {
                    Object object = ackMsg.getEntity();
                    //先将获取的Object对象转成String
                    String entityStr = JsonUtil.toJson(object);
                    //先将获取的json象转成实体
                    TFtSjDetailEntity tFtSjDetailEntity = JsonUtil.fromJson(entityStr, TFtSjDetailEntity.class);
                    if (tFtSjDetailEntity != null) {
                        sjryList = tFtSjDetailEntity.getSjryList();
                        sjfjList = tFtSjDetailEntity.getSjfjList();
                        //将当事人信息放在当事人页面
                        eventEntryAdd_person.setTFtSjRyEntityList(sjryList);
                        //将附件信息放在附件页面
                        eventEntryAdd_attachment.setTFtSjFjEntityList(sjfjList, true);
                    }
                }
            }
        }
    }

    /**
     * //查询已经上报详情数据
     */
    public void initOnLineData() {
        final Message msg = new Message();
        msg.what = 0;
        if (netWorkConnection.isWIFIConnection()) {
            Map<String, String> paramsMap = new HashMap<String, String>();
            paramsMap.put("userId", userId);
            paramsMap.put("id", taskEntity.getId());
            // 发送请求
            OkHttpUtil.sendRequest(Constants.SERVICE_DETAIL_EVENT, paramsMap, new Callback() {

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



    @OnClick({R.id.left_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            /** 返回键 **/
            case R.id.left_btn:
                onBackPressed();
                break;
        }
    }

}

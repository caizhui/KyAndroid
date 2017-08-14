package com.ky.kyandroid.activity.evententry;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
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
import com.ky.kyandroid.db.dao.FileEntityDao;
import com.ky.kyandroid.db.dao.TFtSjEntityDao;
import com.ky.kyandroid.db.dao.TFtSjRyEntityDao;
import com.ky.kyandroid.entity.TFtSjDetailEntity;
import com.ky.kyandroid.entity.TFtSjEntity;
import com.ky.kyandroid.entity.TFtSjFjEntity;
import com.ky.kyandroid.entity.TFtSjRyEntity;
import com.ky.kyandroid.util.JsonUtil;
import com.ky.kyandroid.util.OkHttpUtil;
import com.ky.kyandroid.util.SpUtil;
import com.ky.kyandroid.util.StringUtils;
import com.ky.kyandroid.util.SweetAlertDialogUtil;

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
 * 事件录入详情页面
 */

public class EventEntryDetailActivity extends FragmentActivity {


    /**
     * TAG
     */
    private static final String TAG = "EventEntryDetailActivity";

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
    private EventEntryDetail_Basic eventEntryDetail_basic;

    /**
     * 事件录入- 当事人
     */
    private EventEntryDetail_Person eventEntryDetail_person;

    /**
     * 事件录入 - 附件
     */
    private EventEntryDetail_Attachment eventEntryDetail_attachment;

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


    private TFtSjEntityDao tFtSjEntityDao;

    public TFtSjRyEntityDao tFtSjRyEntityDao;

    private FileEntityDao fileEntityDao;

    private TFtSjEntity tFtSjEntity;

    String userId;

    // 获取事件附件 - 子表信息
    List<TFtSjFjEntity> sjfjList;
    // 人员具体信息
    List<TFtSjRyEntity> sjryList;

    //事件跟踪
    List<String> progressList;

    /**
     * 事件Id；
     */
    private String sjId;

    /**
     * 弹出框工具类
     */
    private SweetAlertDialogUtil sweetAlertDialogUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evententry_detail);
        ButterKnife.bind(this);
        initEvent();
        sweetAlertDialogUtil = new SweetAlertDialogUtil(EventEntryDetailActivity.this);
        userId = sp.getString(USER_ID, "");
        intent = getIntent();
        tFtSjEntity = (TFtSjEntity) intent.getSerializableExtra("tFtSjEntity");
        //当从督查督办那边点击事件名称跳转到事件详情里面时，只传了一个事件Id
        if(tFtSjEntity ==  null){
            tFtSjEntity = new TFtSjEntity();
            sjId  =intent.getStringExtra("sjId");
            tFtSjEntity.setId(sjId);
        }
        fileEntityDao= new FileEntityDao();
        tFtSjEntityDao = new TFtSjEntityDao();
        tFtSjRyEntityDao = new TFtSjRyEntityDao();
        //查看已经上报的事件信息，则获取传过来的uuid
        uuid = tFtSjEntity.getId();
        initToolbar();
        initPageView();
        initViewData();
        initOnLineData();

    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        sweetAlertDialogUtil = new SweetAlertDialogUtil(this);
        sp = SpUtil.getSharePerference(this);
        // 初始化网络工具
        netWorkConnection = new NetWorkConnection(this);
    }

    /**
     * 初始化PageView
     */
    @SuppressWarnings("deprecation")
    private void initPageView() {
        eventEntryDetail_basic = new EventEntryDetail_Basic(intent);
        eventEntryDetail_person = new EventEntryDetail_Person(intent,uuid);
        eventEntryDetail_attachment = new EventEntryDetail_Attachment(intent,uuid);
        // 设置Fragment集合
        List<Fragment> fragmList = new ArrayList<Fragment>();
        fragmList.add(eventEntryDetail_basic);
        fragmList.add(eventEntryDetail_person);
        fragmList.add(eventEntryDetail_attachment);

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
        centerText.setText("事件详细信息");

        /** 将右边按钮隐藏*/
        rightBtn.setVisibility(View.INVISIBLE);
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
                        fragment_viewpager.setCurrentItem(0);
                        break;
                    case R.id.radiobtn_person:
                        fragment_viewpager.setCurrentItem(1);
                        break;
                    case R.id.radiobtn_attachment:
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
                    sweetAlertDialogUtil.dismissAlertDialog();
                    Toast.makeText(EventEntryDetailActivity.this, message, Toast.LENGTH_SHORT).show();
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
                        tFtSjEntity = tFtSjDetailEntity.getFtSj();
                        sjryList = tFtSjDetailEntity.getSjryList();
                        sjfjList = tFtSjDetailEntity.getSjfjList();
                        progressList = tFtSjDetailEntity.getProgressList();
                        eventEntryDetail_basic.settTftSjEntityEntity(tFtSjEntity,progressList);
                        //将当事人信息放在当事人页面
                        eventEntryDetail_person.setTFtSjRyEntityList(sjryList);
                        //将附件信息放在附件页面
                        eventEntryDetail_attachment.setTFtSjFjEntityList(sjfjList, true);
                        //将其他信息放在附件页面
                        eventEntryDetail_attachment.settFtSjDetailEntityList(tFtSjDetailEntity);
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
            paramsMap.put("id", tFtSjEntity.getId());
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

package com.ky.kyandroid.activity.evententry;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
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

import com.ky.kyandroid.R;
import com.ky.kyandroid.activity.supervision.SuperVisionAddActivity;
import com.ky.kyandroid.adapter.FragmentAdapter;
import com.ky.kyandroid.bean.NetWorkConnection;
import com.ky.kyandroid.entity.EventEntity;
import com.ky.kyandroid.entity.TFtSjRyEntity;
import com.ky.kyandroid.util.JsonUtil;
import com.ky.kyandroid.util.SpUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by Caizhui on 2017-6-9.
 * 事件录入新增页面
 */

public class EventEntryAddActivity extends FragmentActivity {


    /**
     * TAG
     */
    private static final String TAG = "EventEntryAddActivity";

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
     * 上报领导按钮
     */
    @BindView(R.id.reporting_leadership_btn)
    Button reportingLeadershipBtn;
    /**
     * 保存按钮
     */
    @BindView(R.id.save_draft_btn)
    Button saveDraftBtn;

    /**
     * 事件录入 - 基本信息
     */
    private EventEntryAdd_Basic eventEntryAdd_basic;

    /**
     * 事件录入- 当事人
     */
    private EventEntryAdd_Person eventEntryAdd_person;

    /**
     * 事件录入 - 附件
     */
    private EventEntryAdd_Attachment eventEntryAdd_attachment;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evententry_add);
        ButterKnife.bind(this);
        uuid = UUID.randomUUID().toString().trim().replaceAll("-", "").toUpperCase() ;
        intent=getIntent();
        initEvent();
        initToolbar();
        initPageView();
        initViewData();
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
        eventEntryAdd_basic = new EventEntryAdd_Basic(intent);
        eventEntryAdd_person = new EventEntryAdd_Person();
        eventEntryAdd_attachment = new EventEntryAdd_Attachment(uuid);
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


    @OnClick({R.id.left_btn,R.id.reporting_leadership_btn,R.id.save_draft_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            /** 返回键 **/
            case R.id.left_btn:
                onBackPressed();
                break;
            /** 上报领导按钮*/
            case R.id.reporting_leadership_btn:
                Intent intent1 = new Intent(this, SuperVisionAddActivity.class);
                startActivity(intent1);
                break;
            /**保存草稿按钮*/
            case R.id.save_draft_btn:
                String userId=sp.getString(USER_ID,"");
                EventEntity eventEntity = eventEntryAdd_basic.PackageData();
                eventEntity.setId(uuid);
                List<TFtSjRyEntity> tFtSjRyEntityList = eventEntryAdd_person.tFtSjRyEntityList();
                /* 得到SD卡得路径 */
                String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath().toString();
                File fileRoute = new File(sdcard + "/img/" + uuid);
                File files[] = fileRoute.listFiles();
                String[] filesName = new String[files.length];
                for(int i=0;i<files.length;i++){
                    filesName[i]=files[i].getName();
                }
                HashMap map =new HashMap();
                map.put("entity",eventEntity);
                map.put("tFtSjRyEntityList",tFtSjRyEntityList);
                map.put("filesName",filesName);
                map.put("type","1");
                String paramMap = JsonUtil.map2Json(map);
                sendMultipart(userId,paramMap,files);
               /* PackageData();
                if ("".equals(message)) {
                    boolean flag ;
                    if("1".equals(type)){
                        flag = eventEntryDao.updateEventEntry(eventEntryEntity);
                        if (flag) {
                            Toast.makeText(EventEntryAdd_Basic.this.getActivity(), "修改成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(this.getActivity(), EventEntryListActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(EventEntryAdd_Basic.this.getActivity(),   "修改失败", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        //事件保存为1，事件提交为2
                        eventEntryEntity.setStatus("1");
                        flag = eventEntryDao.saveEventEntryEntity(eventEntryEntity);
                        if (flag) {
                            Toast.makeText(EventEntryAdd_Basic.this.getActivity(), "保存成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(this.getActivity(), EventEntryListActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(EventEntryAdd_Basic.this.getActivity(),   "保存失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(EventEntryAdd_Basic.this.getActivity(), message, Toast.LENGTH_SHORT).show();
                }*/
                    break;
                }
        }

    /**
     * 上传文件及参数
     */
    private void sendMultipart(String userId,String paramMap,File[] files){
        File sdcache = getExternalCacheDir();
        int cacheSize = 10 * 1024 * 1024;
        //设置超时时间及缓存，下边都应该这样设置的。
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .cache(new Cache(sdcache.getAbsoluteFile(), cacheSize));

        OkHttpClient mOkHttpClient=builder.build();
        MultipartBody.Builder requestBody = new MultipartBody.Builder();
        requestBody.setType(MultipartBody.FORM);
        requestBody.addFormDataPart("userId", userId);//设置post的参数
        requestBody.addFormDataPart("jsonData", paramMap);//设置post的参数
        if(files!=null && files.length>0){
            for(int i = 0;i<files.length;i++){
                requestBody.addFormDataPart("image", files[i].getName(),
                        RequestBody.create(MediaType.parse("application/octet-stream; charset=utf-8"), files[i]));
            }

        }
        Request request = new Request.Builder()
                .header("Authorization", "Client-ID " + "...")
                .url("http://192.168.1.102:8080/ft/kyAndroid/sjSave.action")//请求的url
                .post(requestBody.build())
                .build();

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("InfoMSG", response.body().string());
            }
        });
    }
}

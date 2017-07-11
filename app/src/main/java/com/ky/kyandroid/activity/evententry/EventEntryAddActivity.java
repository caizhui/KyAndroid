package com.ky.kyandroid.activity.evententry;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ky.kyandroid.Constants;
import com.ky.kyandroid.R;
import com.ky.kyandroid.activity.draft.EventDraftListActivity;
import com.ky.kyandroid.adapter.FragmentAdapter;
import com.ky.kyandroid.bean.AckMessage;
import com.ky.kyandroid.bean.NetWorkConnection;
import com.ky.kyandroid.db.dao.FileEntityDao;
import com.ky.kyandroid.db.dao.TFtSjEntityDao;
import com.ky.kyandroid.db.dao.TFtSjRyEntityDao;
import com.ky.kyandroid.entity.FileEntity;
import com.ky.kyandroid.entity.TFtSjDetailEntity;
import com.ky.kyandroid.entity.TFtSjEntity;
import com.ky.kyandroid.entity.TFtSjFjEntity;
import com.ky.kyandroid.entity.TFtSjRyEntity;
import com.ky.kyandroid.util.JsonUtil;
import com.ky.kyandroid.util.OkHttpUtil;
import com.ky.kyandroid.util.SpUtil;
import com.ky.kyandroid.util.StringUtils;
import com.ky.kyandroid.util.SweetAlertDialogUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
     * 上报领导，保存草稿LinearLayout
     */
    @BindView(R.id.btn_linearlayout)
    public LinearLayout btnLinearlayout;

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


    private TFtSjEntityDao tFtSjEntityDao;

    public TFtSjRyEntityDao tFtSjRyEntityDao;

    private FileEntityDao fileEntityDao;

    /**
     * type 0：新增 1：修改
     **/
    private String type;

    private TFtSjEntity tFtSjEntity;

    String userId;

    // 获取事件附件 - 子表信息
    List<TFtSjFjEntity> sjfjList;
    // 人员具体信息
    List<TFtSjRyEntity> sjryList;

    private TFtSjEntity eventEntity;
    /**
     * 弹出框工具类
     */
    private SweetAlertDialogUtil sweetAlertDialogUtil;

    /**
     * 操作类型add edit
     */
    String czlx;

    /**
     * 存放图片List
     */
    private List<FileEntity>   fileEntityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evententry_add);
        ButterKnife.bind(this);
        initEvent();
        sweetAlertDialogUtil = new SweetAlertDialogUtil(EventEntryAddActivity.this);
        userId = sp.getString(USER_ID, "");
        intent = getIntent();
        type = intent.getStringExtra("type");
        tFtSjEntity = (TFtSjEntity) intent.getSerializableExtra("tFtSjEntity");
        fileEntityDao= new FileEntityDao();
        tFtSjEntityDao = new TFtSjEntityDao();
        tFtSjRyEntityDao = new TFtSjRyEntityDao();
        //1表示草稿修改或者查看已经上报的事件信息，则获取传过来的uuid，否则新建
        if ("1".equals(type)) {
            uuid = tFtSjEntity.getId();
        } else {
            uuid = UUID.randomUUID().toString().trim().replaceAll("-", "").toUpperCase();
        }
        initToolbar();
        initPageView();
        initViewData();
        //当type=1时，判断是修改草稿信息还是查看上报事件详细信息
        if ("1".equals(type)) {
            if ("0".equals(tFtSjEntity.getZt())) {
                //查询保存在本地的详细信息
                initData();
            }
        }
        //3 街道退回
        if(tFtSjEntity!=null){
            if("3".equals(tFtSjEntity.getZt())){
                initOnLineData();
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
        eventEntryAdd_basic = new EventEntryAdd_Basic(intent);
        eventEntryAdd_person = new EventEntryAdd_Person(intent,uuid);
        eventEntryAdd_attachment = new EventEntryAdd_Attachment(intent,uuid);
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
                    Toast.makeText(EventEntryAddActivity.this, message, Toast.LENGTH_SHORT).show();
                    break;
                // 获取详细信息数据成功
                case 1:
                    handleTransation(message);
                    break;
                //上传数据成功
                case 2:
                    //将本地的草稿数据删除
                    boolean flag = tFtSjEntityDao.deleteEventEntry(eventEntity.getId());
                    sweetAlertDialogUtil.dismissAlertDialog();
                    if(flag){
                        Toast.makeText(EventEntryAddActivity.this, "上报成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EventEntryAddActivity.this, EventEntryListActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(EventEntryAddActivity.this, "上报失败", Toast.LENGTH_SHORT).show();
                    }

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
                        //将其他信息放在附件页面
                        eventEntryAdd_attachment.settFtSjDetailEntityList(tFtSjDetailEntity);
                    }
                }
            }
        }
    }


    /**
     * 查看保存在我本地的详细信息
     */
    public void initData() {
        sjryList = tFtSjRyEntityDao.queryListBySjId(uuid);
        eventEntryAdd_person.setTFtSjRyEntityList(sjryList, true);
    }


    /**
     * 上传文件及参数
     */
    private void sendMultipart(String userId, String paramMap, File[] files,String czlx) {
        File sdcache = this.getExternalCacheDir();
        int cacheSize = 10 * 1024 * 1024;
        //设置超时时间及缓存，下边都应该这样设置的。
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .cache(new Cache(sdcache.getAbsoluteFile(), cacheSize));

        OkHttpClient mOkHttpClient = builder.build();
        MultipartBody.Builder requestBody = new MultipartBody.Builder();
        requestBody.setType(MultipartBody.FORM);
        requestBody.addFormDataPart("userId", userId);//设置post的参数
        requestBody.addFormDataPart("jsonData", paramMap);//设置post的参数
        requestBody.addFormDataPart("czlx", czlx);//设置post的参数
        if (files != null && files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                //uploadFles
                requestBody.addFormDataPart("uploadFles", files[i].getName(),
                        RequestBody.create(MediaType.parse("application/octet-stream; charset=utf-8"), files[i]));
            }

        }
        Request request = new Request.Builder()
                .header("Authorization", "Client-ID " + "...")
                .url(Constants.SERVICE_BASE_URL + Constants.SERVICE_SAVE_EVENTENTRY)//请求的url
                .post(requestBody.build())
                .build();

        final Message msg = new Message();
        msg.what = 0;
        mOkHttpClient.newCall(request).enqueue(new Callback() {
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
                    msg.obj = "网络异常,请确认网络情况";
                }
                mHandler.sendMessage(msg);
            }
        });
    }

    @OnClick({R.id.left_btn, R.id.reporting_leadership_btn, R.id.save_draft_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            /** 返回键 **/
            case R.id.left_btn:
                onBackPressed();
                break;
            /** 上报领导按钮*/
            case R.id.reporting_leadership_btn:
                eventEntity = eventEntryAdd_basic.PackageData();
                fileEntityList = eventEntryAdd_attachment.PackageData();
                if(fileEntityList!=null && fileEntityList.size()>0){
                    for(int i=0;i<fileEntityList.size();i++){
                        FileEntity fileEntity = fileEntityList.get(i);
                        if(fileEntity.getUuid() == 0){
                            fileEntity.setSjId(uuid);
                            fileEntityDao.saveFileEntity(fileEntity);
                        }else{
                            fileEntityDao.updateFileEntity(fileEntity);
                        }
                    }
                }
                if (eventEntity != null) {
                    //当上报领导时，如果id为空，表示状态为0，表示是通过草稿去上报的，否则就是直接上报的
                    if ("0".equals(eventEntity.getZt())) {
                        //如果是第一次上传，要设置一个uuid，如果是从草稿中上传，就直接拿草稿里面的uuid
                        eventEntity.setId(uuid);
                    }
                    //当状态为3或uuid不为空时，表示是退回修改再上传的，uuid不为空是先保存了草稿
                    if ("3".equals(eventEntity.getZt())){
                        czlx="edit";
                    }else if(eventEntity.getUuid()!=null){
                        eventEntity.setId(eventEntity.getUuid());
                        czlx="edit";
                    }else{
                        czlx="add";
                    }
                    HashMap map = new HashMap();
                    //上报领导，状态为1
                    eventEntity.setZt("1");
                    List<TFtSjRyEntity> tFtSjRyEntityList = eventEntryAdd_person.tFtSjRyEntityList();
                    tFtSjRyEntityDao.deleteEventEntryByuuid(uuid);
                /* 得到SD卡得路径 */
                    String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath().toString();
                    File fileRoute = new File(sdcard + "/img/" + uuid);
                    File files[] = fileRoute.listFiles();
                    map.put("entity", eventEntity);
                    if (tFtSjRyEntityList != null) {
                        map.put("tFtSjRyEntityList", tFtSjRyEntityList);
                    }
                    fileEntityList = fileEntityDao.queryList(uuid);
                    if (files != null && files.length > 0) {
                        String[] filesName = new String[files.length];
                        String[] filesMs = new String[files.length];
                        for (int i = 0; i < files.length; i++) {
                            if(fileEntityList!=null&& fileEntityList.size()>0){
                                //这里循环遍历存放文件信息的List，如果在本地获取的文件名跟我们从数据库中获取的一致，则表示是同一条记录,就将数据库中的描述信息变成文件名称
                                for(int j=0;j<fileEntityList.size();j++){
                                    if(fileEntityList.get(j).getFileName().equals(files[i].getName())){
                                        filesMs[i] =fileEntityList.get(j).getFileMs();
                                    }
                                }
                            }
                            filesName[i]=files[i].getName();
                        }
                        map.put("filesName", filesName);
                        map.put("filesMs", filesMs);
                    }
                    String paramMap = JsonUtil.map2Json(map);
                    sweetAlertDialogUtil.loadAlertDialog();
                    sendMultipart(userId, paramMap, files,czlx);
                }
                break;
            /**保存草稿按钮*/
            case R.id.save_draft_btn:
                TFtSjEntity tempenenEntity = eventEntryAdd_basic.PackageData();
                if(tempenenEntity.getId()!=null){
                    //做这一步操作是当时间退回，状态为3时，保存草稿的时候，在数据库添加一条记录，将本身的id放在uuid中，为了再上报领导时又重新去新增上报
                    tempenenEntity.setUuid(tempenenEntity.getId());
                }
                fileEntityList = eventEntryAdd_attachment.PackageData();
                if(fileEntityList!=null && fileEntityList.size()>0){
                    for(int i=0;i<fileEntityList.size();i++){
                        FileEntity fileEntity = fileEntityList.get(i);
                        if(fileEntity.getUuid() == 0){
                            fileEntity.setSjId(uuid);
                            fileEntityDao.saveFileEntity(fileEntity);
                        }else{
                            fileEntityDao.updateFileEntity(fileEntity);
                        }

                    }
                }
                if (tempenenEntity != null) {
                    boolean flag = false;
                    String message = "";
                    if ("1".equals(tempenenEntity.getZt())) {
                        flag = tFtSjEntityDao.updateTFtSjEntity(tempenenEntity);
                        message = "修改";
                    } else {
                        tempenenEntity.setId(uuid);
                        //保存草稿，状态为0
                        tempenenEntity.setZt("0");
                        flag = tFtSjEntityDao.saveTFtSjEntity(tempenenEntity);
                        message = "保存";
                    }
                    if (flag) {
                        Toast.makeText(EventEntryAddActivity.this, message + "成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EventEntryAddActivity.this, EventDraftListActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(EventEntryAddActivity.this, message + "失败", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

}

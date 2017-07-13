package com.ky.kyandroid.activity.dispatch;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ky.kyandroid.Constants;
import com.ky.kyandroid.R;
import com.ky.kyandroid.activity.evententry.EventEntryListActivity;
import com.ky.kyandroid.adapter.EventImageListAdapter;
import com.ky.kyandroid.bean.NetWorkConnection;
import com.ky.kyandroid.db.dao.FileEntityDao;
import com.ky.kyandroid.entity.FileEntity;
import com.ky.kyandroid.entity.TFtSjEntity;
import com.ky.kyandroid.entity.TFtZtlzEntity;
import com.ky.kyandroid.util.FileManager;
import com.ky.kyandroid.util.JsonUtil;
import com.ky.kyandroid.util.SpUtil;
import com.ky.kyandroid.util.SweetAlertDialogUtil;
import com.ky.kyandroid.view.SelectPicPopupWindow;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemLongClick;
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
 * Created by Caizhui on 2017/7/1.
 * 街道自行处理
 */

public class StreetHandleActivity extends AppCompatActivity {

    /**
     * 导航栏左边按钮
     */
    @BindView(R.id.left_btn)
    ImageView leftBtn;
    /**
     * 导航栏中间文字
     */
    @BindView(R.id.center_text)
    TextView centerText;
    /**
     * 导航栏右边按钮
     */
    @BindView(R.id.right_btn)
    Button rightBtn;
    /**
     * 处理上报按钮
     */
    @BindView(R.id.add_handler)
    Button addHandler;
    /**
     * 新增附件按钮
     */
    @BindView(R.id.add_btn)
    Button addBtn;
    /**
     * 处理时间
     */
    @BindView(R.id.happen_time_edt)
    EditText happenTimeEdt;
    /**
     * 处理内容
     */
    @BindView(R.id.return_edt)
    EditText returnEdt;
    /**
     * 附件L
     */
    @BindView(R.id.file_list)
    ListView fileList;

    @BindView(R.id.main)
    LinearLayout main;

    private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果

    String sdcard;

    File fileRoute;

    private SelectPicPopupWindow menuWindow;
    private Uri uri;
    private File cutfile;


    private Bitmap tupbitmap;

    /**
     * 照片名称
     */
    private String photoName;


    /**
     * 存放图片List
     */
    private List<FileEntity> fileEntityList;

    /**
     * 文件实体
     */
    private FileEntity fileEntity;

    /**
     * 文件adapter
     */
    private EventImageListAdapter adapter;

    private TFtSjEntity tFtSjEntity;

    private TFtZtlzEntity tFtZtlzEntity;


    private Intent intent;

    private String  uuid;

    /**
     * SharedPreferences
     */
    private SharedPreferences sp;
    /**
     * 网路工具类
     */
    private NetWorkConnection netWorkConnection;

    private SweetAlertDialogUtil sweetAlertDialogUtil;

    /**
     * 用户ID
     */
    public static final String USER_ID = "userId";

    private String imageUrl;

    private  String userId;

    /**
     * 1，表示拍照，2表示相册
     */
    private String isPhoto;

    FileEntityDao fileEntityDao;

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
                    Toast.makeText(StreetHandleActivity.this, message, Toast.LENGTH_SHORT).show();
                    break;
                //自行处理数据成功
                case 2:
                    //将本地的草稿数据删除
                    sweetAlertDialogUtil.dismissAlertDialog();
                    //处理完成之后，将手机的附件删除
                    FileManager.delFile(imageUrl);
                    Toast.makeText(StreetHandleActivity.this, "处理成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(StreetHandleActivity.this, EventEntryListActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_streethandle_operation);
        ButterKnife.bind(this);
        rightBtn.setVisibility(View.INVISIBLE);
        initEvent();
        intent = getIntent();
        tFtSjEntity = (TFtSjEntity) intent.getSerializableExtra("tFtSjEntity");
        tFtZtlzEntity = (TFtZtlzEntity) intent.getSerializableExtra("tFtZtlzEntity");
        centerText.setText(tFtZtlzEntity.getName());
        fileEntityDao = new FileEntityDao();
        if(tFtSjEntity!=null){
            uuid = tFtSjEntity.getId();
        }
        fileEntityList =new ArrayList<FileEntity>();
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            /* 得到SD卡得路径 */
            sdcard = Environment.getExternalStorageDirectory().getAbsolutePath().toString();
            imageUrl= sdcard + "/img/" + uuid + "/";
            fileRoute = new File(imageUrl);
            //显示图片或者创建文件路径
            if (!fileRoute.exists()) {
                fileRoute.mkdirs();
            }
        } else {
            Toast.makeText(StreetHandleActivity.this, "没有SD卡", Toast.LENGTH_LONG).show();
        }
        //初始化imageList
        adapter = new EventImageListAdapter(fileEntityList, StreetHandleActivity.this,false);
        fileList.setAdapter(adapter);
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        sp = SpUtil.getSharePerference(this);
        // 初始化网络工具
        netWorkConnection = new NetWorkConnection(this);
        userId = sp.getString(USER_ID, "");
        sweetAlertDialogUtil = new SweetAlertDialogUtil(this);
        happenTimeEdt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    happenTimeEdt.clearFocus();
                    Calendar c = Calendar.getInstance();
                    new DatePickerDialog(StreetHandleActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            //Date date = new Date(System.currentTimeMillis());
                            //SimpleDateFormat dateFormat = new SimpleDateFormat(" HH:mm:ss");
                            String time = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                            //time += dateFormat.format(date);
                            happenTimeEdt.setText(time);
                        }
                    }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
                }
                return false;
            }
        });
    }

    @OnItemLongClick(R.id.file_list)
    public boolean OnItemLongClick(final int position){
        final FileEntity fileEntity = (FileEntity) adapter.getItem(position);
        View view = fileList.getChildAt(position);
        EditText editText = (EditText) view.findViewById(R.id.image_ms);
        editText.clearFocus();
        AlertDialog.Builder builder = new AlertDialog.Builder(StreetHandleActivity.this);
        builder.setTitle("信息");
        builder.setMessage("确定要删除该条记录吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(fileEntityList!=null && fileEntityList.size()>0){
                    //删除保存在本地的图片
                    FileManager.delFile(fileRoute + "/" +fileEntity.getFileName());
                    boolean flag = fileEntityDao.deleteEventEntry(fileEntity.getUuid());
                    if(flag){
                        if(fileEntityList.get(position)!=null){
                            fileEntityList.remove(position);
                        }
                        adapter.notifyDataSetChanged(fileEntityList);
                    }else{
                        Toast.makeText(StreetHandleActivity.this,"删除失败",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.create().show();
        return false;
    }

    @OnClick({R.id.add_btn,R.id.add_handler,R.id.left_btn})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.left_btn:
                onBackPressed();
                break;
            //新增附件
            case R.id.add_btn:
                menuWindow = new SelectPicPopupWindow(StreetHandleActivity.this, itemsOnClick);
                // 显示窗口
                menuWindow.showAtLocation(main, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
                break;
            //处理
            case R.id.add_handler:
                /* 得到SD卡得路径 */
                File files[] = fileRoute.listFiles();
                HashMap paramsMap = new HashMap();
                paramsMap.put("userId", userId);
                paramsMap.put("sjId", tFtSjEntity.getId());
                paramsMap.put("action", tFtZtlzEntity.getAction());
                paramsMap.put("actionName", tFtZtlzEntity.getActionname());
                paramsMap.put("zt", tFtSjEntity.getZt());
                paramsMap.put("nextZt", tFtZtlzEntity.getNextzt());
                if (files != null && files.length > 0) {
                    String[] filesName = new String[files.length];
                    for (int i = 0; i < files.length; i++) {
                        filesName[i] = files[i].getName();
                    }
                    paramsMap.put("filesName", filesName);
                }
                String message ="";
                if(happenTimeEdt!=null){
                    if("".equals(happenTimeEdt.getText().toString())){
                        message+="处理时间不能为空\n";
                    }else{
                        paramsMap.put("lrclsj", happenTimeEdt.getText().toString());
                    }
                }
                if(returnEdt!=null){
                    if("".equals(returnEdt.getText().toString())){
                        message+="处理情况不能为空\n";
                    }else{
                        paramsMap.put("lrclqk", returnEdt.getText().toString());
                    }
                }
                if("".equals(message)){
                    String map = JsonUtil.map2Json(paramsMap);
                    sendMultipart(userId, map, files);
                }else{
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    /**
     * 上传文件及参数
     */
    private void sendMultipart(String userId, String paramMap, File[] files) {
        sweetAlertDialogUtil.loadAlertDialog();
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
        if (files != null && files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                //uploadFles
                requestBody.addFormDataPart("uploadFles", files[i].getName(),
                        RequestBody.create(MediaType.parse("application/octet-stream; charset=utf-8"), files[i]));
            }

        }
        Request request = new Request.Builder()
                .header("Authorization", "Client-ID " + "...")
                .url(Constants.SERVICE_BASE_URL + Constants.SERVICE_ZXCL_HADLE)//请求的url
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


    // popupwindow点击事件
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                case R.id.btn_take_photo:
                    String sdStatus = Environment.getExternalStorageState();
                    if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                        Log.v("TestFile", "SD card is not avaiable/writeable right now.");
                        return;
                    }
                    // 调用系统的拍照功能
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra("camerasensortype", 2);// 调用前置摄像头
                    intent.putExtra("autofocus", true);// 自动对焦
                    intent.putExtra("fullScreen", false);// 全屏
                    intent.putExtra("showActionIcons", false);
                    // 指定调用相机拍照后照片的储存路径
                    File out = new File(fileRoute, getPhotoFileName());
                    uri = Uri.fromFile(out);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
                    break;
                case R.id.btn_pick_photo:
                    Intent intents = new Intent(Intent.ACTION_PICK, null);
                    intents.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(intents, PHOTO_REQUEST_GALLERY);
                    break;
                default:
                    break;
            }
        }
    };


    // 图片上传回调方法
    @SuppressLint("UseSparseArrays")
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PHOTO_REQUEST_TAKEPHOTO:
                isPhoto = "1";
                startPhotoZoom(uri, 600);
                break;
            case PHOTO_REQUEST_GALLERY:
                if (data != null) {
                    isPhoto = "2";
                    uri = data.getData();
                    startPhotoZoom(uri, 600);
                }
                break;
            case PHOTO_REQUEST_CUT:
                if (resultCode == -1) {
                    if (uri != null) {
                        try {
                            Bitmap bitmapFromUri = getBitmapFromUri(uri, StreetHandleActivity.this);
                            //先把拍照之后保存在本地的原图删掉。
                            if ("1".equals(isPhoto)) {
                                boolean flag = FileManager.delFile(fileRoute + "/" + photoName);
                            }
                            if (bitmapFromUri != null) {
                                File file = SavePicInLocal(bitmapFromUri);
                                FileInputStream fis = new FileInputStream(file);
                                tupbitmap = BitmapFactory.decodeStream(fis);
                                fileEntity = new FileEntity();
                                if(tupbitmap!=null){
                                    fileEntity.setHaveMs(false);
                                    fileEntity.setBitmap(tupbitmap);
                                    fileEntity.setSjId(uuid);
                                    fileEntity.setFileName(photoName);
                                    fileEntityDao.saveFileEntity(fileEntity);
                                }
                                if(fileEntityList==null){
                                    fileEntityList =new ArrayList<FileEntity>();
                                }
                                fileEntityList.add(fileEntity);
                                if (fileEntityList != null && fileEntityList.size() > 0) {
                                    adapter.notifyDataSetChanged(fileEntityList);
                                }
                                // attachmentImg.setImageBitmap(tupbitmap);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
        }
    }

    ;

    private void startPhotoZoom(Uri uri, int size) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", size);
        intent.putExtra("outputY", size);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("return-data", false);

        // 去黑边
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    // 生成裁剪之后的图片
    private File SavePicInLocal(Bitmap bitmap) {
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        ByteArrayOutputStream baos = null; // 字节数组输出流
        try {
            baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] byteArray = baos.toByteArray();// 字节数组输出流转换成字节数组
            cutfile = new File(fileRoute, getPhotoFileName());
            // 将字节数组写入到刚创建的图片文件中
            fos = new FileOutputStream(cutfile);
            bos = new BufferedOutputStream(fos);
            bos.write(byteArray);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        return cutfile;
    }

    // 读取uri所在的图片
    public static Bitmap getBitmapFromUri(Uri uri, Context mContext) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), uri);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 使用系统当前日期加以调整作为照片的名称
    @SuppressLint("SimpleDateFormat")
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        photoName = dateFormat.format(date) + ".jpg";
        return photoName;
    }

}

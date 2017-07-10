package com.ky.kyandroid.activity.evententry;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.ky.kyandroid.R;
import com.ky.kyandroid.adapter.EventImageListAdapter;
import com.ky.kyandroid.db.dao.FileEntityDao;
import com.ky.kyandroid.entity.FileEntity;
import com.ky.kyandroid.entity.TFtSjDetailEntity;
import com.ky.kyandroid.entity.TFtSjEntity;
import com.ky.kyandroid.entity.TFtSjFjEntity;
import com.ky.kyandroid.entity.TFtSjGlsjEntity;
import com.ky.kyandroid.entity.TFtSjLogEntity;
import com.ky.kyandroid.util.FileManager;
import com.ky.kyandroid.view.SelectPicPopupWindow;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemLongClick;

import static com.ky.kyandroid.util.FileManager.delFile;

/**
 * Created by Caizhui on 2017-6-9.
 * 事件录入附件界面
 */

@SuppressLint("ValidFragment")
public class EventEntryAdd_Attachment extends Fragment {
    /**
     *  新增附件按钮
     */
    @BindView(R.id.add_attachment)
    Button addAttachment;

    /**
     *  事件关联日志按钮
     */
    @BindView(R.id.event_log)
    Button eventLogBtn;

    /**
     *  事件关联按钮
     */
    @BindView(R.id.event_relevance)
    Button eventRelevanceBtn;

   /* *//**
     * 图片控件
     *//*
    @BindView(R.id.attachment_img)
    ImageView attachmentImg;*/

    @BindView(R.id.main)
    LinearLayout main;

    /**
     * 附件List
     */
    @BindView(R.id.image_list)
    ListView imageList;

    private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果

    String sdcard;

    File fileRoute;

    private SelectPicPopupWindow menuWindow;
    private Uri uri;
    private File cutfile;

    private Map<String, Object> map;

    private Bitmap tupbitmap;

    /**
     * 照片名称
     */
    private String photoName;

    public String uuid;
    /**
     * 0表示可以新增，1表示可以修改或者查看详情
     */
    public  String type;

    public Intent  intent;

    /**
     * 1，表示拍照，2表示相册
     */
    private String isPhoto;

    @SuppressLint("ValidFragment")
    public EventEntryAdd_Attachment(Intent intent,String uuid) {
        this.intent= intent;
        this.uuid = uuid;
    }

    /**
     * 事件实体
     */
    private TFtSjEntity tFtSjEntity;

    // 获取事件附件 - 子表信息
    public List<TFtSjFjEntity> sjfjList;

    /**
     * 事件其他信息全部信息
     */
    private TFtSjDetailEntity tFtSjDetailEntity;

    /**
     * 事件关联Map信息
     */
    private Map<String,TFtSjEntity> glsjListMap;

    private boolean flag = false;

    /**
     * 存放图片List
     */
    private List<FileEntity>   fileEntityList;

    /**
     * 文件实体
     */
    private FileEntity fileEntity;

    /**
     * 文件adapter
     */
    private EventImageListAdapter adapter;

    /**
     * 返回图片信息List
     */
    private List<FileEntity>   returnFileList;

    FileEntityDao fileEntityDao;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.evententeradd_attachment_fragment, container, false);
        ButterKnife.bind(this, view);
        type = intent.getStringExtra("type");
        tFtSjEntity = (TFtSjEntity) intent.getSerializableExtra("tFtSjEntity");
        //,判断是否为空，是为了防止切换页签的时候将实例重新初始化
        if(fileEntityList==null){
            fileEntityList =new ArrayList<FileEntity>();
        }
        //初始化imageList,判断是否为空，是为了防止切换页签的时候将实例重新初始化
        if(adapter==null){
            adapter = new EventImageListAdapter(fileEntityList, EventEntryAdd_Attachment.this.getActivity());
        }
        imageList.setAdapter(adapter);
        if(tFtSjEntity!=null){
            uuid = tFtSjEntity.getId();
        }
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            /* 得到SD卡得路径 */
            sdcard = Environment.getExternalStorageDirectory().getAbsolutePath().toString();
            fileRoute = new File(sdcard + "/img/" + uuid + "/");
            //显示图片或者创建文件路径
            appendImage();
        } else {
            Toast.makeText(EventEntryAdd_Attachment.this.getActivity(), "没有SD卡", Toast.LENGTH_LONG).show();
        }
        return view;
    }

    @OnItemLongClick(R.id.image_list)
    public boolean OnItemLongClick(final int position){
        final FileEntity fileEntity = (FileEntity) adapter.getItem(position);
        View view = imageList.getChildAt(position);
        EditText editText = (EditText) view.findViewById(R.id.image_ms);
        editText.clearFocus();
        //只有当flag为false，表示事件还是草稿状态，图片还在本地的时候
        if(!flag){
            AlertDialog.Builder builder = new AlertDialog.Builder(EventEntryAdd_Attachment.this.getActivity());
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
                            Toast.makeText(EventEntryAdd_Attachment.this.getActivity(),"删除失败",Toast.LENGTH_SHORT).show();
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
        }
       return false;
    }



    /**
     * 显示图片或者创建文件路径
     */
    public void appendImage() {
        //如果文件夹不存在，表示第一次进来，需要创建文件夹，否则表示已经进来过，我们需要获取图片显示
        if (!fileRoute.exists()) {
            fileRoute.mkdirs();
        } else {
            fileEntityDao = new FileEntityDao();
            //当切换页签的时候不查询数据库，只有保存草稿之后才查询数据库
            if(fileEntityList!=null && fileEntityList.size()==0){
                fileEntityList = fileEntityDao.queryList(uuid);
            }
            if (uuid != null && !"".equals(uuid)) {
                //判断是否切换页签，如果切换页签，我们的fileEntityList = fileEntityDao.queryList();查询为null，所以我们需要在后面将fileEntity加入到我们的fileEntityList中
                boolean isTap= false;
                File files[] = fileRoute.listFiles();
                if (files != null && files.length > 0) {
                    FileInputStream fis = null;
                    try {
                        for(int i=0;i<files.length;i++){
                            fis = new FileInputStream(files[i]);
                            tupbitmap = BitmapFactory.decodeStream(fis);
                            //如果fileEntityList有值，则表示fileEntity是有对象的，否则据创建对象
                            if(fileEntityList!=null && fileEntityList.size()>i){
                                fileEntity = fileEntityList.get(i);
                            }else{
                                if(fileEntity== null){
                                    fileEntity = new FileEntity();
                                }
                                isTap= true;
                            }
                            if(fileEntityList==null){
                                fileEntityList =new ArrayList<FileEntity>();
                            }
                            if(tupbitmap!=null){
                                //这里循环遍历存放文件信息的List，如果在本地获取的文件名跟我们从数据库中获取的一致，则表示是同一条记录
                                if(fileEntityList!=null &&fileEntityList.size()>i){
                                    for(int j=0;j<fileEntityList.size();j++){
                                        String fileName =files[i].getName();
                                        if(fileEntityList.get(j).getFileName()!=null && fileEntityList.get(j).getFileName().equals(fileName)){
                                            fileEntity.setBitmap(tupbitmap);
                                            fileEntity.setFileMs(fileEntityList.get(j).getFileMs());
                                        }else{
                                            fileEntity.setBitmap(tupbitmap);
                                        }
                                    }
                                }
                                if(isTap){
                                    fileEntity.setBitmap(tupbitmap);
                                    fileEntityList.add(fileEntity);
                                }
                                // fileEntity.setFileMs(fileEntityList.get(i).getFileMs());
                            }

                            if (fileEntityList != null && fileEntityList.size() > 0) {
                                adapter.notifyDataSetChanged(fileEntityList);
                            }
                        }

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @OnClick({R.id.add_attachment,R.id.event_log,R.id.event_relevance})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.add_attachment:
                menuWindow = new SelectPicPopupWindow(EventEntryAdd_Attachment.this.getActivity(), itemsOnClick);
                // 显示窗口
                menuWindow.showAtLocation(main, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
                break;
            case R.id.event_log:
                Intent intent =new Intent(this.getActivity(),EventLogListActivity.class);
                List<TFtSjLogEntity> tFtSjLogEntityList = null;
                if(tFtSjDetailEntity!=null){
                    tFtSjLogEntityList =tFtSjDetailEntity.getSjlogList();
                }
                intent.putExtra("tFtSjLogEntityList", (Serializable) tFtSjLogEntityList);
                startActivity(intent);
                break;
            case R.id.event_relevance:
                Intent intent1 =new Intent(this.getActivity(),EventRelevanceListActivity.class);
                List<TFtSjGlsjEntity> tFtSjGlsjEntityList = null;
                if(tFtSjDetailEntity!=null){
                    tFtSjGlsjEntityList =tFtSjDetailEntity.getGlsjList();
                    glsjListMap = tFtSjDetailEntity.getGlsjListMap();
                }
                intent1.putExtra("tFtSjGlsjEntityList", (Serializable) tFtSjGlsjEntityList);
                intent1.putExtra("glsjListMap", (Serializable) glsjListMap);
                startActivity(intent1);
                break;
        }
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
                            Bitmap bitmapFromUri = getBitmapFromUri(uri, EventEntryAdd_Attachment.this.getActivity());
                            if (bitmapFromUri != null) {
                                //先把拍照之后保存在本地的原图删掉。
                                if ("1".equals(isPhoto)) {
                                    boolean flag = delFile(fileRoute + "/" + photoName);
                                }
                                File file = SavePicInLocal(bitmapFromUri);
                                FileInputStream fis = new FileInputStream(file);
                                tupbitmap = BitmapFactory.decodeStream(fis);
                                fileEntity = new FileEntity();
                                if(tupbitmap!=null){
                                    fileEntity.setFileName(photoName);
                                    fileEntity.setBitmap(tupbitmap);
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


    /**
     * 当查看已经上报事件详情时初始化数据,显示文件
     */
    public void settFtSjDetailEntityList(TFtSjDetailEntity tFtSjDetailEntity) {
        this.tFtSjDetailEntity = tFtSjDetailEntity;
    }
    public List<FileEntity> PackageData(){
        returnFileList = new ArrayList<FileEntity>();
        if(adapter!=null){
            int count =adapter.getCount();
            if(count>0){
                for(int i = 0 ;i<count;i++){
                    FileEntity fileEntity = (FileEntity) adapter.getItem(i);
                    View view = imageList.getChildAt(i);
                    EditText editText = (EditText) view.findViewById(R.id.image_ms);
                    fileEntity.setFileMs(editText.getText().toString());
                    returnFileList.add(fileEntity);
                }
            }
        }
     return returnFileList;
    }

}

package com.ky.kyandroid.activity.evententry;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
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
import com.ky.kyandroid.bean.BitmapInfo;
import com.ky.kyandroid.db.dao.FileEntityDao;
import com.ky.kyandroid.entity.FileEntity;
import com.ky.kyandroid.entity.TFtSjDetailEntity;
import com.ky.kyandroid.entity.TFtSjEntity;
import com.ky.kyandroid.entity.TFtSjFjEntity;
import com.ky.kyandroid.entity.TFtSjGlsjEntity;
import com.ky.kyandroid.entity.TFtSjLogEntity;
import com.ky.kyandroid.util.FileManager;
import com.ky.kyandroid.util.PermissionUtil;
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
import java.util.HashMap;
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
     * 标识
     */
    final private String TAG = "EventEntryAdd_Attachment";

    /**
     * 权限请求码
     */
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
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
    private static final int PHOTO_REQUEST_VIDEO = 2;// 选择照片或视频
    private static final int PHOTO_REQUEST_AUDIO= 4;// 选择音乐或录音
    private static final int PHOTO_REQUEST_CUT = 3;// 结果

    String sdcard;

    File fileRoute;

    private SelectPicPopupWindow menuWindow;
    private Uri uri;
    private File cutfile;

    private Map<String, Object> map;

    /**
     * 照片名称
     */
    private String photoName;

    /**
     * 照片ID - 区分文件夹
     */
    public String imageId;

    /**
     * 操作类型
     */
    private String czlx;
    /**
     * 0表示可以新增，1表示可以修改或者查看详情
     */
    public  String type;

    public Intent  intent;

    /**
     * 1，表示拍照, 2 本地照片或视频  3本地音乐或录音
     */
    private String medioType;

    @SuppressLint("ValidFragment")
    public EventEntryAdd_Attachment(Intent intent,String uuid) {
        this.intent= intent;
        this.imageId = uuid;
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
    private ArrayList<FileEntity> fileEntityList;


    /**
     * 移除的网络存放图片List
     */
    private List<FileEntity> removeFileEntityList;

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
    private List<FileEntity> returnFileList;

    FileEntityDao fileEntityDao;

    private DisplayMetrics metrics;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.evententeradd_attachment_fragment, container, false);
        ButterKnife.bind(this, view);
        fileEntityDao = new FileEntityDao();
        metrics = getResources().getDisplayMetrics();
        tFtSjEntity = (TFtSjEntity) intent.getSerializableExtra("tFtSjEntity");
        fileEntityList = new ArrayList<FileEntity>();
        removeFileEntityList = new ArrayList<FileEntity>();
        adapter = new EventImageListAdapter(fileEntityList,fileEntityDao,EventEntryAdd_Attachment.this.getActivity(),false);
        imageList.setAdapter(adapter);

        // 判断是否有内存卡
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //显示图片&者创建文件路径
            appendImage();
        } else {
            Toast.makeText(EventEntryAdd_Attachment.this.getActivity(), "没有SD卡", Toast.LENGTH_LONG).show();
        }
        return view;
    }

    @OnItemLongClick(R.id.image_list)
    public boolean OnItemLongClick(final int ps){
        final FileEntity selectFileEntity = (FileEntity) adapter.getItem(ps);
        View view = imageList.getChildAt(ps);
        EditText editText = (EditText) view.findViewById(R.id.image_ms);
        editText.clearFocus();
        // 删除附件
        AlertDialog.Builder builder = new AlertDialog.Builder(EventEntryAdd_Attachment.this.getActivity());
        builder.setTitle("信息");
        builder.setMessage("确定要删除该附件？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (selectFileEntity == null){
                    Toast.makeText(EventEntryAdd_Attachment.this.getActivity(), "删除失败", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 先进行移除操作
                fileEntityList.remove(ps);
                // 删除本地图片
                if (selectFileEntity.getBitmap() != null){
                    // 删除记录与位置图片
                    fileEntityDao.deleteEventEntry(selectFileEntity.getUuid());
                    //删除保存在本地的图片
                    FileManager.delFile(fileRoute + "/" + selectFileEntity.getFileName());
                }else{
                    // 网络移除就是把isshow 改成 0
                    selectFileEntity.setIsShow("0");
                    removeFileEntityList.add(selectFileEntity);
                }

                // 刷新适配器
                if (fileEntityList != null ) {
                    adapter.notifyDataSetChanged(fileEntityList);
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



    public String getSDPath(){
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (sdCardExist)
        {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();
    }

    /**
     * 显示图片或者创建文件路径
     */
    public void appendImage() {
        sdcard = getSDPath();
        fileRoute = new File(sdcard + "/img/" + imageId + "/");
        //FileManager.delFile(sdcard + "/img/");
        //如果文件夹不存在，
        if (!fileRoute.exists()) {
            fileRoute.mkdirs();
        }

        // 根据相对路径显示图片
        if (sjfjList != null && sjfjList.size() > 0) {
            //每次进来都给fileEntityList重新初始化一次
            fileEntityList =new ArrayList<FileEntity>();
            for(int i=0;i<sjfjList.size();i++){
                fileEntity = new FileEntity();
                if(sjfjList.get(i).getUrl()!=null){
                    fileEntity.setFileUrl(sjfjList.get(i).getUrl());
                    fileEntity.setFileMs(sjfjList.get(i).getWjms());
                }
                fileEntityList.add(fileEntity);
            }
            if (fileEntityList != null) {
                adapter.notifyDataSetChanged(fileEntityList);
            }
        }

        // 本地上传的文件显示
        initLocationFile();
    }


    /**
     * 本地上传的文件显示
     */
    private void initLocationFile(){
        if (imageId != null && !"".equals(imageId)) {
            List<FileEntity> fileList = fileEntityDao.queryList(imageId);
            // 存在本地记录
            try {
                if(fileList != null && fileList.size() > 0){
                    for (FileEntity entity : fileList){
                        String fileName = entity.getFileName();
                        File file = new File(fileRoute + "/" + fileName);
                        if (file.isFile()){
                            Bitmap tupbitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                            if(tupbitmap!=null){
                                // 本地图片
                                entity.setBitmap(tupbitmap);
                                fileEntityList.add(entity);
                            }
                        }

                    }
                }
            } catch (FileNotFoundException e) {
                Log.i("附件展示:","附件照片加载失败>> "+e.getMessage());
            }

        }

        // 刷新适配器
        if (fileEntityList != null ) {
            adapter.notifyDataSetChanged(fileEntityList);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                // 相机与读写的权限
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    // 再次点击
                    addAttachment.performClick();
                } else {
                    // Permission Denied
                    Toast.makeText(getActivity(), "访问相机受限,无法拍照", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @OnClick({R.id.add_attachment,R.id.event_log,R.id.event_relevance})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.add_attachment:
                // 询问定位拍照权限
                int storagePermission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
                if (storagePermission != PackageManager.PERMISSION_GRANTED) {
                    // 添加拍照与读写权限
                    PermissionUtil.requestPermissions(getActivity(),new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE_ASK_PERMISSIONS);
                }else{
                    menuWindow = new SelectPicPopupWindow(EventEntryAdd_Attachment.this.getActivity(), itemsOnClick);
                    // 显示窗口 设置layout在PopupWindow中显示的位置
                    menuWindow.showAtLocation(main, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                }
                break;
            // 办理日志
            case R.id.event_log:
                Intent intent =new Intent(this.getActivity(),EventLogListActivity.class);
                List<TFtSjLogEntity> tFtSjLogEntityList = null;
                if(tFtSjDetailEntity!=null){
                    tFtSjLogEntityList =tFtSjDetailEntity.getSjlogList();
                }
                intent.putExtra("tFtSjLogEntityList", (Serializable) tFtSjLogEntityList);
                startActivity(intent);
                break;
            // 关联事件
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
                    File out = new File(fileRoute, getPhotoFileName(""));
                    if (Build.VERSION.SDK_INT >= 24){
                        uri = FileProvider.getUriForFile(EventEntryAdd_Attachment.this.getActivity(),"com.yang.cameratest.fileprovider",out);
                    }else {
                        uri = Uri.fromFile(out);
                    }
                    //uri = Uri.fromFile(out);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
                    break;
                case R.id.btn_pick_photo:
                    Intent video_intent = new Intent(Intent.ACTION_PICK, null);
                    video_intent.setType("video/*;image/*");
                    /*video/*;audio/*;image/*/
                    startActivityForResult(video_intent, PHOTO_REQUEST_VIDEO);
                    break;
                case R.id.btn_pick_audio:
                    Intent audio_intent = new Intent(Intent.ACTION_GET_CONTENT, null);
                    audio_intent.setType("audio/*");
                    startActivityForResult(audio_intent, PHOTO_REQUEST_AUDIO);
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
                medioType = "1";
                showPhoto(uri);
                break;
            case PHOTO_REQUEST_VIDEO:
                if (data != null) {
                    medioType = "2";
                    uri = data.getData();
                    showPhoto(uri);
                }
                break;
            case PHOTO_REQUEST_AUDIO:
                if (data != null) {
                    medioType = "3";
                    uri = data.getData();
                    showPhoto(uri);
                }
                break;
            case PHOTO_REQUEST_CUT:
                if (resultCode == -1) {
                    if (uri != null) {
                        try {
                            BitmapInfo mapInfo = getBitmapFromUri(uri, EventEntryAdd_Attachment.this.getActivity());
                            if (mapInfo != null) {
                                //先把拍照之后保存在本地的原图删掉。
                                if ("1".equals(medioType)) {
                                    delFile(fileRoute + "/" + photoName);
                                }
                                InitSavePicInLocal(mapInfo);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
        }
    }

    /**
     * 显示照片
     */
    public void showPhoto(Uri uri){
        if(uri!=null) {
            try {
                BitmapInfo mapInfo = getBitmapFromUri(uri, EventEntryAdd_Attachment.this.getActivity());
                if (mapInfo != null) {
                    //先把拍照之后保存在本地的原图删掉。
                    if ("1".equals(medioType)) {
                        delFile(fileRoute + "/" + photoName);
                    }
                    InitSavePicInLocal(mapInfo);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

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
    private void InitSavePicInLocal(BitmapInfo mapInfo) {
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        ByteArrayOutputStream baos = null; // 字节数组输出流
        FileEntity entity = new FileEntity();
        try {
            baos = new ByteArrayOutputStream();
            if (medioType.equals("1")){
                mapInfo.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, baos);
                cutfile = new File(fileRoute, getPhotoFileName(""));
                byte[] byteArray = baos.toByteArray();// 字节数组输出流转换成字节数组
                // 将字节数组写入到刚创建的图片文件中
                fos = new FileOutputStream(cutfile);
                bos = new BufferedOutputStream(fos);
                bos.write(byteArray);
            }else if(medioType.equals("2") || medioType.equals("3")){
                // 视频文件
                String suffix = mapInfo.getFileInfoMap().get("suffix");
                cutfile = new File(fileRoute, getPhotoFileName(suffix));
                FileManager.copyFile(mapInfo.getFileInfoMap().get("uri_path"),cutfile.getPath());
            }

            if(mapInfo.getBitmap() !=null){
                entity.setFileName(cutfile.getName());
                entity.setSjId(imageId);
                entity.setBitmap(mapInfo.getBitmap());
                entity.setFilePath(cutfile.getPath());
                fileEntityDao.saveFileEntity(entity);
            }
            fileEntityList.add(entity);
            if (fileEntityList != null ) {
                adapter.notifyDataSetChanged(fileEntityList);
            }

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
    }

    // 读取uri所在的图片
    public BitmapInfo getBitmapFromUri(Uri uri, Context mContext) {
        BitmapInfo mapInfo = new BitmapInfo();
        Bitmap bitmap = null;
        try {
            // 文件路径uri.getPath()
            String uriPath = getPath(uri);
            // 文件后缀
            String suffix = uriPath.substring(uriPath.lastIndexOf(".")+1);
            switch (suffix){
                case "jpg":case "png":
                    medioType = "1";
                    bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), uri);
                    break;
                case "mp4":case "3gp":
                    medioType = "2";
                    bitmap = ThumbnailUtils.createVideoThumbnail(uriPath, MediaStore.Images.Thumbnails.MINI_KIND);
                    // 视频文件处理
                    Map<String,String> vedio_fileMap = new HashMap<>();
                    vedio_fileMap.put("uri_path",uri.getPath());
                    vedio_fileMap.put("suffix",suffix);
                    mapInfo.setFileInfoMap(vedio_fileMap);
                    break;
                case "mp3":
                    medioType = "3";
                    bitmap = createAlbumArt(uriPath);
                    // 音乐文件处理
                    Map<String,String> audio_fileMap = new HashMap<>();
                    audio_fileMap.put("uri_path",uriPath);
                    audio_fileMap.put("suffix",suffix);
                    mapInfo.setFileInfoMap(audio_fileMap);
                    break;
            }
            if(bitmap != null){
                //创建新的图像大小
                bitmap = Bitmap.createScaledBitmap(bitmap, 600, 600, true);
            }
            mapInfo.setBitmap(bitmap);
            return mapInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get file path
     */
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
        if (cursor != null){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }else{
            return uri.getPath();
        }

    }

    /*
    public File getFileByUri(Uri uri) {
        String path = null;
        if ("file".equals(uri.getScheme())) {
            path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = activity.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(Images.ImageColumns.DATA).append("=").append("'" + path + "'").append(")");
                Cursor cur = cr.query(Images.Media.EXTERNAL_CONTENT_URI, new String[] { Images.ImageColumns._ID, Images.ImageColumns.DATA }, buff.toString(), null, null);
                int index = 0;
                int dataIdx = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(Images.ImageColumns._ID);
                    index = cur.getInt(index);
                    dataIdx = cur.getColumnIndex(Images.ImageColumns.DATA);
                    path = cur.getString(dataIdx);
                }
                cur.close();
                if (index == 0) {
                } else {
                    Uri u = Uri.parse("content://media/external/images/media/" + index);
                    System.out.println("temp uri is :" + u);
                }
            }
            if (path != null) {
                return new File(path);
            }
        } else if ("content".equals(uri.getScheme())) {
            // 4.2.2以后
            String[] proj = { MediaStore.Images.Media.DATA };
            Cursor cursor = activity.getContentResolver().query(uri, proj, null, null, null);
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                path = cursor.getString(columnIndex);
            }
            cursor.close();

            return new File(path);
        } else {
            Log.i(TAG, "Uri Scheme:" + uri.getScheme());
        }
        return null;
    }
    */

    /**
     * @Description 获取专辑封面
     * @param filePath 文件路径，like XXX/XXX/XX.mp3
     * @return 专辑封面bitmap
     */
    public Bitmap createAlbumArt(final String filePath) {
        Bitmap bitmap = null;
        //能够获取多媒体文件元数据的类
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath); //设置数据源
            bitmap = retriever.getFrameAtTime();//得到字节型数据
            if (bitmap == null){
                bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.music_icon); //默认图片
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return bitmap;
    }

    // 使用系统当前日期加以调整作为照片的名称
    @SuppressLint("SimpleDateFormat")
    private String getPhotoFileName(String suffix) {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        photoName = dateFormat.format(date) + ("".equals(suffix) ? ".jpg" : "."+suffix);
        return photoName;
    }


    /**
     * 当查看已经上报事件详情时初始化数据,显示文件
     */
    public void setTFtSjFjEntityList(List<TFtSjFjEntity> sjfjList, boolean flag) {
        this.sjfjList = sjfjList;
        this.flag = flag;
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

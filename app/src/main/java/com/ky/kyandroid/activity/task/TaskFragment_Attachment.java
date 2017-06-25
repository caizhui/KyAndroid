package com.ky.kyandroid.activity.task;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ky.kyandroid.AppContext;
import com.ky.kyandroid.Constants;
import com.ky.kyandroid.R;
import com.ky.kyandroid.entity.TFtSjFjEntity;
import com.ky.kyandroid.util.FileManager;
import com.ky.kyandroid.view.SelectPicPopupWindow;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Caizhui on 2017-6-9.
 * 我的任务附件界面
 */

@SuppressLint("ValidFragment")
public class TaskFragment_Attachment extends Fragment {
    @BindView(R.id.add_attachment)
    Button addAttachment;
    @BindView(R.id.attachment_img)
    ImageView attachmentImg;

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

    private Map<String, Object> map;

    private Bitmap tupbitmap;

    /**
     * 照片名称
     */
    private String photoName;

    public String uuid;

    /**
     * 1，表示拍照，2表示相册
     */
    private String isPhoto;

    @SuppressLint("ValidFragment")
    public TaskFragment_Attachment(String uuid) {
        this.uuid = uuid;
    }

    // 获取事件附件 - 子表信息
    public List<TFtSjFjEntity> sjfjList;

    private boolean flag = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.evententeradd_attachment_fragment, container, false);
        ButterKnife.bind(this, view);
        addAttachment.setVisibility(View.GONE);
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            /* 得到SD卡得路径 */
            sdcard = Environment.getExternalStorageDirectory().getAbsolutePath().toString();
            fileRoute = new File(sdcard + "/img/" + uuid + "/");
            //显示图片或者创建文件路径
            appendImage();
        } else {
            Toast.makeText(TaskFragment_Attachment.this.getActivity(), "没有SD卡", Toast.LENGTH_LONG).show();
        }
        return view;
    }


    /**
     * 显示图片或者创建文件路径
     */
    public void appendImage() {
        //当flag为true时，表示是去查看已经上报事件的图片
        if (flag) {
            if (sjfjList != null && sjfjList.size() > 0) {
                ImageLoader.getInstance().displayImage(Constants.SERVICE_BASE_URL + sjfjList.get(0).getUrl()
                        , attachmentImg, AppContext.getImgBuilder());
            }
        }
        //如果文件夹不存在，表示第一次进来，需要创建文件夹，否则表示已经进来过，我们需要获取图片显示
        if (!fileRoute.exists()) {
            fileRoute.mkdirs();
        } else {
            //访问本地图片信息
            if (uuid != null && !"".equals(uuid)) {
                File files[] = fileRoute.listFiles();
                if (files != null && files.length > 0) {
                    FileInputStream fis = null;
                    try {
                        fis = new FileInputStream(files[0]);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    tupbitmap = BitmapFactory.decodeStream(fis);
                    attachmentImg.setImageBitmap(tupbitmap);
                }
            }
        }
    }

    @OnClick(R.id.add_attachment)
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.add_attachment:
                menuWindow = new SelectPicPopupWindow(TaskFragment_Attachment.this.getActivity(), itemsOnClick);
                // 显示窗口
                menuWindow.showAtLocation(main, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
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
                            Bitmap bitmapFromUri = getBitmapFromUri(uri, TaskFragment_Attachment.this.getActivity());
                            if (bitmapFromUri != null) {
                                //先把拍照之后保存在本地的原图删掉。
                                if ("1".equals(isPhoto)) {
                                    boolean flag = FileManager.delFile(fileRoute + "/" + photoName);
                                }
                                File file = SavePicInLocal(bitmapFromUri);
                                FileInputStream fis = new FileInputStream(file);
                                tupbitmap = BitmapFactory.decodeStream(fis);
                                attachmentImg.setImageBitmap(tupbitmap);
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
    public void setTFtSjFjEntityList(List<TFtSjFjEntity> sjfjList, boolean flag) {
        this.sjfjList = sjfjList;
        this.flag = flag;
    }
}

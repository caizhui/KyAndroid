package com.ky.kyandroid.activity.evententry;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ky.kyandroid.AppContext;
import com.ky.kyandroid.Constants;
import com.ky.kyandroid.R;
import com.ky.kyandroid.adapter.EventImageListAdapter;
import com.ky.kyandroid.db.dao.FileEntityDao;
import com.ky.kyandroid.entity.FileEntity;
import com.ky.kyandroid.entity.TFtSjDetailEntity;
import com.ky.kyandroid.entity.TFtSjEntity;
import com.ky.kyandroid.entity.TFtSjFjEntity;
import com.ky.kyandroid.entity.TFtSjGlsjEntity;
import com.ky.kyandroid.entity.TFtSjLogEntity;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Caizhui on 2017-6-9.
 * 事件附件详情界面
 */

@SuppressLint("ValidFragment")
public class EventEntryDetail_Attachment extends Fragment {

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


    @BindView(R.id.main)
    LinearLayout main;

    /**
     * 附件List
     */
    @BindView(R.id.image_list)
    ListView imageList;


    public String uuid;

    public Intent  intent;

    @SuppressLint("ValidFragment")
    public EventEntryDetail_Attachment(Intent intent, String uuid) {
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

    /**
     * 在Fragment onCreateView方法中缓存View
     * 解决:
     * 做页面切换的时候，只要一来回切换fragment，
     * fragment页面就会重新初始化，
     * 也就是执行onCreateView()方法，导致每次Fragment的布局都重绘，无
     * 法保持Fragment原有状态
     */
    protected WeakReference<View> mRootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null || mRootView.get() == null) {
            View view=inflater.inflate(R.layout.evententerdetail_attachment_fragment, null);
            ButterKnife.bind(this, view);
            tFtSjEntity = (TFtSjEntity) intent.getSerializableExtra("tFtSjEntity");
            fileEntityList =new ArrayList<FileEntity>();
            fileEntityDao = new FileEntityDao();
            adapter = new EventImageListAdapter(fileEntityList,fileEntityDao,EventEntryDetail_Attachment.this.getActivity(),true);
            imageList.setAdapter(adapter);
            if(tFtSjEntity!=null){
                uuid = tFtSjEntity.getId();
            }
            //显示图片
            appendImage();
            mRootView = new WeakReference<View>(view);
        } else {
            ViewGroup parent = (ViewGroup) mRootView.get().getParent();
            if (parent != null) {
                parent.removeView(mRootView.get());
            }
        }
        return mRootView.get();
    }

    /**
     * 显示图片或者创建文件路径
     */
    public void appendImage() {
        //当flag为true时，表示是去查看已经上报事件的图片
        if (flag) {
            if (sjfjList != null && sjfjList.size() > 0) {
                //每次进来都给fileEntityList重新初始化一次
                fileEntityList =new ArrayList<FileEntity>();
                for(int i=0;i<sjfjList.size();i++){
                    fileEntity = new FileEntity();
                    if(sjfjList.get(i).getUrl()!=null){
                        fileEntity.setFileUrl(sjfjList.get(i).getUrl());
                        fileEntity.setFileMs(sjfjList.get(i).getWjms());
                        fileEntity.setFjlx(sjfjList.get(i).getFjlx());
                        fileEntity.setFilePath(sjfjList.get(i).getUrl());
                        fileEntity.setFileType("online");
                        // 文件路径
                        String urlPath = fileEntity.getFileUrl();
                        // 文件后缀
                        String suffix = urlPath.substring(urlPath.lastIndexOf(".") + 1);
                        switch (suffix){
                            case "mp4":
                                if (fileEntity.getBitmap() == null){
                                    fileEntity.setBitmap(createVideoThumbnail(Constants.SERVICE_BASE_URL + urlPath,600,600));
                                }
                                break;
                            case "mp3":
                                //默认图片
                                fileEntity.setBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.music_icon));
                                break;
                        }

                    }
                    fileEntityList.add(fileEntity);
                    //加载完一次就把文件实体清空一次
                }
                if (fileEntityList != null && fileEntityList.size() > 0) {
                    adapter.notifyDataSetChanged(fileEntityList);
                }

            }
        }
    }


    /**
     * 获取网络视频第一帖做为图片
     *
     * @param url
     * @param width
     * @param height
     * @return
     */
    private Bitmap createVideoThumbnail(String url, int width, int height) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            if (Build.VERSION.SDK_INT >= 14) {
                retriever.setDataSource(url, new HashMap<String, String>());
            } else {
                retriever.setDataSource(url);
            }
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException ex) {
        } catch (RuntimeException ex) {
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
            }
        }
        if (bitmap != null) {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        }
        return bitmap;
    }

    @OnClick({R.id.event_log,R.id.event_relevance})
    public void OnClick(View view) {
        switch (view.getId()) {
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

}

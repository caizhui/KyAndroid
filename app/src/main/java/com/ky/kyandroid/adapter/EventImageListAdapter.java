package com.ky.kyandroid.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.ky.kyandroid.AppContext;
import com.ky.kyandroid.Constants;
import com.ky.kyandroid.R;
import com.ky.kyandroid.db.dao.FileEntityDao;
import com.ky.kyandroid.entity.FileEntity;
import com.ky.kyandroid.util.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventImageListAdapter extends BaseAdapter {
    public List<FileEntity> list;
    public Context context;
    private boolean isDetail;
    private FileEntityDao fileEntityDao;

    boolean isShow = false;


    public EventImageListAdapter(Context context) {
        super();
        list = new ArrayList<FileEntity>();
        this.context = context;
    }

    public EventImageListAdapter(List<FileEntity> list, FileEntityDao fileEntityDao, Context context, boolean isDetail) {
        super();
        this.list = list;
        this.context = context;
        this.fileEntityDao = fileEntityDao;
        this.isDetail = isDetail;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.activity_image_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);// 绑定ViewHolder对象
        } else {
            holder = (ViewHolder) convertView.getTag();// 取出ViewHolder对象
        }
        FileEntity viewEntity = list.get(position);
        //fileUrl不为空 表示从后台获取的文件链接，否则是查看本地图片
        if (viewEntity.getFileUrl() != null && !"".equals(viewEntity.getFileUrl())) {
            // 文件路径
            String urlPath = viewEntity.getFileUrl();
            // 文件后缀
            String suffix = urlPath.substring(urlPath.lastIndexOf(".") + 1);
            switch (suffix){
                case "mp4":
                case "mp3":
                    holder.attachmentImg.setImageBitmap(viewEntity.getBitmap());
                    break;
                default:
                    ImageLoader.getInstance().displayImage(Constants.SERVICE_BASE_URL + viewEntity.getFileUrl()
                            , holder.attachmentImg, AppContext.getImgBuilder());
                    break;
            }

        } else {
            holder.attachmentImg.setImageBitmap(viewEntity.getBitmap());
        }
        //判断是否显示描述控件
        if (!viewEntity.isHaveMs()) {
            holder.imageMs.setVisibility(View.GONE);
        }
        if (isDetail) {
            holder.imageMs.setEnabled(false);
            holder.imageMs.setHint("");
            holder.fjlx_text.setVisibility(View.VISIBLE);
            if ("1".equals(viewEntity.getFjlx())) {
                holder.fjlx_text.setText("事件上传附件");
            } else if ("2".equals(viewEntity.getFjlx())) {
                holder.fjlx_text.setText("事件处理附件");
            }
        }

        holder.imageMs.setText(viewEntity.getFileMs());
        holder.imageMs.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                FileEntity selectEntity = list.get(position);
                if (selectEntity != null) {
                    selectEntity.setFileMs(editable.toString());
                    fileEntityDao.updateFileEntityByFileName(selectEntity, "fileMs");
                }
            }
        });

        holder.attachmentImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FileEntity selectEntity = list.get(position);
                // 在线处理
                String ip = selectEntity.getFileType().equals("online") ? Constants.SERVICE_BASE_URL : "";
                // 文件路径
                String uriPath = ip + selectEntity.getFilePath();
                // 文件后缀
                String suffix = uriPath.substring(uriPath.lastIndexOf(".") + 1);
                AlertDialog dialog = new AlertDialog.Builder(context).create();
                View dialogV = null;
                int dialogH = LayoutParams.WRAP_CONTENT;
                switch (suffix) {
                    case "mp4":
                    case "mp3":
                        VideoView vv = new VideoView(context);
                        vv.setVideoURI(Uri.parse(uriPath));
                        vv.setMediaController(new MediaController(context));
                        vv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                        if ("mp3".equals(suffix)) {
                            vv.requestFocus();
                            dialogH = 450;
                        }

                        vv.start();
                        dialogV = vv;
                        dialog.setView(dialogV);
                        break;
                    default:
                        dialogV = getView(holder.attachmentImg, position);
                        dialog.setView(dialogV);
                        break;
                }
                if (dialogV != null) {
                    dialog.show();
                    //设置窗口的大小
                    dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, dialogH);
                    final AlertDialog finalDialog = dialog;
                    dialogV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finalDialog.dismiss();
                        }
                    });
                }

            }
        });
        return convertView;
    }

    private ImageView getView(ImageView attachmentImg, int position) {
        ImageView imgView = new ImageView(context);
        imgView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        //当从后台拿时候，需要先从缓存中拿图片。
        if (list.get(position).getFileUrl() != null) {
            Bitmap bitmap = ImageLoader.getInstance().loadImageSync(Constants.SERVICE_BASE_URL + list.get(position).getFileUrl());
            attachmentImg.setImageBitmap(bitmap);
        }
       /* ImageLoader.getInstance().displayImage(Constants.SERVICE_BASE_URL + list.get(position).getFileUrl()
                ,attachmentImg, AppContext.getImgBuilder());*/
        // InputStream is = context.getResources().(attachmentImg.getDrawable());
        Drawable drawable = attachmentImg.getDrawable();
        imgView.setImageDrawable(drawable);

        return imgView;
    }


    public void notifyDataSetChanged(List<FileEntity> list) {
        this.list = list;
        super.notifyDataSetChanged();
    }


    class ViewHolder {
        @BindView(R.id.attachment_img)
        ImageView attachmentImg;

        @BindView(R.id.image_ms)
        EditText imageMs;

        @BindView(R.id.fjlx_text)
        TextView fjlx_text;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}

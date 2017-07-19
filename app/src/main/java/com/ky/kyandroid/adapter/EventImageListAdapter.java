package com.ky.kyandroid.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ky.kyandroid.AppContext;
import com.ky.kyandroid.Constants;
import com.ky.kyandroid.R;
import com.ky.kyandroid.db.dao.FileEntityDao;
import com.ky.kyandroid.entity.FileEntity;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventImageListAdapter extends BaseAdapter {
    public List<FileEntity> list;
    public Context context;
    private boolean isDetail;
    private FileEntityDao fileEntityDao;

    boolean isShow =false;


    public EventImageListAdapter(Context context) {
        super();
        list = new ArrayList<FileEntity>();
        this.context = context;
    }

    public EventImageListAdapter(List<FileEntity> list,FileEntityDao fileEntityDao, Context context,boolean isDetail) {
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
        //fileUrl不为空 表示从后台获取的文件链接，否则是查看本地图片
        if(list.get(position).getFileUrl()!=null &&  !"".equals(list.get(position).getFileUrl())){
            ImageLoader.getInstance().displayImage(Constants.SERVICE_BASE_URL + list.get(position).getFileUrl()
                    ,holder.attachmentImg, AppContext.getImgBuilder());
        }else{
            holder.attachmentImg.setImageBitmap(list.get(position).getBitmap());
        }
        //判断是否显示描述控件
        if(!list.get(position).isHaveMs()){
            holder.imageMs.setVisibility(View.GONE);
        }
        if(isDetail){
            holder.imageMs.setEnabled(false);
            holder.imageMs.setHint("");
            holder.fjlx_text.setVisibility(View.VISIBLE);
            if("1".equals(list.get(position).getFjlx())){
                holder.fjlx_text.setText("事件上传附件");
            }else if("2".equals(list.get(position).getFjlx())){
                holder.fjlx_text.setText("事件处理附件");
            }
        }
        holder.imageMs.setText(list.get(position).getFileMs());
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
                if (selectEntity != null){
                    selectEntity.setFileMs(editable.toString());
                    fileEntityDao.updateFileEntityByFileName(selectEntity,"fileMs");
                }
            }
        });

        holder.attachmentImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog  dialog = new AlertDialog.Builder(context).create();
                ImageView imgView = getView(holder.attachmentImg);
                dialog.setView(imgView);
                dialog.show();

                // 点击图片消失
                final AlertDialog finalDialog = dialog;
                imgView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        finalDialog.dismiss();
                    }
                });
            }
        });
        return convertView;
    }
    private ImageView getView(ImageView attachmentImg) {
        ImageView imgView = new ImageView(context);
        imgView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
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

     @BindView(R.id.attachment_img1)
     ImageView attachment_img1;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}

package com.ky.kyandroid.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;

import com.ky.kyandroid.AppContext;
import com.ky.kyandroid.Constants;
import com.ky.kyandroid.R;
import com.ky.kyandroid.entity.FileEntity;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventImageListAdapter extends BaseAdapter {
    public List<FileEntity> list;
    public Context context;


    public EventImageListAdapter(Context context) {
        super();
        list = new ArrayList<FileEntity>();
        this.context = context;
    }

    public EventImageListAdapter(List<FileEntity> list, Context context) {
        super();
        this.list = list;
        this.context = context;
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
        ViewHolder holder;
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
        holder.imageMs.setText(list.get(position).getFileMs());
        return convertView;
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

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}

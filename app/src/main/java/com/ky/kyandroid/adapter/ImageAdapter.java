package com.ky.kyandroid.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

/**
 * Created by Caizhui on 2017/6/19.
 */

public class ImageAdapter extends BaseAdapter {
    //声明Context
    private Context context;
    //图片源数组
    private Integer[] imageInteger= new Integer[10];

    //声明 ImageAdapter
    public ImageAdapter(Context c, Integer imageInteger[]){
        context = c;
        this.imageInteger = imageInteger;
    }

    @Override
    //获取图片的个数
    public int getCount() {
        return imageInteger.length;
    }

    @Override
    //获取图片在库中的位置
    public Object getItem(int position) {

        return position;
    }

    @Override
    //获取图片在库中的位置
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageView = new ImageView(context);
        //给ImageView设置资源
        imageView.setImageResource(imageInteger[position]);
        //设置比例类型
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        //设置布局 图片128x192显示
        imageView.setLayoutParams(new Gallery.LayoutParams(128, 192));
        return imageView;
    }
}

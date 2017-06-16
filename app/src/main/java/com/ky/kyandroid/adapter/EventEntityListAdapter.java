package com.ky.kyandroid.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ky.kyandroid.R;
import com.ky.kyandroid.entity.EventEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventEntityListAdapter extends BaseAdapter {
    public List<EventEntity> list;
    public Context context;


    public EventEntityListAdapter(Context context) {
        super();
        list = new ArrayList<EventEntity>();
        this.context = context;
    }

    public EventEntityListAdapter(List<EventEntity> list, Context context) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.activity_evententry_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);// 绑定ViewHolder对象
        } else {
            holder = (ViewHolder) convertView.getTag();// 取出ViewHolder对象
        }
        holder.thingName.setText(list.get(position).getSJMC());
        holder.thingAddress.setText(list.get(position).getFSDD());
        holder.thingTime.setText(list.get(position).getFSSJ());
        return convertView;
    }

/**
 * 存放控件
 */
    class ViewHolder {
        @BindView(R.id.thing_name)
        TextView thingName;
        @BindView(R.id.thing_address)
        TextView thingAddress;
        @BindView(R.id.thing_time)
        TextView thingTime;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


    public void notifyDataSetChanged(List<EventEntity> list) {
        this.list = list;
        super.notifyDataSetChanged();
    }


}

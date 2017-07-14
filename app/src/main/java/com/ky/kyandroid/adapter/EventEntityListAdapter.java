package com.ky.kyandroid.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ky.kyandroid.R;
import com.ky.kyandroid.entity.MsgNoticeEntity;
import com.ky.kyandroid.entity.TFtSjEntity;
import com.ky.kyandroid.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventEntityListAdapter extends BaseAdapter {
    public List<TFtSjEntity> list;
    public Context context;


    public EventEntityListAdapter(Context context) {
        super();
        this.list = new ArrayList<TFtSjEntity>();
        this.context = context;
    }

    public EventEntityListAdapter(List<TFtSjEntity> list, Context context) {
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
        holder.thingName.setText(list.get(position).getSjmc());
        holder.thingAddress.setText(list.get(position).getFsdd());
        holder.thingTime.setText(list.get(position).getFssj());
        if("1".equals(list.get(position).getZt())){
            holder.thingStatus.setText("录入");
        }else if("0".equals(list.get(position).getZt())){
            holder.thingStatus.setText("草稿");
        }else{
            String ztName = list.get(position).getZtname();
            if (StringUtils.isBlank(ztName)){
                holder.thingStatus.setText("未知状态");
            }else{
                holder.thingStatus.setText(list.get(position).getZtname());
            }

        }
        return convertView;
    }

/**
 * 存放控件
 */
    class ViewHolder {
        @BindView(R.id.thing_name)
        TextView thingName;
        @BindView(R.id.thing_status)
        TextView thingStatus;
        @BindView(R.id.thing_address)
        TextView thingAddress;
        @BindView(R.id.thing_time)
        TextView thingTime;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


    public void notifyDataSetChanged(List<TFtSjEntity> list) {
        this.list = list;
        super.notifyDataSetChanged();
    }

    /**
     * @param addList
     */
    public void addDataSetChanged(List<TFtSjEntity> addList){
        this.list.addAll(addList);
        this.notifyDataSetChanged(list);
    }

    public List<TFtSjEntity> getList() {
        return list;
    }
}

package com.ky.kyandroid.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ky.kyandroid.R;
import com.ky.kyandroid.entity.TaskEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskEntityListAdapter extends BaseAdapter {
    public List<TaskEntity> list;
    public Context context;


    public TaskEntityListAdapter(Context context) {
        super();
        list = new ArrayList<TaskEntity>();
        this.context = context;
    }

    public TaskEntityListAdapter(List<TaskEntity> list, Context context) {
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
            convertView = View.inflate(context, R.layout.activity_task_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);// 绑定ViewHolder对象
        } else {
            holder = (ViewHolder) convertView.getTag();// 取出ViewHolder对象
        }
        holder.thingName.setText(list.get(position).getSjmc());
        holder.thingAddress.setText(list.get(position).getFsdd());
        holder.thingTime.setText(list.get(position).getFssj());
        holder.thingStatus.setText(list.get(position).getZtname());
        holder.taskContentText.setText(list.get(position).getRwnr());
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
        @BindView(R.id.task_content_text)
        TextView taskContentText;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


    public void notifyDataSetChanged(List<TaskEntity> list) {
        this.list = list;
        super.notifyDataSetChanged();
    }

    /**
     * @param addList
     */
    public void addDataSetChanged(List<TaskEntity> addList){
        this.list.addAll(addList);
        this.notifyDataSetChanged(list);
    }

    public List<TaskEntity> getList() {
        return list;
    }

}

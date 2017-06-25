package com.ky.kyandroid.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ky.kyandroid.R;
import com.ky.kyandroid.entity.TFtSjLogEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventLogListAdapter extends BaseAdapter {
    public List<TFtSjLogEntity> list;
    public Context context;


    public EventLogListAdapter(Context context) {
        super();
        list = new ArrayList<TFtSjLogEntity>();
        this.context = context;
    }

    public EventLogListAdapter(List<TFtSjLogEntity> list, Context context) {
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
            convertView = View.inflate(context, R.layout.activity_eventlog_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);// 绑定ViewHolder对象
        } else {
            holder = (ViewHolder) convertView.getTag();// 取出ViewHolder对象
        }
        holder.eventlogDepartment.setText(list.get(position).getYhbm()+"("+list.get(position).getYhm()+")");
        holder.eventlogCz.setText(list.get(position).getCz());
        holder.eventlogCzsj.setText(list.get(position).getCzsj());
        holder.eventlogCzsj.setText(list.get(position).getCzsj());
        if (!"".equals(list.get(position).getCzms())) {
            holder.eventlogCzms.setText(list.get(position).getCzms());
        } else {
            holder.czmsLinearlayout.setVisibility(View.GONE);
        }
        return convertView;
    }

    static class ViewHolder {
        /**
         * 部门
         */
        @BindView(R.id.eventlog_department)
        TextView eventlogDepartment;
        /**
         * 操作
         */
        @BindView(R.id.eventlog_cz)
        TextView eventlogCz;
        /**
         * 操作时间
         */
        @BindView(R.id.eventlog_czsj)
        TextView eventlogCzsj;
        /**
         * 操作描述
         */
        @BindView(R.id.eventlog_czms)
        TextView eventlogCzms;
        @BindView(R.id.czms_linearlayout)
        LinearLayout czmsLinearlayout;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

/**
 * 存放控件
 */


}

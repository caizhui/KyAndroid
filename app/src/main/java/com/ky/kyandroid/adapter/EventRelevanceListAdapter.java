package com.ky.kyandroid.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ky.kyandroid.R;
import com.ky.kyandroid.entity.TFtSjGlsjEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 事件关联Adapter
 */
public class EventRelevanceListAdapter extends BaseAdapter {
    public List<TFtSjGlsjEntity> list;
    public Context context;


    public EventRelevanceListAdapter(Context context) {
        super();
        list = new ArrayList<TFtSjGlsjEntity>();
        this.context = context;
    }

    public EventRelevanceListAdapter(List<TFtSjGlsjEntity> list, Context context) {
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
            convertView = View.inflate(context, R.layout.activity_eventrelevance_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);// 绑定ViewHolder对象
        } else {
            holder = (ViewHolder) convertView.getTag();// 取出ViewHolder对象
        }
        holder.eventrelevanceGlsj.setText(list.get(position).getGlsjId() + "(" + list.get(position).getGlgx() + ")");
        holder.eventrelevanceLrr.setText(list.get(position).getLrr());
        holder.eventrelevanceLrbm.setText(list.get(position).getLrbm());
        holder.eventrelevanceLrsj.setText(list.get(position).getLrsj());
        return convertView;
    }

    class ViewHolder {
        /**
         * 关联事件
         */
        @BindView(R.id.eventrelevance_glsj)
        TextView eventrelevanceGlsj;
        /**
         *  录入人
         */
        @BindView(R.id.eventrelevance_lrr)
        TextView eventrelevanceLrr;
        /**
         * 录入部门
         */
        @BindView(R.id.eventrelevance_lrbm)
        TextView eventrelevanceLrbm;
        /**
         *录入时间
         */
        @BindView(R.id.eventrelevance_lrsj)
        TextView eventrelevanceLrsj;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

/**
 * 存放控件
 */


}

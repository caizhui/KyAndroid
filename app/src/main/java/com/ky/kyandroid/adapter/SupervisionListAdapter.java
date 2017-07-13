package com.ky.kyandroid.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ky.kyandroid.R;
import com.ky.kyandroid.entity.TFtDbEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SupervisionListAdapter extends BaseAdapter {
    public List<TFtDbEntity> list;
    public Context context;


    public SupervisionListAdapter(Context context) {
        super();
        list = new ArrayList<TFtDbEntity>();
        this.context = context;
    }

    public SupervisionListAdapter(List<TFtDbEntity> list, Context context) {
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
            convertView = View.inflate(context, R.layout.activity_supervision_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);// 绑定ViewHolder对象
        } else {
            holder = (ViewHolder) convertView.getTag();// 取出ViewHolder对象
        }
         holder.dbName.setText(list.get(position).getDbmc());
         holder.dbGlsj.setText(list.get(position).getSjmc());
         holder.dbTime.setText(list.get(position).getDbsj());
        if ("1".equals(list.get(position).getDblx())) {
            holder.dbStatus.setText("超时");
        } else if ("2".equals(list.get(position).getDblx())) {
            holder.dbStatus.setText("不落实");
        } else {
                holder.dbStatus.setText("落实不彻底");
        }
        return convertView;
    }

    /**
     * 存放控件
     */

    public void notifyDataSetChanged(List<TFtDbEntity> list) {
        this.list = list;
        super.notifyDataSetChanged();
    }


    static class ViewHolder {
        @BindView(R.id.db_name)
        TextView dbName;
        @BindView(R.id.db_status)
        TextView dbStatus;
        @BindView(R.id.db_glsj)
        TextView dbGlsj;
        @BindView(R.id.db_time)
        TextView dbTime;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

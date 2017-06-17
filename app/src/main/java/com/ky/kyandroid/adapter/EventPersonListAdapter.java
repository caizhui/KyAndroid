package com.ky.kyandroid.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ky.kyandroid.R;
import com.ky.kyandroid.entity.TFtSjRyEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventPersonListAdapter extends BaseAdapter {
    public List<TFtSjRyEntity> list;
    public Context context;


    public EventPersonListAdapter(Context context) {
        super();
        list = new ArrayList<TFtSjRyEntity>();
        this.context = context;
    }

    public EventPersonListAdapter(List<TFtSjRyEntity> list, Context context) {
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
            convertView = View.inflate(context, R.layout.activity_person_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);// 绑定ViewHolder对象
        } else {
            holder = (ViewHolder) convertView.getTag();// 取出ViewHolder对象
        }
        holder.personName.setText(list.get(position).getXm());
        holder.personSex.setText(list.get(position).getXb());
        holder.personIdcard.setText(list.get(position).getZjhm());
        return convertView;
    }
    /**
     * 存放控件
     */
    class ViewHolder {
        @BindView(R.id.person_name)
        TextView personName;
        @BindView(R.id.person_sex)
        TextView personSex;
        @BindView(R.id.person_idcard)
        TextView personIdcard;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public void notifyDataSetChanged(List<TFtSjRyEntity> list) {
        this.list = list;
        super.notifyDataSetChanged();
    }
}

package com.ky.kyandroid.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ky.kyandroid.R;
import com.ky.kyandroid.entity.YpqbmEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DisplayDepartmentListAdapter extends BaseAdapter {
    public List<YpqbmEntity> list;
    public Context context;


    public DisplayDepartmentListAdapter(Context context) {
        super();
        list = new ArrayList<YpqbmEntity>();
        this.context = context;
    }

    public DisplayDepartmentListAdapter(List<YpqbmEntity> list, Context context) {
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
            convertView = View.inflate(context, R.layout.activity_ypqbm_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);// 绑定ViewHolder对象
        } else {
            holder = (ViewHolder) convertView.getTag();// 取出ViewHolder对象
        }
        String bmlx="";
        if("1".equals(list.get(position).getBmlx())){
            bmlx = "事权单位";
        }else if("2".equals(list.get(position).getBmlx())){
            bmlx = "稳控单位";
        }else if("3".equals(list.get(position).getBmlx())){
            bmlx = "协办单位";
        }else if("4".equals(list.get(position).getBmlx())){
            bmlx = "责任单位";
        }
        holder.departmentText.setText(list.get(position).getBmmc() + "(" + bmlx + ")");
        holder.handlerTime.setText(list.get(position).getClsx());
        holder.handlerText.setText(list.get(position).getRwnr());
        return convertView;
    }
    public void notifyDataSetChanged(List<YpqbmEntity> list) {
        this.list = list;
        super.notifyDataSetChanged();
    }

    static class ViewHolder {
        /**
         *  部门
         */
        @BindView(R.id.department_text)
        TextView departmentText;
        /**
         *  处理时限
         */
        @BindView(R.id.handler_time)
        TextView handlerTime;
        /**
         *  处理内容
         */
        @BindView(R.id.handler_text)
        TextView handlerText;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

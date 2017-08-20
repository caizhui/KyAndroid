package com.ky.kyandroid.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ky.kyandroid.R;
import com.ky.kyandroid.entity.TFtAppReportEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class JobBulletinListAdapter extends BaseAdapter {
    public List<TFtAppReportEntity> list;
    public Context context;


    public JobBulletinListAdapter(Context context) {
        super();
        list = new ArrayList<TFtAppReportEntity>();
        this.context = context;
    }

    public JobBulletinListAdapter(List<TFtAppReportEntity> list, Context context) {
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
            convertView = View.inflate(context, R.layout.activity_jobbulletin_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);// 绑定ViewHolder对象
        } else {
            holder = (ViewHolder) convertView.getTag();// 取出ViewHolder对象
        }
         holder.jobTitle.setText(list.get(position).getTitle());
         holder.jobSbr.setText(list.get(position).getRepBy());
         holder.jobTime.setText(list.get(position).getRepTime());
        return convertView;
    }

    /**
     * 存放控件
     */

    public void notifyDataSetChanged(List<TFtAppReportEntity> list) {
        this.list = list;
        super.notifyDataSetChanged();
    }

    /**
     * @param addList
     */
    public void addDataSetChanged(List<TFtAppReportEntity> addList){
        this.list.addAll(addList);
        this.notifyDataSetChanged(list);
    }

    public List<TFtAppReportEntity> getList() {
        return list;
    }

    static class ViewHolder {
        @BindView(R.id.job_title)
        TextView jobTitle;
        @BindView(R.id.job_time)
        TextView jobTime;
        @BindView(R.id.job_sbr)
        TextView jobSbr;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

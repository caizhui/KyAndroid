package com.ky.kyandroid.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ky.kyandroid.R;
import com.ky.kyandroid.activity.evententry.EventEntryAdd_Person;
import com.ky.kyandroid.db.dao.DescEntityDao;
import com.ky.kyandroid.db.dao.TFtSjRyEntityDao;
import com.ky.kyandroid.entity.MsgNoticeEntity;
import com.ky.kyandroid.entity.TFtSjRyEntity;
import com.ky.kyandroid.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventPersonListAdapter extends BaseAdapter {
    public List<TFtSjRyEntity> list;
    public Context context;
    public DescEntityDao descEntityDao;
    private TFtSjRyEntityDao tFtSjRyEntityDao;
    private boolean delBtn;

    public EventPersonListAdapter(Context context) {
        super();
        list = new ArrayList<TFtSjRyEntity>();
        this.context = context;
    }

    public EventPersonListAdapter(List<TFtSjRyEntity> list, DescEntityDao descEntityDao, TFtSjRyEntityDao tFtSjRyEntityDao, Context context,boolean delBtn) {
        super();
        this.list = list;
        this.descEntityDao = descEntityDao;
        this.tFtSjRyEntityDao = tFtSjRyEntityDao;
        this.context = context;
        this.delBtn = delBtn;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.activity_person_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);// 绑定ViewHolder对象
        } else {
            holder = (ViewHolder) convertView.getTag();// 取出ViewHolder对象
        }
        TFtSjRyEntity sjRyEntity = list.get(position);
        // 姓名
        holder.personName.setText(sjRyEntity.getXm());
        // 手机电话
        String yddh = StringUtils.isBlank(sjRyEntity.getYddh()) ? "" : ("(" + sjRyEntity.getYddh() + ")");
        holder.person_phone.setText(yddh);
        // 性别
        holder.personSex.setText("1".equals(sjRyEntity.getXb()) ? "男" : "女");
        // 民族
        holder.person_mz.setText(descEntityDao.queryName("nation", sjRyEntity.getMz()));
        // 是否党员
        holder.person_dy.setText(descEntityDao.queryName("dy", sjRyEntity.getSfdy()));
        // 证件类型
        String zjlx = StringUtils.isBlank(sjRyEntity.getZjlx()) ? "" : ("(" + descEntityDao.queryName("crd", sjRyEntity.getZjlx()) + ")");
        // 证件号码
        String zjhm = StringUtils.isBlank(sjRyEntity.getZjhm()) ? "未填写" : (sjRyEntity.getZjhm() + zjlx);
        holder.personIdcard.setText(zjhm);
        // 是否显示删除记录按钮
        if (delBtn) {
            holder.delete_btn.setVisibility(View.VISIBLE);
            holder.delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("信息");
                    builder.setMessage("确定要删除该条记录吗？");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            TFtSjRyEntity sjRyEntity = list.get(position);
                            list.remove(position);
                            boolean flag = tFtSjRyEntityDao.deleteEventEntry(sjRyEntity.getUuid());
                            if (flag) {
                                Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "删除失败", Toast.LENGTH_SHORT).show();
                            }
                            notifyDataSetChanged();
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    builder.create().show();
                }
            });
        }else{
            holder.delete_btn.setVisibility(View.GONE);
        }
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
        @BindView(R.id.person_phone)
        TextView person_phone;
        @BindView(R.id.person_mz)
        TextView person_mz;
        @BindView(R.id.person_dy)
        TextView person_dy;
        @BindView(R.id.delete_btn)
        ImageButton delete_btn;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public void notifyDataSetChanged(List<TFtSjRyEntity> list) {
        this.list = list;
        super.notifyDataSetChanged();
    }
}

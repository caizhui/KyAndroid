package com.ky.kyandroid.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.ky.kyandroid.R;

/**
 * 父ListView适配器
 * 
 * @author zihao
 * 
 */
public class JobGroupAdapter extends BaseAdapter {

	Context mContext;// 上下文对象
	String[][] mGroupItems;// item标题数组
	int mPosition = 0;// 选中的位置

	/**
	 * 构造方法
	 *
	 * @param context
	 *            // 上下文对象
	 * @param groupArr
	 *            // item标题数组
	 */
	public JobGroupAdapter(Context context, String[][] groupArr) {
		this.mContext = context;
		this.mGroupItems = groupArr;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		// 初始化布局控件
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.group_item_layout, null);
			holder.groupName = (TextView) convertView.findViewById(R.id.group_textView);
			holder.checkBox = (CheckBox) convertView.findViewById(R.id.group_checkBox);
			holder.checkBox.setVisibility(View.INVISIBLE);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		String[] index_group = mGroupItems[position];

		// 设置控件内容
		holder.groupName.setText(index_group[1]);
		// 是否选中
		holder.checkBox.setChecked("1".equals(index_group[0]));
		holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
				if (checked){
					mGroupItems[position][0] = "1";
				}else{
					mGroupItems[position][0] = "0";
				}
			}
		});

		if (mPosition == position) {
			holder.groupName.setTextColor(mContext.getResources().getColor(
					R.color.list_item_text_pressed_bg));
			convertView.setBackgroundColor(mContext.getResources().getColor(
					R.color.group_item_pressed_bg));
		} else {
			holder.groupName.setTextColor(mContext.getResources().getColor(
					android.R.color.black));
			convertView.setBackgroundColor(mContext.getResources().getColor(
					R.color.group_item_bg));
		}

		return convertView;
	}

	/**
	 * 获取item总数
	 */
	@Override
	public int getCount() {
		return mGroupItems.length;
	}

	/**
	 * 获取某一个Item的内容
	 */
	@Override
	public Object getItem(int position) {
		return mGroupItems[position];
	}

	/**
	 * 获取当前item的ID
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	static class ViewHolder {
		/** 父Item名称 **/
		TextView groupName;
		/** 是否选中 **/
		CheckBox checkBox;
	}

	/**
	 * 设置选择的位置
	 * 
	 * @param position
	 */
	public void setSelectedPosition(int position) {
		this.mPosition = position;
	}

	public void setChildData(String[][] mGroupItems){
		this.mGroupItems = mGroupItems;
	}

	public String[][] getmGroupItems() {
		return this.mGroupItems;
	}
}

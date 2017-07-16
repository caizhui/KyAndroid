package com.ky.kyandroid.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.DrawableUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ky.kyandroid.Constants;
import com.ky.kyandroid.R;
import com.ky.kyandroid.entity.MsgNoticeEntity;
import com.ky.kyandroid.util.OkHttpUtil;
import com.ky.kyandroid.util.StringUtils;
import com.ky.kyandroid.view.SlideView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 类名称：消息通知提醒适配器<br/>
 * 类描述：<br/>
 * 
 * 创建人： Cz <br/>
 * 创建时间：2016年9月28日 下午2:45:32 <br/>
 * @updateRemark 修改备注：
 *     
 */
public class MsgNoticeListAdapter extends BaseAdapter {
	private List<MsgNoticeEntity> list;
	private Context context;
	private Handler mHandler;
	private SlideView.OnSlideListener onSlideListener;

	public MsgNoticeListAdapter(Context context,Handler mHandler, SlideView.OnSlideListener onSlideListener) {
		super();
		list= new ArrayList<MsgNoticeEntity>();
		this.mHandler = mHandler;
		this.context = context;
		this.onSlideListener = onSlideListener;
	}

	public MsgNoticeListAdapter(List<MsgNoticeEntity> list, Context context, SlideView.OnSlideListener onSlideListener) {
		super();
		this.list = list;
		this.context = context;
		this.onSlideListener = onSlideListener;
	}

	public List<MsgNoticeEntity> getList() {
		return list;
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
        SlideView slideView = (SlideView) convertView;
        if (slideView == null) {
        	slideView = new SlideView(context);
            View itemView = View.inflate(context, R.layout.msg_info_listview_item, null);
            slideView.setContentView(itemView);
            holder = new ViewHolder();
        	holder.tv_center_name = (TextView) slideView.findViewById(R.id.tv_center_name);
			holder.tv_center_name2 = (TextView) slideView.findViewById(R.id.tv_center_name2);
			holder.tv_center_name3 = (TextView) slideView.findViewById(R.id.tv_center_name3);
			holder.tv_right_name = (TextView) slideView.findViewById(R.id.tv_right_name);
			holder.tv_right_name2 = (TextView) slideView.findViewById(R.id.tv_right_name2);
			holder.deleteHolder = (ViewGroup)slideView.findViewById(R.id.holder);
			slideView.setOnSlideListener(onSlideListener);
            slideView.setTag(holder);
		}else {
            holder = (ViewHolder) slideView.getTag();
        }
        
		MsgNoticeEntity entity = list.get(position);
		entity.slideView = slideView;
		entity.slideView.shrink();
		// 发送人
		holder.tv_center_name.setText("发送人:" + entity.getFsr());
		// 消息类型
		if ("1".equals(entity.getLx())) {
			holder.tv_center_name2.setText("消息类型:事件处理");
		} else if("2".equals(entity.getLx())) {
			holder.tv_center_name2.setText("消息类型:督办处理");
		}else if("3".equals(entity.getLx())){
			holder.tv_center_name2.setText("消息类型:申请延期");
		}
		// 内容
		holder.tv_center_name3.setText("内容信息:" + entity.getNr());
		// 时间
		holder.tv_right_name.setText(entity.getFssj());
		// 已读与未读 根据阅读时间判断
		if (StringUtils.isBlank(entity.getYdsj())) {
			holder.tv_right_name2.setText("未读");
			holder.tv_right_name2.setBackground(context.getResources().getDrawable(R.drawable.meg_bg_shape_red));
		}else{
			holder.tv_right_name2.setText("已读");
			holder.tv_right_name2.setBackground(context.getResources().getDrawable(R.drawable.meg_bg_shape_gray));
		}
		holder.deleteHolder.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MsgNoticeEntity entity = list.get(position);
				updateTaskState(entity.getId(),"czlx","remove");
				list.remove(position);
				notifyDataSetChanged();
			}
		});
		return slideView;
	}
	
	/** 存放控件 */
	public class ViewHolder {
		public TextView tv_left_name;
		public TextView tv_left_name2;
		public TextView tv_center_name;
		public TextView tv_center_name2;
		public TextView tv_center_name3;
		public TextView tv_right_name;
		public TextView tv_right_name2;
		public ViewGroup deleteHolder;
	}

	/**
	 * @param list
	 */
	public void notifyDataSetChanged(List<MsgNoticeEntity> list) {
		this.list = list;
		super.notifyDataSetChanged();
	}
	
	/**
	 * @param addList
	 */
	public void addDataSetChanged(List<MsgNoticeEntity> addList){
		this.list.addAll(addList);
		this.notifyDataSetChanged(list);
	}
	
	/**
	 * 更改已读状态
	 * 
	 * @param id
	 */
	private void updateTaskState(String id,String fileName,String fileValue){
		if (!StringUtils.isBlank(id)) {
			Map<String,String> requestMap = new HashMap<String, String>();
			requestMap.put("id", id);
			requestMap.put(fileName, fileValue);
			Log.i("MsgNoticeListAdapter", "更新: " +fileName + "=" + fileValue);
			OkHttpUtil.sendRequest(Constants.SERVICE_NOTICE_EDIT_HADLE, requestMap, new Callback() {
				@Override
				public void onResponse(Call arg0, Response response) throws IOException {
					Log.i("MsgNoticeListAdapter", response.isSuccessful() ? "成功" : "失败");
					if (response.isSuccessful()){
						mHandler.sendEmptyMessage(9);
					}
				}
				@Override
				public void onFailure(Call arg0, IOException arg1) {}
			});
		}
	}
}


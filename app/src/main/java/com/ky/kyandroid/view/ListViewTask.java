package com.ky.kyandroid.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import com.ky.kyandroid.entity.MsgNoticeEntity;

/**
 * 类名称：左滑动ListView<br/>
 * 类描述：<br/>
 * 
 * 创建人： Cz <br/>
 * 创建时间：2016年11月2日 下午4:41:24 <br/>
 * @updateRemark 修改备注：
 *     
 */
public class ListViewTask extends ListView {

	/** ListViewTask */
	private static final String TAG = "ListViewTask";

	/** SlideView */
	private SlideView mFocusedItemView;

	public ListViewTask(Context context) {
		super(context);
	}

	public ListViewTask(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ListViewTask(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * 收缩列表子项
	 */
	public void shrinkListItem(int position) {
		View item = getChildAt(position);
		if (item != null) {
			try {
				((SlideView) item).shrink();
			} catch (ClassCastException e) {
				e.printStackTrace();
			}
		}
	}

	private int flag = -1;
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN: {
				int x = (int) event.getX();
				int y = (int) event.getY();
				int position = pointToPosition(x, y);
				Log.e(TAG, "postion=" + position + ",x=" + x + ",y=" + y);
				if (position != INVALID_POSITION) {
					MsgNoticeEntity task = (MsgNoticeEntity) getItemAtPosition(position);
					mFocusedItemView = task.slideView;
					Log.e(TAG, "FocusedItemView=" + mFocusedItemView);
				}
			}
		}

		if (mFocusedItemView != null) {
			flag = mFocusedItemView.onRequireTouchEvent(event);
		}
		if (flag == 0) {
			Log.e(TAG, "已经返回ture");
			return true;
		}else{
			return super.onTouchEvent(event);
		}
	}
	

}

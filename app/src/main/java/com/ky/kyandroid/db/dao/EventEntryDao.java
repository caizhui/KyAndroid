package com.ky.kyandroid.db.dao;

import android.util.Log;

import com.ky.kyandroid.db.BaseDao;
import com.ky.kyandroid.entity.EventEntryEntity;

import org.xutils.ex.DbException;

import java.util.List;


/**
 * 类名称：事件录入dao<br/>
 */
class EventEntryDao extends BaseDao {

	/** 标识 */
	private static final String TAG = "EventEntryDao";
	
	/**
	 * 保存系统字典项
	 * 
	 * @param entity
	 * @return
	 */
	public boolean saveEventEntryEntity(EventEntryEntity entity){
		boolean flag = false;
		try {
			saveOrUpdateEntity(entity);
			flag = true;
		} catch (DbException e) {
			Log.i(TAG, "事件录入保存异常-saveDescEntity>> " + e.getMessage());
		}
		return flag;
	}
	
	/**
	 * 是否数据
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean ifExist() {
		boolean flag = false;
		try {
			List<EventEntryEntity> list = (List<EventEntryEntity>) queryList(EventEntryEntity.class);
			if (list != null && list.size() > 0) {
				flag = true;
			}
		} catch (DbException e) {
			Log.i(TAG, "数据查询异常-ifExist>> " + e.getMessage());
		}
		return flag;
	}

	/**
	 * 查找列表
	 *
	 * @param
	 * @return
	 */
	public List<EventEntryEntity> queryList(){
		try {
			List<EventEntryEntity> eventEntryList = db.selector(EventEntryEntity.class).findAll();
			for (EventEntryEntity entity : eventEntryList) {
				eventEntryList.add(entity);
			}
			return eventEntryList;
		} catch (DbException e) {
			Log.i(TAG, "信息查询异常-queryListForCV >> " + e.getMessage());
		}
		return null;
	}

	/**
	 * 根据名称查找列表
	 * 
	 * @param thingName
	 * @return
	 */
	public List<EventEntryEntity> queryListForthingName(String thingName){
		try {
			List<EventEntryEntity> eventEntryList = db.selector(EventEntryEntity.class).where("thingname", "==", thingName).findAll();
			for (EventEntryEntity entity : eventEntryList) {
				eventEntryList.add(entity);
			}
			return eventEntryList;
		} catch (DbException e) {
			Log.i(TAG, "信息查询异常-queryListForCV >> " + e.getMessage());
		}
		return null;
	}

}
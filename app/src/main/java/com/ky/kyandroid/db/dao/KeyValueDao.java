package com.ky.kyandroid.db.dao;

import android.util.Log;

import com.ky.kyandroid.db.BaseDao;
import com.ky.kyandroid.entity.EventEntity;

import org.xutils.common.util.KeyValue;
import org.xutils.ex.DbException;

import java.util.List;

/**
 * Created by Caizhui on 2017/6/11.
 * 字典表Dao
 */

public class KeyValueDao extends BaseDao {
    /** 标识 */
    private static final String TAG = "KeyValueDao";

    /**
     * 保存系统字典项
     *
     * @param entity
     * @return
     */
    public boolean saveKeyValueEntity(KeyValue entity){
        boolean flag = false;
        try {
            saveOrUpdateEntity(entity);
            flag = true;
        } catch (DbException e) {
            Log.i(TAG, "字典信息保存异常-saveDescEntity>> " + e.getMessage());
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
            List<KeyValue> list = (List<KeyValue>) queryList(KeyValue.class);
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
    public List<EventEntity> queryListByType(String type) {
        try {
            List<EventEntity> eventEntryList = db.selector(EventEntity.class).and("type","=",type).findAll();
            if (eventEntryList != null && eventEntryList.size() > 0) {
                return eventEntryList;
            }
        } catch (DbException e) {
            Log.i(TAG, "信息查询异常-queryListForCV >> " + e.getMessage());
        }
        return null;
    }
}

package com.ky.kyandroid.db.dao;

import android.util.Log;

import com.ky.kyandroid.db.BaseDao;
import com.ky.kyandroid.entity.TFtSjRyEntity;

import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.util.List;

/**
 * Created by Caizhui on 2017/6/11.
 * 事件Dao
 */

public class TFtSjRyEntityDao extends BaseDao {
    /** 标识 */
    private static final String TAG = "TFtSjRyEntityDao";

    /**
     * 保存系统字典项
     *
     * @param entity
     * @return
     */
    public boolean saveTFtSjRyEntity(TFtSjRyEntity entity){
        boolean flag = false;
        try {
            saveOrUpdateEntity(entity);
            flag = true;
        } catch (DbException e) {
            Log.i(TAG, "信息保存异常>> " + e.getMessage());
        }
        return flag;
    }


    /**
     * 查找列表
     *
     * @param
     * @return
     */
    public List<TFtSjRyEntity> queryListBySjId(String sjId) {
        try {
            List<TFtSjRyEntity> eventEntryList = db.selector(TFtSjRyEntity.class).where("sjid","=",sjId).findAll();
            if (eventEntryList != null && eventEntryList.size() > 0) {
                return eventEntryList;
            }
        } catch (DbException e) {
            Log.i(TAG, "信息查询异常-queryList >> " + e.getMessage());
        }
        return null;
    }


    public boolean deleteEventEntry(String id) {
        boolean flag =false;
        try {
            db.delete(TFtSjRyEntity.class, WhereBuilder.b("id","=", id));
            flag=true;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return flag;
    }

}

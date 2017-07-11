package com.ky.kyandroid.db.dao;

import android.util.Log;

import com.ky.kyandroid.db.BaseDao;
import com.ky.kyandroid.entity.TFtSjEntity;

import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.util.List;

/**
 * Created by Caizhui on 2017/6/11.
 * 事件Dao
 */

public class TFtSjEntityDao extends BaseDao {
    /** 标识 */
    private static final String TAG = "TFtSjEntityDao";

    @Override
    public boolean ifExist() {
        return false;
    }

    /**
     * 保存系统字典项
     *
     * @param entity
     * @return
     */
    public boolean saveTFtSjEntity(TFtSjEntity entity){
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
    public List<TFtSjEntity> queryList() {
        try {
            List<TFtSjEntity> eventEntryList = db.selector(TFtSjEntity.class).findAll();
            if (eventEntryList != null && eventEntryList.size() > 0) {
                return eventEntryList;
            }
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
    public List<TFtSjEntity> queryListForthingName(String thingName){
        try {
            List<TFtSjEntity> eventEntryList = db.selector(TFtSjEntity.class).where("thingname", "==", thingName).findAll();
            for (TFtSjEntity entity : eventEntryList) {
                eventEntryList.add(entity);
            }
            return eventEntryList;
        } catch (DbException e) {
            Log.i(TAG, "信息查询异常-queryListForCV >> " + e.getMessage());
        }
        return null;
    }

    public boolean deleteEventEntry(String id) {
        boolean flag =false;
        try {
            db.delete(TFtSjEntity.class, WhereBuilder.b("id","=", id));
            flag=true;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 修改
     * @param entity
     * @return
     */
    public boolean updateTFtSjEntity(TFtSjEntity entity){
        boolean flag =false;
        try {
            db.update(entity, String.valueOf(WhereBuilder.b("id","=", entity.getId())),"sjmc","fssj","fsdd","sfsqqt","dcbm",
                    "sjly","zysq","sjgyqk","gm","bxxs","sfsw","sfsj",
                    "sfsyq","sfgacz","ssjd","ldps","jwd","clsx","sssq","ssjwh","zt");
            flag = true;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return flag;
    }
}

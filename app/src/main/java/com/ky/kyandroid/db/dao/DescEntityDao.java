package com.ky.kyandroid.db.dao;

import android.util.Log;

import com.ky.kyandroid.db.BaseDao;
import com.ky.kyandroid.entity.DescEntity;

import org.xutils.ex.DbException;

import java.util.List;

/**
 * Created by Caizhui on 2017/6/11.
 * 字典表Dao
 */

public class DescEntityDao extends BaseDao {
    /** 标识 */
    private static final String TAG = "DescEntityDao";

    /**
     * 保存系统字典项
     *
     * @param entity
     * @return
     */
    public boolean saveDescEntity(DescEntity entity){
        boolean flag = false;
        try {
            saveOrUpdateEntity(entity);
            flag = true;
        } catch (DbException e) {
            Log.i(TAG, "字典信息保存异常-DescEntity>> " + e.getMessage());
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
            List<DescEntity> list = (List<DescEntity>) queryList(DescEntity.class);
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
    public List<DescEntity> queryListByType(String type) {
        try {
            List<DescEntity> descEntryList = db.selector(DescEntity.class).and("type","=",type).findAll();
            if (descEntryList != null && descEntryList.size() > 0) {
                return descEntryList;
            }
        } catch (DbException e) {
            Log.i(TAG, "信息查询异常-queryListForCV >> " + e.getMessage());
        }
        return null;
    }
}

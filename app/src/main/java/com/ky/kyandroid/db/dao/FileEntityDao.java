package com.ky.kyandroid.db.dao;

import android.util.Log;

import com.ky.kyandroid.db.BaseDao;
import com.ky.kyandroid.entity.FileEntity;
import com.ky.kyandroid.entity.TFtSjEntity;

import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.util.List;

/**
 * Created by Caizhui on 2017/6/11.
 * 文件Dao
 */

public class FileEntityDao extends BaseDao {
    /** 标识 */
    private static final String TAG = "FileEntityDao";

    /**
     * 保存文件信息
     *
     * @param entity
     * @return
     */
    public boolean saveFileEntity(FileEntity entity){
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
    public List<FileEntity> queryList(String sjId) {
        try {
            List<FileEntity> eventEntryList = db.selector(FileEntity.class).where("sjId","==",sjId).findAll();
            if (eventEntryList != null && eventEntryList.size() > 0) {
                return eventEntryList;
            }
        } catch (DbException e) {
            Log.i(TAG, "信息查询异常-queryList >> " + e.getMessage());
        }
        return null;
    }


    public boolean  deleteEventEntry(String uuid) {
        boolean flag =false;
        try {
            db.delete(TFtSjEntity.class, WhereBuilder.b("uuid","=",uuid ));
            flag=true;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return flag;
    }

    public boolean  deleteEventEntryBySjId(String sjId) {
        boolean flag =false;
        try {
            db.delete(TFtSjEntity.class, WhereBuilder.b("sjId","==",sjId ));
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
    public boolean updateFileEntity(FileEntity entity){
        boolean flag =false;
        try {
            db.update(entity, String.valueOf(WhereBuilder.b("uuid","==", entity.getUuid())),"fileUrl","fileMs");
            flag = true;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return flag;
    }
}

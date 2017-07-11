package com.ky.kyandroid.db.dao;

import android.util.Log;

import com.ky.kyandroid.db.BaseDao;
import com.ky.kyandroid.entity.TFtQhEntity;
import com.ky.kyandroid.util.StringUtils;

import org.xutils.db.Selector;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

/**
 * 区域DAO
 */
public class TFtQhEntityDao extends BaseDao {

    /** 标识 */
    private static final String TAG = "TFtQhEntityDao";


    /**
     * 是否数据
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public boolean ifExist() {
        boolean flag = false;
        try {
            List<TFtQhEntity> list = (List<TFtQhEntity>) queryList(TFtQhEntity.class);
            if (list != null && list.size() > 0) {
                flag = true;
            }
        } catch (DbException e) {
            Log.i(TAG, "区域表查询异常-ifExist>> " + e.getMessage());
        }
        return flag;
    }

    /**
     * 保存系统字典项
     *
     * @param entity
     * @return
     */
    public boolean saveQhEntity(TFtQhEntity entity){
        boolean flag = false;
        try {
            saveOrUpdateEntity(entity);
            flag = true;
        } catch (DbException e) {
            Log.i(TAG, "区域列表数据保存异常-saveQhEntity>> " + e.getMessage());
        }
        return flag;
    }

    /**
     * 根据类型查找区域列表
     *
     * @param jddm
     * @return
     */
    public List<TFtQhEntity> queryList(String jddm) {
        List<TFtQhEntity> qhList = new ArrayList<TFtQhEntity>();
        try {
            Selector<TFtQhEntity> selector = db.selector(TFtQhEntity.class).where("1","==","1");
            if (StringUtils.isNotBlank(jddm)){
                selector.and("jddm","==",jddm);
            }
            qhList = selector.findAll();
        } catch (DbException e) {
            Log.i(TAG, "系统字典查询异常-queryListForCV >> " + e.getMessage());
        }
        return qhList;
    }

    /**
     * 根据类型查找区域列表
     *
     * @param id
     * @return
     */
    public List<TFtQhEntity> queryListById(String id) {
        List<TFtQhEntity> qhList = new ArrayList<TFtQhEntity>();
        try {
            Selector<TFtQhEntity> selector = db.selector(TFtQhEntity.class).where("1","==","1");
            if (StringUtils.isNotBlank(id)){
                selector.and("id","==",id);
            }
            qhList = selector.findAll();
        } catch (DbException e) {
            Log.i(TAG, "系统字典查询异常-queryListForCV >> " + e.getMessage());
        }
        return qhList;
    }

}

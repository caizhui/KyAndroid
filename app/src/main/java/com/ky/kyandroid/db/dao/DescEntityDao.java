package com.ky.kyandroid.db.dao;

import android.util.Log;

import com.ky.kyandroid.bean.CodeValue;
import com.ky.kyandroid.db.BaseDao;
import com.ky.kyandroid.entity.DescEntity;

import org.xutils.ex.DbException;

import java.util.ArrayList;
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
            Log.i(TAG, "系统字典保存异常-saveDescEntity>> " + e.getMessage());
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
            Log.i(TAG, "系统字典查询异常-ifExist>> " + e.getMessage());
        }
        return flag;
    }

    /**
     * 根据类型查找字典列表
     *
     * @param type
     * @return
     */
    public List<CodeValue> queryListForCV(String type){
        List<CodeValue> cvList = new ArrayList<CodeValue>();
        try {
            List<DescEntity> descList = db.selector(DescEntity.class).where("type", "==", type).findAll();
            if(descList!=null &&descList.size()>0){
                for (DescEntity descEntity : descList) {
                    CodeValue cv = new CodeValue(descEntity.getCode(), descEntity.getValue());
                    cvList.add(cv);
                }
            }
        } catch (DbException e) {
            Log.i(TAG, "系统字典查询异常-queryListForCV >> " + e.getMessage());
        }
        return cvList;
    }
    /**
     * 查父类型
     *
     * @param
     * @return
     */
    public List<CodeValue> queryListByDcbm(String type){
        List<CodeValue> cvList = new ArrayList<CodeValue>();
        try {
            List<DescEntity> descList = db.selector(DescEntity.class).where("type", "==", type).findAll();
            if(descList!=null && descList.size()>0){
                for (DescEntity descEntity : descList) {
                    CodeValue cv = new CodeValue(descEntity.getCode(), descEntity.getValue());
                    cvList.add(cv);
                }
            }
        } catch (DbException e) {
            Log.i(TAG, "系统字典查询异常-queryListForCV >> " + e.getMessage());
        }
        return cvList;
    }

    /**
     * 查父类型
     *
     * @param
     * @return
     */
    public List<CodeValue> queryPidList(String type){
        List<CodeValue> cvList = new ArrayList<CodeValue>();
        try {
            List<DescEntity> descList = db.selector(DescEntity.class).where("parentId", "==", "0").and("type","==",type).findAll();
            if(descList!=null && descList.size()>0) {
                for (DescEntity descEntity : descList) {
                    CodeValue cv = new CodeValue(descEntity.getCode(), descEntity.getValue());
                    cvList.add(cv);
                }
            }
        } catch (DbException e) {
            Log.i(TAG, "系统字典查询异常-queryListForCV >> " + e.getMessage());
        }
        return cvList;
    }

    /**
     * 根据父id查找子内容
     *
     * @param
     * @return
     */
    public List<CodeValue> queryValueListByPid(String type,String parentId){
        List<CodeValue> cvList = new ArrayList<CodeValue>();
        try {
            List<DescEntity> descList = db.selector(DescEntity.class).where("type", "==", type).and("parentId", "==", parentId).findAll();
            if(descList!=null && descList.size()>0) {
                for (DescEntity descEntity : descList) {
                    CodeValue cv = new CodeValue(descEntity.getCode(), descEntity.getValue());
                    cvList.add(cv);
                }
            }
        } catch (DbException e) {
            Log.i(TAG, "系统字典查询异常-queryListForCV >> " + e.getMessage());
        }
        return cvList;
    }

    /**
     * 根据代码类型与代码查找名称
     * @param type
     * @param code
     * @return
     */
    public String queryName(String type,String code){
        List<DescEntity> descList = null;
        try {
            descList = db.selector(DescEntity.class).where("type", "==", type).and("code", "==", code).findAll();
            if(descList.size()==0){
                return "";
            }
        } catch (DbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return descList.get(0).getValue();
    }

    /**
     * 根据代码类型与名称查找代码
     * @param type
     * @param value
     * @return
     */
    public String queryCodeByName(String type,String value){
        List<DescEntity> descList = null;
        try {
            descList = db.selector(DescEntity.class).where("type", "==", type).and("value", "==", value).findAll();
            if(descList.size()==0){
                return "";
            }
        } catch (DbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return descList.get(0).getCode();
    }

}

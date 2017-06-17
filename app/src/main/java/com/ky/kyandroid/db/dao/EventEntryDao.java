package com.ky.kyandroid.db.dao;

import android.util.Log;

import com.ky.kyandroid.db.BaseDao;
import com.ky.kyandroid.entity.EventEntity;

import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.util.List;

/**
 * Created by Caizhui on 2017/6/11.
 * 事件Dao
 */

public class EventEntryDao extends BaseDao {
    /** 标识 */
    private static final String TAG = "EventEntryDao";

    /**
     * 保存系统字典项
     *
     * @param entity
     * @return
     */
    public boolean saveEventEntryEntity(EventEntity entity){
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
            List<EventEntity> list = (List<EventEntity>) queryList(EventEntity.class);
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
    public List<EventEntity> queryList() {
        try {
            List<EventEntity> eventEntryList = db.selector(EventEntity.class).findAll();
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
    public List<EventEntity> queryListForthingName(String thingName){
        try {
            List<EventEntity> eventEntryList = db.selector(EventEntity.class).where("thingname", "==", thingName).findAll();
            for (EventEntity entity : eventEntryList) {
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
            db.delete(EventEntity.class, WhereBuilder.b("id","=", id));
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
    public boolean updateEventEntry(EventEntity entity){
        boolean flag =false;
        try {
            db.update(entity, String.valueOf(WhereBuilder.b("id","=", entity.getId())),"uuid","thingname","happentime","happenaddress","petitiongroups",
                    "fielddepartmen","patternmanifestation","scope","fieldsinvolved","foreignrelated","involvedxinjiang","involvepublicopinion",
                    "publicsecuritydisposal","belongstreet","belongcommunity","mainappeals","eventsummary","leadershipinstructions","status");
            flag = true;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return flag;
    }
}

package com.ky.kyandroid.db;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.util.List;

/**
 * 类名称：基础类
 */
public class BaseDao {

	/** org.xutils.DbManager */
	protected DbManager db;

	public BaseDao() {
		db = DatabaseOpenHelper.getInstance();
	}

	/**
	 * 保存方法，将BD_descDao实例存进数据库
	 * @param obj
	 * @throws DbException
	 */
	public void saveOrUpdateEntity(Object obj) throws DbException {
		try {
			db.saveOrUpdate(obj);
		} catch (DbException e) {
			throw new DbException(e.getMessage());
		}
	}

	/**
	 * 修改方法
	 * 
	 * @param obj
	 * @param parmas
	 * @throws DbException
	 */
	public void updateEntity(Object obj, String... parmas) throws DbException {
		try {
			db.update(obj, parmas);
		} catch (DbException e) {
			throw new DbException(e.getMessage());
		}
	}

	/**
	 * 删除实体
	 * 
	 * @param obj
	 * @throws DbException
	 */
	public void delEntity(Object obj) throws DbException {
		try {
			db.delete(obj);
		} catch (DbException e) {
			throw new DbException(e.getMessage());
		}
	}

	/**
	 * 查询列表
	 * 
	 * @param clazz
	 * @return
	 * @throws DbException
	 */
	public List<?> queryList(Class<?> clazz) throws DbException {
		try {
			return db.findAll(clazz);
		} catch (DbException e) {
			throw new DbException(e.getMessage());
		}
	}

	public Object queryById(Class<?> clazz,int id) throws DbException {

		try {

			return db.findById(clazz,id);
		} catch (DbException e) {
			throw new DbException(e.getMessage());
		}
	}


}

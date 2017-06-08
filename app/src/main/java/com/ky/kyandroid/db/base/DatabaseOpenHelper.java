package com.ky.kyandroid.db.base;

import org.xutils.DbManager;
import org.xutils.db.table.TableEntity;
import org.xutils.x;

/**
 * 
 * 类名称：数据库帮助类，使用xUtils3
 * 
 * 创建人： caizhui
 * 创建时间：2016年11月28日
 *
 */
public class DatabaseOpenHelper {
	private DbManager.DaoConfig daoConfig;
	private static DbManager db;
	// 数据库名
	private final String DB_NAME = "mydb.db";
	// 数据库版本号
	private final int VERSION = 1;

	private DatabaseOpenHelper() {
		daoConfig = new DbManager.DaoConfig()
				.setDbName(DB_NAME)
				.setDbVersion(VERSION)
				// 版本升级的监听方法
				.setDbUpgradeListener(new DbManager.DbUpgradeListener() {
					@Override
					public void onUpgrade(DbManager db, int oldVersion,
                                          int newVersion) {
					}
				})
				.setDbOpenListener(new DbManager.DbOpenListener() {
					@Override
					public void onDbOpened(DbManager db) {
						// 开启WAL, 对写入加速提升巨大(作者原话)
						db.getDatabase().enableWriteAheadLogging();
					}
				})
				// 将数据库存储在你想存储的地方，如果不设置，那么数据库默认存储在/data/data/你的应用程序/database/xxx.db下
				// .setDbDir(new File("/sdcard/download/"))
				// 设置是否开启事务
				.setAllowTransaction(false)
				.setTableCreateListener(new DbManager.TableCreateListener() {
					@Override
					public void onTableCreated(DbManager db,
							TableEntity<?> table) {
						// db.getDatabase().
					}
				});
		db = x.getDb(daoConfig);
	}

	public static DbManager getInstance() {
		if (db == null) {
			new DatabaseOpenHelper();
		}
		return db;
	}
}

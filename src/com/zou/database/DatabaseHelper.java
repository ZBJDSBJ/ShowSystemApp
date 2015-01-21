package com.zou.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * <br>类描述:数据库帮助类
 * <br>功能详细描述:用于数据库操作
 * 
 */
public class DatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "app_manage.db";
	private static final int DATABASE_VERSION = 3;


	/** <默认构造函数>
	 */
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql = "CREATE TABLE " + Provider.AppColumns.TABLE_NAME + "("
				+ Provider.AppColumns._ID
				+ " INTEGER   PRIMARY KEY AUTOINCREMENT,"
				+ Provider.AppColumns.PACKAGE_NAME + " TEXT  NOT NULL,"
				+ Provider.AppColumns.OPEN_COUNT + " INT  NULL)";
		try {
			db.execSQL(sql);// 需要异常捕获
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// 在onUpgrade方法里删除现有表，然后手动调用onCtreate创建表
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		try {
			db.execSQL("DROP TABLE IF EXISTS " + Provider.AppColumns.TABLE_NAME);
			onCreate(db);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

}

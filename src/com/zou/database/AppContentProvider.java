package com.zou.database;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;


/**
 * <br>类描述:操作数据库app_info表的ContentProvider
 * <br>功能详细描述:利用ContentProvider来实现对数据库的相关增、删、改、查操作
 * 
 */
public class AppContentProvider extends ContentProvider {

	private static HashMap<String, String> sAppinfosProjectionMap;

	private static final int APPINFOS = 1;
	private static final int APPINFOS_ID = 2;

	private static final UriMatcher sUriMatcher;
	private DatabaseHelper mOpenHelper;
	private SQLiteDatabase mDb;

	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		// 这个地方的persons要和PersonColumns.CONTENT_URI中最后面的一个Segment一致
		sUriMatcher.addURI(Provider.AUTHORITY, "appinfos", APPINFOS); // 匹配记录集合
		sUriMatcher.addURI(Provider.AUTHORITY, "appinfos/#", APPINFOS_ID); // 通过ID匹配单条数据

		sAppinfosProjectionMap = new HashMap<String, String>();
		sAppinfosProjectionMap.put(Provider.AppColumns._ID, Provider.AppColumns._ID);
		sAppinfosProjectionMap.put(Provider.AppColumns.PACKAGE_NAME,
				Provider.AppColumns.PACKAGE_NAME);
		sAppinfosProjectionMap.put(Provider.AppColumns.OPEN_COUNT,
				Provider.AppColumns.OPEN_COUNT);
	}

	@Override
	public boolean onCreate() {
		mOpenHelper = new DatabaseHelper(getContext());//初始化数据库帮助类
		return true;
	}

	/* 查询函数 */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		try {
			mDb = mOpenHelper.getReadableDatabase();// 如果数据库不能打开将会抛出异常，例如当数据库的磁盘空间满了的时候就会打开失败，当打开失败后会继续尝试以只读方式打开数据库。如果该问题成功解决，则只读数据库对象就会关闭，然后返回一个可读写的数据库对象。
		} catch (SQLiteException e) {
			// TODO: handle exception
			Log.i("readDatabase", "failed to open Database");
		}
		int match = sUriMatcher.match(uri);
		switch (match) {
		case APPINFOS:
			return mDb.query(Provider.AppColumns.TABLE_NAME, projection,
					selection, selectionArgs, null, null, sortOrder);
		case APPINFOS_ID:
			long mId = ContentUris.parseId(uri);
			selection = Provider.AppColumns._ID + " = ?";
			selectionArgs = new String[] { String.valueOf(mId) };
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		return mDb.query(Provider.AppColumns.TABLE_NAME, projection,
				selection, selectionArgs, null, null, sortOrder);
	}

	@Override
	public String getType(Uri uri) {
		switch (sUriMatcher.match(uri)) {
		case APPINFOS:
			return Provider.CONTENT_TYPE;
		case APPINFOS_ID:
			return Provider.CONTENT_ITEM_TYPE;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	/* 插入函数 */
	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		// Validate the requested uri
		if (sUriMatcher.match(uri) != APPINFOS) {
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		ContentValues values;
		if (initialValues != null) {
			values = new ContentValues(initialValues);
		} else {
			values = new ContentValues();
		}

		SQLiteDatabase db = null;
		try {
			db = mOpenHelper.getWritableDatabase();// 如果数据库不能打开将会抛出异常，例如当数据库的磁盘空间满了的时候,数据库就只能读而不能写，倘若使用的是getWritableDatabase()
													// 方法就会出错。
		} catch (SQLiteException e) {
			// TODO: handle exception
			Log.i("readDatabase", "failed to open Database");
		}

		long rowId = db.insert(Provider.AppColumns.TABLE_NAME,
				Provider.AppColumns.PACKAGE_NAME, values);
		if (rowId > 0) {
			Uri noteUri = ContentUris.withAppendedId(
					Provider.AppColumns.CONTENT_URI, rowId);
			getContext().getContentResolver().notifyChange(noteUri, null);
			return noteUri;
		}

		throw new SQLException("Failed to insert row into " + uri);
	}

	/* 删除函数 */
	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		SQLiteDatabase db = null;
		try {
			db = mOpenHelper.getWritableDatabase();// 如果数据库不能打开将会抛出异常，例如当数据库的磁盘空间满了的时候,数据库就只能读而不能写，倘若使用的是getWritableDatabase()
													// 方法就会出错。
		} catch (SQLiteException e) {
			// TODO: handle exception
			Log.i("readDatabase", "failed to open Database");
		}

		int count;
		switch (sUriMatcher.match(uri)) {
		case APPINFOS:
			count = db.delete(Provider.AppColumns.TABLE_NAME, where,
					whereArgs);
			break;

		case APPINFOS_ID:
			String noteId = null;
			if (uri.getPathSegments() != null
					&& uri.getPathSegments().size() > 1) {// 先对得到的List<String>判空，在对我们所需要的值—id判空
				noteId = uri.getPathSegments().get(1);// 取得对应的Id值，在这里当我们进行判空后也可以用get(1)来去id
			}
			count = db.delete(Provider.AppColumns.TABLE_NAME,
					BaseColumns._ID
							+ "="
							+ noteId
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	/* 更新函数 */
	@Override
	public int update(Uri uri, ContentValues values, String where,
			String[] whereArgs) {
		SQLiteDatabase db = null;
		try {
			db = mOpenHelper.getWritableDatabase();// 如果数据库不能打开将会抛出异常，例如当数据库的磁盘空间满了的时候,数据库就只能读而不能写，倘若使用的是getWritableDatabase()
													// 方法就会出错。
		} catch (SQLiteException e) {
			// TODO: handle exception
			Log.i("readDatabase", "failed to open Database");
		}
		int count;
		switch (sUriMatcher.match(uri)) {
		case APPINFOS:
			count = db.update(Provider.AppColumns.TABLE_NAME, values, where,
					whereArgs);
			break;

		case APPINFOS_ID:
			String noteId = uri.getPathSegments().get(1);
			count = db.update(Provider.AppColumns.TABLE_NAME, values,
					BaseColumns._ID
							+ "="
							+ noteId
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

}

package com.zou.database;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * <br>类描述:Provider类
 * <br>功能详细描述:存放所需的常量
 * 
 */
public class Provider {
	public static final String AUTHORITY = "com.jacp.provider.demo.appinfo";// 这个是每个Provider的标识，在Manifest中使用

	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.jacp.demo";

	public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.jacp.demo";

	public static final Uri URI = Uri
			.parse("content://com.jacp.provider.demo.appinfo/appinfos");

	
	/**
	 * <br>类描述:一个存放常量信息的类
	 * <br>功能详细描述:跟数据库中app_info表相关的常量
	 * 
	 * @author  zhouzhenwu
	 * @date  [2014-7-27]
	 */
	public static final class AppColumns implements BaseColumns {
		// CONTENT_URI跟数据库的表关联，最后根据CONTENT_URI来查询对应的表
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/appinfos");
 
		public static final String TABLE_NAME = "app_info";
		public static final String _ID = "_id";
		public static final String  PACKAGE_NAME = "packageName";
		public static final String OPEN_COUNT = "openCount";

	}
}

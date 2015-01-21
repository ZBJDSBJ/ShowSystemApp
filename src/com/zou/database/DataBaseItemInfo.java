/*
 * 文 件 名:  DataBaseItemInfo.java
 * 版    权:  3G
 * 描    述:  <描述>
 * 修 改 人:  zhouzhenwu
 * 修改时间:  2014-8-12
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.zou.database;

/**
 * <br>
 * 类描述:数据库信息实体类 <br>
 * 功能详细描述:
 * 
 */
public class DataBaseItemInfo {
	private String mId;
	private String mPackageName;
	private int mOpenCount;

	public DataBaseItemInfo(String id,String packageName, int openCount) {
		this.mId = id;
		this.mPackageName = packageName;
		this.mOpenCount = openCount;
	}
	
	
	public String getmId() {
		return mId;
	}

	public void setmId(String mId) {
		this.mId = mId;
	}

	public String getmPackageName() {
		return mPackageName;
	}

	public void setmPackageName(String mPackageName) {
		this.mPackageName = mPackageName;
	}

	public int getmOpenCount() {
		return mOpenCount;
	}

	public void setmOpenCount(int mOpenCount) {
		this.mOpenCount = mOpenCount;
	}

}

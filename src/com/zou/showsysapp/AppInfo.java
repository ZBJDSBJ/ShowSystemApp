package com.zou.showsysapp;

import android.content.ComponentName;
import android.graphics.drawable.Drawable;

public class AppInfo {
	//打开应用所需要的componentName——包含packageName和ActivityName
	private ComponentName mComponentName = null;
	//程序打开次数
	private int mOpenCount = 0;
	// 应用的名称
	private String mAppName = "";
	// 应用的图标
	private Drawable mAppIcon = null;
//	//包名称
//	private String mPackageName = "";
//
//	public String getmPackageName() {
//		return mPackageName;
//	}
//
//	public void setmPackageName(String mPackageName) {
//		this.mPackageName = mPackageName;
//	}

	public int getmOpenCount() {
		return mOpenCount;
	}

	public void setmOpenCount(int mOpenCount) {
		this.mOpenCount = mOpenCount;
	}

	public ComponentName getmComponentName() {
		return mComponentName;
	}

	public void setmComponentName(ComponentName mComponentName) {
		this.mComponentName = mComponentName;
	}

	public String getmAppName() {
		return mAppName;
	}

	public void setmAppName(String mAppName) {
		this.mAppName = mAppName;
	}

	public Drawable getmAppIcon() {
		return mAppIcon;
	}

	public void setmAppIcon(Drawable mAppIcon) {
		this.mAppIcon = mAppIcon;
	}
}

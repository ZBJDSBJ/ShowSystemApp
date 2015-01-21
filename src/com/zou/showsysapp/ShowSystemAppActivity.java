package com.zou.showsysapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.zou.database.Provider;
import com.zou.utils.GoProgressBar;

public class ShowSystemAppActivity extends ActionBarActivity {
	private ListView mAppListView;
	private MyAdapter mAdapter;
	private AppInfo[] mAppInfos;// 用于存放所需应用信息的实体数组，存放为数组形式是为了方便按Name排序
	//	private MyBroadcastReceiver mReceiver;
	private GoProgressBar mGoProgressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		init();
	}

	private void init() {
		mAdapter = new MyAdapter(this);// 初始化adapter
		mAppListView = (ListView) findViewById(R.id.lv_app);// 初始化listView
		mAppListView.setAdapter(mAdapter);// 为listView设置adapter
		mGoProgressBar = (GoProgressBar) findViewById(R.id.modify_progress);

		QueryAppInfo queryAppInfo = new QueryAppInfo();// 初始化异步线程类
		queryAppInfo.execute();// 启动异步线程

		mAppListView.setOnItemClickListener(new OnItemClickListener() { // 为ListView设置单击Item的点击监听
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						openDialog(position);// 打开提示对话框,在此传入position参数
					}
				});

		//		mReceiver = new MyBroadcastReceiver();// 初始化广播接收器；
		//		IntentFilter filter = new IntentFilter();
		//		filter.addAction(Intent.ACTION_PACKAGE_ADDED);// 应用被添加的广播
		//		filter.addAction(Intent.ACTION_PACKAGE_REMOVED);// 应用被卸载的广播
		//		filter.addDataScheme("package"); // 注意一定要加这句，不然无法接收到来自系统应用的广播,因为此广播为隐式事件，并且action和data都不为空，所以必须要有过滤的匹配
		//		registerReceiver(mReceiver, filter);// 广播接收器的注册
	}

	public class QueryAppInfo extends AsyncTask<Integer, Integer, AppInfo[]> {
		@Override
		protected AppInfo[] doInBackground(Integer... params) {
			List<AppInfo> appInfos = new ArrayList<AppInfo>();
			showProgress();
			
			List<ResolveInfo> resolveInfos = new ArrayList<ResolveInfo>();// 初始化一个List<ResolveInfo>
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);//只有可以启动应用的Activitys才会被使用
			PackageManager manager = getPackageManager();// 得到一个PackageManager
			// 取得我们所需应用的ResolveInfo信息的集合
			try {
				resolveInfos = manager.queryIntentActivities(intent,
						PackageManager.COMPONENT_ENABLED_STATE_DISABLED);// 查询手机所有支持分享的应用，含一些可以手动更新的系统应有
			} catch (Exception e) {
			}
			if (resolveInfos != null) {
				for (ResolveInfo resolveInfo : resolveInfos) {
					int openCount = 0;
					AppInfo appInfo = new AppInfo();
					ComponentName componentName = new ComponentName(
							resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name);// 取得一个componentName—packageName与activityName的一个组合

					Cursor cursor = null;
					try {
						// 查询对应的packageName相符的数据库中的信息
						cursor = getContentResolver().query(
								Provider.URI,
								null,
								Provider.AppColumns.PACKAGE_NAME + "=" + "'"
										+ resolveInfo.activityInfo.packageName + "'", null, null);
						if (cursor != null && cursor.moveToFirst()) {// 如果cursor不为空，并且cursor查询数据不为空
							// 如果数据不为空就将openCount查询出来
							openCount = cursor.getInt(cursor
									.getColumnIndex(Provider.AppColumns.OPEN_COUNT));
							Log.i("queryOpenCount", openCount + "");
						} else {
							insertAppInfoToDatabase(resolveInfo.activityInfo.packageName);// 如果数据库中没有此条应用记录，则说明是第一次启动，则将应用信息加入数据库
						}
					} catch (Exception e) {
						
					} finally {
						if (cursor != null) {
							cursor.close();
						}
					}

					appInfo.setmAppIcon(resolveInfo.loadIcon(manager));// 设置Icon用于显示
					appInfo.setmAppName(resolveInfo.loadLabel(manager).toString());// 设置AppName用于显示
					appInfo.setmComponentName(componentName);// 设置ComponentName用于打开应用
					appInfo.setmOpenCount(openCount);

					appInfos.add(appInfo);
				}
			}

			mAppInfos = new AppInfo[appInfos.size()];// 定义一个大小与符合要求的
			mAppInfos = appInfos.toArray(mAppInfos);// 将List转换成数组
			Arrays.sort(mAppInfos, new Comparator<AppInfo>() { // 调用sort(T[],compator<?superT>)方法来实现按名字排序数组中的数据
						@Override
						public int compare(AppInfo lhs, AppInfo rhs) {
							// 按照打开次数从多到少排序，如果次数一样则按照名字字符串顺序排序
							if (lhs.getmOpenCount() != rhs.getmOpenCount()) {// 判断打开次数是否相等
								// 当打开次数不等时，将打开此处多的那个向前排
								//								Integer i = lhs.getmOpenCount();
								//								Integer b = rhs.getmOpenCount(); 
								//								return b.compareTo(i);//在没有自己重写compareTo方法时，不能直接将int类型的值进行比较，必须转换成Integer类型
								return rhs.getmOpenCount() - lhs.getmOpenCount();
							} else {
								// 当打开次数相等时，按名字的字符串排序
								return lhs.getmAppName().compareTo(rhs.getmAppName());
							}

						}
					});
			return mAppInfos;
		}

		/** {@inheritDoc} */

		@Override
		protected void onPostExecute(AppInfo[] result) {
			if (result != null) {
				mAdapter.upData(result);// 更新Adapter中的数据

				dismissProgress();
			}
			super.onPostExecute(result);
		}
	}

	private void openDialog(final int itemPosition) {
		AlertDialog.Builder dialogBuilder = new Builder(this);// 初始化一个Builder用于创建dialog

		dialogBuilder.setTitle(getString(R.string.mainAcitvity_openDialog_title));// 为dialog设置一个title
		dialogBuilder.setPositiveButton(getString(R.string.mainActivity_openDialog_positiveButton),
				new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {// 打开按钮的点击事件
						ComponentName componentName = mAppInfos[itemPosition].getmComponentName();// 得到对应应用的componentName
						try {
							Intent intent = new Intent();// 初始化一个intent
							intent.setComponent(componentName);// 向intent中设置Component属性
							startActivity(intent);
						} catch (ActivityNotFoundException e) {
							Toast.makeText(
									ShowSystemAppActivity.this,
									getString(R.string.mainAcitivity_openDialog_toast_start_failed),
									Toast.LENGTH_SHORT).show();
						}
						String packageName = null;
						packageName = componentName.getPackageName();
						////						AddOpenCount addOpenCount = new AddOpenCount();
						//						addOpenCount.execute(packageName);// 通过packgaeName找到对应应用并更改对应数据库中的的openCount值

						dialog.dismiss();// 对话框消失
					}
				});

		dialogBuilder.setNegativeButton(getString(R.string.mainActivity_openDialog_negativeButton),
				new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {// 取消按钮的点击事件
						dialog.dismiss();// 对话框消失
					}
				});
		dialogBuilder.create().show();
	}

	/**
	 * <br> 功能简述:向数据库插入数据 
	 * <br> 功能详细描述:如果是新应用或者是第一启动时应用时才会将数据插入，插入的应用的初始OpenCount为0 
	 * 
	 * @param packageName
	 */
	private void insertAppInfoToDatabase(String packageName) {
		// 向数据库中插入数据信息
		ContentValues values = new ContentValues();
		values.put(Provider.AppColumns.PACKAGE_NAME, packageName);
		try {
			getContentResolver().insert(Provider.URI, values);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * <br>功能简述:显示进度条
	 * <br>功能详细描述:
	 * <br>注意:
	 */
	private void showProgress() {
		if (mGoProgressBar != null) {
			mGoProgressBar.setVisibility(View.VISIBLE);
		}
	}
	/**
	 * <br>功能简述:隐藏进度条
	 * <br>功能详细描述:
	 * <br>注意:
	 */
	private void dismissProgress() {
		if (mGoProgressBar != null) {
			mGoProgressBar.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onDestroy() {
		//		unregisterReceiver(mReceiver);// 广播接收器的反注册
		super.onDestroy();
	}
}

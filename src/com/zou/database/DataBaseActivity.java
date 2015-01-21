/*
 * 文 件 名:  DataBaseActivity.java
 * 版    权:  3G
 * 描    述:  <描述>
 * 修 改 人:  zhouzhenwu
 * 修改时间:  2014-8-12
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.zou.database;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

import com.zou.showsysapp.R;

/**
 * <br>
 * 类描述: <br>
 * 功能详细描述:
 * 
 */
public class DataBaseActivity extends Activity {
	private ListView mDbListView;
	private DatabaseAdapter mDbAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		init();
	}

	private void init() {
		setContentView(R.layout.database_list);

		mDbListView = (ListView) findViewById(R.id.lv_database);
		mDbAdapter = new DatabaseAdapter(this);
		mDbListView.setAdapter(mDbAdapter);

		QueryDataItemInfo queryDataItemInfo = new QueryDataItemInfo();
		queryDataItemInfo.execute();

	}

	private class QueryDataItemInfo extends
			AsyncTask<Integer, Integer, List<DataBaseItemInfo>> {

		/** {@inheritDoc} */

		@Override
		protected List<DataBaseItemInfo> doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			List<DataBaseItemInfo> listData = new ArrayList<DataBaseItemInfo>();
			Cursor cursor = null;
			try {
				cursor = getContentResolver().query(Provider.URI, null, null,
						null, null);
				if (cursor != null) {
					while (cursor.moveToNext()) {
						String id = cursor.getString(cursor
								.getColumnIndex(Provider.AppColumns._ID));
						String packageName = cursor
								.getString(cursor
										.getColumnIndex(Provider.AppColumns.PACKAGE_NAME));
						int openCount = cursor
								.getInt(cursor
										.getColumnIndex(Provider.AppColumns.OPEN_COUNT));
						listData.add(new DataBaseItemInfo(id, packageName,
								openCount));
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
			} finally {
				if (cursor != null) {
					cursor.close();
				}
			}

			return listData;
		}

		/** {@inheritDoc} */

		@Override
		protected void onPostExecute(List<DataBaseItemInfo> result) {
			// TODO Auto-generated method stub
			mDbAdapter.upData(result);
			super.onPostExecute(result);
		}
	}

}

/*
 * 文 件 名:  DatabaseAdapter.java
 * 版    权:  3G
 * 描    述:  <描述>
 * 修 改 人:  zhouzhenwu
 * 修改时间:  2014-8-12
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.zou.database;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zou.showsysapp.R;

/**
 * <br>类描述:
 * <br>功能详细描述:
 * 
 */
public class DatabaseAdapter extends BaseAdapter {

	private List<DataBaseItemInfo> mData;
	private LayoutInflater mInflater;
	private Context mContext;
	
	public DatabaseAdapter(Context context) {
		this.mContext = context;
		this.mInflater = LayoutInflater.from(context);
	}
	
	
	public void upData(List<DataBaseItemInfo> data) {
		if (data != null) {
			mData = data;
			notifyDataSetChanged();
		}
	}
	
	/** {@inheritDoc} */

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (mData != null) {
			return mData.size();
		} else {
			return 0;
		}
	}

	/** {@inheritDoc} */

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	/** {@inheritDoc} */

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	/** {@inheritDoc} */

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final DataBaseItemInfo  itemInfo = mData.get(position);
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.database_item, null);
			
			holder.appId = (TextView) convertView.findViewById(R.id.tv_app_id);
			holder.packageName = (TextView) convertView.findViewById(R.id.tv_app_packageName);
			holder.openCount = (TextView) convertView.findViewById(R.id.tv_app_openCount);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (itemInfo != null) {
			holder.appId.setText(itemInfo.getmId());
			holder.packageName.setText(itemInfo.getmPackageName());
			holder.openCount.setText(itemInfo.getmOpenCount() + "");
		}
		return convertView;
	}

	private class ViewHolder{
		TextView appId;
		TextView packageName;
		TextView openCount;
	}
}

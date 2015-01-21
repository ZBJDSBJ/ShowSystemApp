package com.zou.showsysapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {
	private AppInfo[] mData;
	private Context mContext;
	private LayoutInflater mInflater;

	public MyAdapter(Context context) {
		this.mContext = context;
		this.mInflater = LayoutInflater.from(context);
	}

	public void upData(AppInfo[] data) {
		if (data != null) {
			this.mData = data;
			notifyDataSetChanged();
		}
	}


	@Override
	public int getCount() {
		if (mData != null) {
			return mData.length;
		} else {
			return 0;
		}
	}


	@Override
	public Object getItem(int position) {
		return mData.length;
	}


	@Override
	public long getItemId(int position) {
		return position;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = new ViewHolder();
		if (null == convertView) {
			convertView = mInflater.inflate(R.layout.item, null);
			viewHolder.appIcon = (ImageView) convertView.findViewById(R.id.iv_app_ico);
			viewHolder.appname = (TextView) convertView.findViewById(R.id.tv_app_name);
			viewHolder.packegename = (TextView) convertView
					.findViewById(R.id.tv_app_packege_name);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		// 获得应用的信息
		final AppInfo appInfo = mData[position];
		if (appInfo != null) {
			viewHolder.appname.setText(appInfo.getmAppName());
			viewHolder.appIcon.setImageDrawable(appInfo.getmAppIcon());
			viewHolder.packegename.setText(appInfo.getmComponentName().getPackageName());
		}

		return convertView;
	}

	public class ViewHolder {
		public ImageView appIcon;
		public TextView appname;
		public TextView packegename;
	}

}

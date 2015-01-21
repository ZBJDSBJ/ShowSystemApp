package com.zou.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;
/**
 * 
 * <br>类描述:
 * <br>功能详细描述:
 * 
 */
public class GoProgressBar extends LinearLayout {
	public GoProgressBar(Context context) {
		super(context);
	}

	public GoProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		//		this.setBackgroundResource(R.drawable.themestore_main_list_item_selector2);
		// this.setBackgroundColor(R.color.theme_bg);
		//		//采用新的加载方法
		//		ProgressBar progressBar = (ProgressBar) this.findViewById(R.id.progressbar);
		//		progressBar.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
		//		Drawable drawable = getContext().getResources().getDrawable(R.drawable.go_account_progress_green);
		//		progressBar.setIndeterminateDrawable(drawable);
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return true;
	}
}
package com.zou.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * <br>
 * 类描述: Loading动画的自定义View类<br>
 * 功能详细描述:
 * 
 */
public class GoLoadingView extends View {
	private Paint mPaint;
	private float mCx1; // 圆1的圆心x坐标
	private float mCx2; // 圆2的圆心x坐标
	private float mCy; // 两圆圆心的y坐标（两圆的ｙ坐标不会变化，此为共享数据）
	private float mRadius1; // 圆1的半径
	private float mRadius2; // 圆2的半径
	private float mR; // 圆的最大半径（View的高度为２R，View的宽度为４R）
	private static final float SCALE_MIN = 0.3f; // 圆的最小缩放系数
	private static final float SCALE_WIDTHT = 4.0f; // View宽度与最大半径R的系数比（这里View的宽度为半径R的４倍）
	private static final float SCALE_INSTANCE = 1.3f; // 初始化圆２时，圆１的移动距离与最大半径Ｒ的系数比
	private static final int MAX_ALPHA = 200; //loading动画中圆的alpha值的最高值
	private long mCurrentTime = 0; // 当前时间
	private long mLastTime = 0; // 上一次时间
	private long mDeltaTime = 0; // 两次界面更新的差值时间
	private static final long DURATION = 1200; // 完成一次动画的总时间(单位毫秒)
	private float mDaltaDistance = 0; // 两次界面更新的差值距离

	/**
	 * <默认构造函数>
	 */
	public GoLoadingView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/** {@inheritDoc} */

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		if (mCy == 0) { // 刚进入Loading动画时的初始化操作
			mCy = getHeight() / 2; // 圆心的y轴坐标始终为View高度的一半，此为两个圆的共享数据
			mR = mCy; // 最大半径为View高度的一半
			mCx1 = (SCALE_WIDTHT / 2) * mR; // 圆一的初始圆心的x轴的坐标为View宽度的2Ｒ处，此时圆一最大，颜色最深
			mRadius1 = setRadius(mCx1);
			mCx2 = (SCALE_WIDTHT / 2 - SCALE_INSTANCE) * mR; // 圆二初始位置距离圆一左边1.3R出
			mRadius2 = setRadius(mCx2);
			mPaint = new Paint();
			mPaint.setColor(Color.GREEN);
		}
	}

	/** {@inheritDoc} */

	@Override
	protected void onDraw(Canvas canvas) {
		mCurrentTime = System.currentTimeMillis(); // 得到当前时间
		if (mLastTime != 0) {
			mDeltaTime = mCurrentTime - mLastTime; // 求出两次更新的间隔时间
		} else {
			mDeltaTime = 0;
		}
		mDaltaDistance = (float) ((mDeltaTime * (SCALE_WIDTHT - SCALE_MIN * 2) * mR / DURATION)); //更具收到消息的时间决定要移动的距离

		// 球1的绘制
		if (mCx1 >= SCALE_MIN * mR && mCx1 <= (SCALE_WIDTHT - SCALE_MIN) * mR) {
			mPaint.setAlpha(setCircleAlpha(mCx1)); // 为画笔设置透明度
			canvas.drawCircle(mCx1, mCy, mRadius1, mPaint); // 画球1
		}
		if ((mCx1 + mDaltaDistance) <= (SCALE_WIDTHT - SCALE_MIN) * mR) {
			mCx1 += mDaltaDistance;
			mRadius1 = setRadius(mCx1);
		} else {
			mCx1 = SCALE_MIN * mR + ((mCx1 + mDaltaDistance) - (SCALE_WIDTHT - SCALE_MIN) * mR);
		}

		// 球2的绘制
		if (mCx2 != 0) { // 如果球2已经初始化
			if (mCx2 >= SCALE_MIN * mR && mCx2 <= (SCALE_WIDTHT - SCALE_MIN) * mR) {
				mPaint.setAlpha(setCircleAlpha(mCx2)); // 为画笔设置透明度
				canvas.drawCircle(mCx2, mCy, mRadius2, mPaint);
			}

			if ((mCx2 + mDaltaDistance) <= (SCALE_WIDTHT - SCALE_MIN) * mR) {
				mCx2 += mDaltaDistance;
				mRadius2 = setRadius(mCx2);
			} else {
				mCx2 = SCALE_MIN * mR + ((mCx2 + mDaltaDistance) - (SCALE_WIDTHT - SCALE_MIN) * mR);
			}
		} else if (mCx2 == 0 && mCx1 >= SCALE_INSTANCE * mR) { // 在第一个圆运动到1.3R处时进行初始化第二个圆的操作
			mCx2 = SCALE_MIN * mR;
			mRadius2 = setRadius(mCx2);
		}

		this.postInvalidate(); //发送消息更新界面更新界面
		mLastTime = mCurrentTime; //记录更新前的时间
		super.onDraw(canvas);
	}

	/**
	 * <br>
	 * 功能简述: <br>
	 * 功能详细描述: <br>
	 * 注意:
	 * 
	 * @param cx
	 *            圆心的x坐标
	 * @return 当前圆的透明度Alpha值
	 */
	public int setCircleAlpha(float cx) {
		float scale = (float) (SCALE_MIN + (1 - SCALE_MIN)
				* (1 - Math.abs(cx - 2 * mR) / ((SCALE_WIDTHT / 2 - SCALE_MIN) * mR))); // 透明度Alpha的变化比
		return (int) (MAX_ALPHA * Math.pow(scale, 3)); // alpha值是3次方的增速变化，最高值为200
	}

	/**
	 * <br>
	 * 功能简述:根据圆的圆心x坐标的位置来动态计算出圆的半径 <br>
	 * 功能详细描述: <br>
	 * 注意:
	 * 
	 * @param cx
	 *            圆心的x坐标
	 * @return 当前圆的半径
	 */
	public float setRadius(float cx) {
		float scale = (float) (SCALE_MIN + (1 - SCALE_MIN)
				* (1 - Math.abs(cx - 2 * mR) / ((SCALE_WIDTHT / 2 - SCALE_MIN) * mR))); // 半径缩放比
		return (float) (mR * scale);
	}
}

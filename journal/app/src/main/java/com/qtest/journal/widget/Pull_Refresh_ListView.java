package com.qtest.journal.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ListView;

import com.qtest.journal.widget.PullRefreshView.Pull_Listenner;

public class Pull_Refresh_ListView extends ListView implements Pull_Listenner {

	public Pull_Refresh_ListView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	public Pull_Refresh_ListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public Pull_Refresh_ListView(Context context) {
		super(context);
	}

	/**
	 * 是否可以下啦
	 * 
	 * @return
	 */
	@Override
	public boolean isPullDownAble() {
		boolean isPullDownAble = false;
		int firstP = getFirstVisiblePosition();
		// 判断是否可以下啦
		if (firstP == 0) {
			View child0 = getChildAt(0);
			if (child0 != null) {
				int firstTop = child0.getTop();
				if (firstTop >= 0 - 5) {
					isPullDownAble = true;
				}
			} else {
				isPullDownAble = true;
			}
		}
		return isPullDownAble;
	}

	private VelocityTracker mVelocityTracker;

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		boolean isHandle = super.onInterceptTouchEvent(ev);

		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(ev);

		int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			break;
		case MotionEvent.ACTION_MOVE:
			// 判断是否让item响应 ACTION_UP事件
			ViewConfiguration configuration = ViewConfiguration
					.get(getContext());
			int mMaximumFlingVelocitx = configuration
					.getScaledMaximumFlingVelocity();
			final VelocityTracker velocityTracker = mVelocityTracker;
			final int pointerId = ev.getPointerId(0);
			velocityTracker.computeCurrentVelocity(1000, mMaximumFlingVelocitx);
			final float velocityX = velocityTracker.getXVelocity(pointerId);
			final float velocityY = velocityTracker.getYVelocity(pointerId);
			float ratio = velocityY / velocityX;
			if (Math.abs(ratio) < 0.5) {
				isHandle = false;
			}
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			if (mVelocityTracker != null) {
				mVelocityTracker.recycle();
				mVelocityTracker = null;
			}
			break;
		}
		return isHandle;
	}

}

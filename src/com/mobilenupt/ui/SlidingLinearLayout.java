package com.mobilenupt.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class SlidingLinearLayout extends LinearLayout {
	/* 手势动作最开始时的x坐标 */
	private float lastMotionX = -1;

	public SlidingLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			this.lastMotionX = (int) ev.getRawX();
		}
		if (this.lastMotionX < 150)
			return true;
		else if (MenuHorizontalScrollView.menuOut)
			return true;
		else
			return super.onInterceptTouchEvent(ev);
	}

}

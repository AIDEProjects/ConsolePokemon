package com.goldsprite.consolepokemon;
import android.widget.*;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

public class MyHoriScrollView extends HorizontalScrollView {

    public MyHoriScrollView(Context context) {
        super(context);
    }

    public MyHoriScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyHoriScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

	@Override
	public boolean onTouchEvent(MotionEvent ev)
	{
		//return super.onTouchEvent(ev);
		return false;
	}
	
    // 处理触摸事件，确保可以同时滚动
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                // 判断是否应该拦截事件
                break;
        }
		return false;
        //return super.onInterceptTouchEvent(ev);
    }

    // 这里可以添加其他你需要的逻辑
}

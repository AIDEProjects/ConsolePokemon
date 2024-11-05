package com.goldsprite.consolepokemon;

import android.widget.*;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

public class MyListView extends ListView {

    public MyListView(Context context) {
        super(context);
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

	@Override
	public boolean onTouchEvent(MotionEvent ev)
	{
		// TODO: Implement this method
		//return super.onTouchEvent(ev);
		return false;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev)
	{
		// TODO: Implement this method
		//return super.onInterceptTouchEvent(ev);
		return false;
	}
	
	

	
	
    /*// 处理横向和纵向滚动
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // 可以根据实际需要调整滚动方向的优先级
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                // 在这里加入你的逻辑来判断是横向滑动还是纵向滑动
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }*/

    /*// 提供一个自定义的 onMeasure 方法，调整宽高以支持横向滚动
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 设置宽为 MAX_VALUE，允许横向滚动
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }*/
}

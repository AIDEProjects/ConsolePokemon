package com.goldsprite.consolepokemon;

import android.content.Context;
import android.graphics.Canvas;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.graphics.Color;
import android.text.TextWatcher;
import android.view.View.OnKeyListener;
import android.text.StaticLayout;
import android.text.Layout;
import android.text.StaticLayout.Builder;
import android.graphics.Typeface;

public class CustomEditText extends View {

    private TextPaint paint;
    private StringBuilder inputText = new StringBuilder();
    private int textX = 0; 
    private int textY = 0; 
    private InputMethodManager imm;

	private int cursorPosition = 0;  // 当前光标位置
    private boolean cursorVisible = true;  // 光标可见性
    private final int BLINK_INTERVAL = 500;
	private final Runnable cursorBlinkRunnable = new Runnable() {
        @Override
        public void run() {
            cursorVisible = !cursorVisible;
            invalidate();  // 重新绘制光标
            postDelayed(this, BLINK_INTERVAL);
        }
    };

	private StaticLayout staticLayout;

	private CompletedInputListener completedInputListener;
	private OnTextChangedListener onTextChanged;

	private boolean runErr;

	private int viewWidth;

	private int viewHeight;

	private boolean ajustpanInit;
	private int initialHeight;

	private float lineSpacing = 10;
	
	private int maxLineCount = 100;

	private StaticLayout emptyLinesLayout;


    public CustomEditText(Context context) {
        super(context);
        init(context);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

	public int getLineCount() {
		return inputText.toString().split("\n").length;
	}


	public void setCompletedInputListener(CompletedInputListener completedInputListener) {
		this.completedInputListener = completedInputListener;
	}

	public void setTextChangedListener(OnTextChangedListener onTextChanged) {
		this.onTextChanged = onTextChanged;
	}

    private void init(Context context) {
        paint = new TextPaint();
        paint.setColor(0xFFFFFFFF);
        paint.setTextSize(38);
		paint.setAntiAlias(false);
		paint.setTypeface(Typeface.MONOSPACE);
		
        imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		
		post(cursorBlinkRunnable);
		post(new Runnable(){
			public void run(){
				invalidate();
			}
		});
		
		String text = "";
		for(int i=0;i<50;i++){
			text += "\n";
		}
		emptyLinesLayout = StaticLayout.Builder.obtain(text, 0, text.length(), paint, viewWidth)
			.setAlignment(Layout.Alignment.ALIGN_NORMAL) // 设置对齐方式
			.setLineSpacing(lineSpacing-paint.getFontMetrics().bottom, 1.0f) // 设置行间距
			.setMaxLines(Integer.MAX_VALUE) // 设置最大行数
			.setIncludePad(false)
			.build();
    }

	@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);
		setMeasuredDimension(viewWidth, viewHeight); // 设置视图大小
		
		DebugWindow.setDebugInfo(1, String.format("viewWidth: %d, viewHeight: %d", viewWidth, viewHeight));

		// 如果还没有初始化过高度，则进行初始化
        if (!ajustpanInit) {
            initialHeight = getMeasuredHeight();
            ajustpanInit = true;
        }
    }

    @Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		try {
			if (runErr) {
				return;
			}
			canvas.save();
			int ajustpanDiff = 0;
			if (staticLayout != null){
				if(getHeight(staticLayout) > viewHeight) {
					ajustpanDiff = viewHeight - getHeight(staticLayout);
				}
				canvas.translate(0 * offsetX + textX, offsetY + textY + ajustpanDiff); // 应用偏移量
			}
			drawText(canvas);
			if (cursorVisible) {
				drawCursor(canvas);
			}
			
			canvas.restore();
		} catch (Exception e) {
			runErr = true;
			DebugWindow.addErrLog(e);
		}
	}

	private void drawCursor(Canvas canvas) {
		// 定义光标所在行
		int cursorLine = staticLayout.getLineForOffset(cursorPosition); // 通过光标的 offset 获取行号

		// 获取光标的 x 位置
		float cursorX = staticLayout.getPrimaryHorizontal(cursorPosition);

		// 获取光标的 y 位置
		float cursorY = staticLayout.getLineBaseline(cursorLine) + staticLayout.getTopPadding(); // 加入上填充

		// 获取 TextPaint 的 FontMetrics
		TextPaint.FontMetrics fontMetrics = paint.getFontMetrics();

		// 设置光标的颜色和宽度
		int oldColor = paint.getColor();
		paint.setColor(Color.RED); // 设定光标颜色
		float cursorWidth = 8; // 光标宽度
		float offsetX = 2; // 偏移量
		float offsetY = 4;

		// 设置光标绘制区域的边界
		float rectLeft = cursorX + offsetX;                    // 矩形的左边界
		float rectTop = cursorY + fontMetrics.ascent +offsetY;          // 矩形的上边界
		float rectRight = cursorX + cursorWidth + offsetX;     // 矩形的右边界
		float rectBottom = cursorY + fontMetrics.descent +offsetY;      // 矩形的下边界

		// 绘制光标矩形
		canvas.drawRect(rectLeft, rectTop, rectRight, rectBottom, paint);

		// 恢复原来的颜色
		paint.setColor(oldColor);
	}
	


	private void drawText(Canvas canvas) {
		// 使用 StaticLayout.Builder 创建 StaticLayout
		String text = inputText.toString();
		staticLayout = StaticLayout.Builder.obtain(text, 0, text.length(), paint, viewWidth)
			.setAlignment(Layout.Alignment.ALIGN_NORMAL) // 设置对齐方式
			.setLineSpacing(lineSpacing-paint.getFontMetrics().bottom, 1.0f) // 设置行间距
			.setMaxLines(Integer.MAX_VALUE) // 设置最大行数
			.setIncludePad(false)
			.build();
		staticLayout.draw(canvas);
		
		emptyLinesLayout.draw(canvas);
		setMeasuredDimension(viewWidth, getHeight(staticLayout)+emptyLinesLayout.getHeight()); // 设置视图大小
	}

	public int getHeight(StaticLayout staticLayout) {
		return staticLayout.getHeight();// + (int)(inputText.toString().endsWith("\n") ?paint.getTextSize() + lineSpacing: 0);
	}

	public void moveCursor(int offset) {
        cursorPosition = Math.max(0, Math.min(inputText.length(), cursorPosition + offset));
        invalidate(); // 重新绘制视图
    }

	private float offsetX = 0; // 水平偏移量
	private float offsetY = 0; // 垂直偏移量
	private float maxOffset = 200;
	boolean dragged = false;
	float dragDistance = 2;
	float startX, startY;
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				startX = ev.getX();
				startY = ev.getY();
				dragged = false; // 重置拖拽标志
				break;

			case MotionEvent.ACTION_MOVE:
				float dx = ev.getX() - startX;
				float dy = ev.getY() - startY;

				// 如果滑动距离超过一定阈值，则判定为拖拽
				if (Math.abs(dx) > dragDistance || Math.abs(dy) > dragDistance) {
					dragged = true;
				}
				offsetX += dx; // 更新水平偏移量
				offsetY += dy; // 更新垂直偏移量
				startX = ev.getX(); // 更新起始点
				startY = ev.getY(); // 更新起始点
				invalidate(); // 请求重绘
				break;

			case MotionEvent.ACTION_UP:
				if (!dragged) {
					// 点击事件的逻辑，比如处理点击光标
					this.requestFocus();
					// Assume moveCursor() sets cursor position at the end
					moveCursor(inputText.length());
					imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT);
				}
				break;
		}

		return true; // 返回 true 阻止系统默认行为
	}

    @Override
    public boolean onCheckIsTextEditor() {
        return true; 
    }

	public String getText() { return inputText.toString(); }

	public void setSelection(int pos) { moveCursor(pos); }

	public void addText(String str) {
		inputText.append(str);
		limitLineCount();
		moveCursor(inputText.length());
		invalidate(); // 刷新视图以重绘
	}
	

	public void limitLineCount(){
		int index = inputText.length()-1;
		for(int i=0;i<maxLineCount;i++){
			int temp = inputText.lastIndexOf("\n", index);
			if(temp == -1){
				return;
			}
			index = temp - 1;
		}

		String temp = inputText.substring(index+2, inputText.length());
		inputText.setLength(0);
		inputText.append(temp);
	}

	public void delChar() {
		if (inputText.length() > 0) {
			int start = inputText.length() - 1;
			inputText.deleteCharAt(start);
			moveCursor(inputText.length());
			invalidate(); // 刷新视图以重绘
		}
	}

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        // 返回自定义的 InputConnection
        return new MyInputConnection(this, true);
    }

    private class MyInputConnection extends BaseInputConnection {
        public MyInputConnection(View targetView, boolean fullEditor) {
            super(targetView, fullEditor);
        }

        @Override
        public boolean commitText(CharSequence text, int newCursorPosition) {
            // 将输入的文本添加到 inputText 中
            addText(text.toString());
			onTextChanged.onTextChanged();
            return true;
        }

        @Override
        public boolean deleteSurroundingText(int beforeLength, int afterLength) {
            // 实现删除文字的逻辑
            delChar();
			onTextChanged.onTextChanged();
            return true;
        }

		@Override
        public boolean sendKeyEvent(KeyEvent event) {
            // 处理换行符
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
				if (completedInputListener != null) {
					completedInputListener.onCompletedInput();
				}
				addText("\n");
                return true;
            }
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
                if(inputText.toString().endsWith("\n")){
					return true;
				}
				return deleteSurroundingText(1, 0);
            }
			/*else if(onKeyListener != null){
			 onKeyListener.onKey(CustomEditText.this, event.getKeyCode(), event);
			 }*/

            return super.sendKeyEvent(event);
        }

        // 其他 InputConnection 方法的实现......
    }


	public interface CompletedInputListener {
		public void onCompletedInput();
	}
	public interface OnTextChangedListener {
		public void onTextChanged();
	}

}


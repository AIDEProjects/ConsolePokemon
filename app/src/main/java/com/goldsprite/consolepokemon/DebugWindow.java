package com.goldsprite.consolepokemon;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DebugWindow {

	private static MainActivity ctx;
	private static DebugWindow instance;
	private ToggleButton floatingDebugLayout_toggleButton;
	private LinearLayout floatingDebugLayout;
	private ListView debugInfoList;
	private ArrayList<String> debugInfos;
	private ArrayAdapter<String> debugInfoListAdapter;

	private ToggleButton floatingLogLayout_toggleButton;
	private LinearLayout floatingLogLayout;
	private ListView logList;
	private ArrayList<LogEntry> logs;
	private static int logsTick;
	private LogAdapter logListAdapter;
	public static int maxLine = 200;

	private static DebugWindow.Hierarchy hierarchy;


    public DebugWindow(MainActivity ctx) {
		this.ctx = ctx;
		instance = this;
		hierarchy = new Hierarchy();

		init();
	}

	private void init() {
		floatingDebugLayout = (LinearLayout) ctx.findViewById(R.id.floatingDebugLayout);
		View floatingDebugWindow = LayoutInflater.from(ctx).inflate(R.layout.debug_window, null);
		floatingDebugLayout.addView(floatingDebugWindow);

		debugInfoList = floatingDebugWindow.findViewById(R.id.debugInfoList);
		debugInfoList.setDivider(null);
		debugInfos = new ArrayList<>();
		for (int i=0;i < 20;i++) {
			debugInfos.add(i, "");
		}
		debugInfos.set(0, "Debug-Info...");
		debugInfoListAdapter = new ArrayAdapter<String>(ctx, R.layout.list_item_debug, debugInfos);
		debugInfoList.setAdapter(debugInfoListAdapter);

		floatingDebugLayout.setTranslationY(-5000);
		floatingDebugLayout_toggleButton = (ToggleButton)ctx.findViewById(R.id.floatingDebugLayout_toggleButton);
		floatingDebugLayout_toggleButton.setOnCheckedChangeListener(new ToggleButton.OnCheckedChangeListener(){
				@Override
				public void onCheckedChanged(CompoundButton btn, boolean isChecked) {
					if (isChecked) {
						showDebugLayout();
					} else {
						hideDebugLayout();
					}
				}

				private void showDebugLayout() {
					floatingDebugLayout.setTranslationY(-floatingDebugLayout.getHeight());
					floatingDebugLayout.setVisibility(View.VISIBLE);
					floatingDebugLayout.animate()
						.translationY(0)
						.setDuration(300)
						.start();
				}

				private void hideDebugLayout() {
					floatingDebugLayout.animate()
						.translationY(-floatingDebugLayout.getHeight())
						.setDuration(300)
						.withEndAction(new Runnable(){public void run() {
								floatingDebugLayout.setVisibility(View.GONE);
							}})
						.start();
				}
			});



		floatingLogLayout = (LinearLayout) ctx.findViewById(R.id.floatingLogLayout);
		View floatingLogWindow = LayoutInflater.from(ctx).inflate(R.layout.log_window, null);
		floatingLogLayout.addView(floatingLogWindow);

		logList = floatingLogWindow.findViewById(R.id.logList);
		logList.setDivider(null);
		logs = new ArrayList<>();
		logs.add(new LogEntry("Log-Info..."));
		logListAdapter = new LogAdapter(ctx, R.layout.list_item_log, logs);
		logList.setAdapter(logListAdapter);
		
		floatingLogLayout.setTranslationY(-5000);
		floatingLogLayout_toggleButton = (ToggleButton)ctx.findViewById(R.id.floatingLogLayout_toggleButton);
		floatingLogLayout_toggleButton.setOnCheckedChangeListener(new ToggleButton.OnCheckedChangeListener(){
				@Override
				public void onCheckedChanged(CompoundButton btn, boolean isChecked) {
					if (isChecked) {
						showLogLayout();
					} else {
						hideLogLayout();
					}
				}

				private void showLogLayout() {
					floatingLogLayout.setTranslationY(-floatingLogLayout.getHeight());
					floatingLogLayout.setVisibility(View.VISIBLE);
					floatingLogLayout.animate()
						.translationY(0)
						.setDuration(300)
						.start();
				}

				private void hideLogLayout() {
					floatingLogLayout.animate()
						.translationY(-floatingLogLayout.getHeight())
						.setDuration(300)
						.withEndAction(new Runnable(){public void run() {
								floatingLogLayout.setVisibility(View.GONE);
							}})
						.start();
				}
			});

	}

	public static void setDebugInfo(final int line, final String str) {
		ctx.runOnUiThread(new Runnable(){public void run() {
					if (line > 0 && line < instance.debugInfos.size()) {
						instance.debugInfos.set(line, str);
						instance.debugInfoListAdapter.notifyDataSetChanged();
					}
				}});
	}

	public static void addLog(final String str) {
		addLog("v", str);
	}
	public static void addLog(final String level, final String str) {
		ctx.runOnUiThread(new Runnable(){public void run() {
					String[] strs = str.split("\n");
					for(String i : strs){
						instance.logs.add(new LogEntry(level, String.format("[%d] %s", logsTick++, i)));
					}
					if (instance.logs.size() > maxLine) {
						int len = instance.logs.size()-maxLine;
						for(int i2=0;i2<len;i2++){
							instance.logs.remove(0);
						}
					}
					instance.logListAdapter.notifyDataSetChanged();
				}});
	}
	public static void addErrLog(Exception e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		addLog("e", "Err: \n"+sw.toString());
		MainActivity.toast("注意，运行时抛出了一个新的报错在Log");
	}


	public static Hierarchy getHierarchy() {
		return hierarchy;
	}

	HashMap<String, Object>hierarchy_map = new HashMap<>();

	public class Hierarchy {

		/*public void bindVector2(String label, Vector2 vec2){
		 if(!hierarchy_map.containsKey(label)){
		 hierarchy_map.put(label, vec2);
		 }
		 }*/

	}
	
	
	public static class LogEntry {
		public String level;
		public String msg;

		public LogEntry(String msg){
			this("v", msg);
		}
		public LogEntry(String level, String msg){
			this.level = level;
			this.msg = msg;
		}
	}
	
	

}

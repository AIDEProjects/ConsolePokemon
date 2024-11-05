package com.goldsprite.views;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import java.util.List;
import java.util.ArrayList;
import android.content.Context;
import java.util.Arrays;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import com.goldsprite.consolepokemon.*;
import android.util.Log;

public class TerminalLine {
    private ListView listView;
    private List<String> lines = new ArrayList<>(Arrays.asList(
													 "哈哈/itemEditText.setText(lines.get(position));哈", 
													 "哈哈/itemEditText.setText(lines.get(position));哈", 
													 "哈哈/itemEditText.setText(lines.get(position));哈"
	));
    private TerminalAdapter adapter;

    public TerminalLine(Context ctx, ListView listView) {
        this.listView = listView;
        adapter = new TerminalAdapter(ctx, lines);
        listView.setAdapter(adapter);
    }

    // 添加一行
    public void addLine(String line) {
        lines.add(line);
        adapter.notifyDataSetChanged();
        // 确保 ListView 滚动到最新项
        listView.setSelection(lines.size() - 1);
    }
    // 添加一行
    public void addLine(int lineNumber, String line) {
        lines.add(lineNumber, line);
        adapter.notifyDataSetChanged();
        // 确保 ListView 滚动到最新项
        listView.setSelection(lines.size() - 1);
    }
	
	// 设置指定行数的文本
    public void setLineContent(int lineNumber, String content) {
        if (lineNumber >= 0 && lineNumber < lines.size()) {
            lines.set(lineNumber, content); // 更新指定行的内容
            adapter.notifyDataSetChanged(); // 通知适配器数据已改变
        } else {
            // 可以选择抛出异常或打印日志
            Log.w("TerminalLine", "Line number out of bounds: " + lineNumber);
        }
    }

    // 获取当前行数
    public int getLineCount() {
        return lines.size();
    }
}


package com.goldsprite.consolepokemon;

import android.content.*;
import android.view.*;
import android.widget.*;
import com.goldsprite.consolepokemon.*;
import java.util.*;

public class TerminalAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final List<String> lines;

    public TerminalAdapter(Context context, List<String> lines) {
        super(context, R.layout.listview_item, lines);
        this.context = context;
        this.lines = lines;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_item, parent, false);
        }

        EditText itemEditText = (EditText)convertView.findViewById(R.id.item_edittext);
		itemEditText.setText(lines.get(position));
		/*
        // 处理可编辑与不可编辑的情况
        if (position == lines.size() - 1) {
            // 最后一行可编辑
            //itemEditText.setFocusableInTouchMode(true);
            //itemEditText.setText(lines.get(position));
        } else {
            // 其他行不可编辑
            itemEditText.setFocusable(false);
            itemEditText.setFocusableInTouchMode(false);
            itemEditText.setText(lines.get(position));
        }*/

        return convertView;
    }
}


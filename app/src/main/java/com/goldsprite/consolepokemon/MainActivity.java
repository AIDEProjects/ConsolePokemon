package com.goldsprite.consolepokemon;

import android.app.*;
import android.content.*;
import android.os.*;
import android.text.*;
import android.view.*;
import android.view.inputmethod.*;
import android.widget.*;
import com.goldsprite.views.*;
import java.util.regex.*;
import Main;
import tools.*;
import java.util.function.*;
import java.io.*;
import android.*;
import com.goldsprite.consolepokemon.R;
import java.lang.Process;

public class MainActivity extends Activity { 
	public static MainActivity instance;
    private MyListView listView;
    private TerminalLine terminalLine;

    private CustomEditText editText;
    private TextView lineNumbers;

	private String lastTxt="";

	private PipedOutputStream pos = new PipedOutputStream();
	private PipedInputStream pis = new PipedInputStream();
	private String consoleTxt="";

	private boolean started;
	private String cmd="";

	private LinearLayout gameLayout;

	public String gameDirPath = "/sdcard/consolePokemon/";
	public String gameSavesPath = gameDirPath + "gameSaves.json";
	public String gameLogsPath = gameDirPath + "logs.txt";


	private DebugWindow debugWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		try {
			init();
		} catch (Exception e) {
			DebugWindow.addErrLog(e);
		}
	}

	private void init() {
		instance = this;
		requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
        setContentView(R.layout.activity_main);

		new Thread(){public void run() {
				try {
					pis.connect(pos);
					while (true) {
						if (!cmd.equals("")) {
							debugWindow.addLog("新指令: " + cmd);
							/*Handler h = new Handler();
							 h.postDelayed(new Runnable(){public void run(){
							 toast("已超时1000ms无响应");
							 }}
							 , 1000);*/
							pos.write((cmd + "\n").getBytes());
							pos.flush();
							debugWindow.addLog("数据已刷出");
							//h.
							cmd = "";
						}
						//Thread.sleep(200);
					}
				} catch (Exception e) {
					debugWindow.addErrLog(e);
				}
			}}.start();

		editText = (CustomEditText) findViewById(R.id.text);
		editText.addText("\n输入start开启游戏.\n");
		//lineNumbers = (TextView) findViewById(R.id.lineNumbers);

		editText.post(new Runnable(){public void run() {
					updateLineNumbers();
				}});

		editText.setSelection(editText.getText().length());

		editText.setCompletedInputListener(
			new CustomEditText.CompletedInputListener(){
				public void onCompletedInput() {
					cmdHandle();
				}
			}
		);


		debugWindow = new DebugWindow(this);
	}

    private void updateLineNumbers() {
		String text = editText.getText().toString();
		//int lineCount = getLineCount(text);
		int lineCount = editText.getLineCount();
		StringBuilder lineNumbersText = new StringBuilder(); // 用于构建行号的字符串

        for (int i = 1; i <= lineCount; i++) {
            lineNumbersText.append(i).append(i == lineCount ?"": "\n"); // 添加行号到字符串中
        }

        //lineNumbers.setText(lineNumbersText.toString()); // 更新行号显示
    }

	private int getLineCount(String text) {
		// 定义正则表达式匹配换行符
        Pattern pattern = Pattern.compile("\n");
        Matcher matcher = pattern.matcher(text);

        // 初始化计数器
        int lineCount = 0;

        // 使用循环统计匹配次数
        while (matcher.find()) {
            lineCount++;
        }

        // 由于第一行没有换行符，加1来算上
        lineCount += 1;
		return lineCount;
	}


	public void cmdHandle() {
		String text = editText.getText().toString();
		int lastLineStart = text.lastIndexOf("\n", text.length() - 2);
		final String fullCmds = text.substring(Math.max(0, lastLineStart + 1));
		//toast(fullCmds);
		if (started) {
			sendCmd(fullCmds);
		} else 
		if (!started && fullCmds.equals("start")) {
			new Thread(){public void run() {
					try {
						Log.enableCustomIO(pis, new Consumer<String>(){public void accept(final String str) {
									addText(str+"\n");
								}});
						started = true;
						Main.main(null);
					} catch (Exception e) {
						debugWindow.addErrLog(e);
					}
				}}.start();
		}

	}

	public void sendCmd(String cmd) {
		this.cmd = cmd;
		//addText(cmd);
	}

	public void addText(final String str) {
		runOnUiThread(
			new Runnable(){
				public void run() {
					try {
						editText.addText(str);
						editText.setSelection(editText.getText().length());

					} catch (Exception e) {
						debugWindow.addErrLog(e);
					}
				}
			}
		);
	}


	public int findNthNewLineIndex(String str, int n) {
        int index = -1;
        for (int i = 0; i < n; i++) {
            index = str.indexOf('\n', index + 1);
            if (index == -1) {
                return -1; // 如果找不到第 n 个换行符，提前返回
            }
        }
        return index;
    }

	public int findNthNewLineIndexFromEnd(String str, int n) {
		int index = str.length(); // 从字符串的末尾开始
		for (int i = 0; i < n; i++) {
			index = str.lastIndexOf('\n', index - 1);
			if (index == -1) {
				return -1; // 如果找不到第 n 个换行符，提前返回
			}
		}
		return index;
	}


	static Toast toast;

	public static void toast(final Object str) {
		instance.runOnUiThread(
			new Runnable() {
				public void run() {
					toast = Toast.makeText(instance, "" + str, Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.TOP, 0, 0);
					toast.show();
				}
			}
		);
	}


}


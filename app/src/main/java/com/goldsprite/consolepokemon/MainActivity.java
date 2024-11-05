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
import consolepokemon.examples.V0_4_1_Demo;

public class MainActivity extends Activity{ 
	public static MainActivity instance;
    private MyListView listView;
    private TerminalLine terminalLine;

    private EditText editText;
    private TextView lineNumbers;

	private String lastTxt="";

	private PipedOutputStream pos = new PipedOutputStream();
	private PipedInputStream pis = new PipedInputStream();
	private String consoleTxt="";

	private boolean started;
	private String cmd="";

	private LinearLayout gameLayout;

	public String gameDirPath = "/sdcard/consolePokemon/";
	public String gameSavesPath = gameDirPath+"gameSaves.json";
	public String gameLogsPath = gameDirPath+"logs.txt";
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
		instance = this;
		requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
        setContentView(R.layout.activity_main);
		
		//saveLog();
		
		try{
			new Thread(){public void run(){
					try{
						pis.connect(pos);
						while(true){
							if(!cmd.equals("")){
								//toast("新指令: " + cmd);
								pos.write((cmd+"\n").getBytes());
								pos.flush();
								//toast("数据已刷出");
								cmd ="";
							}
							//Thread.sleep(200);
						}
					}catch (Exception e){
						toast(e.getMessage());
					}
				}}.start();
		}catch (Exception e){
			toast(e.getMessage());
		}

		editText = (EditText) findViewById(R.id.text);
		editText.setText("输入start开启游戏.\n");
		lineNumbers = (TextView) findViewById(R.id.lineNumbers);

		editText.post(new Runnable(){public void run(){
					updateLineNumbers();
				}});

		editText.setSelection(editText.getText().length());
		editText.setOnTouchListener(new View.OnTouchListener() {
				boolean tragged = false;
				float x, y;
				@Override
				public boolean onTouch(View view, MotionEvent ev){
					if (ev.getAction() == MotionEvent.ACTION_DOWN){
						x = ev.getX();y = ev.getY();
						tragged = false;
					}
					if (ev.getAction() == MotionEvent.ACTION_MOVE){
						float xd = ev.getX() - x, yd = ev.getY() - y;
						//滑动超50则判定取消
						if (Math.sqrt(xd * xd + yd * yd) > 50){
							tragged = true;
						}
					}
					//点击屏幕任意地方自动进入编辑
					if (ev.getAction() == MotionEvent.ACTION_UP && !tragged){
						// 设置光标始终在最后面
						editText.setSelection(editText.getText().length());

						final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
					}

					return true; // 返回 true 阻止系统默认行为
				}
			});


		findViewById(R.id.main_layout).setOnTouchListener(new View.OnTouchListener() {
				boolean tragged = false;
				float x, y;
				@Override
				public boolean onTouch(View view, MotionEvent ev){
					if (ev.getAction() == MotionEvent.ACTION_DOWN){
						x = ev.getX();y = ev.getY();
						tragged = false;
					}
					if (ev.getAction() == MotionEvent.ACTION_MOVE){
						float xd = ev.getX() - x, yd = ev.getY() - y;
						//滑动超50则判定取消
						if (Math.sqrt(xd * xd + yd * yd) > 50){
							tragged = true;
						}
					}
					//点击屏幕任意地方自动进入编辑
					if (ev.getAction() == MotionEvent.ACTION_UP && !tragged){
						//toast("onclick");
						editText.setSelection(editText.getText().length());

						final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
					}

					return true;
				}
			});

		editText.addTextChangedListener(new TextWatcher() {
				@Override
				public void afterTextChanged(Editable s){
					updateLineNumbers();
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after){}

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count){}
			});
		editText.setOnKeyListener(new View.OnKeyListener() {
				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event){
					int selectionStart = editText.getSelectionStart();
					int selectionEnd = editText.getSelectionEnd();

					if (selectionStart != selectionEnd){
						//确保无法多选文本
						//editText.setSelection(editText.getSelectionEnd(), editText.getSelectionEnd());
						return true;
					}
					// 检查是否为按下的删除键
					if (event.getAction() == KeyEvent.ACTION_DOWN){
						if (keyCode == KeyEvent.KEYCODE_DEL || keyCode == KeyEvent.KEYCODE_DPAD_LEFT){

							// 检查光标位置之前的字符是否是换行符
							if (selectionStart > 0 && editText.getText().charAt(selectionStart - 1) == '\n'){
								// 如果在换行符之前，返回true，阻止默认事件
								return true; // 阻止删除换行符
							}else{
								return false;
							}
						}
						if (keyCode == KeyEvent.KEYCODE_ENTER){
							cmdHandle();
							return false;
						}
						if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT || keyCode == KeyEvent.KEYCODE_BACK){
							return false;
						}
					}
					return true; // 继续处理其他按键
				}
			});
	}

    private void updateLineNumbers(){
		String text = editText.getText().toString();
		int lineCount = getLineCount(text);
        StringBuilder lineNumbersText = new StringBuilder(); // 用于构建行号的字符串

        for (int i = 1; i <= lineCount; i++){
            lineNumbersText.append(i).append(i == lineCount ?"": "\n"); // 添加行号到字符串中
        }

        lineNumbers.setText(lineNumbersText.toString()); // 更新行号显示
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


	public void cmdHandle(){
		String text = editText.getText().toString();
		int lastLineStart = text.lastIndexOf("\n", text.length() - 2);
		final String fullCmds = text.substring(Math.max(0, lastLineStart + 1));
		//toast(fullCmds);
		if (started){
			sendCmd(fullCmds);
		}else 
		if (!started && fullCmds.equals("start")){
			new Thread(){public void run(){
					try{
						Log.enableCustomIO(pis, new Consumer<String>(){public void accept(final String str){
									addText(str);
								}});
						started = true;
						Main.main(null);
					}catch (Exception e){
						toast(e.getMessage());
					}
				}}.start();
		}

	}
	
	public void sendCmd(String cmd){
		this.cmd = cmd;
		addText(cmd);
	}

	public void addText(final String str){
		runOnUiThread(new Runnable(){public void run(){
					consoleTxt += str + "\n";
					int nthLineIndex = findNthNewLineIndexFromEnd(consoleTxt, 100-1);
					if(nthLineIndex != -1){
						consoleTxt = consoleTxt.substring(nthLineIndex+1);
					}
					editText.setText(consoleTxt);
					editText.setSelection(editText.getText().length());
				}});
	}


	public static void toast(final Object str){
		instance.runOnUiThread(new Runnable(){public void run(){
					Toast.makeText(instance, "" + str, Toast.LENGTH_SHORT).show();
				}});
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
	
	
	private void saveLog() {
		new Thread(new Runnable(){public void run() {
					try {
						toast("读取log...");
						String str="logcat...\n", line="";

						ProcessBuilder pb = new ProcessBuilder("logcat", "-d");
						Process process = pb.start();
						BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));

						//String dirPath = "/storage/emulated/0/BraveJourney2_Reproduction/logs/";
						String dirPath = gameDirPath;
						File dir = new File(dirPath);
						if (!dir.exists()) {
							dir.mkdirs();
						}
						File file = new File(dirPath + "logs.txt");
						if (!file.exists()) {
							file.createNewFile();
						}
						FileOutputStream fos = new FileOutputStream(file);

						while ((line = br.readLine()) != null) {
							if (str.isEmpty()) {
							}
							String st = line + "\n";
							str += st;
						}

						fos.write(str.getBytes());
						fos.flush();
						fos.close();

						//pb = new ProcessBuilder("logcat", "-c");
						//process = pb.start();
						toast("读取log完成");
					} catch (Exception e) {}
				}}).start();

	}
	


}


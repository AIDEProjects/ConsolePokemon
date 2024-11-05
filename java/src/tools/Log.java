package tools;
import consolepokemon.core.yabis.Yabi;
import consolepokemon.core.trainers.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import consolepokemon.core.utils.*;
import java.io.*;

public class Log {
	public static Scanner scan;
	// 存储类型与转换函数的映射
    private static Map<Class<?>, Function<String, ?>> converters = new HashMap<Class<?>, Function<String, ?>>(){{
			put(Integer.class, new Function<String, Integer>(){public Integer apply(String str) {return Integer.parseInt(str);}});
			put(Long.class, new Function<String, Long>(){public Long apply(String str) {return Long.parseLong(str);}});
			put(Float.class, new Function<String, Float>(){public Float apply(String str) {return Float.parseFloat(str);}});
			put(String.class, new Function<String, String>(){public String apply(String str) {return str;}});
		}};

	static boolean customIO;
	static InputStream is = System.in;
	static Consumer<String> outPrintln;

	public static void enableCustomIO(InputStream is2, Consumer<String> outPrintln2) {
		customIO = true;
		is = is2;
		outPrintln = outPrintln2;
	}

	public static void v() { Log.v(""); }
	public static void v(Object str) {
		if (!customIO) {
			System.out.println("" + str);
		} else {
			outPrintln.accept("" + str);
		}
	}
	public static void v(Object str, Object... args) {
		v(String.format("" + str, args));
	}

	public static void clearLine(int line) {
		String str="";
		for (int i=0;i < line - 1;i++) {
			str += "\n";
		}
		Log.v(str);
	}

    public static <T> T input(T t) {
		if (scan == null) {
			scan = new Scanner(is);
		}
		Class<T> type = (Class<T>)t.getClass();
		if (scan.hasNextLine()) {
			String userInput = scan.nextLine();
			Function<String, ?> converter = converters.get(type);
			if (converter != null) {
				T inputStr = type.cast(converter.apply(userInput));
				//System.out.println("接收到数据: " + inputStr);
				
				return inputStr;
			}
			throw new IllegalArgumentException("不支持的类型: " + type);
		}
		throw new NullPointerException("阻塞接收被跳过了.");
    }

	public static void yabi(Yabi yabi) {
		Log.v(Icons.format(""
						   + String.format("NAME：%s", yabi.name)
						   + String.format("\n//LV：%d-(✨%.1f/%.1f)", yabi.LV, yabi.EXP, yabi.NeedEXP)
						   + String.format("\n//HP：%.1f/%.1f", yabi.HP, yabi.maxHP)
						   + String.format("\n//ATK️：%.1f", yabi.ATK)
						   + String.format("\n//SP️：%.1f", yabi.SP)
						   + String.format("\n//CRIT：%.1f%%", yabi.CRIT * 100)
						   ));
	}

	public static void yabiStatus(Yabi yabi) {
		Log.v(Icons.format(""
						   + String.format("NAME：%s", yabi.name)
						   + String.format("\n//HP：%.1f/%.1f", yabi.HP, yabi.maxHP)
						   ));
	}

	public static void trainer(Trainer trainer) {
		Log.v(
			trainer.getUuid() + ":" + trainer.getCard().name
			+ "-" + trainer.getStatus().name
			+ ", {" + Trainer.yabiNameList(trainer) + "}"
		);
	}
	public static void trainerYabis(Trainer trainer) {
		List<Yabi> yabis = trainer.getYabis();
		String card = trainer.getCard().name;
		Log.v(card + "的阵容：");
		int i=0;
		for (Yabi y : yabis) {
			Log.v(i++);
			Log.yabi(y);
		}
	}
}

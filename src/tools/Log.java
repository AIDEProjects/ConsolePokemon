package tools;
import consolepokemon.core.yabis.Yabi;
import consolepokemon.core.trainers.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class Log
{
	public static Scanner scan = new Scanner(System.in);
	// 存储类型与转换函数的映射
    private static Map<Class<?>, Function<String, ?>> converters = new HashMap<Class<?>, Function<String, ?>>(){{
			put(Integer.class, new Function<String, Integer>(){public Integer apply(String str){return Integer.parseInt(str);}});
			put(Float.class, new Function<String, Float>(){public Float apply(String str){return Float.parseFloat(str);}});
			put(String.class, new Function<String, String>(){public String apply(String str){return str;}});
		}};
	
	public static void v(){ Log.v(""); }
	public static void v(Object str){
		System.out.println(""+str);
	}
	
	public static void clearLine(int line)
	{
		String str="";
		for(int i=0;i<line-1;i++){
			str+="\n";
		}
		Log.v(str);
	}

    public static <T> T input(T t) {
		Class<T> type = (Class<T>)t.getClass();
        String userInput = scan.nextLine();
        Function<String, ?> converter = converters.get(type);
        if (converter != null) {
            return type.cast(converter.apply(userInput));
        } else {
            throw new IllegalArgumentException("不支持的类型: " + type);
        }
    }
	
	public static void yabi(Yabi yabi){
		Log.v(
			"NAME："+yabi.name
			+"\n"+"💖HP："+yabi.HP+"/"+yabi.maxHP
			+"\n"+"⚔️ATK："+yabi.ATK
			+"\n"+"⚡️SP："+yabi.SP
			+"\n"+"💥CRIT："+(yabi.CRIT*100)+"%"
		);
	}
	
	public static void yabiStatus(Yabi yabi){
		Log.v(
			"NAME："+yabi.name
			+"\n"+"💖HP："+yabi.HP+"/"+yabi.maxHP
		);
	}
	
	public static void trainer(Trainer trainer){
		Log.v(
			trainer.getUuid()+":"+trainer.getCard().name
			+"-"+trainer.getStatus().name
			+", {"+Trainer.yabiNameList(trainer)+"}"
		);
	}
	public static void trainerYabis(Trainer trainer){
		var yabis = trainer.getYabis();
		var card = trainer.getCard().name;
		Log.v(card+"的阵容：");
		int i=0;
		for(Yabi y : yabis){
			Log.v(i++);
			Log.yabi(y);
		}
	}
}

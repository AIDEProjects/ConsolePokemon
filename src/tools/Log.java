package tools;
import consolepokemon.core.yabi.Yabi;

public class Log
{
	public static void v(){ Log.v(""); }
	public static void v(String str){
		System.out.println(str);
	}
	
	public static void yabi(Yabi yabi){
		Log.v(
			"亚比NAME："+yabi.name
			+"\n"+"亚比HP/MAXHP："+yabi.hp+"/"+yabi.maxHp
			+"\n"+"亚比ATK："+yabi.atk
			+"\n"+"亚比DEX："+yabi.sp
		);
	}
	public static void yabiStatus(Yabi yabi){
		Log.v(
			"亚比NAME："+yabi.name
			+"\n"+"亚比HP/MAXHP："+yabi.hp+"/"+yabi.maxHp
		);
	}
}

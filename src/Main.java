import java.util.*;
import consolepokemon.core.Yabi;
import consolepokemon.core.TorpidWooden;
import consolepokemon.core.QuickRabbit;

public class Main {
	
	public static void main(String[] args) {
		new Main().run();
    }
	
	public void run(){
		YabiDemo();
	}
	
	
	public void YabiDemo(){
		List<Yabi> yabis = new ArrayList<>();
		
		TorpidWooden yabi = new TorpidWooden();
		QuickRabbit yabi2 = new QuickRabbit();
		
		yabis.add(yabi);
		yabis.add(yabi2);
		for(Yabi i : yabis){
			LogYabi(i);
			Log("\n");
		}
	}
	
	public static void LogYabi(Yabi yabi){
		Log(
			"亚比NAME："+yabi.name
			+"\n"+"亚比MAXHP："+yabi.maxHp
			+"\n"+"亚比HP："+yabi.hp
			+"\n"+"亚比ATK："+yabi.atk
			+"\n"+"亚比DEX："+yabi.dex
		);
	}
	
	public static void Log(String str){
		System.out.println(str);
	}
    
}

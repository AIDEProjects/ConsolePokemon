package tools;
import consolepokemon.core.yabis.Yabi;
import consolepokemon.core.trainers.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class Log
{
	public static void v(){ Log.v(""); }
	public static void v(Object str){
		System.out.println(""+str);
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
			"亚比NAME："+yabi.name
			+"\n"+"亚比HP/MAXHP："+yabi.HP+"/"+yabi.maxHP
		);
	}

	public static void trainer(ITrainer trainer){
		List<String> nameList = trainer.getYabis().stream().map(new Function<Yabi, String>(){public String apply(Yabi yabi){ return yabi.name; }}).collect(Collectors.toList());
		Log.v(
			"Trainer: "+trainer.getCard().name
			+", uuid: "+trainer.getUuid()
			+", statu: "+trainer.getStatus().name
			+", {"+String.join(",", nameList.toArray(new String[]{}))+"}"
		);
	}
	public static void trainerYabis(ITrainer trainer){
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

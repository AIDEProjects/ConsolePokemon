package consolepokemon.examples;

import consolepokemon.core.yabi.QuickRabbit;
import consolepokemon.core.yabi.Yabi;
import consolepokemon.core.yabi.TorpidWooden;
import java.util.ArrayList;
import java.util.List;
import tools.Log;

public class YabiDemo 
{
	public YabiDemo(){
		List<Yabi> yabis = new ArrayList<>();
		TorpidWooden yabi = new TorpidWooden();
		QuickRabbit yabi2 = new QuickRabbit();

		yabis.add(yabi);
		yabis.add(yabi2);
		for(Yabi i : yabis){
			LogYabi(i);
			Log.v("\n");
		}
	}

	public static void LogYabi(Yabi yabi){
		Log.v(
			"亚比NAME："+yabi.name
			+"\n"+"亚比MAXHP："+yabi.maxHp
			+"\n"+"亚比HP："+yabi.hp
			+"\n"+"亚比ATK："+yabi.atk
			+"\n"+"亚比DEX："+yabi.dex
		);
	}
}

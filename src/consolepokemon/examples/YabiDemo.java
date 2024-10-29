package consolepokemon.examples;

import consolepokemon.core.yabis.QuickRabbit;
import consolepokemon.core.yabis.Yabi;
import consolepokemon.core.yabis.TorpidWooden;
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
			+"\n"+"亚比MAXHP："+yabi.maxHP
			+"\n"+"亚比HP："+yabi.HP
			+"\n"+"亚比ATK："+yabi.ATK
			+"\n"+"亚比DEX："+yabi.SP
		);
	}
}

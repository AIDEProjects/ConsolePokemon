package consolepokemon.examples.v0_4_1;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import consolepokemon.core.datas.GlobalData;
import consolepokemon.core.items.*;
import consolepokemon.core.yabis.Yabi;
import consolepokemon.core.trainers.Trainer;
import tools.Log;
import consolepokemon.core.trainers.HumanTrainer;
import consolepokemon.core.trainers.WildYabi;
import consolepokemon.core.yabis.TorpidWooden;
import consolepokemon.core.yabis.QuickRabbit;
import tools.MyGson;

public class TestGameSerialize 
{
	public TestGameSerialize(){
		handle();
	}
	
	private void handle() {
		GlobalData globalDatas = new GlobalData();
		HumanTrainer player = new HumanTrainer();
		player.addYabi(new TorpidWooden());
		player.inventory.addItem(new BloodCrystal());
		player.gainCoin(3);
		globalDatas.gameDatas.duelMatcher.bindTrainer(player);

		globalDatas.gameDatas.duelMatcher.addTrainer(WildYabi.newWildYabi(QuickRabbit.class));
		globalDatas.gameDatas.duelMatcher.addTrainer(WildYabi.newWildYabi(TorpidWooden.class));
		
		String j_gameDatas = MyGson.toJson(globalDatas);
		Log.v(j_gameDatas);
		
		GlobalData to_gameDatas = MyGson.fromJson(j_gameDatas, GlobalData.class);
		Log.v(to_gameDatas);
	}
	
}

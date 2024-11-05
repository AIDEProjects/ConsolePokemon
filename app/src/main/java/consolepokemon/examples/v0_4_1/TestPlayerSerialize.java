package consolepokemon.examples.v0_4_1;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import consolepokemon.core.trainers.HumanTrainer;
import consolepokemon.core.yabis.Yabi;
import java.util.LinkedHashMap;
import consolepokemon.core.yabis.TorpidWooden;
import consolepokemon.core.yabis.QuickRabbit;
import tools.Log;
import consolepokemon.core.trainers.Trainer;
import consolepokemon.core.trainers.WildYabi;
import consolepokemon.core.items.BloodCrystal;
import consolepokemon.core.inventories.Inventory;
import consolepokemon.core.items.Item;
import java.util.List;

public class TestPlayerSerialize 
{

	public Gson gson;
	public TestPlayerSerialize(){
		GsonBuilder gb = new GsonBuilder();
		gb.registerTypeAdapter(Yabi.class, new Yabi.YabiSerializer());
		gb.registerTypeAdapter(Yabi.class, new Yabi.YabiDeserializer());
		gb.registerTypeAdapter(Item.class, new Item.ItemSerializer());
		gb.registerTypeAdapter(Item.class, new Item.ItemDeserializer());
		gb.setPrettyPrinting();
		gson = gb.create();
		
		handle();
	}

	private void handle() {
		HumanTrainer trainer = new HumanTrainer();
		trainer.addYabi(new TorpidWooden());
		trainer.inventory.addItem(new BloodCrystal());
		trainer.gainCoin(3);
		
		String j_trainer = gson.toJson(trainer);
		Log.v(j_trainer);
		
		HumanTrainer to_trainer = gson.fromJson(j_trainer, HumanTrainer.class);
		Log.v(to_trainer);
		
		
		Yabi yabi = new TorpidWooden();
		String j_yabi = gson.toJson(yabi);
		Log.v(j_yabi);
		
	 	LinkedHashMap<Long, Yabi> yabis = new LinkedHashMap<>();
		yabis.put(1l, new TorpidWooden());
		yabis.put(2l, new QuickRabbit());
		String j_yabis = gson.toJson(yabis);
		Log.v(j_yabis);
		
		WildYabi torpidWooden = WildYabi.newWildYabi(TorpidWooden.class);
		String j_t = gson.toJson(torpidWooden);
		Log.v(j_t);
		
	}
	
}

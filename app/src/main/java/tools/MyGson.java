package tools;

import com.google.gson.GsonBuilder;
import consolepokemon.core.items.Item;
import consolepokemon.core.trainers.Trainer;
import consolepokemon.core.yabis.Yabi;
import com.google.gson.Gson;

public class MyGson 
{

	public static Gson gson;
	
	static{
		GsonBuilder gb = new GsonBuilder();
		gb.registerTypeAdapter(Trainer.class, new Trainer.TrainerSerializer());
		gb.registerTypeAdapter(Trainer.class, new Trainer.TrainerDeserializer());
		gb.registerTypeAdapter(Yabi.class, new Yabi.YabiSerializer());
		gb.registerTypeAdapter(Yabi.class, new Yabi.YabiDeserializer());
		gb.registerTypeAdapter(Item.class, new Item.ItemSerializer());
		gb.registerTypeAdapter(Item.class, new Item.ItemDeserializer());
		gb.setPrettyPrinting();
		gson = gb.create();
	};
	
	public static String toJson(Object obj){
		String json = gson.toJson(obj);
		json = json.replace("  ", "\t");
		return json;
	}
	
	public static <T> T fromJson(String json, Class<T> clazz){
		return gson.fromJson(json, clazz);
	}
	
	
}

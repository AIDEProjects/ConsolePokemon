package consolepokemon.core.inventories;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import consolepokemon.core.items.Item;
import consolepokemon.core.trainers.HumanTrainer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import tools.Log;

public class Inventory
{
	
	public static class InventorySerializer implements JsonSerializer<Inventory> {
		@Override
		public JsonElement serialize(Inventory src, Type typeOfSrc, JsonSerializationContext context) {
			JsonObject jsonObject = new JsonObject();
			JsonArray itemsArray = new JsonArray();

			for (Item item : src.items) {
				JsonObject itemObject = new JsonObject();
				itemObject.add("type", new JsonPrimitive(item.getClass().getName())); // 存储类路径
				itemObject.add("data", context.serialize(item)); // 序列化 Item 数据
				itemsArray.add(itemObject);
			}

			jsonObject.add("items", itemsArray);
			return jsonObject;
		}
	}

	public static class InventoryDeserializer implements JsonDeserializer<Inventory> {
		@Override
		public Inventory deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			JsonObject jsonObject = json.getAsJsonObject();
			Inventory inventory = new Inventory();

			JsonArray itemsArray = jsonObject.getAsJsonArray("items");
			for (JsonElement itemElement : itemsArray) {
				JsonObject itemObject = itemElement.getAsJsonObject();
				String typeName = itemObject.get("type").getAsString(); // 获取类路径
				JsonElement dataElement = itemObject.get("data"); // 获取数据部分

				try {
					// 动态加载类并实例化
					Class<?> clazz = Class.forName(typeName);
					Item item = context.deserialize(dataElement, clazz);
					inventory.addItem(item);
				} catch (ClassNotFoundException e) {
					throw new JsonParseException("Unknown type: " + typeName, e);
				}
			}

			return inventory;
		}
	}
	
	public HumanTrainer owner;
	public List<Item> items = new ArrayList<>();
	
	public Inventory(){}
	public Inventory(HumanTrainer owner){
		setOwner(owner);
	}
	
	public void setOwner(HumanTrainer owner){
		this.owner = owner;
	}
	
	public void addItem(Item i){
		if(!hasItem(i)){
			items.add(i);
		}else{
			for(Item item : items){
				if(item.getClass().equals(i.getClass())){
					item.count+=i.count;
					break;
				}
			}
		}
	}
	
	public boolean hasItem(Item i){
		for(Item item : items){
			if(item.getClass().equals(i.getClass())){
				return true;
			}
		}
		return false;
	}

	public void removeItem(int index){
		if(index >= 0 && index < items.size()){
			removeItem(items.get(index));
		}
	}
	public void removeItem(Item i){
		if(items.contains(i)){
			items.remove(i);
		}
	}
	
	public Item getItem(int index){
		if(index >= 0 && index < items.size()){
			return items.get(index);
		}
		return null;
	}
	
	public void useItem(int index){
		Item item = getItem(index);
		if(item !=null){
			item.use(owner);
		}
	}

	public void show(){
		Log.v("背包物品如下: ");
		String itemsStr = "";
		itemsStr += String.format("晶币x%d\n", owner.coin);
		for(int i=0;i<items.size();i++){
			Item item = getItem(i);
			itemsStr +=String.format("{%d-%sx%d}", i, item.getName(), item.count);
			itemsStr += i!=items.size()-1?", ": "";
		}
		Log.v(itemsStr);
	}
	
	
	
	
	
}

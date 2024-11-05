package consolepokemon.core.items;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import consolepokemon.core.trainers.HumanTrainer;
import consolepokemon.core.yabis.Yabi;
import java.lang.reflect.Type;
import tools.Log;
import com.google.gson.JsonDeserializer;

public abstract class Item {

	public static class ItemSerializer implements JsonSerializer<Item> {
		@Override
		public JsonElement serialize(Item src, Type typeOfSrc, JsonSerializationContext context) {
			JsonObject jsonObject = new JsonObject();
			jsonObject.add("type", new JsonPrimitive(src.getClass().getName())); // 存储类路径
			jsonObject.add("data", context.serialize(src)); // 序列化 Item 数据
			return jsonObject;
		}
	}

	public static class ItemDeserializer implements JsonDeserializer<Item> {
		@Override
		public Item deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			JsonObject jsonObject = json.getAsJsonObject();
			String typeName = jsonObject.get("type").getAsString(); // 获取类路径

			try {
				// 动态加载类并实例化
				Class<?> clazz = Class.forName(typeName);
				// 反序列化具体的数据
				Item item = context.deserialize(jsonObject.get("data"), clazz);
				return item;
			} catch (ClassNotFoundException e) {
				throw new JsonParseException("Unknown type: " + typeName, e);
			}
		}
	}

	public String name=getName();
	public abstract String getName();
	public abstract void use(Object t);
	public int count = 1;
	public void setCount(int count) { this.count = count; }

	@Override
	public Item clone() {
		try {
			Class clazz = this.getClass();
			Item cln = (Item)clazz.newInstance();
			cln.count = this.count;
			return cln;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void useItem_heal(Item item, HumanTrainer t, float heal, boolean isRatio) {
		Log.v("选择你要使用的Yabi, 请输入uuid：");
		String yabiStr = "";
		for (int i=0;i < t.getYabis().size();i++) {
			Yabi yabi = t.getYabis().get(i);
			yabiStr += String.format("{%d-%s: 💖%.1f/%.1f}", yabi.getUuid(), yabi.getName(), yabi.getHP(), yabi.maxHP);
			yabiStr += (i != t.getYabis().size() - 1) ?", ": "";
		}
		Log.v(yabiStr);
		boolean done = false;
		while (!done) {
			long uuid = Log.input(0L);
			Yabi y = t.getYabi(uuid);
			if (y != null) {
				y.heal(heal * (isRatio ?y.maxHP : 1));
				Log.v("物品%sx1已被使用.", item.getName());
				done = true;
				int laterCount = item.count - 1;
				if (laterCount < 0) {
					t.inventory.removeItem(item);
				} else {
					item.count -= 1;
				}
			} else {
				Log.v("请重新输入正确的uuid.");
			}
		}
	}
}

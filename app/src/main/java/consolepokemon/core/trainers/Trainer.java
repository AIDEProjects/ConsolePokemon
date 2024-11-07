package consolepokemon.core.trainers;
import com.goldsprite.consolepokemon.DebugWindow;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import consolepokemon.core.utils.Utils;
import consolepokemon.core.yabis.Yabi;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import tools.Log;

public abstract class Trainer
{
	public static class TrainerSerializer implements JsonSerializer<Trainer> {
		@Override
		public JsonElement serialize(Trainer src, Type typeOfSrc, JsonSerializationContext context) {
			JsonObject jsonObject = new JsonObject();
			jsonObject.add("type", new JsonPrimitive(src.getClass().getName())); // 存储类路径
			jsonObject.add("data", context.serialize(src)); // 序列化 Item 数据
			return jsonObject;
		}
	}

	public static class TrainerDeserializer implements JsonDeserializer<Trainer> {
		@Override
		public Trainer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			JsonObject jsonObject = json.getAsJsonObject();
			String typeName = jsonObject.get("type").getAsString(); // 获取类路径

			try {
				// 动态加载类并实例化
				Class<?> clazz = Class.forName(typeName);
				// 反序列化具体的数据
				Trainer src = context.deserialize(jsonObject.get("data"), clazz);
				return src;
			} catch (ClassNotFoundException e) {
				DebugWindow.addErrLog(new JsonParseException("Unknown type: " + typeName, e));
				return null;
			}
		}
	}
	
	public enum Status{
		Idle("闲置中"), 
		Duel("对决中"), 
		Defeat("已战败");

		public final String name;
		private Status(String name){this.name = name;}
	}
	public enum Card{
		WildYabi("野生亚比"), 
		Human("人类");

		public final String name;
		private Card(String name){this.name = name;}
	}
	public Status status = Status.Idle;
	public abstract Card getCard();
	public final long uuid;
	
	public LinkedHashMap<Long, Yabi> yabis = new LinkedHashMap<>();
	public long currentYabiUuid;
	public Yabi getCurrentYabi(){ return getYabi(currentYabiUuid); }
	
	
	public Trainer(){
		uuid = Utils.newUuid();
	}
	
	public boolean setCurrentYabi(long uuid)
	{
		Yabi yabi = getYabi(uuid);
		if (yabi == null)
		{
			Log.v("切换首战亚比失败, 找不到此亚比.");
			return false;
		}
		if (!yabi.isAlive())
		{
			Log.v("切换首战亚比失败, 亚比已战败.");
			return false;
		}
		yabis.remove(yabi.getUuid());
		LinkedHashMap<Long, Yabi> temp = new LinkedHashMap<Long, Yabi>(yabis);
		yabis.clear();
		yabis.put(yabi.getUuid(), yabi);
		yabis.putAll(temp);
		Log.v("切换首战亚比为" + yabi.toString() + "成功.");
		currentYabiUuid = yabi.getUuid();
		return true;
	}
	
	public String displayRepos(){
		return String.format("{%d-%s-%s: [%s]}", getUuid(), getCard().name, getStatus().name, Trainer.yabiNameList(this));
	}
	public String displayName(){
		return String.format("{%d-%s-%s}", getUuid(), getCard().name, getStatus().name);
	}
	@Override
	public String toString(){ return displayName(); }
	
	public long getUuid(){ return this.uuid; }
	
	public Status getStatus(){ return this.status; }
	
	public void setStatus(Status statu)
	{
		status = statu;
	}

	public Yabi getYabi(long uuid)
	{
		return yabis.get(uuid);
	}
	
	public void addYabi(Yabi yabi)
	{
		if(!yabis.containsValue(yabi)){
			boolean isEmpty = yabis.isEmpty();
			yabis.put(yabi.getUuid(), yabi);
			if(isEmpty){
				currentYabiUuid = yabi.getUuid();
			}
		}
	}

	public List<Yabi> getYabis()
	{
		return Arrays.asList(yabis.values().toArray(new Yabi[]{}));
	}
	
	public static String yabiNameList(Trainer trainer){
		/*List<String> nameList = trainer.getYabis().stream().map(new Function<Yabi, String>(){public String apply(Yabi yabi){ 
			return yabi.getUuid()+":"+yabi.name+"-"+(yabi.isAlive()?"可用":"战败"); 
		}}).collect(Collectors.toList());
		*/
		List<String> nameList=new ArrayList<String>();
		for(Yabi yabi : trainer.getYabis()){
			String formatName = yabi.getUuid()+":"+yabi.name+"-"+(yabi.isAlive()?"可用":"战败");
			nameList.add(formatName);
		}
		return String.join(", ", nameList.toArray(new String[]{}));
	}
	
	public boolean hasActiveYabi(){
		boolean hasActive=false;
		for(Yabi y : yabis.values()){
			if(y.isAlive()){
				hasActive = true;
				break;
			}
		}
		return hasActive;
		//return yabis.values().stream().anyMatch(new Predicate<Yabi>(){public boolean test(Yabi yabi){return yabi.isAlive();}});
	}
	
}

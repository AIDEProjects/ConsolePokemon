package consolepokemon.core.yabis;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import consolepokemon.core.items.Item;
import consolepokemon.core.utils.Icons;
import consolepokemon.core.utils.Utils;
import java.lang.reflect.Type;
import tools.Log;

public abstract class Yabi
{
	public static class YabiSerializer implements JsonSerializer<Yabi> {
		@Override
		public JsonElement serialize(Yabi src, Type typeOfSrc, JsonSerializationContext context) {
			JsonObject jsonObject = new JsonObject();
			jsonObject.add("type", new JsonPrimitive(src.getClass().getName())); // 存储类路径
			jsonObject.add("data", context.serialize(src)); // 序列化 Item 数据
			return jsonObject;
		}
	}

	public static class YabiDeserializer implements JsonDeserializer<Yabi> {
		@Override
		public Yabi deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			JsonObject jsonObject = json.getAsJsonObject();
			String typeName = jsonObject.get("type").getAsString(); // 获取类路径

			try {
				// 动态加载类并实例化
				Class<?> clazz = Class.forName(typeName);
				// 反序列化具体的数据
				Yabi src = context.deserialize(jsonObject.get("data"), clazz);
				return src;
			} catch (ClassNotFoundException e) {
				throw new JsonParseException("Unknown type: " + typeName, e);
			}
		}
	}
	
	private long uuid;
	public long getUuid(){ return uuid; }
	public String name="Yabi";
	public String getName() { return name; }
	@Override
	public String toString(){ return String.format("{%d:%s}", uuid, name); }
	public float HP;
	public float getHP() { return HP; }
	public void setHP(float HP) { this.HP = HP; }
	public float maxHP=20;

	public float ATK=5;//攻击
	public float SP=3;//速度
	public float CRIT=0.2f;//暴击率
	public float CRITVAL = 2f;//暴击倍率

	public int productCoin=10;//战胜后提供晶币
	
	public float productEXP=15;//战胜后提供经验
	public int LV;//等级
	public float EXP;//经验
	public float NeedEXP=30;//升级所需经验

	
	public Yabi(){
		init();
	}

	private void init(){
		uuid = Utils.newUuid();
		initProps();
		HP = maxHP;
	}

	protected abstract void initProps();
	
	public boolean isAlive(){
		return HP > 0;
	}
	
	public void attack(Yabi victim){
		boolean isCritical = Utils.ran() < CRIT;
		float damage = (isCritical ? CRITVAL : 1) * ATK;
		Log.v(Icons.format("%s %s %s, //HP-%.1f", this, (isCritical?"//CRIT":"//ATK"), victim, damage));
		victim.hurt(damage);
	}
	
	private void hurt(float damage){
		setHP(getHP() - damage);
		if(isAlive()){
			//Log.v(name+"-"+damage+"💖，剩余💖："+getHP());
		}else{
			//Log.v(name+"-"+damage+"💖|致命，剩余💖："+getHP());
			dead();
		}
	}
	
	private void dead(){
		setHP(0);
		//Log.v(name+"💀");
	}

	public void heal(){
		heal(maxHP);
	}
	public void heal(float healVal){
		float laterHp = getHP()+healVal;
		laterHp = Math.min(maxHP, laterHp);
		if(laterHp == maxHP){
			Log.v("亚比%s已被治愈💖.", name);
			setHP(maxHP);
		}else{
			Log.v("亚比%s已治疗💖+%.1f.", name, healVal);
			setHP(healVal);
		}
	}
	
	public void updateLV(float exp){
		float laterExp = EXP+exp;
		if(laterExp < NeedEXP){
			Log.v("亚比%s经验增加+%.1f, 当前: %.1f/%.1f.", name, exp, laterExp, NeedEXP);
			EXP = laterExp;
		}else{
			float yu = laterExp%NeedEXP;
			int addLV = (int)((laterExp-yu)/NeedEXP);
			levelUp(addLV);
			EXP = yu;
		}
	}
	public void levelUp(){
		levelUp(1);
	}
	public void levelUp(int addLV){
		float addProps = 1 * addLV;
		LV += addLV;
		Log.v("亚比%s升级了LV+%d，当前%d级.", name, addLV, LV);

		Log.v("全属性+%.1f.", addProps);
		maxHP +=addProps * 2f;
		ATK += addProps;
		SP += addProps;
		CRIT += Math.min(1f, addProps*0.05f);
		CRITVAL += addProps*0.1f;

		heal();
	}
	
}

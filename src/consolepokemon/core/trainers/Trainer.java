package consolepokemon.core.trainers;
import consolepokemon.core.yabis.*;
import java.util.*;
import java.util.stream.*;
import java.util.function.*;
import consolepokemon.core.utils.*;
import tools.*;

public abstract class Trainer
{
	/*
	public String customName="";
	public String getYabiName(Yabi yabi){
		if(customName.isBlank()){
			return String.format("{%d-%s}", yabi.getUuid(), yabi.name);
		}
		return String.format("{%d-%s的%s}", yabi.getUuid(), customName, yabi.name);
	}*/
	
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
	public Yabi current;
	
	public Trainer(){
		uuid = Utils.newUuid();
	}
	
	public boolean setCurrent(long uuid)
	{
		Yabi yabi = getYabi(uuid);
		if (yabi == null)
		{
			Log.v("切换首战亚比失败, 找不到此亚比.");
			return false;
		}
		yabis.remove(yabi.getUuid());
		var temp = new LinkedHashMap<Long, Yabi>(yabis);
		yabis.clear();
		yabis.put(yabi.getUuid(), yabi);
		yabis.putAll(temp);
		Log.v("切换首战亚比为" + yabi.toString() + "成功.");
		current = yabi;
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
			if(yabis.isEmpty()){
				current=yabi;
			}
			yabis.put(yabi.getUuid(), yabi);
		}
	}

	public List<Yabi> getYabis()
	{
		return yabis.values().stream().toList();
	}
	
	public static String yabiNameList(Trainer trainer){
		List<String> nameList = trainer.getYabis().stream().map(new Function<Yabi, String>(){public String apply(Yabi yabi){ 
			return yabi.getUuid()+":"+yabi.name+"-"+(yabi.isAlive()?"可用":"战败"); 
		}}).collect(Collectors.toList());
		return String.join(", ", nameList.toArray(new String[]{}));
	}
	
	public boolean hasActiveYabi(){
		return yabis.values().stream().anyMatch(new Predicate<Yabi>(){public boolean test(Yabi yabi){return yabi.isAlive();}});
	}
	
}

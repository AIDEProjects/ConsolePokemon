package consolepokemon.core.trainers;
import java.util.*;
import consolepokemon.core.yabis.*;

public interface ITrainer
{
	public enum Status{
		Idle("闲置中"), 
		Duel("对决中"), 
		Defeat("已战败");
		
		public final String name;
		public Status(String name){this.name = name;}
	}
	public enum Card{
		WildYabi("野生亚比"), 
		Human("人类");
		
		public final String name;
		public Card(String name){this.name = name;}
	}
	
	public static int uuidSeed;

	public Status getStatus();
	public void setStatus(Status statu);
	
	public Card getCard();
	public int getUuid();
	
	public Yabi getYabi(int index);
	
	public List<Yabi> getYabis();
	
	public void addYabi(Yabi yabi);
}

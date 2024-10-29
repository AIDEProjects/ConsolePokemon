package consolepokemon.core.trainers;
import consolepokemon.core.yabis.*;

public class WildYabi extends Trainer
{
	public Card getCard(){ return ITrainer.Card.WildYabi; }
	
	public static WildYabi newWildYabi(Class<? extends Yabi> clazz){
		var wildYabi = new WildYabi();
		try{
		wildYabi.addYabi(clazz.newInstance());
		}catch(Exception e){}
		return wildYabi;
	}
}

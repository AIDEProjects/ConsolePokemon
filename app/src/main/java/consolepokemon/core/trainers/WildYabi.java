package consolepokemon.core.trainers;
import consolepokemon.core.yabis.*;

public class WildYabi extends Trainer
{
	public Card getCard(){ return Trainer.Card.WildYabi; }
	
	public static WildYabi newWildYabi(Class<? extends Yabi> clazz){
		WildYabi wildYabi = new WildYabi();
		try{
			wildYabi.addYabi(clazz.newInstance());
		}catch(Exception e){}
		return wildYabi;
	}

	@Override
	public String displayName()
	{
		return String.format("{%d-%s:%s-%s}", getUuid(), getCard().name, getYabis().get(0).name, getStatus().name);
	}
}

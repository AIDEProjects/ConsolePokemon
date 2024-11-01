package consolepokemon.core.trainers;
import consolepokemon.core.yabis.*;
import java.util.*;
import tools.*;
import consolepokemon.core.items.*;
import consolepokemon.core.inventories.*;

public class HumanTrainer extends Trainer
{
	public int coin = 0;
	public Inventory inventory = new Inventory(this);
	public Card getCard(){ return Trainer.Card.Human; }
	
	public void gainCoin(int coin){
		Log.v("人类训练家-%d获得晶币x%d", getUuid(), coin);
		this.coin+=coin;
	}
}

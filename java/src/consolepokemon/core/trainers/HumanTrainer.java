package consolepokemon.core.trainers;
import consolepokemon.core.yabis.*;
import java.util.*;
import tools.*;
import consolepokemon.core.items.*;
import consolepokemon.core.inventories.*;
import com.google.gson.*;
import java.lang.reflect.Type;

public class HumanTrainer extends Trainer
{
	public int coin = 0;
	public Inventory inventory;
	public Card getCard(){ return Trainer.Card.Human; }
	
	public HumanTrainer(){
		inventory = new Inventory(this);
	}
	
	public void gainCoin(int coin){
		Log.v("人类训练家-%d获得晶币x%d", getUuid(), coin);
		this.coin+=coin;
	}
}

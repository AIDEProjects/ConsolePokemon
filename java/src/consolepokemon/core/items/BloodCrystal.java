package consolepokemon.core.items;
import consolepokemon.core.trainers.*;
import consolepokemon.core.yabis.*;
import tools.*;

//血色晶体
public class BloodCrystal extends Item
{
	@Override
	public String getName(){ return "血色晶体"; }

	@Override
	public void use(Object t){
		HumanTrainer t2 = (HumanTrainer)t;
		Item.useItem_heal(this, t2, 1, true);
	}
}

package consolepokemon.core.items;
import consolepokemon.core.yabis.*;
import consolepokemon.core.trainers.*;
import tools.*;

//营养剂
public class NutrientBooster extends Item
{
	@Override
	public String getName(){ return "营养剂"; }

	@Override
	public void use(Object t){
		HumanTrainer t2 = (HumanTrainer)t;
		Item.useItem_heal(this, t2, 0.5f, true);
	}
}

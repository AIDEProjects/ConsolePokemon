package consolepokemon.core.items;
import consolepokemon.core.yabis.*;
import consolepokemon.core.trainers.*;
import tools.*;

//营养剂
public class NutrientBooster extends Item<HumanTrainer>
{
	@Override
	public String getName(){ return "营养剂"; }

	@Override
	public void use(HumanTrainer t){
		Item.useItem_heal(this, t, 0.5f, true);
	}
}

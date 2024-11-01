package consolepokemon.core.items;
import consolepokemon.core.trainers.*;
import consolepokemon.core.yabis.*;
import tools.*;

//血色晶体
public class BloodCrystal extends Item<HumanTrainer>
{
	@Override
	public String getName(){ return "血色晶体"; }

	@Override
	public void use(HumanTrainer t){
		Item.useItem_heal(this, t, 1, true);
	}
}

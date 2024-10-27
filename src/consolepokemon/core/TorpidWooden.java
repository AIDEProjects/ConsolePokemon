package consolepokemon.core;

public class TorpidWooden extends Yabi
{
	public static String getName(){
		return "呆呆木";
	}
	public static float getMaxHealth(){
		return 30;
	}
	
	@Override
	public void initProps(){
		name = getName();
		maxHp = getMaxHealth();
		atk=7;
		dex=2;
	}
}

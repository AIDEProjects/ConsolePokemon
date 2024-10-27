package consolepokemon.core;

public class QuickRabbit extends Yabi
{
	public static String getName(){
		return "迅捷兔";
	}

	@Override
	public void initProps(){
		name = getName();
		maxHp = 15;
		atk = 3;
		dex=6;
	}
}

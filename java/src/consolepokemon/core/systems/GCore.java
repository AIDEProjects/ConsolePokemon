package consolepokemon.core.systems;
import consolepokemon.core.systems.turnsystem.*;

public class GCore
{
	public static DuelMatcher matcher = new DuelMatcher();
	public static DuelManager dueler = new DuelManager();
	
	public GCore(){
		init();
	}
	
	public void init(){
	}
	
	public void update(){
		//matcher.update();
	}
}

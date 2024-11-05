package consolepokemon.core.datas;
import consolepokemon.core.systems.DuelMatcher;
import consolepokemon.core.inventories.Shop;

public class GameData implements Data
{
	public boolean finishNoob;
	public boolean finishNoob_shop;
	public boolean finishNoob_loots_noobCoin;
	
	public DuelMatcher duelMatcher = new DuelMatcher();
	
	public Shop shop;
}

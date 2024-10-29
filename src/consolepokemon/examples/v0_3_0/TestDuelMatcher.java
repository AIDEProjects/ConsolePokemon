package consolepokemon.examples.v0_3_0;
import consolepokemon.core.systems.*;
import consolepokemon.core.trainers.*;
import consolepokemon.core.yabis.*;
import tools.*;

public class TestDuelMatcher
{
	public TestDuelMatcher(){
		var matcher = new DuelMatcher();
		
		var wildYabi = WildYabi.newWildYabi(TorpidWooden.class);
		var wildYabi2 = WildYabi.newWildYabi(QuickRabbit.class);
		var player = new HumanTrainer();
		matcher.bindTrainer(player);
		
		matcher.addTrainer(wildYabi);
		matcher.addTrainer(wildYabi2);
		matcher.addTrainer(player);
		
		Log.v("玩家：");
		Log.trainer(player);
		matcher.showOtherTrainers();
		//matcher.enterDuel(wildYabi2);
	}
}

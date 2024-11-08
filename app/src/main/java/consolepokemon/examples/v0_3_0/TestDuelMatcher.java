package consolepokemon.examples.v0_3_0;
import consolepokemon.core.systems.*;
import consolepokemon.core.trainers.*;
import consolepokemon.core.yabis.*;
import tools.*;

public class TestDuelMatcher
{
	public TestDuelMatcher(){
		DuelMatcher matcher = new DuelMatcher();
		
		WildYabi wildYabi = WildYabi.newWildYabi(TorpidWooden.class);
		WildYabi wildYabi2 = WildYabi.newWildYabi(QuickRabbit.class);
		HumanTrainer player = new HumanTrainer();
		matcher.bindTrainer(player);
		
		matcher.addTrainer(wildYabi);
		matcher.addTrainer(wildYabi2);
		matcher.addTrainer(player);
		
		Log.v("玩家：");
		Log.trainer(player);
		matcher.showOtherTrainers();
		Log.v("\n输入uuid匹配对局：");
		long targetUuid = Log.input(0L);
		Trainer target = matcher.getTrainer(targetUuid);
		Log.v("发起对局(仅限双方首只亚比)：%s vs %s.", player.displayRepos(), target.displayRepos());
		matcher.attemptEnterDuel(target);
	}
}

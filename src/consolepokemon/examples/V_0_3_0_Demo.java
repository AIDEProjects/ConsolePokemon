package consolepokemon.examples;

import consolepokemon.core.systems.*;
import consolepokemon.core.trainers.*;
import consolepokemon.core.yabis.*;
import tools.*;
import java.io.*;

public class V_0_3_0_Demo
{
	public V_0_3_0_Demo(){
		gameLoop();
	}
	public void gameLoop(){
		var game = new GCore();
		game.matcher = new DuelMatcher();
		game.dueler = new DuelManager();
		
		var matcher = game.matcher;

		var wildYabi = WildYabi.newWildYabi(TorpidWooden.class);
		var wildYabi2 = WildYabi.newWildYabi(QuickRabbit.class);
		var player = new HumanTrainer();
		matcher.bindTrainer(player);

		matcher.addTrainer(wildYabi);
		matcher.addTrainer(wildYabi2);
		matcher.addTrainer(player);

		Log.v("玩家：");
		Log.v(player.displayRepos());
		matcher.showOtherTrainers();
		Log.v("\n输入uuid匹配对局：");
		var targetUuid = Log.input(0);
		var target = matcher.getTrainer(targetUuid);
		Log.v(String.format("发起对局：%s vs %s.", player.displayRepos(), target.displayRepos()));
		matcher.attemptEnterDuel(target);

		Log.v("\n赠送你一只亚比？(0)呆呆木 (1)迅捷兔");
		var id = Log.input(0);
		var yabi = id==0?new TorpidWooden():(id==1?new QuickRabbit():null);
		player.addYabi(yabi);
		Log.v(String.format("你获得了新亚比: {uuid:%d-%s}", yabi.getUuid(), yabi.getName()));

		Log.v("\n赠送你一只亚比？(0)呆呆木 (1)迅捷兔");
		id = Log.input(0);
		yabi = id==0?new TorpidWooden():(id==1?new QuickRabbit():null);
		player.addYabi(yabi);
		Log.v(String.format("你获得了新亚比: {uuid:%d-%s}", yabi.getUuid(), yabi.getName()));

		while(!player.getStatus().equals(Trainer.Status.Defeat)){
			Log.v("\n你当前的阵容: ");
			Log.v(player.displayRepos());
			matcher.showOtherTrainers();

			Log.v("\n输入uuid匹配对局：");
			targetUuid = Log.input(0);
			target = matcher.getTrainer(targetUuid);
			Log.v(String.format("发起对局：%s vs %s.", player.displayName(), target.displayName()));
			matcher.attemptEnterDuel(target);
		}
		Log.v("你已战败，游戏结束.");
		Log.v("是否重来？(y/n): ");
		if(Log.input("").equals("y")){
			Log.clearLine(100);
			gameLoop();
		}else{
			Log.v("游戏结束, 回车退出.");
			Log.input("");
			System.exit(0);
		}
	}
}

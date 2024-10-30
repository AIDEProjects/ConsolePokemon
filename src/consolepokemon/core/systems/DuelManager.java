package consolepokemon.core.systems;
import consolepokemon.core.trainers.*;
import java.util.function.*;
import consolepokemon.core.systems.turnsystem.*;
import consolepokemon.core.yabis.*;
import consolepokemon.examples.*;
import tools.*;

public class DuelManager
{
	public void startDuel(final Trainer trainer, final Trainer trainer2, final Consumer<Trainer> callback)
	{
		//new Thread(new Runnable(){public void run(){

		var tds = new TrainerDuelSystem();
		tds.enterDuel(trainer, trainer2);

		trainer2.getYabis().get(0).ATK = 100;
		
		Log.v(String.format("双方首战亚比：%s vs %s", trainer.current, trainer2.current));
		Log.yabi(trainer.current);
		Log.yabi(trainer2.current);
		/*Log.v("你: ");
		Log.v(trainer.displayRepos());

		Log.v("敌方: ");
		Log.v(trainer2.displayRepos());*/
		Log.v();

		tds.turnStep();
		
		while (!tds.isCompleted())
		{
			Log.v("选择" + tds.current().current + "的操作: (0)attack (1)flee (2)cgYabi");
			tds.turnAction(tds.current(), Log.input(0));
			tds.turnStep();
		}
		
		/*
		Log.v("\n你: ");
		Log.trainer(trainer);

		trainer2.getYabis().get(0).ATK = 100;
		Log.v("敌方: ");
		Log.trainer(trainer2);
		Log.v();*/
		//}}).start();
	}
}

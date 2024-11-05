package consolepokemon.core.systems;
import consolepokemon.core.trainers.*;
import java.util.function.*;
import consolepokemon.core.systems.turnsystem.*;
import consolepokemon.core.yabis.*;
import consolepokemon.examples.*;
import tools.*;
import consolepokemon.core.utils.*;

public class DuelManager
{
	public void startDuel(final Trainer trainer, final Trainer trainer2, final Consumer<Trainer> callback)
	{
		//new Thread(new Runnable(){public void run(){

		TrainerDuelSystem tds = new TrainerDuelSystem();
		tds.enterDuel(trainer, trainer2);
		
		//trainer.current.ATK = 100;
		
		Log.v("双方首战亚比：%s vs %s", trainer.getCurrentYabi(), trainer2.getCurrentYabi());
		Log.yabi(trainer.getCurrentYabi());
		Log.yabi(trainer2.getCurrentYabi());
		/*Log.v("你: ");
		Log.v(trainer.displayRepos());

		Log.v("敌方: ");
		Log.v(trainer2.displayRepos());*/
		Log.v();

		tds.turnStep();
		
		while (!tds.isCompleted())
		{
			int action =-1;
			if(tds.isNonSelected()){
				Log.v();
			}
			if(tds.current() == trainer){
				Log.v("选择" + tds.current().getCurrentYabi() + "的操作: (0)attack (1)flee (2)cgYabi");
				action = Log.input(0);
			}else{
				action = Utils.ran()<0.08f ?1 :0;
				Log.v("AI-"+tds.current().getCurrentYabi().name+"选择了"+(action==1?"逃跑":"攻击"));
			}
			tds.turnAction(tds.current(), action);
			if(tds.isAllSelected()){
				Log.v();
			}
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

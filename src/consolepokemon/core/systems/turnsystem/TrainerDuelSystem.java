package consolepokemon.core.systems.turnsystem;
import consolepokemon.core.trainers.*;
import java.util.*;
import tools.*;
import consolepokemon.core.yabis.*;
import java.util.function.*;

public class TrainerDuelSystem extends TurnSystem<Trainer>
{

	@Override
	public void enterDuel(Trainer trainer, Trainer trainer2)
	{
		super.enterDuel(trainer, trainer2);
		trainer.setStatus(Trainer.Status.Duel);
		trainer2.setStatus(Trainer.Status.Duel);
	}
	
	@Override
	public void turnCompleted(Trainer winner, Trainer loser)
	{
		winner.setStatus(Trainer.Status.Idle);
		loser.setStatus(Trainer.Status.Defeat);
		Log.v("战斗结束：胜者-" + winner.toString() + ", 败者-" + loser.toString());
		//Log.v("----------\n");
	}

	@Override
	public boolean isDefeat(Trainer dueler)
	{
		return !dueler.hasActiveYabi();
	}

	@Override
	public boolean isFirst(Trainer first, Trainer second)
	{
		return first.current.SP > second.current.SP;
	}

	@Override
	public void turnAction(final Trainer trainer, int action)
	{
		super.turnAction(trainer, action);
		var trainer2 = duelers.stream().filter(new Predicate<Trainer>(){public boolean test(Trainer t){ return t != trainer; }}).findFirst().get();
		turnStep(trainer, trainer2, action, 0);
		if(!isCompleted){
			Log.v();
		}
	}
	
	@Override
	public boolean turnStep(Trainer trainer, Trainer trainer2, int action){
		return turnStep(trainer, trainer2, action, 1);
	}
	//actionType：0为立即行动，1为等待先后手判定后行动
	public boolean turnStep(Trainer trainer, Trainer trainer2, int action, int actionType)
	{
		var yabi1 = trainer.current;
		var yabi2 = trainer2.current;
		if(actionType == 1){
			if (action == 0)
			{
				yabi1.attack(yabi2);
				Log.yabiStatus(yabi2);
				if (!yabi2.isAlive())
				{
					Log.v(yabi2 + "💀");
					Yabi nextYabi = trainer2.getYabis().stream().filter(new Predicate<Yabi>(){public boolean test(Yabi yabi){return yabi.isAlive();}}).findFirst().orElse(null);
					if(nextYabi != null){
						Log.v(trainer2+"下一Yabi出战: "+nextYabi);
						cgYabi(trainer2, nextYabi);
					}else{
						Log.v(trainer2+"已无可用亚比.");
						isCompleted = true;
						turnCompleted(trainer, trainer2);
					}
				}
			}
			if (action == 1)
			{
				isCompleted = true;
				Log.v(yabi1 + "逃跑了，战斗结束");
				turnCompleted(trainer2, trainer);
			}
		}
		if(actionType == 0){
			if (action == 2)
			{
				Log.v("指定你要切换的亚比: ");
				Log.trainer(trainer);
				var cgYabi = Log.input(0);
				var old = trainer.current;
				cgYabi(trainer, trainer.getYabi(cgYabi));
				Log.v(String.format("%s切换亚比: %s -> %s", trainer.getCard().name, old, trainer.current));
			}
		}
		return isCompleted;
	}
	
	public void cgYabi(Trainer you, Yabi now){
		if(now == null){
			throw new IllegalArgumentException("没有找到想要切换到的亚比.");
		}
		you.current = now;
	}
	
}

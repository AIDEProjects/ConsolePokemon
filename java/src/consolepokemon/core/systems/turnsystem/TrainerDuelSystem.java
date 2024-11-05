package consolepokemon.core.systems.turnsystem;
import consolepokemon.core.trainers.*;
import java.util.*;
import tools.*;
import consolepokemon.core.yabis.*;
import java.util.function.*;
import consolepokemon.core.systems.*;
import consolepokemon.core.utils.*;

public class TrainerDuelSystem extends TurnSystem<Trainer>
{
	public List<List<Yabi>> engagedYabis = new ArrayList<>();
	
	@Override
	public void enterDuel(Trainer trainer, Trainer trainer2)
	{
		super.enterDuel(trainer, trainer2);
		trainer.setStatus(Trainer.Status.Duel);
		trainer2.setStatus(Trainer.Status.Duel);
		
		engagedYabis.clear();
		engagedYabis.add(new ArrayList<Yabi>(Arrays.asList(trainer.getCurrentYabi())));
		engagedYabis.add(new ArrayList<Yabi>(Arrays.asList(trainer2.getCurrentYabi())));
	}
	
	@Override
	public void turnCompleted(Trainer winner, Trainer loser)
	{
		winner.setStatus(Trainer.Status.Idle);
		boolean isLoserDie = !loser.hasActiveYabi();
		if(isLoserDie){
			loser.setStatus(Trainer.Status.Defeat);
		}
		Log.v("战斗结束：胜者-" + winner.toString() + ", 败者-" + loser.toString());
		
		//获取经验
		float gainExp = 0;
		for(Yabi y : loser.getYabis()){
			gainExp += y.isAlive() ? 0 : y.productEXP;
		}
		List<Yabi> winnerEngagedYabis = engagedYabis.get(duelers.indexOf(winner));
		float gainExpPart = gainExp / winnerEngagedYabis.size();
		for(Yabi y : winnerEngagedYabis){
			y.updateLV(gainExpPart);
		}
		
		//获取晶币
		if(winner instanceof HumanTrainer){
			HumanTrainer human = (HumanTrainer)winner;
			int gainCoin = 0;
			for(Yabi y : loser.getYabis()){
				gainCoin += y.isAlive() ? 0 : y.productCoin;
			}
			human.gainCoin(gainCoin);
		}
		
		//刷新亚比列表
		if(isLoserDie){
			GCore.matcher.spawnWildYabi();
		}
	}

	@Override
	public boolean isDefeat(Trainer dueler)
	{
		return !dueler.hasActiveYabi();
	}

	@Override
	public boolean isFirst(Trainer first, Trainer second)
	{
		return first.getCurrentYabi().SP >= second.getCurrentYabi().SP;
	}

	@Override
	public void turnAction(final Trainer trainer, int action)
	{
		super.turnAction(trainer, action);
		//Trainer trainer2 = duelers.stream().filter(new Predicate<Trainer>(){public boolean test(Trainer t){ return t != trainer; }}).findFirst().get();
		Trainer trainer2=null;
		for(Trainer t : duelers){
			if(t != trainer){
				trainer2 = t;
				break;
			}
		}
		turnStep(trainer, trainer2, action, 0);
	}
	
	@Override
	public boolean turnStep(Trainer trainer, Trainer trainer2, int action){
		return turnStep(trainer, trainer2, action, 1);
	}
	//actionType：0为立即行动，1为等待先后手判定后行动
	public boolean turnStep(Trainer trainer, Trainer trainer2, int action, int actionType)
	{
		Yabi yabi1 = trainer.getCurrentYabi();
		Yabi yabi2 = trainer2.getCurrentYabi();
		if(actionType == 1){
			if (action == 0)
			{
				yabi1.attack(yabi2);
				if (!yabi2.isAlive())
				{
					Log.v(yabi2 + "💀");
					//Yabi nextYabi = trainer2.getYabis().stream().filter(new Predicate<Yabi>(){public boolean test(Yabi yabi){return yabi.isAlive();}}).findFirst().orElse(null);
					Yabi nextYabi=null;
					for(Yabi y : trainer2.getYabis()){
						if(y.isAlive()){
							nextYabi = y;
							break;
						}
					}
					if(nextYabi != null){
						Log.v(trainer2+"下一Yabi出战: "+nextYabi);
						cgYabi(trainer2, nextYabi);
						
					}else{
						Log.v(trainer2+"已无可用亚比.");
						isCompleted = true;
						turnCompleted(trainer, trainer2);
					}
				}else{
					Log.v(Icons.format("%s//HP：%.1f/%.1f", yabi2, yabi2.HP, yabi2.maxHP));
				}
			}
			if (action == 1)
			{
				isCompleted = true;
				Log.v(trainer + "逃跑了，战斗结束");
				turnCompleted(trainer2, trainer);
			}
		}
		if(actionType == 0){
			if (action == 2)
			{
				Log.v("指定你要切换的亚比: ");
				Log.trainer(trainer);
				int cgYabi = Log.input(0);
				Yabi old = trainer.getCurrentYabi();
				cgYabi(trainer, trainer.getYabi(cgYabi));
				Log.v("%s切换亚比: %s -> %s", trainer.getCard().name, old, trainer.getCurrentYabi());
				//不损耗行动点
				super.turnAction(trainer, -1);
			}
		}
		return isCompleted;
	}
	
	public void cgYabi(Trainer you, Yabi now){
		if(now == null){
			throw new IllegalArgumentException("没有找到想要切换到的亚比.");
		}
		you.setCurrentYabi(now.getUuid());
		List<Yabi> youEngagedYabis = engagedYabis.get(duelers.indexOf(you));
		if(!youEngagedYabis.contains(now)){
			youEngagedYabis.add(now);
		}
	}

	@Override
	public void exchange()
	{
		super.exchange();
		engagedYabis.add(engagedYabis.remove(0));
	}
	
}

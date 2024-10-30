package consolepokemon.core.systems;
import consolepokemon.core.trainers.*;
import java.util.*;
import tools.*;
import consolepokemon.core.systems.turnsystem.*;
import java.util.function.*;

public class DuelMatcher
{
	public Trainer bindTrainer;
	
	public Map<Integer, Trainer> worldTrainers = new HashMap<>();
	
	//是否符合进入对决条件
	public boolean canEnterDuel(Trainer target){
		Trainer defeatTrainer = bindTrainer.hasActiveYabi()?(target.hasActiveYabi()?null:target):bindTrainer;
		if(defeatTrainer != null){
			Log.v(String.format("进入对决失败，原因：%s无可出战亚比.", defeatTrainer.displayRepos()));
			return false;
		}
		return true;
	}
	
	//尝试进入对决
	public void attemptEnterDuel(Trainer target)
	{
		if(canEnterDuel(target)){
			var callback = new Consumer<Trainer>(){public void accept(Trainer trainer){
				Log.v(String.format("对局结束，获胜者：{uuid:%d-%s, [%s]}", trainer.getUuid(), trainer.getCard().name, Trainer.yabiNameList(trainer)));
			}};
			GCore.dueler.startDuel(bindTrainer, target, callback);
		}
	}
	
	
	public void bindTrainer(Trainer trainer){
		this.bindTrainer = trainer;
	}
	
	public void addTrainer(Trainer trainer){
		if(!worldTrainers.containsValue(trainer)){
			worldTrainers.put(trainer.getUuid(), trainer);
		}
	}
	public Trainer getTrainer(int uuid){
		var trainer = worldTrainers.get(uuid);
		if(trainer == null){
			throw new NoSuchElementException(String.format("没有找到Uuid为%d的训练家.", uuid));
		}
		return trainer;
	}
	
	public void showOtherTrainers(){
		Log.v("其他worldTrainer列表：");
		for(Trainer t : worldTrainers.values()){
			if(!t.equals(bindTrainer)){
				Log.v(t.displayRepos());
			}
		}
	}
}

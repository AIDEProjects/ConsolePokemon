package consolepokemon.core.systems;
import consolepokemon.core.trainers.*;
import java.util.*;
import tools.*;
import consolepokemon.core.systems.turnsystem.*;
import java.util.function.*;
import consolepokemon.core.utils.*;
import consolepokemon.core.yabis.*;

public class DuelMatcher
{
	public Class[] enemyList;
	
	public Trainer bindTrainer;
	
	public Map<Long, Trainer> worldTrainers = new HashMap<>();
	
	public DuelMatcher(){
		enemyList = new Class[]{TorpidWooden.class, QuickRabbit.class};
	}
	
	//是否符合进入对决条件
	public boolean canEnterDuel(Trainer target){
		Trainer defeatTrainer = bindTrainer.hasActiveYabi()?(target.hasActiveYabi()?null:target):bindTrainer;
		if(defeatTrainer != null){
			Log.v("进入对决失败，原因：%s无可出战亚比.", defeatTrainer.displayRepos());
			return false;
		}
		return true;
	}
	
	//尝试进入对决
	public void attemptEnterDuel(Trainer target)
	{
		if(canEnterDuel(target)){
			var callback = new Consumer<Trainer>(){public void accept(Trainer trainer){
					Log.v("对局结束，获胜者：{uuid:%d-%s, [%s]}", trainer.getUuid(), trainer.getCard().name, Trainer.yabiNameList(trainer));
			}};
			GCore.dueler.startDuel(bindTrainer, target, callback);
		}
	}

	//刷新野生亚比
	public void spawnWildYabi()
	{
		//20%两只，80%一只
		var ranCount = Utils.ran()<0.20f ? 2 : 1;
		var wildYabi = new WildYabi();
		for(int i=0;i<ranCount;i++){
			var ranIndex = (int)(Utils.ran()*enemyList.length);
			var yabiType = enemyList[ranIndex];
			try{
				var yabi = (Yabi)yabiType.newInstance();
				wildYabi.addYabi(yabi);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		Log.v("新的敌对列表已刷新: ");
		Log.v(wildYabi.displayRepos());
		addTrainer(wildYabi);
	}
	
	
	public void bindTrainer(Trainer trainer){
		this.bindTrainer = trainer;
		addTrainer(trainer);
	}
	
	public void addTrainer(Trainer trainer){
		if(!worldTrainers.containsValue(trainer)){
			worldTrainers.put(trainer.getUuid(), trainer);
		}
	}
	public Trainer getTrainer(long uuid){
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

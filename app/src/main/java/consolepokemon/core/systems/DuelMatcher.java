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
	public transient Class[] enemyTypeList;
	
	private long bindTrainerUuid;
	public Trainer getBindTrainer(){ return worldTrainers.get(bindTrainerUuid); }
	public void bindTrainer(Trainer trainer){
		bindTrainerUuid = trainer.getUuid();
		addTrainer(trainer);
	}
	
	public LinkedHashMap<Long, Trainer> worldTrainers = new LinkedHashMap<>();
	
	
	public DuelMatcher(){
		enemyTypeList = new Class[]{TorpidWooden.class, QuickRabbit.class};
	}
	
	//是否符合进入对决条件
	public boolean canEnterDuel(Trainer target){
		Trainer defeatTrainer = getBindTrainer().hasActiveYabi()?(target.hasActiveYabi()?null:target):getBindTrainer();
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
			Consumer<Trainer> callback = new Consumer<Trainer>(){public void accept(Trainer trainer){
					Log.v("对局结束，获胜者：{uuid:%d-%s, [%s]}", trainer.getUuid(), trainer.getCard().name, Trainer.yabiNameList(trainer));
			}};
			GCore.dueler.startDuel(getBindTrainer(), target, callback);
		}
	}

	//刷新野生亚比
	public void spawnWildYabi()
	{
		//20%两只，80%一只
		int ranCount = Utils.ran()<0.20f ? 2 : 1;
		WildYabi wildYabi = new WildYabi();
		for(int i=0;i<ranCount;i++){
			int ranIndex = (int)(Utils.ran()*enemyTypeList.length);
			Class yabiType = enemyTypeList[ranIndex];
			try{
				Yabi yabi = (Yabi)yabiType.newInstance();
				wildYabi.addYabi(yabi);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		Log.v("新的敌对列表已刷新: ");
		Log.v(wildYabi.displayRepos());
		addTrainer(wildYabi);
	}
	
	public void addTrainer(Trainer trainer){
		if(!worldTrainers.containsValue(trainer)){
			worldTrainers.put(trainer.getUuid(), trainer);
		}
	}
	public Trainer getTrainer(long uuid){
		Trainer trainer = worldTrainers.get(uuid);
		if(trainer == null){
			throw new NoSuchElementException(String.format("没有找到Uuid为%d的训练家.", uuid));
		}
		return trainer;
	}
	
	public void showOtherTrainers(){
		Log.v("其他worldTrainer列表：");
		for(Trainer t : worldTrainers.values()){
			if(!t.equals(getBindTrainer())){
				Log.v(t.displayRepos());
			}
		}
	}
}

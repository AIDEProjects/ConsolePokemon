package consolepokemon.core.systems;
import com.goldsprite.consolepokemon.DebugWindow;
import consolepokemon.core.trainers.Trainer;
import consolepokemon.core.trainers.WildYabi;
import consolepokemon.core.utils.Utils;
import consolepokemon.core.yabis.QuickRabbit;
import consolepokemon.core.yabis.TorpidWooden;
import consolepokemon.core.yabis.Yabi;
import java.util.LinkedHashMap;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import tools.Log;

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
		Yabi defeatCurrentYabi = getBindTrainer().getCurrentYabi().isAlive()?(target.getCurrentYabi().isAlive()?null:target.getCurrentYabi()):getBindTrainer().getCurrentYabi();
		if(defeatCurrentYabi != null){
			Log.v("进入对决失败，原因：%s首战亚比已战败.", defeatCurrentYabi);
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
			Log.v(String.format("没有找到Uuid为%d的训练家.", uuid));
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

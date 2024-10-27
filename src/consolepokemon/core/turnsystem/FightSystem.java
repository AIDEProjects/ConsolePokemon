package consolepokemon.core.turnsystem;
import consolepokemon.core.yabi.Yabi;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class FightSystem
{
	private List<Yabi> yabis = new ArrayList<>();
	private List<Integer> actions = new ArrayList<>();
	private boolean isCompleted;
	public boolean isCompleted() { return isCompleted; }
	
	public FightSystem(Yabi yabi, Yabi yabi2){
		yabis.add(yabi);
		actions.add(-1);
		yabis.add(yabi2);
		actions.add(-1);
	}
	
	public void turnAction(Yabi yabi, int action){
		actions.set(yabis.indexOf(yabi), action);
		exchange();
	}
	
	//回合步进
	public void turnStep(){
		if(isAllSelected()){
			if(!isFirst(yabis.get(0), yabis.get(1))){
				exchange();
			}
			System.out.println(yabis.get(0).name+"先手");
			if(turnStep(yabis.get(0), yabis.get(1), actions.get(0))){
				return;
			}
			turnStep(yabis.get(1), yabis.get(0), actions.get(1));
			for(int i=0;i<actions.size();i++){
				actions.set(i, -1);
			}
		}
	}
	
	//执行行动
	public boolean turnStep(Yabi yabi1, Yabi yabi2, int action){
		if(action==0){
			yabi1.attack(yabi2);
			if(isDefeat(yabi2)){
				System.out.println(yabi2.name+"战败");
				turnCompleted(yabi1, yabi2);
			}
		}
		if(action==1){
			System.out.println(yabi1.name+"逃跑了，战斗结束");
			turnCompleted(yabi2, yabi1);
		}
		return isCompleted;
	}

	//回合结束，决出胜者
	public void turnCompleted(Yabi winner, Yabi loser){
		isCompleted = true;
		System.out.println("战斗结束：胜者-"+winner.name+", 败者-"+loser.name);
	}

	//是否判定为失败
	public boolean isDefeat(Yabi yabi) {
		return !yabi.isAlive();
	}
	
	//是否优先行动
	public boolean isFirst(Yabi first, Yabi second){
		return first.dex > second.dex;
	}
	
	//是否都已决策
	public boolean isAllSelected(){
		return !actions.contains(-1);
	}
	
	//获取当前yabi
	public Yabi current(){
		return yabis.get(0);
	}
	
	//交换顺序
	public void exchange(){
		yabis.add(yabis.remove(0));
		actions.add(actions.remove(0));
	}
}

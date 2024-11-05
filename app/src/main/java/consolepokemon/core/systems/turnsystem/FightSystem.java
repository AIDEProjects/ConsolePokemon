package consolepokemon.core.systems.turnsystem;
import consolepokemon.core.yabis.*;
import java.util.*;
import tools.*;
import java.util.function.*;

public class FightSystem extends TurnSystem<Yabi>
{
	private List<Yabi> yabis = super.duelers;
	
	public FightSystem(){}
	public FightSystem(Yabi yabi, Yabi yabi2){ super(yabi, yabi2); }

	//回合步进
	public void turnStep()
	{
		if (isAllSelected())
		{
			Log.v("----------");
			//决定先手
			if (!isFirst(yabis.get(0), yabis.get(1)))
			{
				exchange();
			}
			Log.v(yabis.get(0).name + "先手");

			//先后执行行动
			if (turnStep(yabis.get(0), yabis.get(1), actions.get(0)))
			{
				return;
			}
			Log.v();
			turnStep(yabis.get(1), yabis.get(0), actions.get(1));

			//下一回合
			nextTurn();
			Log.v("----------\n");
			new Scanner(System.in).nextLine();
		}
	}

	//清空行动
	public void nextTurn()
	{
		for (int i=0;i < actions.size();i++)
		{
			actions.set(i, -1);
		}
	}

	//执行行动
	//action 0 攻击
	//action 1 逃跑
	//action 2 切换亚比
	public boolean turnStep(Yabi yabi1, Yabi yabi2, int action)
	{
		if (action == 0)
		{
			yabi1.attack(yabi2);
			if (isDefeat(yabi2))
			{
				isCompleted = true;
				Log.v(yabi2.name + "战败");
				turnCompleted(yabi1, yabi2);
			}else{
				Log.yabiStatus(yabi2);
			}
		}
		if (action == 1)
		{
			isCompleted = true;
			Log.v(yabi1.name + "逃跑了，战斗结束");
			turnCompleted(yabi2, yabi1);
		}
		return isCompleted;
	}

	//回合结束，决出胜者
	public void turnCompleted(Yabi winner, Yabi loser)
	{
		Log.v("战斗结束：胜者-" + winner.name + ", 败者-" + loser.name);
		Log.v("----------\n");
	}

	//是否判定为失败
	public boolean isDefeat(Yabi yabi)
	{
		return !yabi.isAlive();
	}

	//是否优先行动
	public boolean isFirst(Yabi first, Yabi second)
	{
		return first.SP > second.SP;
	}

	//是否都已决策
	public boolean isAllSelected()
	{
		return !actions.contains(-1);
	}

	//是否都未决策
	public boolean isNonSelected()
	{
		boolean noSelect = true;
		for(int ac : actions){
			if(ac != -1){
				noSelect = false;
				break;
			}
		}
		return noSelect;
	}

	//获取当前yabi
	public Yabi current()
	{
		return yabis.get(0);
	}

	//交换顺序
	public void exchange()
	{
		yabis.add(yabis.remove(0));
		actions.add(actions.remove(0));
	}
	
	public void cgYabi(Yabi old, Yabi now){
		if(!yabis.contains(old)){
			throw new NoSuchElementException("切换亚比失败，找不到被切换的亚比.");
		}
		int index = yabis.indexOf(old);
		yabis.remove(old);
		yabis.add(index, now);
	}
}

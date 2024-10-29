package consolepokemon.core.systems.turnsystem;
import consolepokemon.core.yabis.*;
import java.util.*;
import tools.*;
import java.util.function.*;

public class FightSystem
{
	private List<Yabi> yabis = new ArrayList<>();
	private List<Integer> actions = new ArrayList<>();
	private boolean isCompleted;
	public boolean isCompleted()
	{ return isCompleted; }

	public FightSystem(Yabi yabi, Yabi yabi2)
	{
		yabis.add(yabi);
		actions.add(-1);
		yabis.add(yabi2);
		actions.add(-1);
	}

	//指定行动
	public void turnAction(Yabi yabi, int action)
	{
		actions.set(yabis.indexOf(yabi), action);
		exchange();
	}

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
	public boolean turnStep(Yabi yabi1, Yabi yabi2, int action)
	{
		if (action == 0)
		{
			yabi1.attack(yabi2);
			if (isDefeat(yabi2))
			{
				Log.v(yabi2.name + "战败");
				turnCompleted(yabi1, yabi2);
			}else{
				Log.yabiStatus(yabi2);
			}
		}
		if (action == 1)
		{
			Log.v(yabi1.name + "逃跑了，战斗结束");
			turnCompleted(yabi2, yabi1);
		}
		return isCompleted;
	}

	//回合结束，决出胜者
	public void turnCompleted(Yabi winner, Yabi loser)
	{
		isCompleted = true;
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
		return actions.stream().allMatch(
			new Predicate<Integer>(){public boolean test(Integer v){return v==-1;}});
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
}

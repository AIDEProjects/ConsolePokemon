package consolepokemon.core.systems.turnsystem;
import java.util.*;
import tools.*;
import consolepokemon.core.trainers.*;
import java.util.function.*;

public abstract class TurnSystem<T>
{
	protected List<T> duelers = new ArrayList<>();
	protected List<Integer> actions = new ArrayList<>();
	protected boolean isCompleted;
	public boolean isCompleted()
	{ return isCompleted; }

	public TurnSystem()
	{}
	public TurnSystem(T dueler, T dueler2)
	{ enterDuel(dueler, dueler2); }

	//进入对局
	public void enterDuel(T dueler, T dueler2)
	{
		isCompleted = false;
		duelers.add(dueler);
		actions.add(-1);
		duelers.add(dueler2);
		actions.add(-1);
	}

	public void turnAction(T dueler, int action)
	{
		actions.set(duelers.indexOf(dueler), action);
		exchange();
	}

	//回合步进
	public void turnStep()
	{
		if(isAllSelected() || isNonSelected()){
			//决定先手
			if (!isFirst(duelers.get(0), duelers.get(1)))
			{
				exchange();
			}
			Log.v(duelers.get(0).toString() + "先手");
		}
		if (isAllSelected())
		{
			//Log.v("----------");
			//先后执行行动
			if (turnStep(duelers.get(0), duelers.get(1), actions.get(0)))
			{
				return;
			}
			Log.v();
			turnStep(duelers.get(1), duelers.get(0), actions.get(1));

			//下一回合
			nextTurn();
			//Log.v("----------\n");
			//new Scanner(System.in).nextLine();
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
	public abstract boolean turnStep(T dueler, T dueler2, int action);

	//回合结束，决出胜者
	public abstract void turnCompleted(T winner, T loser);

	//是否判定为失败
	public abstract boolean isDefeat(T dueler);

	//是否优先行动
	public abstract boolean isFirst(T first, T second);

	//是否都已决策
	public boolean isAllSelected()
	{
		return !actions.contains(-1);
	}

	//是否都未决策
	public boolean isNonSelected()
	{
		return actions.stream().allMatch(
			new Predicate<Integer>(){public boolean test(Integer v)
				{return v == -1;}});
	}

	//获取当前dueler
	public T current()
	{
		return duelers.get(0);
	}

	//交换顺序
	public void exchange()
	{
		duelers.add(duelers.remove(0));
		actions.add(actions.remove(0));
	}
}

package consolepokemon.core.trainers;
import consolepokemon.core.yabis.*;
import java.util.*;
import consolepokemon.core.trainers.ITrainer.*;

public abstract class Trainer implements ITrainer
{
	public Status status = Status.Idle;
	public abstract Card getCard();
	public final int uuid;
	
	public List<Yabi> yabis = new ArrayList<>();
	
	public Trainer(){
		uuid = uuidSeed++;
	}
	
	public int getUuid(){ return this.uuid; }
	
	public Status getStatus(){ return this.status; }
	@Override
	public void setStatus(Status statu)
	{
		status = statu;
	}

	@Override
	public Yabi getYabi(int index)
	{
		return yabis.get(index);
	}
	
	public void addYabi(Yabi yabi)
	{
		if(!yabis.contains(yabi)){
			yabis.add(yabi);
		}
	}

	@Override
	public List<Yabi> getYabis()
	{
		return yabis;
	}
	
}

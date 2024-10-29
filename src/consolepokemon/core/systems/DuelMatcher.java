package consolepokemon.core.systems;
import consolepokemon.core.trainers.*;
import java.util.*;
import tools.*;

public class DuelMatcher
{
	public ITrainer bindTrainer;
	
	public List<ITrainer> worldTrainers = new ArrayList<>();
	
	
	public void bindTrainer(ITrainer trainer){
		this.bindTrainer = trainer;
	}
	
	public void addTrainer(ITrainer trainer){
		if(!worldTrainers.contains(trainer)){
			worldTrainers.add(trainer);
		}
	}
	
	public void showOtherTrainers(){
		Log.v("其他worldTrainer列表：");
		for(ITrainer t : worldTrainers){
			if(!t.equals(bindTrainer)){
				Log.trainer(t);
			}
		}
	}
}

package consolepokemon.examples.v0_3_0;
import consolepokemon.core.yabis.*;
import consolepokemon.core.trainers.*;
import tools.*;

/*打印野生亚比与人类训练家的阵容*/
public class TestTrainer
{
	public TestTrainer(){
		TorpidWooden yabi = new TorpidWooden();
		WildYabi trainer = new WildYabi();
		trainer.addYabi(yabi);
		Log.trainer(trainer);
		Log.trainerYabis(trainer);
		Log.v();
		QuickRabbit yabi2 = new QuickRabbit();
		TorpidWooden yabi3 = new TorpidWooden();
		HumanTrainer player = new HumanTrainer();
		player.addYabi(yabi2);
		player.addYabi(yabi3);
		Log.trainer(player);
		Log.trainerYabis(player);
	}
}

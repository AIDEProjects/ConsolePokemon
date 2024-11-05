package consolepokemon.examples.v0_3_0;

import consolepokemon.core.trainers.*;
import consolepokemon.core.yabis.*;
import tools.*;

public class TestPlayer
{
	public TestPlayer(){
		Yabi yabi = new TorpidWooden();
		HumanTrainer player = new HumanTrainer();
		player.addYabi(yabi);
		
		System.out.println("玩家亚比列表: ");
		int i=0;
		for(Yabi y : player.getYabis()){
			Log.v(i++);
			Log.yabi(y);
		}
	}
}

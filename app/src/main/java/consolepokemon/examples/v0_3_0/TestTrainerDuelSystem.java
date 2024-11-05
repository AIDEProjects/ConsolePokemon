package consolepokemon.examples.v0_3_0;
import consolepokemon.core.systems.turnsystem.*;
import consolepokemon.core.trainers.*;
import consolepokemon.core.utils.*;
import consolepokemon.core.yabis.*;
import java.util.*;
import tools.*;
import consolepokemon.examples.*;

public class TestTrainerDuelSystem
{
	public TestTrainerDuelSystem(){
		
		HumanTrainer player = new HumanTrainer();
		player.addYabi(new QuickRabbit());
		player.addYabi(new TorpidWooden());
		Log.v("你: ");
		Log.trainer(player);
		
		WildYabi enemy = WildYabi.newWildYabi(QuickRabbit.class);
		enemy.getYabis().get(0).ATK = 100;
		Log.v("敌方: ");
		Log.trainer(enemy);
		Log.v();
		
		TrainerDuelSystem tds = new TrainerDuelSystem();
		tds.enterDuel(player, enemy);
		while(!tds.isCompleted()){
			Log.v("选择"+tds.current()+"的操作: (0)attack (1)flee (2)cgYabi");
			tds.turnAction(tds.current(), Log.input(0));
			tds.turnStep();
		}
		
	}
//	public static void startDuel(TrainerDuelSystem tds, Trainer you)
//	{
//		var scan = new Scanner(System.in);
//		int i=0;
//		String str="";
//		while(!tds.isCompleted()){
//			Yabi yabi = tds.current().current;
//			int action = 0;
//			if(tds.isNonSelected()){
//				Log.v("----------");
//			}
//			if(yabi == you.current){
//				Log.v("选择"+yabi+"的操作: (0)attack (1)flee (2)cgYabi");
//				if(scan.hasNext()){
//					str = scan.nextLine();
//					action = Integer.parseInt(str);
//					Log.v("你-"+yabi+"选择了"+(action==1?"逃跑":"攻击"));
//				}
//			}else{
//				action = Math.round(Utils.ran()<0.15f?1:0);
//				Log.v("AI-"+yabi+"选择了"+(action==1?"逃跑":"攻击"));
//			}
//
//			tds.turnAction(tds.current(), action);
//			if(i++%2==0){
//				Log.v();
//			}
//			if(tds.isAllSelected()){
//				Log.v("----------");
//				Log.v("");
//				new Scanner(System.in).nextLine();
//			}
//
//			tds.turnStep();
//		}
//		Log.v("战斗结束，退出");
//	}
}

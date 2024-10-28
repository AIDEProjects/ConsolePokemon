package consolepokemon.examples;
import consolepokemon.core.yabi.TorpidWooden;
import consolepokemon.core.yabi.QuickRabbit;
import consolepokemon.core.turnsystem.FightSystem;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import consolepokemon.core.yabi.Yabi;
import java.util.Random;
import tools.Log;

public class BattleSystemDemo
{
	public BattleSystemDemo(){
		List<Yabi> yabis = new ArrayList<>();
		Yabi you = new TorpidWooden();
		yabis.add(you);
		yabis.add(new QuickRabbit());
		
		FightSystem fs = new FightSystem(yabis.get(0), yabis.get(1));
		
		Scanner scan = new Scanner(System.in);
		String str = "";
		int i=0;
		Log.v("----------");
		for(Yabi n : yabis){
			Log.v(n==you?"你":"对方");
			Log.yabi(n);
			if(i++%2==0){
				Log.v();
			}
		}
		Log.v("----------");
		Log.v();
		i=0;
		while(!fs.isCompleted()){
			Yabi yabi = fs.current();
			int action = 0;
			if(fs.isNonSelected()){
				Log.v("----------");
			}
			Log.v("选择"+yabi.name+"的操作: (0)attack (1)flee");
			if(yabi == you){
				if(scan.hasNext()){
					str = scan.nextLine();
					action = Integer.parseInt(str);
					Log.v("你-"+yabi.name+"选择了"+(action==1?"逃跑":"攻击"));
				}
			}else{
				action = Math.round(new Random().nextDouble()<0.25f?1:0);
				Log.v("AI-"+yabi.name+"选择了"+(action==1?"逃跑":"攻击"));
			}
			
			fs.turnAction(yabi, action);
			if(i++%2==0){
				Log.v();
			}
			if(fs.isAllSelected()){
				Log.v("----------");
				Log.v("");
				new Scanner(System.in).nextLine();
			}
			
			fs.turnStep();
		}
		Log.v("战斗结束，退出");
	}
}

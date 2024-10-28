package consolepokemon.examples;
import consolepokemon.core.yabi.TorpidWooden;
import consolepokemon.core.yabi.QuickRabbit;
import consolepokemon.core.turnsystem.FightSystem;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import consolepokemon.core.yabi.Yabi;
import java.util.Random;

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
		System.out.println("你是: "+you.name);
		while(!fs.isCompleted()){
			Yabi yabi = fs.current();
			int action = 0;
			System.out.println("\n选择"+yabi.name+"的操作: (0)attack (1)flee");
			if(yabi == you){
				if(scan.hasNext()){
					str = scan.nextLine();
					action = Integer.parseInt(str);
					System.out.println("你-"+yabi.name+"选择了"+(action==1?"逃跑":"攻击"));
				}
			}else{
				action = (int)Math.round(new Random().nextDouble()<0.25f?1:0);
				System.out.println("AI-"+yabi.name+"选择了"+(action==1?"逃跑":"攻击"));
			}
			fs.turnAction(yabi, action);
			if(fs.isAllSelected()){
				System.out.println("");
			}
			fs.turnStep();
		}
		System.out.println("战斗结束，退出");
	}
}

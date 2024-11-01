package consolepokemon.examples;

import consolepokemon.core.systems.*;
import consolepokemon.core.trainers.*;
import consolepokemon.core.yabis.*;
import tools.*;
import java.io.*;
import consolepokemon.core.utils.*;
import consolepokemon.core.inventories.*;
import consolepokemon.core.items.*;
import com.google.gson.*;
import consolepokemon.core.datas.*;
import java.nio.file.*;
import java.util.*;

public class V_0_3_0_Demo
{
	public String gameSavesPath = "/sdcard/consolePokemon/gameSaves.json";

	private DuelMatcher matcher;

	private HumanTrainer player;

	private int targetUuid;
	public V_0_3_0_Demo(){
		//var dataM = new DataManager();
		gson = new GsonBuilder().
			setPrettyPrinting().
			create();
		loadDatas();
		
		gameLoop();
	}
	
	public Gson gson;
	public GameData gameDatas;

	public void loadDatas(){
		try{
			File file = new File(gameSavesPath);
			if(!file.exists()){
				file.getParentFile().mkdirs();
				file.createNewFile();
				gameDatas = new GameData();
				Log.v("未检测到存档，首次创建数据在：%s.", file.getPath());
				saveDatas();
			}
			var lines = Files.readAllLines(Paths.get(file.getPath()));
			var fileContent = String.join("\n", lines);
			gameDatas = gson.fromJson(fileContent, GameData.class);
			Log.v("数据加载完成.");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void saveDatas(){
		String json = gson.toJson(gameDatas);
		
		try{
			File file = new File(gameSavesPath);
			Files.write(Paths.get(file.getPath()), Arrays.asList(json.split("\n")));
			Log.v("数据保存完成.");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void gameLoop(){
		var game = new GCore();
		game.matcher = new DuelMatcher();
		game.dueler = new DuelManager();
		
		matcher = game.matcher;
		
		player = new HumanTrainer();
		matcher.bindTrainer(player);

		var enemyList = new Class[]{TorpidWooden.class, QuickRabbit.class};
	
		var enemyCount = 3;
		for(int i=0;i<enemyCount;i++){
			var ranIndex = (int)(Utils.ran()*enemyList.length);
			var wildYabi = WildYabi.newWildYabi(enemyList[ranIndex]);
			matcher.addTrainer(wildYabi);
		}

		if(!gameDatas.levelDatas.finishNoob){
			noobTutorial();
		}
		
		Log.v("help查看指令帮助.");
		
		var shop = new Shop();
		shop.addWares(new NutrientBooster(), 10);
		shop.addWares(new BloodCrystal(), 20);
		shop.getItem(0).setCount(999999);
		shop.getItem(1).setCount(999999);
		
		while(player.hasActiveYabi()){
			var fullCmd = Log.input("");
			fullCmd = fullCmd.toLowerCase();
			var cmds = fullCmd.split(" ");
			boolean pass = true;
			//注意指令case匹配全小写
			switch(fullCmd){
				case "help": 
					Log.v(
						"update content查看更新内容(未开放)"
						+"\nq退出游戏"
						+"\nlook repos查看亚比仓库"
						+"\nlook enemys查看敌人列表"
						+"\nduel发起对决"
						+"\nshop look查看商店"
						+"\nshop buy <item-index> <item-count>购买商品"
						+"\ninv look查看背包"
						+"\ninv use <item-index>使用道具"
						+"\nyabi current <uuid>切换首战亚比"
						+"\nloots noobCoin领取新手晶币礼包"
						+"\nsave保存存档文件于同级目录下/saves.json"
					);
					break;
				case "q": 
					System.exit(0);
					break;
				case "update content": 
					//Log.v(updateContent);
					break;
				case "loots noobcoin": 
					player.gainCoin(20);
					break;
				case "save": 
					saveDatas();
					break;
				default: 
				pass = false;
				break;
			}
			if(!pass)switch(cmds[0]){
				case "look": 
					if(cmds.length>1){
						switch(cmds[1]){
							case "repos": 
								Log.v("\n你当前的阵容: ");
								Log.v(player.displayRepos());
								for(Yabi y : player.getYabis()){
									Log.yabi(y);
								}
								break;
							case "enemys": 
								matcher.showOtherTrainers();
								break;
						}
					}else{
						Log.v("指令有误");
					}
					break;
				case "duel": 
					if(cmds.length>1){
						targetUuid = Integer.parseInt(cmds[1]);
						Trainer target = matcher.getTrainer(targetUuid);
						Log.v("发起对局：%s vs %s.", player.displayName(), target.displayName());
						matcher.attemptEnterDuel(target);
					}
					break;
				case "shop": 
					if(cmds.length>1){
						switch(cmds[1]){
							case "look": 
								shop.show();
								break;
							case "buy": 
								if(cmds.length>3){
									var index = Integer.parseInt(cmds[2]);
									var count = Integer.parseInt(cmds[3]);
									shop.buy(player, index, count);
								}
								break;
						}
					}
					break;
				case "inv": 
					if(cmds.length>1){
						var inv = player.inventory;
						switch(cmds[1]){
							case "look": 
								inv.show();
								break;
							case "use": 
								if(cmds.length>2){
									var index = Integer.parseInt(cmds[2]);
									inv.useItem(index);
								}
								break;
						}
					}
					break;
				case "yabi": 
					if(cmds.length>1){
						switch(cmds[1]){
							case "current": 
								boolean done = false;
								Log.v(player.displayRepos());
								while(!done){
									Log.v("你要切换那只亚比？输入uuid: ");
									var uuid = Log.input(0);
									if(player.setCurrent(uuid)){
										done = true;
									}else{
										Log.v("输入有误：无此亚比.");
									}
								}
								break;
						}
					}
					break;
				default:
					Log.v("指令有误");
					break;
			}
			Log.v();
		}
		Log.v("你已战败，游戏结束.");
		Log.v("是否重来？(y/n): ");
		if(Log.input("").equals("y")){
			Log.clearLine(100);
			gameLoop();
		}else{
			Log.v("游戏结束, 回车退出.");
			Log.input("");
			System.exit(0);
		}
	}

	private void noobTutorial()
	{
		Log.v("玩家：");
		Log.v(player.displayRepos());
		matcher.showOtherTrainers();
		Log.v("\n输入uuid匹配对局：");
		var targetUuid = Log.input(0);
		var target = matcher.getTrainer(targetUuid);
		Log.v("发起对局：%s vs %s.", player.displayRepos(), target.displayRepos());
		matcher.attemptEnterDuel(target);

		Log.v("\n赠送你一只亚比？(0)呆呆木 (1)迅捷兔");
		var id = Log.input(0);
		var yabi = id==0?new TorpidWooden():(id==1?new QuickRabbit():null);
		player.addYabi(yabi);
		Log.v("你获得了新亚比: {uuid:%d-%s}", yabi.getUuid(), yabi.getName());

		Log.v("\n赠送你一只亚比？(0)呆呆木 (1)迅捷兔");
		id = Log.input(0);
		yabi = id==0?new TorpidWooden():(id==1?new QuickRabbit():null);
		player.addYabi(yabi);
		Log.v("你获得了新亚比: {uuid:%d-%s}", yabi.getUuid(), yabi.getName());

		Log.v("新手教程结束.");
		gameDatas.levelDatas.finishNoob = true;
	}
	
}

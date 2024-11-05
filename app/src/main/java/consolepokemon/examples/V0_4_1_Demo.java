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
import java.util.function.*;

public class V0_4_1_Demo
{
	public String gameSavesPath = "/sdcard/consolePokemon/gameSaves.json";

	Class[] enemyList;
	
	private DuelMatcher matcher;

	private HumanTrainer player;

	private int targetUuid;

	public GlobalData globalDatas;

	private Shop shop;
	
	
	public V0_4_1_Demo(){
		loadDatas();
		
		globalDatas = new GlobalData();
		
		initGame();
		
		if(!globalDatas.gameDatas.finishNoob){
			noobTutorial();
		}
		
		gameLoop();
	}
	
	public void initGame(){
		GCore game = new GCore();
		game.matcher = globalDatas.gameDatas.duelMatcher;
		enemyList = game.matcher.enemyTypeList;
		game.dueler = new DuelManager();
		matcher = game.matcher;
		
		if(!globalDatas.gameDatas.finishNoob){
			player = new HumanTrainer();
			matcher.bindTrainer(player);
		}else{
			player = (HumanTrainer)matcher.getBindTrainer();
		}

		if(!globalDatas.gameDatas.finishNoob_shop){
			shop = globalDatas.gameDatas.shop = new Shop();
			shop.addWares(new NutrientBooster(), 10);
			shop.addWares(new BloodCrystal(), 20);
			shop.getItem(0).setCount(999999);
			shop.getItem(1).setCount(999999);
		}else{
			shop = globalDatas.gameDatas.shop;
		}
	}
	
	private void noobTutorial()
	{
		//初始化敌方列表
		int enemyCount = 3;
		for(int i=0;i<enemyCount;i++){
			int ranIndex = (int)(Utils.ran()*enemyList.length);
			WildYabi wildYabi = WildYabi.newWildYabi(enemyList[ranIndex]);
			matcher.addTrainer(wildYabi);
		}

		Log.v("玩家：");
		Log.v(player.displayRepos());
		matcher.showOtherTrainers();
		Log.v("\n输入uuid匹配对局：");
		long targetUuid = Log.input(0L);
		Trainer target = matcher.getTrainer(targetUuid);
		Log.v("发起对局：%s vs %s.", player.displayRepos(), target.displayRepos());
		matcher.attemptEnterDuel(target);

		Log.v("\n赠送你一只亚比？(0)呆呆木 (1)迅捷兔");
		int id = Log.input(0);
		Yabi yabi = id==0?new TorpidWooden():(id==1?new QuickRabbit():null);
		player.addYabi(yabi);
		Log.v("你获得了新亚比: {uuid:%d-%s}", yabi.getUuid(), yabi.getName());

		Log.v("\n赠送你一只亚比？(0)呆呆木 (1)迅捷兔");
		id = Log.input(0);
		yabi = id==0?new TorpidWooden():(id==1?new QuickRabbit():null);
		player.addYabi(yabi);
		Log.v("你获得了新亚比: {uuid:%d-%s}", yabi.getUuid(), yabi.getName());

		Log.v("新手教程结束.");
		globalDatas.gameDatas.finishNoob = true;
	}

	public void gameLoop(){

		Log.v("help查看指令帮助.");

		while(player.hasActiveYabi()){
			String fullCmd = Log.input("");
			fullCmd = fullCmd.toLowerCase();
			String[] cmds = fullCmd.split(" ");
			boolean pass = true;
			//注意指令case匹配全小写
			switch(fullCmd){
				case "help": 
					Log.v(
						"update content查看更新内容(未开放)"
						+"\nq退出游戏"
						+"\nlook repos查看亚比仓库"
						+"\nlook enemys查看敌人列表"
						+"\nduel <yabi-uuid>发起对决"
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
					if(!globalDatas.gameDatas.finishNoob_loots_noobCoin){
						player.gainCoin(20);
						globalDatas.gameDatas.finishNoob_loots_noobCoin = true;
					}else{
						Log.v("你已经领取过新手晶币礼包了🙄.");
					}
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
										int index = Integer.parseInt(cmds[2]);
										int count = Integer.parseInt(cmds[3]);
										shop.buy(player, index, count);
									}
									break;
							}
						}
						break;
					case "inv": 
						if(cmds.length>1){
							Inventory inv = player.inventory;
							switch(cmds[1]){
								case "look": 
									inv.show();
									break;
								case "use": 
									if(cmds.length>2){
										int index = Integer.parseInt(cmds[2]);
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
										long uuid = Log.input(0L);
										if(player.setCurrentYabi(uuid)){
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
	
	
	public void loadDatas(){
		try{
			File file = new File(gameSavesPath);
			if(!file.exists()){
				file.getParentFile().mkdirs();
				file.createNewFile();
				globalDatas = new GlobalData();
				Log.v("未检测到存档，首次创建数据...");
				saveDatas();
			}
			List<String> lines = Files.readAllLines(Paths.get(file.getPath()));
			String fileContent = String.join("\n", lines);
			globalDatas = MyGson.fromJson(fileContent, GlobalData.class);
			Log.v("数据加载完成: %s", file.getPath());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void saveDatas(){
		try{
			String json = MyGson.toJson(globalDatas);
			File file = new File(gameSavesPath);
			Files.write(Paths.get(file.getPath()), Arrays.asList(json.split("\n")));
			Log.v("数据保存完成.");
		}catch(Exception e){
			e.printStackTrace();
		}
	}


}

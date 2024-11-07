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
import com.goldsprite.consolepokemon.DebugWindow;
import consolepokemon.core.commands.Command;
import consolepokemon.core.commands.CommandHandler;
import consolepokemon.core.commands.CommandHandler.CMD;
import com.goldsprite.consolepokemon.*;

public class V0_4_1_Demo{
	public String gameSavesPath = "/sdcard/consolePokemon/gameSaves.json";

	Class[] enemyList;

	private DuelMatcher matcher;

	private HumanTrainer player;

	private int targetUuid;

	public GlobalData globalDatas;

	private Shop shop;


	public V0_4_1_Demo(){
		loadDatas();

		initGame();

		if (!globalDatas.gameDatas.finishNoob){
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

		if (!globalDatas.gameDatas.finishNoob){
			player = new HumanTrainer();
			matcher.bindTrainer(player);
		}
		else{
			player = (HumanTrainer)matcher.getBindTrainer();
		}

		if (!globalDatas.gameDatas.finishNoob){
			shop = globalDatas.gameDatas.shop = new Shop();
			shop.addWares(new NutrientBooster(), 10);
			shop.addWares(new BloodCrystal(), 20);
			shop.getItem(0).setCount(999999);
			shop.getItem(1).setCount(999999);
		}
		else{
			shop = globalDatas.gameDatas.shop;
		}
	}

	private void noobTutorial(){
		//初始化敌方列表
		int enemyCount = 3;
		for (int i=0;i < enemyCount;i++){
			int ranIndex = (int)(Utils.ran() * enemyList.length);
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
		Yabi yabi = id == 0 ?new TorpidWooden(): (id == 1 ?new QuickRabbit(): null);
		player.addYabi(yabi);
		Log.v("你获得了新亚比: {uuid:%d-%s}", yabi.getUuid(), yabi.getName());

		Log.v("\n赠送你一只亚比？(0)呆呆木 (1)迅捷兔");
		id = Log.input(0);
		yabi = id == 0 ?new TorpidWooden(): (id == 1 ?new QuickRabbit(): null);
		player.addYabi(yabi);
		Log.v("你获得了新亚比: {uuid:%d-%s}", yabi.getUuid(), yabi.getName());

		Log.v("新手教程结束.");
		globalDatas.gameDatas.finishNoob = true;
	}

	public void gameLoop(){

		Log.v("help查看指令帮助.");

		while (player.hasActiveYabi()){
			String fullCmd = Log.input("");
			CMD[] outVal = new CMD[1];
			String finalCmd = CommandHandler.getCmdHint(fullCmd, outVal);
			boolean right = CommandHandler.isAllRight(finalCmd);
			if(!right){
				if(outVal[0]!=null){
					Log.v("usage: \n%s", outVal[0].getDesription());
				}else{
					Log.v("没有这样的指令.");
				}
				continue;
			}
			
			
			String[] cmds = finalCmd.split(" ");

			CommandHandler cmdHandler = new CommandHandler(finalCmd);
			if (cmdHandler.isRight(CMD.help)){
				String str = "";
				for (int i=0;i < CMD.values().length;i++){
					CMD ci = CMD.values()[i];
					str += ci.getDesription() + (i == CMD.values().length - 1 ?"": "\n");
				}
				Log.v(str);
			}
			else if (cmdHandler.isRight(CMD.duel)){
				targetUuid = Integer.parseInt(cmds[1]);
				Trainer target = matcher.getTrainer(targetUuid);
				if (target != null){
					Log.v("发起对局：%s vs %s.", player.displayName(), target.displayName());
					matcher.attemptEnterDuel(target);
				}
			}
			else if (cmdHandler.isRight(CMD.exit)){
				System.exit(0);
			}
			else if (cmdHandler.isRight(CMD.update_content)){
				//Log.v(updateContent);
			}
			else if (cmdHandler.isRight(CMD.loots_noobCoin)){
				MainActivity.toast("指令" + CMD.loots_noobCoin.getName() + "正确");
				if (!globalDatas.gameDatas.finishNoob_loots_noobCoin){
					player.gainCoin(20);
					globalDatas.gameDatas.finishNoob_loots_noobCoin = true;
				}
				else{
					Log.v("你已经领取过新手晶币礼包了🙄.");
				}
			}
			else if (cmdHandler.isRight(CMD.save)){
				saveDatas();
			}
			else if (cmdHandler.isRight(CMD.look_repos)){
				Log.v("\n你当前的阵容: ");
				Log.v(player.displayRepos());
				for (Yabi y : player.getYabis()){
					Log.yabi(y);
				}
			}
			else if (cmdHandler.isRight(CMD.look_enemys)){
				matcher.showOtherTrainers();
			}
			else if (cmdHandler.isRight(CMD.look_shop)){
				shop.show();
			}
			else if (cmdHandler.isRight(CMD.look_inv)){
				Inventory inv = player.inventory;
				inv.show();
			}
			else if (cmdHandler.isRight(CMD.shop_buy)){
				int index = Integer.parseInt(cmds[2]);
				int count = Integer.parseInt(cmds[3]);
				shop.buy(player, index, count);
			}
			else if (cmdHandler.isRight(CMD.inv_use)){
				Inventory inv = player.inventory;
				int index = Integer.parseInt(cmds[2]);
				inv.useItem(index);
			}
			else if (cmdHandler.isRight(CMD.yabi_current)){
				boolean done = false;
				Log.v(player.displayRepos());
				while (!done){
					Log.v("你要切换那只亚比？输入uuid: ");
					long uuid = Log.input(0L);
					if (player.setCurrentYabi(uuid)){
						done = true;
					}
				}
			}
		}
		Log.v("你已战败，游戏结束.");
		Log.v("是否重来？(y/n): ");
		if (Log.input("").equals("y")){
			Log.clearLine(100);
			gameLoop();
		}
		else{
			Log.v("游戏结束, 回车退出.");
			Log.input("");
			System.exit(0);
		}
	}


	public void loadDatas(){
		try{
			File file = new File(gameSavesPath);
			if (!file.exists()){
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
		}catch (Exception e){
			DebugWindow.addErrLog(e);
		}
	}
	public void saveDatas(){
		try{
			String json = MyGson.toJson(globalDatas);
			File file = new File(gameSavesPath);
			Files.write(Paths.get(file.getPath()), Arrays.asList(json.split("\n")));
			Log.v("数据保存完成.");
		}catch (Exception e){
			DebugWindow.addErrLog(e);
		}
	}


}

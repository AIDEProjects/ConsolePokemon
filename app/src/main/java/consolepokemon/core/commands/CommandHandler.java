package consolepokemon.core.commands;

import java.util.*;
public class CommandHandler {
	
	public enum CMD{
		help("help", "help查看指令帮助"),
		duel("duel %l", "duel <yabi-uuid>发起对决"),
		exit("q", "q退出游戏"),
		update_content("update content", "update content查看更新内容(未开放)"),
		loots_noobCoin("loots noobCoin", "loots noobCoin领取新手晶币礼包"),
		save("save", "save保存存档文件于同级目录下/saves.json"),
		look_repos("look repos", "look repos查看亚比仓库"),
		look_enemys("look enemys", "look enemys查看敌人列表"),
		look_shop("look shop", "look shop查看商店"),
		look_inv("look inv", "look inv查看背包"),
		shop_buy("shop buy %d %d", "shop buy <item-index> <item-count>购买商品"), 
		inv_use("inv use %d", "inv use <item-index>使用道具"), 
		yabi_current("yabi current", "yabi current <uuid>切换首战亚比"), 
		;

		private String name="";
		private String description="暂无";
		public String getName(){ return name; }
		public String getDesription(){ return description; }
		private CMD(String name){ this.name=name; }
		private CMD(String name, String description){ 
			this.name=name;
			this.description = description;
		}
	}

    private String finalCmd;

	public CommandHandler(String finalCmd){
		this.finalCmd = finalCmd;
	}

	public static boolean isAllRight(String fullCmd){
		CMD[] cmds = CMD.values();
		for (CMD ci : cmds) {
            if (isRight(fullCmd, ci)) {
                return true;
            }
        }
		return false;
	}

	public static boolean isRight(String finalCmd, CMD con){
		finalCmd = finalCmd.toLowerCase();
		String [] cmds = finalCmd.split(" ");
		String filter = con.getName().toLowerCase();
		String[] filters = filter.split(" ");

		if (cmds.length < filters.length){
			return false;
		}
		for (int i=0;i < filters.length;i++){
			String fi = filters[i];
			String ci = cmds[i];
			boolean equal = ci.equals(fi) && !ci.startsWith("%");
			boolean isFormat = isFormat(fi, ci);
			if (!equal && !isFormat){
				return false;
			}
		}
		return true;
	}
	public boolean isRight(CMD con){
		return isRight(finalCmd, con);
	}

	public static boolean isFormat(String format, String str){
		if (!format.startsWith("%")){
			return false;
		}

		String f = format.substring(1, format.length());
		switch (f){
			case "d": 
				if (isInteger(str)){
					return true;
				}
				break;
			case "l": 
				if (isLong(str)){
					return true;
				}
				break;
		}

		return false;
	}


	public static String getCmdHint(String cmdHead) {
		return getCmdHint(cmdHead, null);
	}
	public static String getCmdHint(String cmdHead, CMD[] outVal) {
		if (cmdHead.isEmpty()){
			return cmdHead;
		}
		cmdHead = cmdHead.toLowerCase();
		List<String> ret = new ArrayList<>();
		String[] cmdhs = cmdHead.split(" ");
		for (CMD cc : CMD.values()){
			String[] cmds = cc.getName().split(" ");

			//test
			if (cmds.length == 1 && cmds[0].equals("save")){
				int k;
			}
			if (cmdhs.length > cmds.length){
				continue;
			}
			boolean m=true;
			for (int i=0;i < cmds.length;i++){
				String cmdi = cmds[i];
				if (cmdhs.length <= i){
					m = true;
					continue;
				}
				String cmdhi = cmdhs[i];
				boolean isFormat = isFormat(cmdi, cmdhi);
				if (!cmdi.startsWith(cmdhi)){
					if (!isFormat){
						m = false;
						break;
					}else{
						cmds[i] = cmdhi;
					}
				}
			}
			if (m){
				if (ret.isEmpty()){
					ret.addAll(Arrays.asList(cmds));
					if(outVal != null && outVal.length>0)outVal[0] = cc;
				}
			}
		}
		return ret.size() > 0 ?String.join(" ", ret): cmdHead;
	}

	public static boolean isInteger(String str) {
		try{
			Integer.parseInt(str);
		}catch(Exception e){
			return false;
		}
		return true;
    }
	public static boolean isLong(String str) {
		try{
			Long.parseLong(str);
		}catch(Exception e){
			return false;
		}
		return true;
    }



}

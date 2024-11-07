package consolepokemon.core.commands;

public class Command {
    
	public static String getCmdHint(String cmdHead) {
		String[] cmdhs = cmdHead.split(" ");
		String fullCmd = "";
		for(int i=0;i<cmdhs.length;i++){
			String cmdh = cmdhs[i];
			
			String cmd = "help";
			if(cmd.startsWith(cmdh)){
				cmdhs[i] = cmd;
			}
			
		}
		return String.join(" ", cmdhs);
	}

}

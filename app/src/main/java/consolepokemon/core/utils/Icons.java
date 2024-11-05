package consolepokemon.core.utils;

public class Icons
{
	public enum Icon {
		LV("🎮"), 
		HP("💖"), 
		ATK("⚔️"), 
		SP("⚡️"), 
		CRIT("💥");
		
		public String name;
		private Icon(String name){
			this.name = name;
		}
	}

	public static String format(String str, Object... args){
		return format(String.format(str, args));
	}
	public static String format(String str){
		for(Icon i : Icon.values()){
			String replaced = i.name;
			String pattern = "//"+i.name();
			str = str.replace(pattern, replaced);
		}
		
		return str;
	}
}

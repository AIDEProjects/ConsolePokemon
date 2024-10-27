import java.util.*;
import consolepokemon.core.Yabi;

public class Main {
	
	public static void main(String[] args) {
		new Main().run();
    }
	
	public void run(){
		YabiDemo();
	}
	
	public void YabiDemo(){
		Yabi yabi = new Yabi();
		System.out.println(
			"亚比名字："+yabi.name
			+"\n"+"亚比最大血量："+yabi.maxHealth
			+"\n"+"亚比血量："+yabi.health
		);
	}
    
}

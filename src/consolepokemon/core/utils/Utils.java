package consolepokemon.core.utils;
import java.util.*;

public class Utils
{
	//public static Random seed = new Random();
	public static Random getSeed(){ return new Random(); }
	public static long uuidSeed;
	
	public static float ran(){
		return getSeed().nextFloat();
	}
	
	public static long newUuid(){
		return uuidSeed++;
	}
}

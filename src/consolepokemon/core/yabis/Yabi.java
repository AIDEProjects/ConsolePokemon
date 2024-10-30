package consolepokemon.core.yabis;
import tools.Log;
import java.util.*;
import consolepokemon.core.utils.*;

public abstract class Yabi
{
	public static int uuidSeed;
	private int uuid;
	public int getUuid(){ return uuid; }
	public String name="Yabi";
	public String getName() { return name; }
	@Override
	public String toString(){ return String.format("{%d:%s}", uuid, name); }
	public float HP;
	public float getHP() { return HP; }
	public void setHP(float HP) { this.HP = HP; }
	public float maxHP=20;

	public float ATK=5;//攻击
	public float SP=3;//速度
	public float CRIT=0.5f;//暴击率
	public float CRITVAL = 2f;//暴击倍率

	public Yabi(){
		init();
	}

	private void init(){
		uuid = uuidSeed++;
		initProps();
		HP = maxHP;
	}

	protected abstract void initProps();
	
	public boolean isAlive(){
		return HP > 0;
	}
	
	public void attack(Yabi victim){
		var isCritical = Utils.ran() < CRIT;
		var damage = (isCritical ? CRITVAL : 1) * ATK;
		Log.v(name+(isCritical?"💥":"⚔️")+victim.name+"-"+damage+"💖");
		victim.hurt(damage);
	}
	
	private void hurt(float damage){
		setHP(getHP() - damage);
		if(isAlive()){
			Log.v(name+"-"+damage+"💖，剩余💖："+getHP());
		}else{
			Log.v(name+"-"+damage+"💖|致命，剩余💖："+getHP());
			dead();
		}
	}
	
	private void dead(){
		Log.v(name+"💀");
	}
}

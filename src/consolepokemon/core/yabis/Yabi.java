package consolepokemon.core.yabis;
import tools.Log;
import java.util.*;
import consolepokemon.core.utils.*;

public abstract class Yabi
{
	public String name="Yabi";
	public float HP;
	public float getHP() { return HP; }
	public void setHP(float HP) { this.HP = HP; }
	public float maxHP=20;

	public float ATK=5;//攻击
	public float SP=3;//速度
	public float CRIT=0.2f;//暴击率
	public float CRITVAL = 2f;//暴击倍率

	public Yabi(){
		init();
	}

	public void init(){
		initProps();
		HP = maxHP;
	}

	public abstract void initProps();
	
	public boolean isAlive(){
		return HP > 0;
	}
	
	public void attack(Yabi victim){
		var isCritical = Utils.ran() < CRIT;
		var damage = (isCritical ? CRITVAL : 1) * ATK;
		Log.v(name+"攻击了"+victim.name+"造成"+damage+"点"+(isCritical?"暴击":"")+"伤害");
		victim.hurt(damage);
	}
	
	public void hurt(float damage){
		setHP(getHP() - damage);
		if(isAlive()){
			Log.v(name+"受到了"+damage+"点伤害，剩余血量："+getHP());
		}else{
			Log.v(name+"受到了"+damage+"点致命伤害，剩余血量："+getHP()+", "+name+"死亡");
		}
	}
}

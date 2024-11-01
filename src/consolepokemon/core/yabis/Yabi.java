package consolepokemon.core.yabis;
import tools.Log;
import java.util.*;
import consolepokemon.core.utils.*;

public abstract class Yabi
{
	private long uuid;
	public long getUuid(){ return uuid; }
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
	public float CRIT=0.2f;//暴击率
	public float CRITVAL = 2f;//暴击倍率

	public int productCoin=10;//战胜后提供晶币
	
	public float productEXP=15;//战胜后提供经验
	public int LV;//等级
	public float EXP;//经验
	public float NeedEXP=30;//升级所需经验
	
	public void updateLV(float exp){
		var laterExp = EXP+exp;
		if(laterExp < NeedEXP){
			Log.v("亚比%s经验增加+%.1f, 当前: %.1f/%.1f.", name, exp, laterExp, NeedEXP);
			EXP = laterExp;
		}else{
			var yu = laterExp%NeedEXP;
			int addLV = (int)((laterExp-yu)/NeedEXP);
			levelUp(addLV);
			EXP = yu;
		}
	}
//	public void updateLV(float exp){
//		var laterExp = EXP+exp;
//		if(laterExp < NeedEXP){
//			Log.v(String.format("亚比%s经验增加+%.1f, 当前: %.1f/%.1f.", name, exp, laterExp, NeedEXP));
//			EXP = laterExp;
//		}else{
//			exp = laterExp-=NeedEXP;
//			levelUp();
//			if(exp > 0){
//				updateLV(exp);
//			}
//		}
//	}
	public void levelUp(){
		levelUp(1);
	}
	public void levelUp(int addLV){
		var addProps = 1 * addLV;
		LV += addLV;
		Log.v("亚比%s升级了LV+%d，当前%d级.", name, addLV, LV);
		
		Log.v("全属性+%d.", addProps);
		maxHP +=addProps * 2f;
		ATK += addProps;
		SP += addProps;
		CRIT += Math.min(1f, addProps*0.05f);
		CRITVAL += addProps*0.1f;
		
		heal();
	}
	public void heal(float healVal){
		var laterHp = getHP()+healVal;
		laterHp = Math.min(maxHP, laterHp);
		if(laterHp == maxHP){
			Log.v("亚比%s已被治愈💖.", name);
			setHP(maxHP);
		}else{
			Log.v("亚比%s已治疗💖+%.1f.", name, healVal);
			setHP(healVal);
		}
	}
	public void heal(){
		heal(maxHP);
	}

	public Yabi(){
		init();
	}

	private void init(){
		uuid = Utils.newUuid();
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
		Log.v(Icons.format("%s %s %s, //HP-%.1f", this, (isCritical?"//CRIT":"//ATK"), victim, damage));
		victim.hurt(damage);
	}
	
	private void hurt(float damage){
		setHP(getHP() - damage);
		if(isAlive()){
			//Log.v(name+"-"+damage+"💖，剩余💖："+getHP());
		}else{
			//Log.v(name+"-"+damage+"💖|致命，剩余💖："+getHP());
			dead();
		}
	}
	
	private void dead(){
		setHP(0);
		//Log.v(name+"💀");
	}
}

package consolepokemon.core.yabi;
import tools.Log;

public abstract class Yabi
{
	public String name="Yabi";
	public float hp;
	public float getHp() { return hp; }
	public void setHp(float hp) { this.hp = hp; }
	public float maxHp=20;

	public float atk=5;
	public float sp=3;

	public Yabi(){
		init();
	}

	public void init(){
		initProps();
		hp = maxHp;
	}

	public abstract void initProps();
	
	public boolean isAlive(){
		return hp > 0;
	}
	
	public void attack(Yabi victim){
		Log.v(name+"攻击了"+victim.name+"造成"+atk+"点伤害");
		victim.hurt(atk);
	}
	
	public void hurt(float damage){
		setHp(getHp() - damage);
		if(isAlive()){
			Log.v(name+"受到了"+damage+"点伤害，剩余血量："+getHp());
		}else{
			Log.v(name+"受到了"+damage+"点致命伤害，剩余血量："+getHp()+", "+name+"死亡");
		}
	}
}

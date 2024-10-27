package consolepokemon.core.yabi;

public abstract class Yabi
{
	public String name="Yabi";
	public float hp;
	public float getHp() { return hp; }
	public void setHp(float hp) { this.hp = hp; }
	public float maxHp=20;

	public float atk=5;
	public float dex=3;

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
		System.out.println(name+"攻击了"+victim.name+"造成"+atk+"点伤害，"+victim.name+"剩余血量："+(victim.getHp()-atk));
		victim.setHp(victim.getHp() - atk);
	}
}

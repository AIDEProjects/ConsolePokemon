package consolepokemon.core;

public abstract class Yabi
{
	public String name="Yabi";
	public float hp;
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
}

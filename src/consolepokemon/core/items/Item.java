package consolepokemon.core.items;
import consolepokemon.core.yabis.*;
import consolepokemon.core.trainers.*;
import tools.*;

public abstract class Item<T>
{
	public abstract String getName();
	public abstract void use(T t);
	public int count = 1;
	
	public void setCount(int count){
		this.count = count;
	}

	@Override
	public Item clone()
	{
		try{
			var clazz = this.getClass();
			var cln = clazz.newInstance();
			cln.count = this.count;
			return (Item)cln;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static void useItem_heal(Item item, HumanTrainer t, float heal, boolean isRatio){
		Log.v("选择你要使用的Yabi, 请输入uuid：");
		String yabiStr = "";
		for(int i=0;i<t.getYabis().size();i++){
			var yabi = t.getYabis().get(i);
			yabiStr +=String.format("{%d-%s: 💖%.1f/%.1f}", yabi.getUuid(), yabi.getName(), yabi.getHP(), yabi.maxHP);
			yabiStr += (i!=t.getYabis().size()-1)?", ": "";
		}
		Log.v(yabiStr);
		boolean done = false;
		while(!done){
			var uuid = Log.input(0);
			var y = t.getYabi(uuid);
			if(y != null){
				y.heal(heal * (isRatio ?y.maxHP : 1));
				Log.v("物品%sx1已被使用.", item.getName());
				done = true;
				var laterCount = item.count-1;
				if(laterCount < 0){
					t.inventory.removeItem(item);
				}else{
					item.count -= 1;
				}
			}else{
				Log.v("请重新输入正确的uuid.");
			}
		}
	}
}

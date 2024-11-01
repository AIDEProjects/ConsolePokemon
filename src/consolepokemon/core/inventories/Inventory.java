package consolepokemon.core.inventories;
import java.util.*;
import consolepokemon.core.items.*;
import consolepokemon.core.trainers.*;
import tools.*;

public class Inventory
{
	public HumanTrainer owner;
	public List<Item> items = new ArrayList<>();
	
	public Inventory(){}
	public Inventory(HumanTrainer owner){
		setOwner(owner);
	}
	
	public void setOwner(HumanTrainer owner){
		this.owner = owner;
	}
	
	public void addItem(Item i){
		if(!hasItem(i)){
			items.add(i);
		}else{
			for(Item item : items){
				if(item.getClass().equals(i.getClass())){
					item.count+=i.count;
					break;
				}
			}
		}
	}
	
	public boolean hasItem(Item i){
		for(Item item : items){
			if(item.getClass().equals(i.getClass())){
				return true;
			}
		}
		return false;
	}

	public void removeItem(int index){
		if(index >= 0 && index < items.size()){
			removeItem(items.get(index));
		}
	}
	public void removeItem(Item i){
		if(items.contains(i)){
			items.remove(i);
		}
	}
	
	public Item getItem(int index){
		if(index >= 0 && index < items.size()){
			return items.get(index);
		}
		return null;
	}
	
	public void useItem(int index){
		var item = getItem(index);
		if(item !=null){
			item.use(owner);
		}
	}

	public void show(){
		Log.v("背包物品如下: ");
		String itemsStr = "";
		itemsStr += String.format("晶币x%d\n", owner.coin);
		for(int i=0;i<items.size();i++){
			var item = getItem(i);
			itemsStr +=String.format("{%d-%sx%d}", i, item.getName(), item.count);
			itemsStr += i!=items.size()-1?", ": "";
		}
		Log.v(itemsStr);
	}
}

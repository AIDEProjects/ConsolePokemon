package consolepokemon.core.inventories;
import java.util.*;
import consolepokemon.core.items.*;
import consolepokemon.core.trainers.*;
import tools.*;

public class Shop extends Inventory
{
	public List<Integer> prices = new ArrayList<>();
	
	//这里无效化商店的两种背包方法
	@Override
	public void addItem(Item i)
	{
	}
	@Override
	public void removeItem(Item i)
	{
	}
	
	public void addWares(Item i, int price){
		super.addItem(i);
		prices.add(price);
	}
	
	public boolean buy(HumanTrainer trainer, int index, int buyCount){

		if(buyCount <= 0){
			Log.v("购买错误，数量至少为1.");
			return false;
		}
		var ware = super.getItem(index);
		if(ware == null){
			Log.v("购买错误，无此商品.");
			return false;
		}
		var laterWareCount = ware.count-buyCount;
		if(laterWareCount < 0){
			Log.v("购买失败，商店没有那么多存货.");
			return false;
		}
		var price = getPrice(ware) * buyCount;
		var laterCoin = trainer.coin - price;
		if(laterCoin < 0){
			Log.v("购买失败，你的晶币不够.");
			return false;
		}
		ware.count = laterWareCount;
		trainer.coin = laterCoin;
		Item wareClone = ware.clone();
		wareClone.count = buyCount;
		trainer.inventory.addItem(wareClone);
		Log.v("成功购买商品<%s>x%d, 您花费%d晶币，剩余%d晶币", ware.getName(), buyCount, price, laterCoin);
		return true;
	}
	
	public int getPrice(Item ware){
		return ware==null?-1 : prices.get(items.indexOf(ware));
	}
	
	public void show(){
		Log.v("商店货物如下: ");
		String waresStr = "";
		for(int i=0;i<items.size();i++){
			var ware = super.getItem(i);
			var price = getPrice(ware);
			waresStr +=String.format("{%d-%sx%d: %d晶币}", i, ware.getName(), ware.count, price);
			waresStr += i!=items.size()-1?", ": "";
		}
		Log.v(waresStr);
	}
}

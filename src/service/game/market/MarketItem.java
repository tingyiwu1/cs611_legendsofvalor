package src.service.game.market;

import src.service.entities.items.Item;

public class MarketItem {
	private Item item;
	private Integer price;

	
	public MarketItem(Item item, Integer price){
		this.item = item;
		this.price = price;
	}

	public Item getItem() {
		return item;
	}
	public Integer getPrice() {
		return price;
	}
}

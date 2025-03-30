
/**
 * The MarketItem class represents an item available in the market along with its price.
 * It encapsulates the details of an item and its associated cost, providing methods
 * to retrieve the item and its price.
 */
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

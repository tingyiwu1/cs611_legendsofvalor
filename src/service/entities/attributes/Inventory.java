/**
 * The Inventory interface represents a collection of items that can be managed
 * by adding, removing, equipping, and unequipping items. It provides methods
 * to interact with the inventory and manage equipped items.
 */
package src.service.entities.attributes;

import java.util.ArrayList;

import src.service.entities.items.Item;

public interface Inventory {

	public void addItem(Item newItem);

	public void removeItem(int idx);
	// public void useItem();

	public ArrayList<Item> getItemsList();

	public int[] getEquippedItems();

	public Boolean equipItem(int isMainHand, int itemIndex);

	public void unequipItem(int equipment_slot);

	public void unequipAllItems();

	public Item getEquippedItem(int i);

}
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
	
	public Item getEquippedItem();
	
} 
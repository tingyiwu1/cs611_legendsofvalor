package src.service.entities.attributes;

import src.service.entities.items.Item;
import src.service.entities.items.Spell;
import src.service.entities.items.Weapon;
import src.util.ItemType;

/**
 * Represents an attack option in the game, which includes details such as
 * the name, description, source item, and damage value of the attack.
 */
public class AttackOption {
	private String name;
	private String description;
	private Item sourceItem;
	private Integer damage;

	public AttackOption(String name, String description, Item sourceItem, Integer damage) {
		this.name = name;
		this.description = description;
		this.sourceItem = sourceItem;
		this.damage = damage;
	}
	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return this.description;
	}

	public Item getSourceItem() {
		return this.sourceItem;
	}

	public Integer getDamage() {
		return this.damage;
	}

	public Integer getRange(){
		// if(this.sourceItem.getItemType() == ItemType.WEAPON || this.sourceItem.getItemType() == ItemType.BIG_WEAPON){
		if(this.sourceItem.getItemType() == ItemType.WEAPON){
			Weapon item = (Weapon) this.sourceItem;
			return item.getRange();
		} else if(this.sourceItem.getItemType() == ItemType.SPELL){
			Spell item = (Spell) this.sourceItem;
			return item.getRange();
		} 

		return null;
	}

	@Override
	public String toString() {
		return "Name: " + this.name + "\nDescription: " + this.description + "\nDamage: " + this.damage;
	}

}

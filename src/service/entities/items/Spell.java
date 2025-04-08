/**
 * The Spell class represents a type of consumable item in the game that can be used
 * to deal damage. It extends the Consumable class and includes attributes such as
 * damage, name, description, level requirement, and maximum uses. This class is 
 * specifically categorized as a SPELL type item.
 */
package src.service.entities.items;

import src.util.ItemType;


public class Spell extends Consumable{
	private int range;

	public Spell(int damage, String name, String description, Integer levelRequirement,
			Integer maxUses, int range) {
		super(damage, name, description, levelRequirement, ItemType.SPELL, maxUses);
		this.range = range;

	}

	public int getRange() {
		return range;
	}
	public void setRange(int range) {
		this.range = range;
	}
	
}

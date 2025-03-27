package src.service.entities.items;

import src.util.ItemType;

public class Spell extends Consumable{

	public Spell(int damage, String name, String description, Integer levelRequirement,
			Integer maxUses) {
		super(damage, name, description, levelRequirement, ItemType.SPELL, maxUses);

	}
	
}

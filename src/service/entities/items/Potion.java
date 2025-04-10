/**
 * The Potion class represents a consumable item in the game that provides buffs
 * or heals a hero when used. Potions have different types, each corresponding 
 * to a specific stat or effect they modify. This class extends the Consumable 
 * class and provides functionality for using potions and applying their effects 
 * to a hero.
 * 
 */
package src.service.entities.items;

import src.service.entities.heroes.Hero;
import src.util.ItemType;
import src.util.StatsTracker;

/*
 * 
 */

public class Potion extends Consumable {
	private int potionType;

	public Potion(int buff, String name, String description, Integer levelRequirement, ItemType itemType, int statType,
			int maxUses) {
		super(buff, name, description, levelRequirement, ItemType.POTION, maxUses);
		this.potionType = statType;
	}

	public String getPotionType() {
		switch (potionType) {
			case (0):
				return "HP";
			case (1):
				return "Strength";
			case (2):
				return "Magic Strength";
			case (3):
				return "Defense ";
			case (4):
				return "Dodge";
			default:
				throw new IllegalArgumentException("Invalid potion type: " + potionType);
		}
	}

	public Boolean use(Hero hero) {
		if (this.getRemainingUses() == 0) {
			return false;
		}
		StatsTracker.addToStats("Potions consumed", 1);
		super.use();
		hero.updateInventory();
		switch (potionType) {
			case (0):
				hero.healDamage(this.getDamage());
				break;
			case (1):
				hero.buffStrength(this.getDamage());
				break;
			case (2):
				hero.buffMagicStrength(this.getDamage());
				break;
			case (3):
				hero.buffDefense(this.getDamage());
				break;
			case (4):
				hero.buffDodge(this.getDamage());
				break;
			default:
				throw new IllegalArgumentException("Invalid potion type: " + potionType);
		}

		return true;
	}

}

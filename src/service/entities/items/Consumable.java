/**
 * The Consumable class represents an abstract item that can be consumed
 * a limited number of times. It implements the Item interface and provides
 * common functionality for consumable items such as tracking remaining uses,
 * maximum uses, and other item properties.

 */
package src.service.entities.items;

import src.util.ItemType;
import src.service.entities.items.Item;

public abstract class Consumable implements Item {
	private Integer damage;
	private String name;
	private String description;
	private Integer levelRequirement;
	private ItemType itemType;

	private Integer remainingUses;
	private Integer maxUses;

	public Consumable(int damage, String name, String description, Integer levelRequirement, ItemType itemType,
			Integer maxUses) {
		this.damage = damage;
		this.name = name;
		this.description = description;
		this.levelRequirement = levelRequirement;
		this.itemType = itemType;
		this.remainingUses = maxUses;
		this.maxUses = maxUses;
	}

	public void setRemainingUses(Integer remainingUses) {
		this.remainingUses = remainingUses;
	}

	public Integer getRemainingUses() {
		return this.remainingUses;
	}

	public void setMaxUses(Integer maxUses) {
		this.maxUses = maxUses;
	}

	public Integer getMaxUses() {
		return this.maxUses;
	}

	@Override
	public Integer getDamage() {
		return this.damage;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	@Override
	public Boolean use() {
		if (this.remainingUses == 0) {
			return false;
			// throw new UnsupportedOperationException("No remaining uses");
		}
		this.remainingUses--;
		return true;
	}

	@Override
	public void setDamage(Integer damage) {
		this.damage = damage;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public Integer getLevelRequirement() {
		return this.levelRequirement;
	}

	@Override
	public void setLevelRequirement(Integer levelRequirement) {
		this.levelRequirement = levelRequirement;
	}

	@Override
	public ItemType getItemType() {
		return this.itemType;
	}

	@Override
	public void setItemType(ItemType itemType) {
		this.itemType = itemType;
	}

}

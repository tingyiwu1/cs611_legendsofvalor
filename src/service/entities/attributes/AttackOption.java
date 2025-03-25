package src.service.entities.attributes;

import src.service.entities.items.Item;

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

	@Override
	public String toString() {
		return "Name: " + this.name + "\nDescription: " + this.description + "\nDamage: " + this.damage;
	}

}

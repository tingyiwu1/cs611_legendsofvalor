package src.service.entities.items;

import src.util.ItemType;

public class Armor implements Item, Equippable {

	private Integer bonusStrength;
	private Integer bonusMagicStrength;
	private Integer bonusDefense;
	private Integer bonusDodge;
	private String name;
	private String description;
	private Integer levelRequirement;
	private ItemType itemType;

	public Armor(String name, String description, ItemType type){
		this.name = name;
		this.description = description;
		this.bonusStrength = 0;
		this.bonusMagicStrength = 0;
		this.bonusDefense = 0;
		this.bonusDodge = 0;
		this.levelRequirement = 0;
		this.itemType = type;
	}

	public Armor(String name, String description, ItemType type, Integer bonusStrength, Integer bonusMagicStrength, Integer bonusDefense, Integer bonusDodge) {
		this.name = name;
		this.description = description;
		this.bonusStrength = bonusStrength;
		this.bonusMagicStrength = bonusMagicStrength;
		this.bonusDefense = bonusDefense;
		this.bonusDodge = bonusDodge;
		this.levelRequirement = 0;
		this.itemType = type;
	}

	@Override
	public Integer getBonusStrength() {
		return this.bonusStrength;
	}

	@Override
	public Integer getBonusMagicStrength() {
		return this.bonusMagicStrength;
	}

	@Override
	public Integer getBonusDefense() {
		return this.bonusDefense;
	}

	@Override
	public Integer getBonusDodge() {
		return this.bonusDodge;
	}

	@Override
	public Integer getDamage() {
		return 0; // Armor does not deal damage
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
		// System.out.println("Armor equipped.");
		return true;
	}

	@Override
	public void setDamage(Integer damage) {
		// Armor does not deal damage, so this method does nothing
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
		// Do nothing as the item type is fixed to ARMOR
	}

	@Override
	public Integer getRemainingUses() {
		return 1000; // Arbitrary large number for unlimited uses
	}

	@Override
	public void setRemainingUses(Integer remainingUses) {
		// Do nothing as armor does not have limited uses
	}

	@Override
	public Integer getMaxUses() {
		return 1000; // Arbitrary large number for unlimited uses
	}

	@Override
	public void setMaxUses(Integer maxUses) {
		// Do nothing as armor does not have limited uses
	}
}

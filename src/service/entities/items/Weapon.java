/**
 * The Weapon class represents a weapon item in the game. It implements the Item and Equippable
 * interfaces, providing functionality for weapons that can be equipped and used by characters.
 */
package src.service.entities.items;

import src.util.ItemType;



public class Weapon implements Item, Equippable {
	
	private Integer damage;
	private String name;
	private String description;
	private Integer levelRequirement;
	private ItemType itemType;

	private Integer bonusStrength;
	private Integer bonusMagicStrength;
	private Integer bonusDefense;
	private Integer bonusDodge;
	private Integer range;

	public Weapon(Integer damage, String name, String description){
		this.damage = damage;
		this.name = name;
		this.description = description;
		this.levelRequirement = 0;
		this.itemType = ItemType.WEAPON;

		this.bonusStrength = 0;
		this.bonusMagicStrength = 0;
		this.bonusDefense = 0;
		this.bonusDodge = 0;

		this.range = 0;

	}
	
	public Weapon(Integer damage, String name, String description, 
			int bonusStrength, int bonusMagicPower, int bonusDefense, int bonusDodge, int range){
		this.damage = damage;
		this.name = name;
		this.description = description;
		this.levelRequirement = 0;
		this.itemType = ItemType.WEAPON;

		this.bonusStrength = bonusStrength;
		this.bonusMagicStrength = bonusMagicPower;
		this.bonusDefense = bonusDefense;
		this.bonusDodge = bonusDodge;
		this.range = range;

	}

	public Integer getRange() {
		return this.range;
	}
	public void setRange(Integer range) {
		this.range = range;
	}
	
	public Integer getDamage(){
		return this.damage;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	public Boolean use(){
		// System.out.println("Weapon used.");
		return true;
	}
	
	public void setDamage(Integer damage){
		this.damage = damage;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void setDescription(String description){
		this.description = description;
	}

	public Integer getLevelRequirement() {
		return 0;
	}
	public void setLevelRequirement(Integer levelRequirement) {
		this.levelRequirement = levelRequirement;
	}

	public Boolean canUse(Integer level){
		return level >= this.levelRequirement;
	}

	public ItemType getItemType() {
		return this.itemType;
	}
	public void setItemType(ItemType itemType) {
		// do nothing
	}

	@Override
	public Integer getRemainingUses() {
		return 1000;
	}

	@Override
	public void setRemainingUses(Integer remainingUses) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'setRemainingUses'");
	}

	@Override
	public Integer getMaxUses() {
		return 1000;
	}

	@Override
	public void setMaxUses(Integer maxUses) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'setMaxUses'");
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
	
}

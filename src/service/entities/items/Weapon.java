package src.service.entities.items;

import src.util.ItemType;

public class Weapon implements Item {
	
	private Integer damage;
	private String name;
	private String description;
	private Integer levelRequirement;
	private ItemType itemType;
	
	public Weapon(Integer damage, String name, String description){
		this.damage = damage;
		this.name = name;
		this.description = description;
		this.levelRequirement = 0;
		this.itemType = ItemType.WEAPON;
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
	
	public void use(){
		System.out.println("Weapon used.");
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
	
}

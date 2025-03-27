package src.service.entities.items;

import src.util.ItemType;

/*
 * Either a damaging item or a spell item
 */
public interface Item {

	public Integer getDamage();
	public String getName();
	public String getDescription();
	public Boolean use();
	public void setDamage(Integer damage);
	public void setName(String name);
	public void setDescription(String description);

	public default void printItem(){
		System.out.println("Name: " + this.getName());
		System.out.println("Description: " + this.getDescription());
		System.out.println("Damage: " + this.getDamage());
	}

	public Integer getLevelRequirement();
	public void setLevelRequirement(Integer levelRequirement);

	public ItemType getItemType();
	public void setItemType(ItemType itemType);

	public Integer getRemainingUses();
	public void setRemainingUses(Integer remainingUses);

	public Integer getMaxUses();
	public void setMaxUses(Integer maxUses);
	
} 
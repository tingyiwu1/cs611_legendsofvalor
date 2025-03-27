package src.service.entities.heroes;

import java.util.ArrayList;

import src.service.entities.Entity;
import src.service.entities.attributes.AttackOption;
import src.service.entities.attributes.Attacks;
import src.service.entities.attributes.Inventory;
import src.service.entities.items.Armor;
import src.service.entities.items.Equippable;
import src.service.entities.items.Item;
import src.service.entities.items.Spell;
import src.service.entities.items.Weapon;
import src.util.ItemType;
import src.util.PrintingUtil;

public class Hero extends Entity implements Attacks, Inventory, Shopper {

	private ArrayList<Item> items;
	private int[] equipment; //helmet, chest, legs, boots
	private Integer gold;

	private Integer experience = 0;
	private final int breakpoint = 150;
	private int levelBoon;

	public Hero(){
		super(500, 1, "DEBUGGING HERO", 50, 50, 50, 50);

		this.items = new ArrayList<Item>();

		// add some default items
		this.items.add(new Weapon(10, "Debugging Dagger", "A simple metal dagger for debugging purposes"));		
		this.items.add(new Weapon(10, "Magical Debug Dagger", "A simple metal dagger for debugging purposes", 5, 5, 5, 5));		


		this.items.add(new Spell(2, "Debugging Dirt Ball!", "A simple debugging dirt attack", 0, 5));
		this.items.add(new Spell(20, "Debugging Fireball!", "A simple debugging fire attack", 0, 5));

		this.items.add(new Armor("Debugging Helmet!", "a basic debugging helm", ItemType.HELMET));

		this.equipment = new int[] {-1, -1, -1, -1, -1, -1}; //main hand, offhand, helmet, chest, legs, boots
		this.levelBoon = 5; //increase all stats per level
		this.gold = 1;
	}

	public static String getShortHeroDisplay(Hero hero){
		return "| " + hero.getName() + " | Level: " + hero.getLevel() + " | EXP:" + hero.getExperience() + "/ " + hero.getBreakpoint() + " | Gold: " + hero.getGold() + " |";
	}

	public static String getHeroDisplay(Hero hero){
		StringBuilder sb = new StringBuilder();

		sb.append("==========================================\n");
		sb.append(hero.getName() + "\n");
		sb.append("==========================================\n");

		// Basic Info
		sb.append("Level: " + hero.getLevel() + " | Experience: " + hero.getExperience() + " / " + hero.getBreakpoint() + "\n");
		sb.append("Gold: " + hero.getGold() + "\n");
		sb.append("Health: " + hero.getCurrentHealth() + " / " + hero.getMaxHealth()  + "\n");
		sb.append("==========================================\n");

		for(int i = 0; i < 4; i++){
			sb.append(getHeroStat(hero, i));
			sb.append("\n");
		}
		
    	sb.append("==========================================\n");


		return sb.toString();
	}

	private static String getHeroStat(Hero hero, int StatType){
		StringBuilder newStatLine = new StringBuilder();
		switch (StatType) {
			case 0: newStatLine.append(PrintingUtil.printWithPadding("Strength", 15) 
				+ hero.getStrength() + " := " + hero.getBaseStrength()); break;
			case 1: newStatLine.append(PrintingUtil.printWithPadding("Magic Strength", 15) 
				+ hero.getMagicStrength() + " := " + hero.getBaseMagicStrength()); break;
			case 2: newStatLine.append(PrintingUtil.printWithPadding("Defense", 15) 
				+ hero.getDefense() + " := " + hero.getBaseDefense()); break;
			case 3: newStatLine.append(PrintingUtil.printWithPadding("Dodge", 15) 
				+ hero.getDodge() + " := " + hero.getBaseDodge()); break;
			default: return "Invalid Stat Type";
		}
		for(int i = 0; i < 6; i++){
			newStatLine.append("+");
			if(hero.getEquippedItems()[i] != -1){
				Equippable currItem = (Equippable) hero.getEquippedItem(i);
				switch (StatType) {
					case 0: newStatLine.append(currItem.getBonusStrength()); break;
					case 1: newStatLine.append(currItem.getBonusMagicStrength()); break;
					case 2: newStatLine.append(currItem.getBonusDefense()); break;
					case 3: newStatLine.append(currItem.getBonusDodge()); break;
				}
			} else {
				newStatLine.append("0");
			}
		}

		return newStatLine.toString();
	}

	public Integer getExperience(){
		return this.experience;
	}
	public boolean willLevelUp(Integer newXp){
		return (this.experience + newXp) >= breakpoint;
	}



	public void gainExperience(Integer newXp) {
		this.experience += newXp; 
    	while (this.experience >= breakpoint) { 
			this.levelUp();
			this.experience -= breakpoint; 
			this.levelUpDefense();
			this.levelUpDodge();
			this.levelUpMagicStrength();
			this.levelUpStrength();
			// recharge spells
			for(int i = 0; i < this.items.size(); i++){
				if(this.items.get(i).getItemType() == ItemType.SPELL){
					this.items.get(i).setRemainingUses(this.items.get(i).getMaxUses());
				}
			}
    	}
	}
	public int getBreakpoint(){
		return this.breakpoint;
	}


	@Override 
	public Integer getGold(){
		return this.gold;
	}
	@Override
	public void spendGold(int amount){
		this.gold -= amount;
	}
	@Override
	public void earnGold(int amount){
		this.gold += amount;
	}
	@Override
	public boolean canAfford(int cost){
		return this.gold > cost;
	}

	


	public Integer getDodge(){
		Integer finalDodge = super.getDodge();
		for (int eq : equipment){
			if (eq != -1) {
				finalDodge += ((Equippable) items.get(eq)).getBonusDodge();
			}
		}
		return finalDodge;
	}
	public Integer getStrength(){
		Integer finalStrength = super.getStrength();
		for (int eq : equipment) {
			if (eq != -1) {
				finalStrength += ((Equippable) items.get(eq)).getBonusStrength();
			}
		}
		return finalStrength;
	}
	public Integer getMagicStrength() {
		Integer finalAgility = super.getMagicStrength();
		for (int eq : equipment) {
			if (eq != -1) {
				finalAgility += ((Equippable) items.get(eq)).getBonusMagicStrength();
			}
		}
		return finalAgility;
	}
	public Integer getDefense() {
		Integer finalDefense = super.getDefense();
		for (int eq : equipment) {
			if (eq != -1) {
				finalDefense += ((Equippable) items.get(eq)).getBonusDefense();
			}
		}
		return finalDefense;
	}

	public int getBaseStrength() {
		return super.getStrength();
	}
	public int getBaseDefense() {
		return super.getDefense();
	}
	public int getBaseMagicStrength() {
		return super.getMagicStrength();
	}
	public int getBaseDodge() {
		return super.getDodge();
	}

	@Override
	public void addItem(Item newItem) {
		this.items.add(newItem);
	}

	@Override
	public void removeItem(int index) {
		this.items.remove(index);
	}

	@Override
	public ArrayList<Item> getItemsList() {
		return this.items;
		
	}

	@Override
	public int[] getEquippedItems() {
		return this.equipment;
	}

	@Override
	public Boolean equipItem(int equipment_slot, int itemIndex) {
		// type checking for item slots!
		// main hand, offhand, helmet, chest, legs, boots
		if(itemIndex == -1){
			this.unequipItem(equipment_slot);
			return true;
		}

		Item newItem = this.items.get(itemIndex);
		if ((equipment_slot == 0 || equipment_slot == 1) &&
			!(newItem.getItemType() == ItemType.WEAPON || newItem.getItemType() == ItemType.BIG_WEAPON)) {
			return false;
		} else if(equipment_slot == 2 && newItem.getItemType() != ItemType.HELMET){
			return false;
		} else if(equipment_slot == 3 && newItem.getItemType() != ItemType.CHEST){
			return false;
		} else if(equipment_slot == 4 && newItem.getItemType() != ItemType.LEGS){
			return false;
		} else if(equipment_slot == 5 && newItem.getItemType() != ItemType.BOOTS){
			return false;
		}


		if(this.items.get(itemIndex).getItemType() == ItemType.BIG_WEAPON){
			this.equipment[0] = itemIndex;
			this.equipment[1] = -1;
		} else {
			this.equipment[equipment_slot] = itemIndex;
		}
		return true;
	}

	@Override
	public void unequipItem(int equipment_slot) {
		this.equipment[equipment_slot] = -1;
	}

	@Override
	public void unequipAllItems() {
		this.equipment = new int[]{-1, -1, -1, -1, -1, -1};
	}

	@Override
	public Item getEquippedItem(int i) {
		return this.items.get(this.equipment[i]);
	}

	@Override
	public Item[] getSpellsList() {
		ArrayList<Item> spells = new ArrayList<Item>();
		for(Item item : this.items){
			if(item.getItemType() == ItemType.SPELL){
				spells.add(item);
			}
		}
		return spells.toArray(new Item[spells.size()]);
	}

	@Override
	public AttackOption mainHandAttack() {
		// (Base Strength + Item Damage) * (1 + level / 10)
		if(this.equipment[0] == -1){
			return new AttackOption("Main Hand Attack", "You have no weapon equipped", 
				new Weapon(0, "Generic Fist", "The only weapon necessary, really"), this.getStrength());
		}

		Item equippedItem = this.items.get(this.equipment[0]);
		String description = "You execute a basic attack with your " + equippedItem.getName();
		int damage = (int) ((this.getStrength() + equippedItem.getDamage()) * (1 + this.getLevel() / 10.0));

		return new AttackOption("Main Hand Attack", description, equippedItem, damage);
	}
	@Override
	public ArrayList<AttackOption> getAttacksList() {

		ArrayList<AttackOption> attacks = new ArrayList<AttackOption>();

		attacks.add(this.mainHandAttack());
		// iterate over inventory, add any possible attacks based on spells

		for(Item item : this.getSpellsList()){
			Spell spell = (Spell) item;
			if(spell.getLevelRequirement() <= this.getLevel()){
				int damage = (this.getMagicStrength() + item.getDamage()) * (1 + this.getLevel() / 10);
				attacks.add(new AttackOption("Spell Attack", "You cast " + spell.getName(), spell, damage));
			}
		}

		return attacks;
		
	}

	public void levelUpStrength(){
		this.strength += this.levelBoon;
	}
	public void levelUpDefense(){
		this.defense += this.levelBoon;
	}
	public void levelUpMagicStrength(){
		this.magicStrength += this.levelBoon;
	}
	public void levelUpDodge(){
		this.dodge += this.levelBoon;
	}
	
} 
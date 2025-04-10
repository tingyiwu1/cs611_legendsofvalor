/**
 * The Hero class represents a playable character in the game. It extends the Entity class
 * and implements the Attacks, Inventory, and Shopper interfaces. Heroes have attributes 
 * such as health, level, strength, magic strength, defense, dodge, experience, and gold. 
 * They can equip items, gain experience, level up, and perform various actions like attacking 
 * and managing their inventory.
 * 
 * Features:
 * - Manage inventory and equipped items.
 * - Gain experience and level up, improving stats and abilities.
 * - Perform attacks using weapons, spells, or potions.
 * - Display hero stats and information in a formatted manner.
 * - Update inventory by removing depleted potions.
 * 
 * Attributes:
 * - items: A list of items in the hero's inventory.
 * - equipment: An array representing equipped items (main hand, offhand, helmet, chest, legs, boots).
 * - gold: The amount of gold the hero possesses.
 * - experience: The current experience points of the hero.
 * - breakpoint: The experience threshold required to level up.
 * - levelBoon: The stat increase per level up.
 * - rng: A Random object for generating random values during level-ups.
 * 
 * Methods:
 * - Constructors: Initialize hero attributes with default or custom values.
 * - setLevelBoon: Set the level boon value.
 * - getShortHeroDisplay: Get a short summary of the hero's stats.
 * - getHeroDisplay: Get a detailed display of the hero's stats.
 * - gainExperience: Add experience points and handle leveling up.
 * - getBreakpoint: Get the experience threshold for leveling up.
 * - Gold management: Methods to earn, spend, and check affordability of gold.
 * - Stat calculations: Methods to calculate final stats considering equipped items.
 * - Buff methods: increase hero stats.
 * - Inventory management: Add, remove, equip, unequip, and update items in the inventory.
 * - Attack methods: Perform main hand attacks and retrieve a list of available attacks.
 * - Level-up methods: Increase stats and health when leveling up.
 * - updateInventory: Remove depleted potions and update equipment indices.
 */
package src.service.entities.heroes;

import java.util.ArrayList;
import java.util.Random;

import src.service.entities.Entity;
import src.service.entities.attributes.AttackOption;
import src.service.entities.attributes.Attacks;
import src.service.entities.attributes.Inventory;
import src.service.entities.attributes.Position;
import src.service.entities.items.Equippable;
import src.service.entities.items.Item;
import src.service.entities.items.Potion;
import src.service.entities.items.Spell;
import src.service.entities.items.Weapon;
import src.service.entities.monsters.Monster;
import src.util.ItemType;
import src.util.PrintingUtil;
import src.util.StatsTracker;

public class Hero extends Entity implements Attacks, Inventory, Shopper {

	private ArrayList<Item> items;
	private int[] equipment; // helmet, chest, legs, boots
	private Integer gold;

	private Integer experience = 0;
	private final int breakpoint = 150;
	private static int levelBoon = 5;
	private static Random rng = new Random();

	public Hero() {
		super(100, 1, "DEBUGGING HERO", 40, 40, 30, 10, new Position(7, 0));

		this.items = new ArrayList<Item>();

		// add some default items
		// this.items.add(new Weapon(1000, "DEBUGGING DEATH", "", 50, 50, 50, 50));
		this.items.add(new Weapon(5, "Basic Dagger", "A simple dagger, coincidentally next to you when you woke up"));
		this.items
				.add(new Spell(30, "Basic Ice Dart", "Conjure a simple shard of Ice, and launch it at the enemy", 0, 5, 2));
		this.items.add(new Potion(50, "Potent Healing Potion", "A brewed healing potion, capable of healing basic injuries",
				0, ItemType.POTION, 0, 3));
		// this.items.add(new Potion(50, "Potent Strengthening Potion", "A brewed
		// healing potion, giving u super strength!!", 0, ItemType.POTION, 1, 1));

		this.equipment = new int[] { 0, -1, -1, -1, -1, -1 }; // main hand, offhand, helmet, chest, legs, boots
		// this.levelBoon = 5; //increase all stats per level
		this.gold = 1;
	}

	public Hero(int hp, int lvl, String name, int str, int mstr, int def, int dodge, Position pos) {
		super(hp, lvl, name, str, mstr, def, dodge, pos);

		this.items = new ArrayList<Item>();

		// this.items.add(new Weapon(1000, "DEBUGGING DEATH", "", 50, 50, 50, 50));
		this.items.add(new Weapon(5, "Basic Dagger", "A simple dagger, coincidentally next to you when you woke up"));
		this.items
				.add(new Spell(30, "Basic Ice Dart", "Conjure a simple shard of Ice, and launch it at the enemy", 0, 5, 2));
		this.items.add(new Potion(50, "Potent Healing Potion", "A brewed healing potion, capable of healing basic injuries",
				0, ItemType.POTION, 0, 3));
		// this.items.add(new Potion(50, "Potent Strengthening Potion", "A brewed
		// healing potion, giving u super strength!!", 0, ItemType.POTION, 1, 1));

		this.equipment = new int[] { 0, -1, -1, -1, -1, -1 }; // main hand, offhand, helmet, chest, legs, boots
		// this.levelBoon = 5; //increase all stats per level
		this.gold = 1;
	}

	public static void setLevelBoon(int i) {
		levelBoon = 5;
	}

	public static String getShortHeroDisplay(Hero hero) {
		return "| " + hero.getName() + " | Level: " + hero.getLevel() + " | EXP:" + hero.getExperience() + "/ "
				+ hero.getBreakpoint() + " | Gold: " + hero.getGold() + " |";
	}

	public static String getHeroDisplay(Hero hero) {
		StringBuilder sb = new StringBuilder();

		sb.append("==========================================\n");
		sb.append(hero.getName() + "\n");
		sb.append("==========================================\n");

		// Basic Info
		sb.append(
				"Level: " + hero.getLevel() + " | Experience: " + hero.getExperience() + " / " + hero.getBreakpoint() + "\n");
		sb.append("Gold: " + hero.getGold() + "\n");
		sb.append("Health: " + hero.getCurrentHealth() + " / " + hero.getMaxHealth() + "\n");
		sb.append("==========================================\n");

		for (int i = 0; i < 4; i++) {
			sb.append(getHeroStat(hero, i));
			sb.append("\n");
		}

		sb.append("==========================================\n");

		return sb.toString();
	}

	private static String getHeroStat(Hero hero, int StatType) {
		StringBuilder newStatLine = new StringBuilder();
		switch (StatType) {
			case 0:
				newStatLine.append(PrintingUtil.printWithPadding("Strength", 15)
						+ hero.getStrength() + " := " + hero.getBaseStrength());
				break;
			case 1:
				newStatLine.append(PrintingUtil.printWithPadding("Magic Strength", 15)
						+ hero.getMagicStrength() + " := " + hero.getBaseMagicStrength());
				break;
			case 2:
				newStatLine.append(PrintingUtil.printWithPadding("Defense", 15)
						+ hero.getDefense() + " := " + hero.getBaseDefense());
				break;
			case 3:
				newStatLine.append(PrintingUtil.printWithPadding("Dodge", 15)
						+ hero.getDodge() + " := " + hero.getBaseDodge());
				break;
			default:
				return "Invalid Stat Type";
		}
		for (int i = 0; i < 6; i++) {
			newStatLine.append("+");
			if (hero.getEquippedItems()[i] != -1) {
				Equippable currItem = (Equippable) hero.getEquippedItem(i);
				switch (StatType) {
					case 0:
						newStatLine.append(currItem.getBonusStrength());
						break;
					case 1:
						newStatLine.append(currItem.getBonusMagicStrength());
						break;
					case 2:
						newStatLine.append(currItem.getBonusDefense());
						break;
					case 3:
						newStatLine.append(currItem.getBonusDodge());
						break;
				}
			} else {
				newStatLine.append("0");
			}
		}

		return newStatLine.toString();
	}

	public Integer getExperience() {
		return this.experience;
	}

	public boolean willLevelUp(Integer newXp) {
		return (this.experience + newXp) >= breakpoint;
	}

	public void gainExperience(Integer newXp) {
		this.experience += newXp;
		while (this.experience >= breakpoint) {
			StatsTracker.addToStats("times Levelled up", 1);
			this.levelUp();
			this.experience -= breakpoint;
			this.levelUpDefense();
			this.levelUpDodge();
			this.levelUpMagicStrength();
			this.levelUpStrength();
			this.levelUpHealth();
			this.healDamage(50);
			// recharge spells
			for (int i = 0; i < this.items.size(); i++) {
				if (this.items.get(i).getItemType() == ItemType.SPELL) {
					this.items.get(i).setRemainingUses(this.items.get(i).getMaxUses());
				}
			}
		}
	}

	public int getBreakpoint() {
		return this.breakpoint;
	}

	@Override
	public EntityType getType() {
		return EntityType.HERO;
	}

	@Override
	public Integer getGold() {
		return this.gold;
	}

	@Override
	public void spendGold(int amount) {
		this.gold -= amount;
	}

	@Override
	public void earnGold(int amount) {
		StatsTracker.addToStats("Gold earned", amount);
		this.gold += amount;
	}

	@Override
	public boolean canAfford(int cost) {
		return this.gold > cost;
	}

	public Integer getDodge() {
		Integer finalDodge = super.getDodge();
		for (int eq : equipment) {
			if (eq != -1) {
				finalDodge += ((Equippable) items.get(eq)).getBonusDodge();
			}
		}
		return finalDodge;
	}

	public Integer getStrength() {
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

	public void buffStrength(int buff) {
		this.setStrength(this.strength + buff);
	}

	public void buffDefense(int buff) {
		this.setDefense(this.defense + buff);
	}

	public void buffMagicStrength(int buff) {
		this.setMagicStrength(this.magicStrength + buff);
	}

	public void buffDodge(int buff) {
		this.setDodge(this.dodge + buff);
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
		if (itemIndex == -1) {
			this.unequipItem(equipment_slot);
			return true;
		}

		Item newItem = this.items.get(itemIndex);
		if ((equipment_slot == 0 || equipment_slot == 1) &&
				!(newItem.getItemType() == ItemType.WEAPON || newItem.getItemType() == ItemType.BIG_WEAPON)) {
			return false;
		} else if (equipment_slot == 2 && newItem.getItemType() != ItemType.HELMET) {
			return false;
		} else if (equipment_slot == 3 && newItem.getItemType() != ItemType.CHEST) {
			return false;
		} else if (equipment_slot == 4 && newItem.getItemType() != ItemType.LEGS) {
			return false;
		} else if (equipment_slot == 5 && newItem.getItemType() != ItemType.BOOTS) {
			return false;
		}

		if (this.items.get(itemIndex).getItemType() == ItemType.BIG_WEAPON) {
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
		this.equipment = new int[] { -1, -1, -1, -1, -1, -1 };
	}

	@Override
	public Item getEquippedItem(int i) {
		return this.items.get(this.equipment[i]);
	}

	@Override
	public Item[] getSpellsList() {
		ArrayList<Item> spells = new ArrayList<Item>();
		for (Item item : this.items) {
			if (item.getItemType() == ItemType.SPELL) {
				spells.add(item);
			}
		}
		return spells.toArray(new Item[spells.size()]);
	}

	@Override
	public AttackOption mainHandAttack() {
		// (Base Strength + Item Damage) * (1 + level / 10)
		if (this.equipment[0] == -1) {
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

		for (Item item : this.getItemsList()) {
			if (item.getItemType() == ItemType.SPELL) {
				Spell spell = (Spell) item;
				if (spell.getLevelRequirement() <= this.getLevel()) {
					int damage = (this.getMagicStrength() + item.getDamage()) * (1 + this.getLevel() / 10);
					attacks.add(new AttackOption("Spell Attack", "You cast " + spell.getName(), spell, damage));
				}
			} else if (item.getItemType() == ItemType.POTION) {
				Potion potion = (Potion) item;
				if (potion.getLevelRequirement() <= this.getLevel()) {
					attacks.add(new AttackOption("Potion Use", "You drink " + potion.getName(), potion, potion.getDamage()));
				}
			}

		}

		return attacks;

	}

	@Override
	public ArrayList<AttackOption> getAttacksListInRange(Position targetPos) {
		Position heroPos = this.getPosition();
		int targetDist = heroPos.distanceTo(targetPos);
		ArrayList<AttackOption> attacks = new ArrayList<AttackOption>();
		ArrayList<AttackOption> allAttacks = this.getAttacksList();
		for (AttackOption attack : allAttacks) {
			if (attack.getRange() != null && attack.getRange() >= targetDist) {
				attacks.add(attack);
			}
		}

		return attacks;
	}

	public ArrayList<AttackOption> getAttacksListInRange(Position targetPos, Monster target) {
		Position heroPos = this.getPosition();
		int targetDist = heroPos.distanceTo(targetPos);
		ArrayList<AttackOption> attacks = new ArrayList<AttackOption>();
		ArrayList<AttackOption> allAttacks = this.getAttacksList();
		for (AttackOption attack : allAttacks) {
			if (attack.getRange() != null && attack.getRange() >= targetDist) {
				attacks.add(attack);
				attack.setMonsterTarget(target);
			}
		}

		return attacks;
	}

	public void levelUpHealth() {
		this.maxHealth += levelBoon * 2 + rng.nextInt(3);
		this.healDamage(levelBoon * 2);
	}

	public void levelUpStrength() {
		this.strength += levelBoon + rng.nextInt(2);
	}

	public void levelUpDefense() {
		this.defense += levelBoon + rng.nextInt(2);
	}

	public void levelUpMagicStrength() {
		this.magicStrength += levelBoon + rng.nextInt(2);
	}

	public void levelUpDodge() {
		this.dodge += (levelBoon) / 2 + rng.nextInt(2);
	}

	public void updateInventory() {
		ArrayList<Item> newItems = new ArrayList<>();
		int[] newEquipment = new int[this.equipment.length];

		int[] indexMapping = new int[this.items.size()];
		int newIndex = 0;

		for (int i = 0; i < this.items.size(); i++) {
			Item item = this.items.get(i);
			if (!(item.getItemType() == ItemType.POTION && item.getRemainingUses() == 0)) {
				newItems.add(item);
				indexMapping[i] = newIndex++;
			} else {
				indexMapping[i] = -1;
			}
		}

		for (int i = 0; i < this.equipment.length; i++) {
			int oldIdx = this.equipment[i];
			if (oldIdx == -1) {
				newEquipment[i] = -1;
			} else if (oldIdx >= 0 && oldIdx < indexMapping.length && indexMapping[oldIdx] != -1) {
				newEquipment[i] = indexMapping[oldIdx];
			} else {
				newEquipment[i] = -1;
			}
		}

		this.items = newItems;
		this.equipment = newEquipment;
	}

}
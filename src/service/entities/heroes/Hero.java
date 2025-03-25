package src.service.entities.heroes;

import java.util.ArrayList;

import src.service.entities.Entity;
import src.service.entities.attributes.AttackOption;
import src.service.entities.attributes.Attacks;
import src.service.entities.attributes.Inventory;
import src.service.entities.items.Item;
import src.service.entities.items.Weapon;
import src.util.ItemType;
import src.util.TextColor;

public class Hero extends Entity implements Attacks, Inventory {

	private ArrayList<Item> items;
	private int[] equipment; //helmet, chest, legs, boots



	public Hero(){
		super(500, 5, "DEBUGGING HERO", 50, 50, 50);

		this.items = new ArrayList<Item>();

		// add some default items
		this.items.add(new Weapon(10, "Debugging Dagger", "A simple metal dagger for debugging purposes"));
		this.items.add(new Weapon(20, "Debugging Sword", "A simple metal sword for debugging purposes"));
		this.items.add(new Weapon(30, "Debugging Big Sword", "A simple metal big sword for debugging purposes"));
		this.items.add(new Weapon(40, "Debugging Big Axe", "A simple metal big axe for debugging purposes"));
		this.items.add(new Weapon(50, "Debugging Big Hammer", "A simple metal big hammer for debugging purposes"));
		this.items.add(new Weapon(60, "Debugging Big Spear", "A simple metal big spear for debugging purposes"));
		this.items.add(new Weapon(70, "Debugging Big Staff", "A simple metal big staff for debugging purposes"));
		this.items.add(new Weapon(80, "Debugging Big Bow", "A simple metal big bow for debugging purposes"));
		this.items.add(new Weapon(90, "Debugging Big Crossbow", "A simple metal big crossbow for debugging purposes"));
		this.items.add(new Weapon(100, "Debugging Big Wand", "A simple metal big wand for debugging purposes"));

		this.equipment = new int[] {-1, -1, -1, -1, -1, -1}; //main hand, offhand, helmet, chest, legs, boots

	}

	public Integer basicDebugAttack(){
		return this.getStrength();
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
	public Item getEquippedItem() {
		return this.items.get(this.equipment[0]);
	}

	@Override
	public Item[] getSpellsList() {
		throw new UnsupportedOperationException("Unimplemented method 'getSpellsList'");
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
		int damage = (this.getStrength() + equippedItem.getDamage()) * (1 + this.getLevel() / 10);

		return new AttackOption("Main Hand Attack", description, equippedItem, damage);
	}
	@Override
	public ArrayList<AttackOption> getAttacksList() {

		ArrayList<AttackOption> attacks = new ArrayList<AttackOption>();

		attacks.add(this.mainHandAttack());
		// iterate over inventory, add any possible attacks based on spells
		return attacks;
		
	}
	
} 
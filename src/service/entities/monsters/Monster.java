package src.service.entities.monsters;

import java.util.ArrayList;

import src.service.entities.Entity;
import src.service.entities.attributes.AttackOption;
import src.service.entities.attributes.Attacks;
import src.service.entities.items.Item;
import src.service.entities.items.Weapon;

public class Monster extends Entity implements Attacks {

	public Monster(){
		super();
	}

	public Integer basicDebugAttack(){
		return this.getStrength();
	}

	@Override
	public Item[] getSpellsList() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getSpellsList'");
	}

	@Override
	public AttackOption mainHandAttack() {
		// TODO Auto-generated method stub

		Item monsterItem = new Weapon(5, "Monster's Scary Weapon", "A weapon used by monsters.");
		int monsterItemDamage = monsterItem.getDamage() + this.getStrength();

		return new AttackOption("Basic Attack", "The monster attacks with it's scary weapon!", monsterItem, monsterItemDamage);
	}

	@Override
	public ArrayList<AttackOption> getAttacksList() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getAttacksList'");
	}

	
}

package src.service.entities.monsters;

import java.util.ArrayList;
import java.util.Random;

import src.service.entities.Entity;
import src.service.entities.attributes.AttackOption;
import src.service.entities.attributes.Attacks;
import src.service.entities.items.Item;
import src.service.entities.items.Weapon;

public class Monster extends Entity implements Attacks {

	private int rewardXP;
	private int rewardGold;

	public Monster(){
		super();
		Random rng = new Random();
		rewardGold = 50 + rng.nextInt(50);
		rewardXP = 50 + rng.nextInt(50);
	}

	public Monster(int xp, int gold){
		this.rewardGold = gold;
		this.rewardXP = xp;
	}

	public Integer basicDebugAttack(){
		return this.getStrength();
	}
	public int getRewardGold() {
		return rewardGold;
	}
	public int getRewardXP() {
		return rewardXP;
	}



	@Override
	public Item[] getSpellsList() { 
		throw new UnsupportedOperationException("Unimplemented method 'getSpellsList'");
	}

	@Override
	public AttackOption mainHandAttack() {
		// TODO Auto-generated method stub

		Item monsterItem = new Weapon(5, "Monster's Scary Weapon", "A weapon used by monsters.", 0, 0, 0, 0);
		int monsterItemDamage = monsterItem.getDamage() + this.getStrength();

		return new AttackOption("Basic Attack", "The monster attacks with it's scary weapon!", monsterItem, monsterItemDamage);
	}

	@Override
	public ArrayList<AttackOption> getAttacksList() {
		throw new UnsupportedOperationException("Unimplemented method 'getAttacksList'");
	}

	
}

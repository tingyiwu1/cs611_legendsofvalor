/**
 * The Monster class represents a monster entity in the game, extending the Entity class
 * and implementing the Attacks interface. It encapsulates the attributes and behaviors
 * of a monster, including its rewards, weapon, and combat capabilities.
 */
package src.service.entities.monsters;

import java.util.ArrayList;
import java.util.Random;

import src.service.entities.Entity;
import src.service.entities.attributes.AttackOption;
import src.service.entities.attributes.Attacks;
import src.service.entities.attributes.Position;
import src.service.entities.items.Item;
import src.service.entities.items.Weapon;



public class Monster extends Entity implements Attacks {

	private int rewardXP;
	private int rewardGold;
	private Item monsterWeapon;
	private String description;

	private static int levelBoon = 3;

	public static void setLevelBoon(int i){
		levelBoon = i;
	}

	public Monster(){
		super();
		Random rng = new Random();
		rewardGold = 50 + rng.nextInt(50);
		rewardXP = 50 + rng.nextInt(50);
		this.monsterWeapon = new Weapon(0, "Generic Debugging Monster Weapon", "a developers tool to trick you");
	}
	//Integer maxHealth, Integer level, String name, Integer strength, Integer magicStrength, Integer defense, Integer dodge
	//xp, gold, description
	//

	public Monster( Integer maxHealth, 
					Integer level, 
					String name,
					Integer strength, Integer magicStrength, Integer defense, Integer dodge,
					int xp, int gold,
					String description,
					Item monsterWeapon){
		super(
			maxHealth + level*levelBoon + new Random().nextInt(10),
			level,
			name,
			strength + level*levelBoon + new Random().nextInt(5),
			magicStrength + level*levelBoon + new Random().nextInt(5),
			defense + level*levelBoon + new Random().nextInt(5),
			dodge + (level*levelBoon)/2 + new Random().nextInt(3),
			new Position(0, 0)
		);

		this.description = description;

		this.monsterWeapon = monsterWeapon;

		Random rng = new Random();
		rewardGold = gold + rng.nextInt(50);
		rewardXP = xp + rng.nextInt(50);

	}

	public Monster(int xp, int gold){
		this.rewardGold = gold;
		this.rewardXP = xp;
	}

	@Override
	public EntityType getType(){
		return EntityType.MONSTER;
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

	public String getDescription() {
		return description;
	}

	public Item getMonsterWeapon() {
		return monsterWeapon;
	}



	@Override
	public Item[] getSpellsList() { 
		throw new UnsupportedOperationException("Unimplemented method 'getSpellsList'");
	}

	@Override
	public AttackOption mainHandAttack() {

		Item monsterItem = this.monsterWeapon;
		int monsterItemDamage = monsterItem.getDamage() + this.getStrength();

		return new AttackOption("Basic Attack", "The " + this.name +" Attacks!", monsterItem, monsterItemDamage);
	}

	@Override
	public ArrayList<AttackOption> getAttacksList() {
		throw new UnsupportedOperationException("Unimplemented method 'getAttacksList'");
	}

	
}

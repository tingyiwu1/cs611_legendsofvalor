/**
 * The Entity class serves as an abstract base class for all entities in the game.
 * It provides common attributes such as health, level, name, strength, magic strength,
 * defense, and dodge, along with methods to manipulate and retrieve these attributes.

 * 
 * This class is intended to be extended by specific entity types.
 */
package src.service.entities;

import src.util.StatsTracker;



public abstract class Entity {

	protected Integer currentHealth;
	protected Integer maxHealth;
	protected Integer level;
	protected String name;
	protected Integer strength;
	protected Integer magicStrength;
	protected Integer defense;
	protected Integer dodge;

	public Entity(){
		this.currentHealth = 100;
		this.maxHealth = 100;
		this.level = 1;
		this.name = "EXAMPLE";
		this.strength = 10;
		this.magicStrength = 10;
		this.dodge = 10;
		this.defense = 10;
	}

	public Entity(Integer maxHealth, Integer level, String name, Integer strength, Integer magicStrength, Integer defense, Integer dodge){
		this.currentHealth = maxHealth;
		this.maxHealth = maxHealth;
		this.level = level;
		this.name = name;
		this.strength = strength;
		this.magicStrength = magicStrength;
		this.defense = defense;
		this.dodge = dodge;
	}


	// public void printStats();
	
	public Integer getCurrentHealth(){
		return this.currentHealth;
	}
	public Integer getMaxHealth(){
		return this.maxHealth;
	}

	public void takeDamage(Integer damage){
		if(damage >= 0){
			this.currentHealth -= damage;
		}
	}
	public void healDamage(Integer heal){
		StatsTracker.addToStats("Health Healed", 1);
		this.currentHealth += heal;
		if(currentHealth > maxHealth){
			currentHealth = maxHealth;
		}
	}

	public Integer getLevel(){	
		StatsTracker.addToStats("Times Levelled Up", 1);
		return this.level;
	}
	public void levelUp(){
		this.level += 1;
	}

	public String getName(){
		return this.name;
	}
	public void setName(String name){
		this.name = name;
	}

	public Integer getStrength(){
		return this.strength;
	}
	public void setStrength(Integer strength){
		this.strength = strength;
	}

	public Integer getMagicStrength(){
		return this.magicStrength;
	}
	public void setMagicStrength(Integer magicStrength){
		this.magicStrength = magicStrength;
	}

	public Integer getDodge(){
		return this.dodge;
	}
	public void setDodge(Integer dodge){
		this.dodge = dodge;
	}

	public Integer getDefense(){
		return this.defense;
	}
	public void setDefense(Integer defense){
		this.defense = defense;
	}
	
} 
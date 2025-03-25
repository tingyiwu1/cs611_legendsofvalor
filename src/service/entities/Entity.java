package src.service.entities;

public abstract class Entity {

	protected Integer currentHealth;
	protected Integer maxHealth;
	protected Integer level;
	protected String name;
	protected Integer strength;
	protected Integer magicStrength;
	protected Integer dodge;

	public Entity(){
		this.currentHealth = 100;
		this.maxHealth = 100;
		this.level = 1;
		this.name = "EXAMPLE";
		this.strength = 10;
		this.magicStrength = 10;
		this.dodge = 10;
	}

	public Entity(Integer maxHealth, Integer level, String name, Integer strength, Integer magicStrength, Integer dodge){
		this.currentHealth = maxHealth;
		this.maxHealth = maxHealth;
		this.level = level;
		this.name = name;
		this.strength = strength;
		this.magicStrength = magicStrength;
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
		this.currentHealth -= damage;
	}
	public void healDamage(Integer heal){
		this.currentHealth += heal;
	}

	public Integer getLevel(){	
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


	
} 
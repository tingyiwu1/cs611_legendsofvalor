/**
 * The Attacks interface defines the contract for entities that can perform 
 * various types of attacks, including physical and magical attacks. It provides 
 * methods to retrieve available spells, perform a main-hand attack, and get a 
 * list of all possible attack options.
 */
package src.service.entities.attributes;

import java.util.ArrayList;

import src.service.entities.items.Item;


public interface Attacks {
	// getSpellsList();
	// getAttacksList();

	public Item[] getSpellsList();

	public AttackOption mainHandAttack();
	// public Integer useMagicAttack();

	public ArrayList<AttackOption> getAttacksList();


	
} 
package src.service.entities.attributes;

import java.util.ArrayList;

import src.service.entities.items.Item;

public interface Attacks {
	// getSpellsList();
	// getAttacksList();

	public Integer basicDebugAttack();

	public Item[] getSpellsList();

	public AttackOption mainHandAttack();
	// public Integer useMagicAttack();

	public ArrayList<AttackOption> getAttacksList();


	
} 
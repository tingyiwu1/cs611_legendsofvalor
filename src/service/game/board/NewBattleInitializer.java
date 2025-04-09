package src.service.game.board;

import src.service.entities.attributes.AttackOption;
import src.service.entities.monsters.Monster;

/* 
 * opens up a new battle
 * works with game board, opens an interface for MainGame to communicate with GameBoard
 */
public interface NewBattleInitializer {
	public Boolean getEnteredBattle();
	public void setEnteredBattle(Boolean enteredBattle);
	
	public void resetBattleInitializer();

	public void setMonsterTarget(Monster target);
	public Monster getMonsterTarget();

	public void setAttackOption(AttackOption attackOption);
	public AttackOption getAttackOption();
}

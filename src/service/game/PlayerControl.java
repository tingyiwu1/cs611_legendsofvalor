/**
 * The PlayerControl interface defines the contract for controlling a player in the game.
 * It provides methods to validate, execute, and process player moves.
 */
package src.service.game;


public interface PlayerControl {
	public Boolean isMoveValid(Character inputtedMove);
	public Boolean makeMove(Character inputtedMove);
	public Boolean processMove(Character inputtedMove, TurnKeeper turnKeeper);
	
} 
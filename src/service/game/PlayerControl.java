/**
 * The PlayerControl interface defines the contract for controlling a player in the game.
 * It provides methods to validate, execute, and process player moves.
 */
package src.service.game;

public interface PlayerControl {
	public boolean isMoveValid(Character inputtedMove);

	public boolean makeMove(Character inputtedMove);

	public boolean processMove(Character inputtedMove, TurnKeeper turnKeeper);

}
package src.service.game.enemyControl;

import src.service.entities.monsters.MonsterTeam;
import src.service.game.TurnKeeper;
import src.service.game.board.GameBoard;

public class enemyController {
	
	public static void makeCurrentEnemyMove(TurnKeeper turnKeeper, GameBoard gameBoard, MonsterTeam monster) {
		// Check if it's the monster's turn
		if (turnKeeper.getCurrentTurn() == TurnKeeper.CurrentTurn.MONSTER) {
			// Get the monster team and make a move
			// Assuming you have a method to get the monster team from the game board
			// MonsterTeam monsterTeam = gameBoard.getMonsterTeam();
			
			// Here you would implement the logic for the monster's move
			// For example, randomly moving a monster or attacking the player
			System.out.println("Monster is making a move...");
			
			// After the monster's move, progress the turn
			turnKeeper.progressTurn();
		} else {
			throw new IllegalStateException("It's not the monster's turn.");
		}
	}

	

}

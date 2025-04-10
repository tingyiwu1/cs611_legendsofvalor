package src.service.game.enemyControl;

import java.util.ArrayList;

import src.service.entities.Entity;
import src.service.entities.attributes.Position;
import src.service.entities.heroes.Hero;
import src.service.entities.monsters.Monster;
import src.service.entities.monsters.MonsterTeam;
import src.service.game.TurnKeeper;
import src.service.game.board.GameBoard;
import src.util.PieceType;

public class EnemyController {

	/*
	 * TODO:
	 * Probably should somewhat refactor this
	 */
	
	public static Boolean makeCurrentEnemyMove(TurnKeeper turnKeeper, GameBoard gameBoard, MonsterTeam monsterTeam) {

		boolean isMonsterAttacking = false;
		// Check if it's the monster's turn
		if (turnKeeper.getCurrentTurn() == TurnKeeper.CurrentTurn.MONSTER) {
			// Get the monster team and make a move
			Monster monster = monsterTeam.getMonsters().get(turnKeeper.getMonsterTeamTurnCount());
			for(Entity entity : gameBoard.getEntityList()){
				if(entity.getType() == Entity.EntityType.HERO){
					Hero hero = (Hero) entity;
					if(monsterWillAttack(monster, hero)){
						isMonsterAttacking = true;
						monsterAttacks(monster, hero);
					}
				}
			}

			if(!isMonsterAttacking){
				EnemyController.monsterMoves(monster, gameBoard, turnKeeper);
			}
			// if(turnKeeper.progressTurn()){
			// 	turnKeeper.resetTurn();
			// }
		} else {
			throw new IllegalStateException("It's not the monster's turn.");
		}

		return isMonsterAttacking;
	}

	private static Boolean monsterWillAttack(Monster monster, Hero hero){
		// Check if the monster is in range of the hero
		Position monsterPos = monster.getPosition();
		Position heroPos = hero.getPosition();
		
		if(monsterPos.distanceTo(heroPos) <= 1){
			return true;
		}
		
		return false;
	}
	
	private static Boolean monsterMoves(Monster monster, GameBoard gameBoard, TurnKeeper turnKeeper){
		Position currPos = monster.getPosition();
		ArrayList<Entity> entityList = gameBoard.getEntityList();
		for(Entity en : entityList){
			if(en.getType() == Entity.EntityType.HERO){
				Hero hero = (Hero) en;
				Position heroPos = hero.getPosition();
				if(currPos.getX() == heroPos.getX()){
					throw new IllegalStateException("Monster cannot progress pass Hero");
				}
			}
		}
		// Move the monster to a new position

		Position newPos = new Position(currPos.getX() + 1, currPos.getY());
		// Check if the new position is valid (within bounds and not occupied)
		if(gameBoard.getPieceAt(newPos).getPieceType() == PieceType.OBSTACLE){
			// try to go around this obstacle
			int oldY = currPos.getY();
			int newY = 0;
			if(oldY == 0 || oldY == 3 || oldY == 6){
				newY = oldY + 1;
			} else {
				newY = oldY - 1;
			}

			// try to go around it
			newPos = new Position(currPos.getX(), newY);
			PieceType pieceType = gameBoard.getPieceAt(newPos).getPieceType();
			if(pieceType == PieceType.OBSTACLE || pieceType == PieceType.WALL){
				// if moving to left/right is still an obstacle, just stay still
				newPos = currPos;
			}
		}
		monster.setPosition(newPos);
		turnKeeper.progressTurn();

		return false;

	}

	private static void monsterAttacks(Monster monster, Hero hero){

	}

	

}

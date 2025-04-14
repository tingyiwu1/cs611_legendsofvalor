package src.service.game.enemyControl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

	public static MonsterAction makeCurrentEnemyMove(TurnKeeper turnKeeper, GameBoard gameBoard,
			List<Monster> monsterList) {

		// Check if it's the monster's turn
		if (turnKeeper.getCurrentTurn() != TurnKeeper.CurrentTurn.MONSTER) {
			throw new IllegalStateException("It's not the monster's turn.");
		}

		// Get the monster team and make a move
		Monster monster = monsterList.get(turnKeeper.getMonsterTeamTurnCount());
		for (Entity entity : gameBoard.getEntitiesInLane(monster.getPosition().getY())) {
			if (entity.getType() == Entity.EntityType.HERO) {
				Hero hero = (Hero) entity;
				if (monsterWillAttack(monster, hero)) {
					return MonsterAction.attack(monster, hero);
				}
			}
		}

		Optional<Position> newPos = EnemyController.monsterMoves(monster, gameBoard, turnKeeper);
		if (newPos.isPresent())
			return MonsterAction.move(monster, newPos.get());
		else {
			turnKeeper.progressTurn();
			return MonsterAction.doNothing(monster);
		}
	}

	private static Boolean monsterWillAttack(Monster monster, Hero hero) {
		// Check if the monster is in range of the hero
		Position monsterPos = monster.getPosition();
		Position heroPos = hero.getPosition();

		if (monsterPos.chebyshevDistance(heroPos) <= 1) {
			return true;
		}

		return false;
	}

	private static Optional<Position> monsterMoves(Monster monster, GameBoard gameBoard, TurnKeeper turnKeeper) {
		Position currPos = monster.getPosition();
		for (Entity e : gameBoard.getEntitiesInLane(monster.getPosition().getY())) {
			if (e.getType() == Entity.EntityType.HERO) {
				Hero hero = (Hero) e;
				Position heroPos = hero.getPosition();
				if (currPos.getX() == heroPos.getX()) {
					return Optional.empty();
				}
			}
		}
		// Move the monster to a new position

		Position newPos = new Position(currPos.getX() + 1, currPos.getY());
		// Check if the new position is valid (within bounds and not occupied)
		if (gameBoard.getPieceAt(newPos).getPieceType() == PieceType.OBSTACLE) {
			// try to go around this obstacle
			int oldY = currPos.getY();
			int newY = 0;
			if (oldY == 0 || oldY == 3 || oldY == 6) {
				newY = oldY + 1;
			} else {
				newY = oldY - 1;
			}

			// try to go around it
			newPos = new Position(currPos.getX(), newY);
			PieceType pieceType = gameBoard.getPieceAt(newPos).getPieceType();
			if (pieceType == PieceType.OBSTACLE || pieceType == PieceType.WALL) {
				// if moving to left/right is still an obstacle, just stay still
				newPos = currPos;
			}
		}
		monster.setPosition(newPos);
		turnKeeper.progressTurn();

		return Optional.of(newPos);

	}
}

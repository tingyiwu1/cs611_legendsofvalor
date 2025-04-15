package src.service.game.board;

import src.util.PieceType;
import src.service.entities.Entity;
import src.service.entities.Player;
import src.service.entities.Entity.EntityType;
import src.service.entities.attributes.AttackOption;
import src.service.entities.attributes.Position;
import src.service.entities.heroes.Hero;
import src.service.entities.monsters.Monster;
import src.service.entities.monsters.MonsterTeam;
import src.service.game.PlayerControl;
import src.service.game.TurnKeeper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/*
 * Game Board for the entire game
 */
public class GameBoard implements PlayerControl, NewBattleInitializer {
	private MapPiece[][] currentBoard;
	private Integer size;
	private Character lastInput = null;
	// private int charX;
	// private int charY;
	private Player player;

	private TurnKeeper turnKeeper;
	private MonsterTeam monsterTeam;
	// later to have BossPosition, with a boss piece

	/*
	 * Vars for NewBattleInitializer
	 */
	private Boolean enteredBattle = false;
	private Monster monsterTarget;
	private AttackOption attackOption;

	// make a new gameboard
	public GameBoard(int size, double wallPercent, double marketPercent, Player player, MonsterTeam monsterTeam,
			TurnKeeper turnKeeper) {
		this.turnKeeper = turnKeeper;
		this.currentBoard = new MapPiece[size][size];
		this.size = size;
		this.player = player;

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				this.currentBoard[i][j] = new MapPiece();
			}
		}
		// this.charX = 7;
		// this.charY = 0;

		// randomly add walls and markets
		// int totalSpaces = size * size;
		// int numMarkets = (int)(totalSpaces * marketPercent);

		Random rand = new Random();
		for (int i = 0; i < this.size; i++) {
			this.currentBoard[i][2].setPieceType(PieceType.WALL);
			this.currentBoard[i][5].setPieceType(PieceType.WALL);
		}

		for (int r = 0; r < this.size; r++) {
			if (r != 2 && r != 5) {
				this.currentBoard[0][r].setPieceType(PieceType.MONSTER_NEXUS);
				this.currentBoard[7][r].setPieceType(PieceType.HERO_NEXUS);
			}
		}

		this.monsterTeam = monsterTeam;

		for (int r = 1; r < this.size - 1; r++) {
			for (int c = 0; c < this.size; c++) {
				if (this.currentBoard[r][c].getPieceType() == PieceType.EMPTY) {
					double randNum = rand.nextDouble();
					/**
					 * Distribution: 50% Empty, 15% Obstacle, 12.5% Bush, 12.5% Cave, 10% Koulou
					 */
					if (randNum < 0.5) {
						// do nothing
					} else if (randNum < 0.65) {
						this.currentBoard[r][c].setPieceType(PieceType.OBSTACLE);
					} else if (randNum < 0.785) {
						this.currentBoard[r][c].setPieceType(PieceType.BUSH);
					} else if (randNum < 0.9) {
						this.currentBoard[r][c].setPieceType(PieceType.CAVE);
					} else {
						this.currentBoard[r][c].setPieceType(PieceType.KOULOU);
					}
				}
			}
		}

	}

	/*
	 * public void setNewBoss() {
	 * boolean[][] visited = new boolean[size][size];
	 * Queue<int[]> queue = new LinkedList<>();
	 * LinkedList<int[]> movableNodes = new LinkedList<>();
	 * queue.add(new int[] { this.charX, this.charY });
	 * visited[this.charX][this.charY] = true;
	 * 
	 * while (!queue.isEmpty()) {
	 * int[] current = queue.poll();
	 * int x = current[0];
	 * int y = current[1];
	 * movableNodes.add(current);
	 * 
	 * int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
	 * for (int[] dir : directions) {
	 * int newX = x + dir[0];
	 * int newY = y + dir[1];
	 * if (newX >= 0 && newX < size && newY >= 0 && newY < size
	 * && !visited[newX][newY]
	 * && this.currentBoard[newX][newY].getPieceType() != PieceType.WALL) {
	 * visited[newX][newY] = true;
	 * queue.add(new int[] { newX, newY });
	 * }
	 * }
	 * }
	 * 
	 * // Randomly choose one of the movable nodes to become the boss
	 * if (!movableNodes.isEmpty()) {
	 * Random rand = new Random();
	 * int[] bossLocation;
	 * do {
	 * bossLocation = movableNodes.get(rand.nextInt(movableNodes.size()));
	 * } while (bossLocation[0] == this.charX && bossLocation[1] == this.charY);
	 * 
	 * this.currentBoard[bossLocation[0]][bossLocation[1]].setPieceType(PieceType.
	 * BOSS);
	 * }
	 * }
	 * 
	 */
	public void setPieceAt(int x, int y, PieceType newType) {
		currentBoard[x][y].setPieceType(newType);
	}

	public ArrayList<Entity> getEntityList() {
		ArrayList<Entity> entityList = new ArrayList<>();
		for (Entity e : player.getParty()) {
			entityList.add(e);
		}
		for (Entity e : monsterTeam.getMonsters()) {
			entityList.add(e);
		}
		return entityList;
	}

	public List<Entity> getEntitiesInLane(int y) {
		int lane = y / 3;
		ArrayList<Entity> entityList = new ArrayList<>();
		for (Entity e : getEntityList()) {
			if (e.getPosition().getY() / 3 == lane) {
				entityList.add(e);
			}
		}
		return Collections.unmodifiableList(entityList);
	}

	public Position getCurrHeroLocation() {
		int turnIdx = this.turnKeeper.getPlayerTeamTurnCount();
		Position currHeroLocation = this.player.getParty()[turnIdx].getPosition();

		return currHeroLocation;
	}

	public boolean characterAtMarket() {
		return getPieceAt(getCurrHeroLocation()).getPieceType() == PieceType.HERO_NEXUS;
	}

	public int getCurrentMarketIndex() {
		return 0;
		// return charX * size + charY;
	}

	public Integer getSize() {
		return this.size;
	}

	public MapPiece getPieceAt(int x, int y) {
		return this.currentBoard[x][y];
	}

	public MapPiece getPieceAt(Position pos) {
		int x = pos.getX();
		int y = pos.getY();
		return this.currentBoard[x][y];
	}

	public MapPiece getCurrentPiece() {
		Position pos = this.getCurrHeroLocation();
		int charX = pos.getX();
		int charY = pos.getY();
		return this.currentBoard[charX][charY];
	}

	public Character getLastInput() {
		return this.lastInput;
	}

	public MonsterTeam getMonsterTeam() {
		return this.monsterTeam;
	}

	public Player getPlayer() {
		return this.player;
	}

	public boolean isAtBoss() {
		return false;
		// return this.getPieceAt(this.charX, this.charY).getPieceType() ==
		// PieceType.BOSS;
	}

	/**
	 * Order (wasd): up, left, down, right
	 */
	public MapPiece[] getAdjacentTiles(Position position) {
		int[][] dirs = {
				{ -1, 0 }, // up
				{ 0, -1 }, // left
				{ 1, 0 }, // down
				{ 0, 1 }, // right
		};
		MapPiece[] adjacentTiles = new MapPiece[4];
		for (int i = 0; i < dirs.length; i++) {
			int newX = position.getX() + dirs[i][0];
			int newY = position.getY() + dirs[i][1];
			if (newX >= 0 && newX < size && newY >= 0 && newY < size) {
				adjacentTiles[i] = this.currentBoard[newX][newY];
			} else {
				adjacentTiles[i] = null; // Out of bounds
			}
		}
		return adjacentTiles;
	}

	public ArrayList<AttackOption> currHeroAttackList() {
		ArrayList<AttackOption> attackList = new ArrayList<AttackOption>();

		List<Monster> monsterList = this.getMonsterTeam().getMonsters();

		for (int i = 0; i < monsterList.size(); i++) {
			Monster currMonster = monsterList.get(i);
			Hero currHero = this.getPlayer().getParty()[turnKeeper.getPlayerTeamTurnCount()];
			ArrayList<AttackOption> currMonsterAttackList = currHero.getAttacksListInRange(currMonster.getPosition(),
					currMonster);

			for (int j = 0; j < currMonsterAttackList.size(); j++) {
				AttackOption currAttackOption = currMonsterAttackList.get(j);
				attackList.add(currAttackOption);
			}
		}

		return attackList;
	}

	@Override
	public boolean makeMove(Character inputtedMove) {
		this.lastInput = Character.toLowerCase(inputtedMove);
		System.out.println(inputtedMove);
		if (!isMoveValid(inputtedMove)) {
			System.out.println("Invalid move");
			return false;
		}

		processMove(inputtedMove, this.turnKeeper);
		return true;
	}

	@Override
	public boolean isMoveValid(Character inputtedMove) {
		// Check if the input is a movement command
		if (inputtedMove == 'w' || inputtedMove == 's' || inputtedMove == 'a' || inputtedMove == 'd') {
			Position pos = this.getCurrHeroLocation();
			int newX = pos.getX();
			int newY = pos.getY();
			if (inputtedMove == 'w') {
				newX--;
			} else if (inputtedMove == 's') {
				newX++;
			} else if (inputtedMove == 'a') {
				newY--;
			} else if (inputtedMove == 'd') {
				newY++;
			}
			// Validate movement boundaries and obstacles
			if (newX < 0 || newX >= this.size || newY < 0 || newY >= this.size
					|| this.currentBoard[newX][newY].getPieceType() == PieceType.WALL) {
				return false;
			}
			// Check if we go past a monster
			for (Entity entity : getEntitiesInLane(pos.getY())) {
				if (entity.getType() == EntityType.MONSTER && entity.getPosition().getX() > newX) {
					return false;
				}
			}
			return true;
		}

		// Check if the input is an attack index
		try {
			int attackIdx = Integer.parseInt(inputtedMove.toString()) - 1;
			ArrayList<AttackOption> heroAttackList = this.currHeroAttackList();
			if (attackIdx < 0 || attackIdx >= heroAttackList.size()) {
				System.out.println("Invalid attack index");
				return false;
			}
			return true;
		} catch (NumberFormatException e) {
			// Handle invalid number format
			System.out.println("Invalid input: not a number.");
			return false;
		}
	}

	@Override
	public boolean processMove(Character inputtedMove, TurnKeeper turnKeeper) {
		if (inputtedMove == 'w' || inputtedMove == 's' || inputtedMove == 'a' || inputtedMove == 'd') {
			Position pos = this.getCurrHeroLocation();
			int newX = pos.getX();
			int newY = pos.getY();
			if (inputtedMove == 'w') {
				newX--;
			} else if (inputtedMove == 's') {
				newX++;
			} else if (inputtedMove == 'a') {
				newY--;
			} else if (inputtedMove == 'd') {
				newY++;
			}
			Position newPos = new Position(newX, newY);
			if (getPieceAt(newPos).getPieceType() == PieceType.OBSTACLE) {
				setPieceAt(newPos.getX(), newPos.getY(), PieceType.EMPTY);
			} else {
				this.getCurrHeroLocation().setX(newX);
				this.getCurrHeroLocation().setY(newY);
			}
			this.turnKeeper.progressTurn();
			return true;
		}

		try {
			Integer attackIdx = Integer.parseInt(inputtedMove.toString()) - 1;
			AttackOption attackOption = this.currHeroAttackList().get(attackIdx);

			this.attackOption = attackOption;
			this.monsterTarget = attackOption.getMonsterTarget();
			this.enteredBattle = true;

		} catch (NumberFormatException e) {
			// Handle invalid number format
			System.out.println("isMoveValid failed!");
		}

		/*
		 * DEBUG: DON'T ROTATE TURN
		 */

		return true;
	}

	@Override
	public void resetBattleInitializer() {
		this.attackOption = null;
		this.enteredBattle = false;
		this.monsterTarget = null;
	}

	@Override
	public Boolean getEnteredBattle() {
		return this.enteredBattle;
	}

	@Override
	public void setEnteredBattle(Boolean enteredBattle) {
		this.enteredBattle = enteredBattle;
	}

	@Override
	public void setMonsterTarget(Monster target) {
		this.monsterTarget = target;
	}

	@Override
	public Monster getMonsterTarget() {
		return this.monsterTarget;
	}

	@Override
	public void setAttackOption(AttackOption attackOption) {
		this.attackOption = attackOption;
	}

	@Override
	public AttackOption getAttackOption() {
		return this.attackOption;
	}

	public boolean isHeroWin() {
		for (Hero hero : player.getParty()) {
			if (getPieceAt(hero.getPosition()).getPieceType() == PieceType.MONSTER_NEXUS) {
				return true;
			}
		}
		return false;
	}

	public boolean isMonsterWin() {
		for (Monster monster : monsterTeam.getMonsters()) {
			if (getPieceAt(monster.getPosition()).getPieceType() == PieceType.HERO_NEXUS) {
				return true;
			}
		}
		return false;
	}

	public Hero getCurrentHero() {
		assert turnKeeper.getCurrentTurn() == TurnKeeper.CurrentTurn.PLAYER : "Not player's turn";
		int turnIdx = this.turnKeeper.getPlayerTeamTurnCount();
		Hero currHero = this.player.getParty()[turnIdx];
		return currHero;
	}

}

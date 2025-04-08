package src.service.game.board;
import src.util.PieceType;
import src.service.entities.Entity;
import src.service.entities.Player;
import src.service.entities.attributes.Position;
import src.service.entities.monsters.Monster;
import src.service.entities.monsters.MonsterTeam;
import src.service.game.PlayerControl;
import src.service.game.TurnKeeper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;


/*
 * Game Board for the entire game
 */
public class GameBoard implements PlayerControl{
	private MapPiece[][] currentBoard;
	private Integer size;
	private Character lastInput = null;
	// private int charX;
	// private int charY;
	private Player player;
	private ArrayList<Entity> entityList;

	private TurnKeeper turnKeeper;
	private MonsterTeam monsterTeam;	
	// later to have BossPosition, with a boss piece

	// make a new gameboard
	public GameBoard(int size, double wallPercent, double marketPercent, Player player, MonsterTeam monsterTeam, TurnKeeper turnKeeper){
		this.turnKeeper = turnKeeper;
		this.currentBoard = new MapPiece[size][size];
		this.size = size;
		this.player = player;

		for(int i = 0; i < size; i++){
			for(int j = 0; j < size; j++){
				this.currentBoard[i][j] = new MapPiece();
			}
		}
		// this.charX = 7;
		// this.charY = 0;

		//randomly add walls and markets
		// int totalSpaces = size * size;
		// int numMarkets = (int)(totalSpaces * marketPercent);

		// Random rand = new Random();
		for(int i = 0; i < this.size; i++){
			this.currentBoard[i][2].setPieceType(PieceType.WALL);	
			this.currentBoard[i][5].setPieceType(PieceType.WALL);	
		}

		for(int r = 0; r < this.size; r++){
			if(r != 2 && r != 5){
				this.currentBoard[0][r].setPieceType(PieceType.MONSTER_NEXUS);
				this.currentBoard[7][r].setPieceType(PieceType.HERO_NEXUS);
			}
		}

		this.monsterTeam = monsterTeam;

		// while(getPieceAt(this.charX, this.charY).getPieceType() != PieceType.EMPTY){
		// 	this.charX += 1;
		// 	if(this.charX == size){
		// 		this.charX = 0;
		// 		this.charY += 1;
		// 	}
		// }
		// this.setNewBoss();

		ArrayList<Entity> entityList = new ArrayList<>();
		for(Entity e : player.getParty()){
			entityList.add(e);
		}
		for(Entity e : monsterTeam.getMonsters()){
			entityList.add(e);
		}
		
		this.entityList = entityList;
	}

	/* 
	public void setNewBoss() {
		boolean[][] visited = new boolean[size][size];
		Queue<int[]> queue = new LinkedList<>();
		LinkedList<int[]> movableNodes = new LinkedList<>();
		queue.add(new int[] { this.charX, this.charY });
		visited[this.charX][this.charY] = true;

		while (!queue.isEmpty()) {
			int[] current = queue.poll();
			int x = current[0];
			int y = current[1];
			movableNodes.add(current);

			int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
			for (int[] dir : directions) {
				int newX = x + dir[0];
				int newY = y + dir[1];
				if (newX >= 0 && newX < size && newY >= 0 && newY < size 
						&& !visited[newX][newY] 
						&& this.currentBoard[newX][newY].getPieceType() != PieceType.WALL) {
					visited[newX][newY] = true;
					queue.add(new int[] { newX, newY });
				}
			}
		}

		// Randomly choose one of the movable nodes to become the boss
		if (!movableNodes.isEmpty()) {
			Random rand = new Random();
			int[] bossLocation;
			do {
				bossLocation = movableNodes.get(rand.nextInt(movableNodes.size()));
			} while (bossLocation[0] == this.charX && bossLocation[1] == this.charY);

			this.currentBoard[bossLocation[0]][bossLocation[1]].setPieceType(PieceType.BOSS);
		}
	}

	*/
	public void setPieceAt(int x, int y, PieceType newType){
		currentBoard[x][y].setPieceType(newType);
	}

	public ArrayList<Entity> getEntityList(){
		return this.entityList;
	}

	public Position getCurrHeroLocation() {
		int turnIdx = this.turnKeeper.getPlayerTeamTurnCount();
		Position currHeroLocation = this.player.getParty()[turnIdx].getPosition();
		
		return currHeroLocation;
	}
	



	public boolean characterAtMarket(){
		return true;
		// return getPieceAt(this.charX, this.charY).getPieceType() == PieceType.HERO_NEXUS;
	}

	public int getCurrentMarketIndex(){
		return 0;
		// return charX * size + charY;
	}

	public Integer getSize(){
		return this.size;
	}

	public MapPiece getPieceAt(int x, int y){
		return this.currentBoard[x][y];
	}

	public MapPiece getCurrentPiece(){
		Position pos = this.getCurrHeroLocation();
		int charX = pos.getX();
		int charY = pos.getY();
		return this.currentBoard[charX][charY];
	}

	public Character getLastInput(){
		return this.lastInput;
	}

	public MonsterTeam getMonsterTeam(){
		return this.monsterTeam;
	}

	public Player getPlayer(){
		return this.player;
	}

	public boolean isAtBoss() {
		return false;
		// return this.getPieceAt(this.charX, this.charY).getPieceType() == PieceType.BOSS;
	}

	@Override
	public Boolean makeMove(Character inputtedMove){
		this.lastInput = Character.toLowerCase(inputtedMove);
		System.out.println(inputtedMove);
		if(!isMoveValid(inputtedMove)){
			System.out.println("Invalid move");
			return false;
		} 

		processMove(inputtedMove, this.turnKeeper);
		return null;
	}

	@Override
	public Boolean isMoveValid(Character inputtedMove){
		Position pos = this.getCurrHeroLocation();
		int newX = pos.getX();
		int newY = pos.getY();
		if(inputtedMove == 'w'){
			newX--;
		} else if(inputtedMove == 's'){
			newX++;
		} else if(inputtedMove == 'a'){
			newY--;
		} else if(inputtedMove == 'd'){
			newY++;
		}
		if(newX < 0 || newX >= this.size || newY < 0 || newY >= this.size 
			|| this.currentBoard[newX][newY].getPieceType() == PieceType.WALL){
			return false;
		} 
		return true;	
	}

	@Override
	public Boolean processMove(Character inputtedMove, TurnKeeper turnKeeper){
		if(inputtedMove == 'q'){
			return true;
		}
		// do the move
	
		if(inputtedMove == 'w'){
			this.getCurrHeroLocation().moveX(-1);
		} else if(inputtedMove == 's'){
			this.getCurrHeroLocation().moveX(1);
		} else if(inputtedMove == 'a'){
			this.getCurrHeroLocation().moveY(-1);
		} else if(inputtedMove == 'd'){
			this.getCurrHeroLocation().moveY(1);
		}

		//TODO: ADD ENTER BATTLE PROCESS

		if(this.turnKeeper.progressTurn()){
			this.turnKeeper.resetTurn();
		}

		return null;
	}


}

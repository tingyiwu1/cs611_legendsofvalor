package src.service.game.board;
import src.util.PieceType;
import src.service.game.PlayerControl;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;


/*
 * Game Board for the entire game
 * But should this instead be extending a board class or implementing a board interface instead?
 */
public class GameBoard implements PlayerControl{
	private MapPiece[][] currentBoard;
	private Integer size;
	private Character lastInput = null;
	private int charX;
	private int charY;
	// later to have BossPosition, with a boss piece

	// make a new gameboard
	public GameBoard(int size, double wallPercent, double marketPercent){
		this.currentBoard = new MapPiece[size][size];
		this.size = size;

		for(int i = 0; i < size; i++){
			for(int j = 0; j < size; j++){
				this.currentBoard[i][j] = new MapPiece();
			}
		}
		this.charX = 0;
		this.charY = 0;

		//randomly add walls and markets
		int totalSpaces = size * size;
		int numWalls = (int)(totalSpaces * wallPercent);
		int numMarkets = (int)(totalSpaces * marketPercent);

		Random rand = new Random();
		for(int i = 0; i < numWalls; i++){
			int index = rand.nextInt(totalSpaces);
			int x = index / size;
			int y = index % size;
			if(this.currentBoard[x][y].getPieceType() == PieceType.WALL){
				i--;
			} else {
				this.currentBoard[x][y] = new MapPiece(PieceType.WALL);
			}
		}
		for(int i = 0; i < numMarkets; i++){
			int index = rand.nextInt(totalSpaces);
			int x = index / size;
			int y = index % size;
			if(this.currentBoard[x][y].getPieceType() == PieceType.WALL){
				i--;
			} else {
				this.currentBoard[x][y] = new MapPiece(PieceType.MARKET);
			}
		}
		while(getPieceAt(this.charX, this.charY).getPieceType() != PieceType.EMPTY){
			this.charX += 1;
			if(this.charX == size){
				this.charX = 0;
				this.charY += 1;
			}
		}
		this.setNewBoss();
	}

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

	public void setPieceAt(int x, int y, PieceType newType){
		currentBoard[x][y].setPieceType(newType);
	}

	public int[] getCharacterLocation() {
		return new int[] {this.charX, this.charY};
	}

	public boolean characterAtMarket(){
		return getPieceAt(this.charX, this.charY).getPieceType() == PieceType.MARKET;
	}

	public int getCurrentMarketIndex(){
		return charX * size + charY;
	}

	public Integer getSize(){
		return this.size;
	}

	public MapPiece getPieceAt(int x, int y){
		return this.currentBoard[x][y];
	}

	public MapPiece getCurrentPiece(){
		return this.currentBoard[charX][charY];
	}

	public Character getLastInput(){
		return this.lastInput;
	}

	public boolean isAtBoss() {
		return this.getPieceAt(this.charX, this.charY).getPieceType() == PieceType.BOSS;
	}

	@Override
	public Boolean makeMove(Character inputtedMove){
		this.lastInput = Character.toLowerCase(inputtedMove);
		System.out.println(inputtedMove);
		if(!isMoveValid(inputtedMove)){
			System.out.println("Invalid move");
			return false;
		} 

		processMove(inputtedMove);
		return null;
	}

	@Override
	public Boolean isMoveValid(Character inputtedMove){
		int newX = this.charX;
		int newY = this.charY;
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
	public Boolean processMove(Character inputtedMove){
		if(inputtedMove == 'q'){
			return true;
		}
		// do the move
	
		if(inputtedMove == 'w'){
			this.charX--;
		} else if(inputtedMove == 's'){
			this.charX++;
		} else if(inputtedMove == 'a'){
			this.charY--;
		} else if(inputtedMove == 'd'){
			this.charY++;
		}

	
		return null;
	}


}

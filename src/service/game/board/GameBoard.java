package src.service.game.board;
import src.util.PieceType;
import src.service.game.PlayerControl;
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
		this.charX = 3;
		this.charY = 3;

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

	}

	public int[] getCharacterLocation(){
		return new int[] {this.charX, this.charY};
	}

	public Integer getSize(){
		return this.size;
	}

	public MapPiece getPieceAt(int x, int y){
		return this.currentBoard[x][y];
	}

	public Character getLastInput(){
		return this.lastInput;
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

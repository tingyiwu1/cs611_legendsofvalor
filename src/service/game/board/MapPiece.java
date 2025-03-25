package src.service.game.board;

import src.util.PieceType;

/*
 * Intended to wrap entities, and allow them to be interacted with on the board. 
 */
public class MapPiece {
	PieceType type;

	public MapPiece(){
		this.type = PieceType.EMPTY;
	}

	/*
	 * Testing constructor for type declaration
	 */
	public MapPiece(PieceType type){
		this.type = type;
	}

	public PieceType getPieceType(){
		return this.type;
	}
	
}

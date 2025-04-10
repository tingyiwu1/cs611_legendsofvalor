package src.service.game.board;

import java.util.ArrayList;

import src.service.game.market.MarketItem;
import src.util.PieceType;

/**
 * Map Piece for the game board, providing different types of tiles that
 * change how the game can be interacted with
 */

public class MapPiece {
	PieceType type;
	ArrayList<MarketItem> marketOfferingsAtSquare;

	public MapPiece() {
		this.type = PieceType.EMPTY;
	}

	/*
	 * Testing constructor for type declaration
	 */
	public MapPiece(PieceType type) {
		this.type = type;
	}

	public PieceType getPieceType() {
		return this.type;
	}

	public void setPieceType(PieceType newType) {
		this.type = newType;
	}

	@Override
	public String toString() {
		return "Piece of type: " + this.type;
	}

}

package src.service.entities.attributes;

import src.service.entities.attributes.Position;

public interface IsOnBoard {

	/**
	 * TODO:
	 * Should position be a class? new Position(int x, int y) type of thing?
	 */
	public int getCharX();
	public int getCharY();
	public void moveCharX(int x);
	public void moveCharY(int y);

	public void setCharX(int x);
	public void setCharY(int y);

	// Add a method to get the full position
	public Position getPosition();
	public void setPosition(Position pos);
	
}
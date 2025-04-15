package src.service.entities.attributes;

public interface IsOnBoard {

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
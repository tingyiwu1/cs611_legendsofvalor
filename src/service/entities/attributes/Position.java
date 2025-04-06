package src.service.entities.attributes;

public class Position {
    private int x;
    private int y;

    // Constructor
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Getters, setters
    public int getX() { return x; }
    public int getY() { return y; }
	public void setX(int x) { this.x = x; }
	public void setY(int y) { this.y = y; }
	// Move the position by dx and dy
	public void moveX(int dx) { this.x += dx; }
	public void moveY(int dy) { this.y += dy; }

	public int[] getPositionXY() {
		return new int[] { x, y };
	}


    @Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof Position)) return false;
		Position other = (Position) obj;
		return this.x == other.x && this.y == other.y;
	}

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
	
}

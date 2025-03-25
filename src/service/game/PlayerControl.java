package src.service.game;

public interface PlayerControl {
	public Boolean isMoveValid(Character inputtedMove);
	public Boolean makeMove(Character inputtedMove);
	public Boolean processMove(Character inputtedMove);
	
} 
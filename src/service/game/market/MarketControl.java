package src.service.game.market;

public interface MarketControl {
  public boolean isMoveValid(int itemIndex);

  public boolean makeMove(int itemIndex);

  public boolean processMove(int itemIndex);
}

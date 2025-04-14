package src.service.game;

import src.service.entities.Player;
import src.service.entities.monsters.MonsterTeam;
import src.service.game.board.GameBoard;
import src.service.game.market.MarketFactory;

public class GameContext {
  public final GameBoard gameBoard;
  public final Player player;
  public final MarketFactory marketFactory;
  public final MonsterTeam monsterTeam;
  public final TurnKeeper turnKeeper;

  public GameContext(GameBoard gameBoard, Player player, MarketFactory marketFactory, MonsterTeam monsterTeam,
      TurnKeeper turnKeeper) {
    this.gameBoard = gameBoard;
    this.player = player;
    this.marketFactory = marketFactory;
    this.monsterTeam = monsterTeam;
    this.turnKeeper = turnKeeper;
  }
}

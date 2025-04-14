package src.service.screens;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import src.service.entities.Player;
import src.service.entities.Player.Difficulty;
import src.service.entities.attributes.Position;
import src.service.entities.heroes.Hero;
import src.service.entities.monsters.MonsterTeam;
import src.service.game.TurnKeeper;
import src.service.game.battle.Battle;
import src.service.game.board.GameBoard;
import src.service.game.market.MarketFactory;
import src.service.game.market.MarketItem;
import src.service.screens.ScreenInterfaces.Process;
import src.util.PrintingUtil;
import src.util.ScreenState;
import src.util.StatsTracker;

public class GameProcess extends Process<GameProcess.GameResult> {
  public static enum GameResult implements Process.Result {
    QUIT, WIN, LOSE
  }

  // TODO: combine all these into single context object
  private final Scanner scanner;
  private final GameBoard currentBoard;
  private final Player player;
  // ScreenState currentScreen;
  // ScreenState previousScreen;
  // Boolean continueToGame;
  private final MarketFactory marketFactory;
  private final MonsterTeam monsterTeam;
  private final TurnKeeper turnKeeper;

  public GameProcess(Scanner scanner) {
    super(scanner);
    this.scanner = scanner;
    this.player = new Player(Difficulty.EASY, 3);
    this.monsterTeam = new MonsterTeam(new Position[] {
        new Position(0, 0),
        new Position(0, 3),
        new Position(0, 6),
    });

    monsterTeam.spawnMonsters(1);

    this.turnKeeper = new TurnKeeper(this.player, this.monsterTeam);

    this.currentBoard = new GameBoard(8, 0.3, 0.2, this.player, this.monsterTeam, this.turnKeeper);
    this.marketFactory = new MarketFactory();
  }

  @Override
  public GameResult run() {
    System.out.println("Welcome to the game!!");

    while (true) {
      Process<ScreenResult<Void>> turnProcess;
      if (turnKeeper.getCurrentTurn() == TurnKeeper.CurrentTurn.PLAYER) {
        if (turnKeeper.shouldSpawnMonsters()) {
          monsterTeam.spawnMonsters(player.getMonsterLevel());
        }
        turnProcess = new HeroTurnProcess(scanner, currentBoard, turnKeeper, player, marketFactory);
      } else {
        turnProcess = new MonsterTurnProcess(scanner, currentBoard, monsterTeam, turnKeeper, player);
      }

      ScreenResult<Void> turnResult = turnProcess.run();
      if (turnResult.isQuit()) {
        return GameResult.QUIT;
      }

      if (currentBoard.isHeroWin()) {
        return GameResult.WIN;
      }
      if (currentBoard.isMonsterWin()) {
        return GameResult.LOSE;
      }
    }
  }
}

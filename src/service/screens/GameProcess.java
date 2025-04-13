package src.service.screens;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import src.service.entities.Player;
import src.service.entities.Player.Difficulty;
import src.service.entities.attributes.Position;
import src.service.entities.monsters.MonsterTeam;
import src.service.game.TurnKeeper;
import src.service.game.battle.Battle;
import src.service.game.board.GameBoard;
import src.service.game.market.MarketItem;
import src.service.screens.ScreenInterfaces.Process;
import src.util.PrintingUtil;
import src.util.ScreenState;
import src.util.StatsTracker;

public class GameProcess implements Process<GameProcess.GameResult> {
  public static enum GameResult implements Process.Result {
    QUIT, WIN, LOSE
  }

  private final Scanner scanner;
  private final GameBoard currentBoard;
  private final Battle currBattle;
  private final Player currentPlayer;
  // ScreenState currentScreen;
  // ScreenState previousScreen;
  // Boolean continueToGame;
  private final HashMap<Integer, ArrayList<MarketItem>> marketHash;
  private final MonsterTeam monsterTeam;
  private final TurnKeeper turnKeeper;

  public GameProcess(Scanner scanner) {
    this.scanner = scanner;
    this.currentPlayer = new Player(Difficulty.EASY, 3);
    this.monsterTeam = new MonsterTeam();

    this.monsterTeam.addGenericMonster(new Position(0, 0));
    this.monsterTeam.addGenericMonster(new Position(0, 3));
    this.monsterTeam.addGenericMonster(new Position(0, 6));

    this.turnKeeper = new TurnKeeper(this.currentPlayer, this.monsterTeam);

    this.currentBoard = new GameBoard(8, 0.3, 0.2, this.currentPlayer, this.monsterTeam, this.turnKeeper);
    this.currBattle = null;

    this.marketHash = new HashMap<>();
  }

  @Override
  public GameResult run() {
    System.out.println("Welcome to the game!!");

    while (true) {
      Process<ScreenResult<Void>> turnProcess;
      if (turnKeeper.getCurrentTurn() == TurnKeeper.CurrentTurn.PLAYER) {
        turnProcess = new HeroTurnProcess(scanner, currentBoard, turnKeeper, currentPlayer);
      } else {
        turnProcess = null;
        // turnProcess = new MonsterTurnProcess(scanner, currentBoard, turnKeeper);
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

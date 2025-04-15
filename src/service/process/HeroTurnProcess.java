package src.service.process;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

import src.service.entities.Player;
import src.service.entities.attributes.AttackOption;
import src.service.entities.attributes.Position;
import src.service.entities.heroes.Hero;
import src.service.game.GameContext;
import src.service.game.TurnKeeper;
import src.service.game.TurnKeeper.CurrentTurn;
import src.service.game.battle.Battle;
import src.service.game.board.GameBoard;
import src.service.game.board.MapPiece;
import src.service.game.market.Market;
import src.service.game.market.MarketFactory;
import src.service.process.display.MapDisplay;
import src.util.PieceType;
import src.util.PrintColor;
import src.util.PrintingUtil;
import src.util.StatsTracker;
import src.util.TextColor;

/**
 * Handles the hero's turn in the game. Run from GameProcess when it is the
 * hero's turn.
 * 
 * Displays the game board and allows the user to select an action based on the
 * current state of the board and the hero's position.
 */
public class HeroTurnProcess extends Process<ScreenResult<Void>> {
  private final GameBoard gameBoard;
  private final TurnKeeper turnKeeper;
  private final Player player;
  private final MarketFactory marketFactory;
  private final MapDisplay mapDisplay;
  private final GameContext gameContext;

  public HeroTurnProcess(Scanner scanner, GameContext gameContext) {
    super(scanner);
    this.gameBoard = gameContext.gameBoard;
    this.turnKeeper = gameContext.turnKeeper;
    this.player = gameContext.player;
    this.marketFactory = gameContext.marketFactory;
    this.mapDisplay = new MapDisplay(gameBoard, turnKeeper);
    this.gameContext = gameContext;
  }

  @Override
  public ScreenResult<Void> run() {
    assert turnKeeper.getCurrentTurn() == TurnKeeper.CurrentTurn.PLAYER : "Not player's turn";
    heroTurn: while (turnKeeper.getCurrentTurn() == CurrentTurn.PLAYER) {
      StatsTracker.addToStats("Screens Visited", 1);

      char input = getInputProcess().runLoop(() -> {
        PrintingUtil.clearScreen();
        display();
      }, () -> System.out.println("Invalid input. Please try again."));
      if (input == 'q') {
        return ScreenResult.quit();
      } else if (input == 'm') {
        Market market = marketFactory.getMarket(gameBoard.getCurrentHero());
        MarketProcess marketProcess = new MarketProcess(scanner, gameBoard.getCurrentHero(), market);
        ScreenResult<?> marketResult = marketProcess.run();

        if (marketResult.isQuit()) {
          return ScreenResult.quit();
        } else if (marketResult.isGoBack()) {
          continue heroTurn;
        } else {
          turnKeeper.progressTurn();
        }
      } else if (input == 'i') {
        InventoryProcess inventoryProcess = new InventoryProcess(scanner, gameContext);
        ScreenResult<?> inventoryResult = inventoryProcess.run();

        if (inventoryResult.isQuit()) {
          return ScreenResult.quit();
        } else if (inventoryResult.isGoBack()) {
          continue heroTurn;
        } else {
          turnKeeper.progressTurn();
        }
      } else if (input == 'r') {
        gameBoard.getCurrentHero().recall();
        turnKeeper.progressTurn();
      } else if (input == 'p') {
        turnKeeper.progressTurn();
      } else if (input == 'w' || input == 'a' || input == 's' || input == 'd') {
        if (gameBoard.makeMove(input) && gameBoard.isHeroWin()) {
          return ScreenResult.success(null);
        }
      } else { // input is battle index
        assert gameBoard.isMoveValid(input);
        gameBoard.makeMove(input);
        assert gameBoard.getEnteredBattle() : "should have entered battle after attack move";
        Battle battle = new Battle(player, gameBoard.getMonsterTeam(), gameBoard.getMonsterTarget(), turnKeeper,
            gameBoard.getAttackOption());
        BattleProcess battleProcess = new BattleProcess(scanner, battle, turnKeeper);
        ScreenResult<?> battleResult = battleProcess.run();
        gameBoard.resetBattleInitializer();
        if (battleResult.isQuit()) {
          return ScreenResult.quit();
        }
      }
    }

    return ScreenResult.success(null);
  }

  private InputProcess<Character> getInputProcess() {
    ArrayList<InputProcess.Option<Character>> options = new ArrayList<>();

    MapPiece[] adjacentTiles = gameBoard.getAdjacentTiles(gameBoard.getCurrHeroLocation());

    if (adjacentTiles[0] != null) {
      PieceType pieceType = adjacentTiles[0].getPieceType();
      if (pieceType == PieceType.OBSTACLE) {
        options.add(new InputProcess.Option<>("w", "Break obstacle above", TextColor.ORANGE, 'w'));
      } else if (gameBoard.isMoveValid('w')) {
        options.add(new InputProcess.Option<>("w", "Move Up", TextColor.BLUE, 'w'));
      }
    }
    if (adjacentTiles[1] != null) {
      PieceType pieceType = adjacentTiles[1].getPieceType();
      if (pieceType == PieceType.OBSTACLE) {
        options.add(new InputProcess.Option<>("a", "Break obstacle left", TextColor.ORANGE, 'a'));
      } else if (gameBoard.isMoveValid('a')) {
        options.add(new InputProcess.Option<>("a", "Move Left", TextColor.BLUE, 'a'));
      }
    }
    if (adjacentTiles[2] != null) {
      PieceType pieceType = adjacentTiles[2].getPieceType();
      if (pieceType == PieceType.OBSTACLE) {
        options.add(new InputProcess.Option<>("s", "Break obstacle below", TextColor.ORANGE, 's'));
      } else if (gameBoard.isMoveValid('s')) {
        options.add(new InputProcess.Option<>("s", "Move Down", TextColor.BLUE, 's'));
      }
    }
    if (adjacentTiles[3] != null) {
      PieceType pieceType = adjacentTiles[3].getPieceType();
      if (pieceType == PieceType.OBSTACLE) {
        options.add(new InputProcess.Option<>("d", "Break obstacle right", TextColor.ORANGE, 'd'));
      } else if (gameBoard.isMoveValid('d')) {
        options.add(new InputProcess.Option<>("d", "Move Right", TextColor.BLUE, 'd'));
      }
    }

    options.add(new InputProcess.Option<>("i", "Access Inventory", TextColor.ORANGE, 'i'));

    if (gameBoard.characterAtMarket()) {
      options.add(new InputProcess.Option<>("m", "Access Nexus Market", TextColor.GREEN, 'm'));
    } else {
      options.add(new InputProcess.Option<>("r", "Recall", TextColor.YELLOW, 'r'));
    }

    ArrayList<AttackOption> heroAttackList = this.gameBoard.currHeroAttackList();
    if (heroAttackList.size() > 0) {
      for (int i = 0; i < heroAttackList.size(); i++) {
        AttackOption currAttackOption = heroAttackList.get(i);
        Position monsterPos = currAttackOption.getMonsterTarget().getPosition();
        options.add(
            new InputProcess.Option<>(
                "" + (i + 1),
                "Attack " + currAttackOption.getMonsterTarget().getName() + " at (" + monsterPos.getX() + ", "
                    + monsterPos.getY() + ") with "
                    + currAttackOption.getSourceItem().getName(),
                TextColor.RED,
                (input) -> {
                  if (input.matches("[1-" + heroAttackList.size() + "]")) {
                    return Optional.of(input.charAt(0));
                  }
                  return Optional.empty();
                }));
      }
    }

    options.add(new InputProcess.Option<>("p", "Pass the Turn", TextColor.CYAN, 'p'));
    options.add(new InputProcess.Option<>("q", "Quit", TextColor.RED, 'q'));

    return new InputProcess<>(scanner, options, "Select an action:");
  }

  private void display() {
    mapDisplay.display();
    Hero activeHero = gameBoard.getCurrentHero();
    PrintColor.blue("Active Hero: ");
    System.out
        .println(activeHero.getName());
    PieceType currPieceType = gameBoard.getPieceAt(activeHero.getPosition()).getPieceType();
    if (currPieceType == PieceType.KOULOU) {
      PrintColor.green("Koulou bonus: +" + Hero.KOULOU_STRENGTH_BONUS + " strength\n");
    } else if (currPieceType == PieceType.CAVE) {
      PrintColor.green("Cave bonus: +" + Hero.CAVE_AGILITY_BONUS + " magic strength\n");
    } else if (currPieceType == PieceType.BUSH) {
      PrintColor.green("Bush bonus: +" + Hero.BUSH_DEXTERITY_BONUS + " dodge\n");
    }

    System.out.println();
  }
}

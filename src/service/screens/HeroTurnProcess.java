package src.service.screens;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.Scanner;

import src.service.entities.Entity;
import src.service.entities.Player;
import src.service.entities.Entity.EntityType;
import src.service.entities.attributes.AttackOption;
import src.service.entities.attributes.Position;
import src.service.game.TurnKeeper;
import src.service.game.TurnKeeper.CurrentTurn;
import src.service.game.battle.Battle;
import src.service.game.board.GameBoard;
import src.service.game.market.ItemFactory;
import src.service.game.market.Market;
import src.service.game.market.MarketFactory;
import src.service.game.market.MarketItem;
import src.service.screens.ScreenInterfaces.InputInterface;
import src.service.screens.ScreenInterfaces.Process;
import src.util.PieceType;
import src.util.PrintColor;
import src.util.PrintingUtil;
import src.util.StatsTracker;
import src.util.TextColor;

public class HeroTurnProcess extends Process<ScreenResult<Void>> {
  private final GameBoard currGameBoard;
  private final int gameSize;
  private final TurnKeeper turnKeeper;
  private final Player player;
  private final MarketFactory marketFactory;
  private final MapDisplay mapDisplay;

  public HeroTurnProcess(Scanner scanner, GameBoard gameBoard, TurnKeeper turnKeeper, Player player,
      MarketFactory marketFactory) {
    super(scanner);
    this.currGameBoard = gameBoard;
    this.gameSize = gameBoard.getSize();
    this.turnKeeper = turnKeeper;
    this.player = player;
    this.marketFactory = marketFactory;
    this.mapDisplay = new MapDisplay(gameBoard, turnKeeper);
  }

  @Override
  public ScreenResult<Void> run() {
    assert turnKeeper.getCurrentTurn() == TurnKeeper.CurrentTurn.PLAYER : "Not player's turn";
    while (turnKeeper.getCurrentTurn() == CurrentTurn.PLAYER) {
      StatsTracker.addToStats("Screens Visited", 1);

      char input = getInputProcess().runLoop(() -> {
        PrintingUtil.clearScreen();
        display();
      }, () -> System.out.println("Invalid input. Please try again."));
      if (input == 'q') {
        return ScreenResult.quit();
      } else if (input == 'm') {
        Market market = marketFactory.getMarket(currGameBoard.getCurrentHero());
        MarketProcess marketProcess = new MarketProcess(scanner, currGameBoard.getCurrentHero(), market);
        ScreenResult<?> marketResult = marketProcess.run();

        if (marketResult.isQuit()) {
          return ScreenResult.quit();
        } else if (marketResult.isGoBack()) {
          continue;
        } else {
          turnKeeper.progressTurn();
        }
      } else if (input == 'i') {
        InventoryProcess inventoryProcess = new InventoryProcess(scanner, player,
            currGameBoard.getCurrentHero());
        ScreenResult<?> inventoryResult = inventoryProcess.run();

        if (inventoryResult.isQuit()) {
          return ScreenResult.quit();
        } else if (inventoryResult.isGoBack()) {
          continue;
        } else {
          turnKeeper.progressTurn();
        }
      } else if (input == 'p') {
        turnKeeper.progressTurn();
      } else if (input == 'w' || input == 'a' || input == 's' || input == 'd') {
        assert currGameBoard.isMoveValid(input);
        currGameBoard.makeMove(input);
        return ScreenResult.success(null);
      } else { // input is battle index
        assert currGameBoard.isMoveValid(input);
        currGameBoard.makeMove(input);
        assert currGameBoard.getEnteredBattle() : "should have entered battle after attack move";
        Battle battle = new Battle(player, currGameBoard.getMonsterTeam(), currGameBoard.getMonsterTarget(), turnKeeper,
            currGameBoard.getAttackOption());
        BattleProcess battleProcess = new BattleProcess(scanner, battle, turnKeeper);
        ScreenResult<?> battleResult = battleProcess.run();
        currGameBoard.resetBattleInitializer();
        // TODO: handle battle result
        if (battleResult.isQuit()) {
          return ScreenResult.quit();
        } else {
          // turnKeeper.progressTurn();
        }
      }
    }

    return ScreenResult.success(null);
  }

  private InputProcess<Character> getInputProcess() {
    ArrayList<InputProcess.Option<Character>> options = new ArrayList<>();

    options.add(new InputProcess.Option<>("w", "Move Up", TextColor.BLUE, 'w'));
    options.add(new InputProcess.Option<>("a", "Move Left", TextColor.BLUE, 'a'));
    options.add(new InputProcess.Option<>("s", "Move Down", TextColor.BLUE, 's'));
    options.add(new InputProcess.Option<>("d", "Move Right", TextColor.BLUE, 'd'));

    options.add(new InputProcess.Option<>("i", "Access Inventory", TextColor.CYAN, 'i'));

    if (currGameBoard.characterAtMarket()) {
      options.add(new InputProcess.Option<>("m", "Access Nexus Market", TextColor.CYAN, 'm'));
    }

    ArrayList<AttackOption> heroAttackList = this.currGameBoard.currHeroAttackList();
    if (heroAttackList.size() > 0) {
      for (int i = 0; i < heroAttackList.size(); i++) {
        AttackOption currAttackOption = heroAttackList.get(i);
        options.add(
            new InputProcess.Option<>(
                "" + (i + 1), "Attack with " + currAttackOption.getSourceItem().getName(),
                TextColor.RED,
                (input) -> {
                  if (input.matches("[1-" + heroAttackList.size() + "]")) {
                    return Optional.of(input.charAt(0));
                  }
                  return Optional.empty();
                }
            // Character.forDigit(i + 1, 10) // TODO: replace move reprensation before
            // passing to game board
            ));

        InputInterface.DisplayInputOption("Attack with" + currAttackOption.getSourceItem().getName(), "" + (i + 1),
            src.util.TextColor.RED);
      }
    }

    options.add(new InputProcess.Option<>("p", "Pass the Turn", TextColor.CYAN, 'p'));
    options.add(new InputProcess.Option<>("q", "Quit", TextColor.RED, 'q'));

    return new InputProcess<>(scanner, options, "Select an action:");
  }

  private void display() {
    mapDisplay.display();
    PrintColor.blue("Active Hero: ");
    System.out
        .println(currGameBoard.getEntityList().get(turnKeeper.getPlayerTeamTurnCount()).getName());
  }

}

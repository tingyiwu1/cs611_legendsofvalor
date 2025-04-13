package src.service.screens;

import java.util.ArrayList;
import java.util.Scanner;

import src.service.entities.Entity;
import src.service.entities.Entity.EntityType;
import src.service.entities.attributes.AttackOption;
import src.service.entities.attributes.Position;
import src.service.game.TurnKeeper;
import src.service.game.TurnKeeper.CurrentTurn;
import src.service.game.board.GameBoard;
import src.service.screens.ScreenInterfaces.InputInterface;
import src.service.screens.ScreenInterfaces.Process;
import src.util.PieceType;
import src.util.PrintColor;
import src.util.PrintingUtil;
import src.util.StatsTracker;
import src.util.TextColor;

public class HeroTurnProcess implements Process<ScreenResult<Void>> {
  private final GameBoard currGameBoard;
  private final int gameSize;
  private final TurnKeeper turnKeeper;
  private final Scanner scanner;

  public HeroTurnProcess(Scanner scanner, GameBoard gameBoard, TurnKeeper turnKeeper) {
    this.scanner = scanner;
    this.currGameBoard = gameBoard;
    this.gameSize = gameBoard.getSize();
    this.turnKeeper = turnKeeper;
  }

  @Override
  public ScreenResult<Void> run() {
    while (turnKeeper.getCurrentTurn() == CurrentTurn.PLAYER) {
      StatsTracker.addToStats("Screens Visited", 1);

      InputProcess<Character> inputProcess = getInputProcess();

      InputResult<Character> inputResult = InputResult.invalid();
      while (inputResult.isInvalid()) {
        PrintingUtil.clearScreen();
        System.out.println("This is the Map Screen!");
        this.displayMap();
        inputResult = inputProcess.run();
        if (inputResult.isInvalid()) {
          System.out.println("Invalid input. Please try again.");
        }
      }

      char input = inputResult.getResult();
      if (input == 'q') {
        return ScreenResult.quit();
      } else if (input == 'm') {
        // TODO: run market process
        ScreenResult<?> marketResult = null;

        if (marketResult.isQuit()) {
          return ScreenResult.quit();
        } else if (marketResult.isGoBack()) {
          continue;
        } else {
          return ScreenResult.success(null);
        }
      } else if (input == 'i') {
        // TODO: run inventory process
        ScreenResult<?> inventoryResult = null;

        if (inventoryResult.isQuit()) {
          return ScreenResult.quit();
        } else if (inventoryResult.isGoBack()) {
          continue;
        } else {
          return ScreenResult.success(null);
        }

      } else { // input is an attackOption index
        currGameBoard.makeMove(input);
        // TODO: extract out to battle process?
        StatsTracker.addToStats("Encountered Enemies", 1);

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
                "A" + (i + 1), "Attack with " + currAttackOption.getSourceItem().getName(),
                TextColor.RED,
                Character.forDigit(i + 1, 10) // TODO: replace move reprensation before passing to game board
            ));

        InputInterface.DisplayInputOption("Attack with" + currAttackOption.getSourceItem().getName(), "" + (i + 1),
            src.util.TextColor.RED);
      }
    }

    options.add(new InputProcess.Option<>("p", "Pass the Turn", TextColor.CYAN, 'p'));
    options.add(new InputProcess.Option<>("q", "Quit", TextColor.RED, 'q'));

    return new InputProcess<>(scanner, options);
  }

  // need to display the game board
  private void displayMap() {

    ArrayList<Entity> entityList = currGameBoard.getEntityList();
    ArrayList<Position> entityPositions = new ArrayList<Position>();
    for (Entity entity : entityList) {
      entityPositions.add(entity.getPosition());
    }
    // rows
    for (int r = 0; r < gameSize; r++) {
      for (int i = 0; i < gameSize; i++) {
        System.out.print("+-------");
      }
      System.out.println("+");

      for (int inner = 0; inner < 3; inner++) {
        for (int c = 0; c < gameSize; c++) {
          PieceType currentPieceType = currGameBoard.getPieceAt(r, c).getPieceType();
          System.out.print("|");
          if (currentPieceType == PieceType.WALL) {
            System.out.print(" XXXXX ");
          } else if (inner == 0 && entityPositions.contains(new Position(r, c))) {
            // Check if a hero is at this position and print " H " in the center row
            boolean hasHero = false;
            boolean hasMonster = false;
            boolean isActiveHero = false;
            for (int idx = 0; idx < entityList.size(); idx++) {
              Entity entity = entityList.get(idx);
              if (entity.getPosition().equals(new Position(r, c))) {
                if (entity.getType() == EntityType.HERO) {
                  hasHero = true;
                  if (turnKeeper.getCurrentTurn() == TurnKeeper.CurrentTurn.PLAYER) {
                    if (idx == turnKeeper.getPlayerTeamTurnCount()) {
                      isActiveHero = true;
                    }
                  }
                } else if (entity.getType() == EntityType.MONSTER) {
                  hasMonster = true;
                }
              }
            }
            // TODO: IS THERE A BETTER WAY TO DISPLAY WHICH HERO IS ACTIVE?
            if (isActiveHero) {
              if (hasHero && hasMonster) {
                PrintColor.blue("AH  ");
                PrintColor.yellow(" M ");
              } else if (hasHero) {
                PrintColor.blue("AH     ");
              } else if (hasMonster) {
                PrintColor.yellow("     M ");
              } else {
                System.out.print("       ");
              }
            } else {
              if (hasHero && hasMonster) {
                PrintColor.red(" H  ");
                PrintColor.yellow(" M ");
              } else if (hasHero) {
                PrintColor.red(" H     ");
              } else if (hasMonster) {
                PrintColor.yellow("     M ");
              } else {
                System.out.print("       ");
              }
            }

          } else if (currentPieceType == PieceType.HERO_NEXUS && inner == 1) {
            PrintColor.blue(" NEXUS ");
          } else if (currentPieceType == PieceType.MONSTER_NEXUS && inner == 1) {
            PrintColor.red(" NEXUS ");
          } else if (currentPieceType == PieceType.BUSH && inner == 1) {
            System.out.print("  ~B~  ");
          } else if (currentPieceType == PieceType.CAVE && inner == 1) {
            System.out.print("  [C]  ");
          } else if (currentPieceType == PieceType.KOULOU && inner == 1) {
            System.out.print("  _K_  ");

          } else if (currentPieceType == PieceType.OBSTACLE && inner == 1) {
            System.out.print("  xxx  ");

          } else if (currentPieceType == PieceType.MARKET && inner == 2) {
            PrintColor.yellow("     M ");
          } else {
            System.out.print("       ");
          }
        }
        System.out.println("|");
      }
    }
    for (int col = 0; col < gameSize; col++) {
      System.out.print("+-------");
    }
    System.out.println("+");
    System.out.println("H = Hero, M = Monster, NEXUS = Nexus, X = Wall");
    if (turnKeeper.getCurrentTurn() == TurnKeeper.CurrentTurn.PLAYER) {
      System.out
          .println("Current Hero: " + currGameBoard.getEntityList().get(turnKeeper.getPlayerTeamTurnCount()).getName());
    } else {
      /*
       * Handle enemy turn progression!
       */
      System.out.println("TODO: Handle Enemy Turn");
    }

  }

}
